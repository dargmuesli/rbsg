package de.uniks.se1ss19teamb.rbsg.ai;

import de.uniks.se1ss19teamb.rbsg.model.Unit;
import de.uniks.se1ss19teamb.rbsg.model.ingame.InGamePlayer;
import de.uniks.se1ss19teamb.rbsg.model.tiles.EnvironmentTile;
import de.uniks.se1ss19teamb.rbsg.model.tiles.UnitTile;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.ui.InGameController;
import de.uniks.se1ss19teamb.rbsg.util.NotificationHandler;
import java.util.*;
import java.util.Map.Entry;
import javafx.util.Pair;

class Nagato extends AI {

    protected static int AMOUNT_HEAVY_TANKS = 7;

    protected Map<String, String> tankPositions = new HashMap<>();

    protected Map<UnitTile, Integer> projectedHP = new HashMap<>();
    protected Map<String, String> toAttack = new HashMap<>(); //Key: Own, Value: Enemy

    /*
     * Nagato strategy:
     *
     * Pick 6 - 8 heavy tanks, 4 - 2 choppers.
     *
     * Position tanks at a forest -> grass border,
     * or if none can be found in the friendly half of map, mountain -> forest.
     *
     * Move choppers as mobile strike force, attacking bazooka > light tank > infantry > jeep,
     * while keeping clear of heavy tanks.
     *
     * Heavy tanks:
     * If enemy in attack range/2 of HT, attack closest and retreat.
     * If enemy downable with in-range tanks and no other enemy HT is in range, attack now and retreat next round.
     * Focus order: heavy tank > chopper > light tank > bazooka > infantry > jeep.
     */

    public Nagato() {
    }

    @SuppressWarnings("static-access")
    @Override
    public void initialize(String playerID, GameSocket socket, InGameController controller) {
        super.initialize(playerID, socket, controller);

        final int sideLength = (int) Math.sqrt(controller.environmentTiles.size());
        // Calculate heavy tank position.

        boolean sideDirLow; // True if starting at 0 on relevant side.
        boolean sideXY; // True if relevant side is on edge of x axis.

        // Figure out which side of the board we are on.
        {
            EnvironmentTile randomFriendly = null;

            for (UnitTile unit : controller.unitTiles) {
                if (unit.getLeader().equals(playerID)) {
                    randomFriendly = controller.environmentTileMapById.get(unit.getPosition());
                    break;
                }
            }

            assert randomFriendly != null;

            float quot = ((float) Math.abs(randomFriendly.getX() - sideLength / 2))
                    / ((float) Math.abs(randomFriendly.getY() - sideLength / 2));

            sideXY = (quot > 1);

            sideDirLow = (sideXY ? randomFriendly.getX() : randomFriendly.getY()) > sideLength / 2;
        }

        // Iterate over our quarter of the field, and "normalize" it to our direction.
        // If four players, take quarter.
        // If two, take half.
        int startShort = sideDirLow ? sideLength - 1 : 0;
        int stopShort = sideLength / 2 - (sideDirLow ? 1 : 0);
        int lineCnt = 0;
        int players = (int) controller.inGameObjects.values().stream().filter(p -> p instanceof InGamePlayer).count();

        SortedMap<Pair<Integer, Integer>, EnvironmentTile> mapQuarter = new TreeMap<>(new PairComperatorXY(sideLength));

        for (int shortSide = startShort; shortSide != stopShort; shortSide -= (sideDirLow ? 1 : -1)) {
            for (int longSide = (players != 2 ? lineCnt : 0);
                 longSide < sideLength - (players != 2 ? lineCnt : 0); longSide++) {
                mapQuarter.put(new Pair<>(longSide, lineCnt), controller.environmentTiles.get(
                    new Pair<>(sideXY ? shortSide : longSide, sideXY ? longSide : shortSide)));
            }

            lineCnt++;
        }

        SortedMap<Integer, Integer> forestEdges = new TreeMap<>();

        // Now that we have a normalized map, let's walk through it.
        for (Entry<Pair<Integer, Integer>, EnvironmentTile> tile : mapQuarter.entrySet()) {
            EnvironmentTile above = mapQuarter.get(new Pair<>(tile.getKey().getKey(), tile.getKey().getValue() + 1));

            // Skip those tiles that are not a forest/mountain edge.
            if (above == null || tile.getValue().getName().equals("Grass")
                    || tile.getValue().getName().equals("Water")) {
                continue;
            }

            // Found a forest/mountain to grass edge.
            if (above.getName().equals("Grass")) {
                forestEdges.put(tile.getKey().getKey(), tile.getKey().getValue());
            }

        }

        // Fill up if there are not enough good positions.
        for (int i = forestEdges.size(); i < AMOUNT_HEAVY_TANKS; i++) {
            //TODO Better alt. positions with bad map.
            Random r = new Random();
            int x;
            int y;

            do {
                x = r.nextInt(sideLength);
                y = r.nextInt(sideLength / 2);
            } while (mapQuarter.get(new Pair<>(x, y)) == null || forestEdges.containsKey(x));

            forestEdges.put(x, y);
        }

        // Pick middle elements.
        //TODO Improve to "biggest block".
        int skip = (forestEdges.size() - AMOUNT_HEAVY_TANKS) / 2;
        Iterator<Entry<Integer, Integer>> it = forestEdges.entrySet().iterator();
        for (int i = 0; i < skip; i++) {
            it.next();
        }

        int unitCnt = -1;

        // Assign each tank a position.
        for (int i = 0; i < AMOUNT_HEAVY_TANKS; i++) {
            Entry<Integer, Integer> position = it.next();

            UnitTile unit;

            do {
                unitCnt++;
                unit = controller.unitTiles.get(unitCnt);
            } while (!(unit.getLeader().equals(playerID) && unit.getType().equals("Heavy Tank")));

            tankPositions.put(unit.getId(), mapQuarter.get(new Pair<>(position.getKey(), position.getValue())).getId());
        }
    }

    @SuppressWarnings("static-access")
    @Override
    protected void doTurnInternal() {
        projectedHP.clear();
        toAttack.clear();
        for (UnitTile tile : ingameController.unitTiles) {
            tile.setMpLeft(tile.getMp());

            if (tile.getLeader().equals(playerID)) {
                continue;
            }
            projectedHP.put(tile, tile.getHp());
        }

        // Move tanks to forest edge.
        tankReposition();
        // Move tanks to attack if they have enough MP to return.
        tankMovementAttackSafe();
        // Determine if closest enemy can be killed with available force and potentially reenforce attack.
        tankMovementAttackAdditional();

        // Move helicopter strike force.
        helicopterMovement();

        // Move to attack phase.
        socket.nextPhase();
        waitForSocket();

        //TODO Tank Movement Needed? For now, Concede
        if (socket.phaseString.equals("Movement Phase")) {
            //FIXME Does not yet work, somehow
            // For now: Manual recommendation
            NotificationHandler.sendInfo("AI wants to concede", logger);
            //socket.leaveGame();
            //socket.disconnect();
            return;
        }

        attackAvailable();

        // Move to 2nd movement phase.
        socket.nextPhase();
        waitForSocket();

        tankRetreat();

        // End turn.
        socket.nextPhase();
        waitForSocket();
    }

    protected void attackAvailable() {
        for (Entry<String, String> attack : toAttack.entrySet()) {
            socket.attackUnit(attack.getKey(), attack.getValue());
            waitForSocket();
        }
    }

    @SuppressWarnings("static-access")
    protected void helicopterMovement() {

        for (UnitTile heli : ingameController.unitTiles) {
            // Iterate over own helis.
            if (!heli.getLeader().equals(playerID) || !heli.getType().equals("Chopper")) {
                continue;
            }

            ingameController.drawOverlay(ingameController.environmentTileMapById.get(
                    heli.getPosition()), heli.getMpLeft(), false,
                    ((InGamePlayer) ingameController.inGameObjects
                    .get(playerID)).getName());

            Map<String, String> previousTileMapByIdLocal = new HashMap<>(ingameController.previousTileMapById);
            if (previousTileMapByIdLocal.isEmpty()) {
                continue;
            }
            List<String> possibleDestinations = new ArrayList<>(previousTileMapByIdLocal.keySet());

            for (UnitTile enemyTank : ingameController.unitTiles) {
                // Iterate over enemy heavy tanks.
                if (enemyTank.getLeader().equals(playerID) || !enemyTank.getType().equals("Heavy Tank")) {
                    continue;
                }

                ingameController.drawOverlay(ingameController.environmentTileMapById.get(
                        enemyTank.getPosition()), enemyTank.getMp() + 1, false,
                        ((InGamePlayer) ingameController.inGameObjects
                        .get(enemyTank.getLeader())).getName());

                possibleDestinations.removeAll(ingameController.previousTileMapById.keySet());
            }

            if (possibleDestinations.isEmpty()) {
                //TODO Heli can't move outside of HT attack range. Find better solution.
                possibleDestinations.addAll(previousTileMapByIdLocal.keySet());
            }

            SortedSet<UnitTile> targets = new TreeSet<>((unitL, unitR) -> {
                // Focus bazooka as highest priority.
                if (unitL.getType().equals("Bazooka Trooper") && !unitL.getType().equals(unitR.getType())) {
                    return -1000;
                }
                if (unitR.getType().equals("Bazooka Trooper") && !unitL.getType().equals(unitR.getType())) {
                    return 1000;
                }

                // Then light tanks.
                if (unitL.getType().equals("Light Tank") && !unitL.getType().equals(unitR.getType())) {
                    return -500;
                }
                if (unitR.getType().equals("Light Tank") && !unitL.getType().equals(unitR.getType())) {
                    return 500;
                }

                EnvironmentTile heliTile = ingameController.environmentTileMapById.get(heli.getPosition());
                EnvironmentTile tileL = ingameController.environmentTileMapById.get(unitL.getPosition());
                EnvironmentTile tileR = ingameController.environmentTileMapById.get(unitR.getPosition());

                int distL = Math.abs(tileL.getX() - heliTile.getX()) + Math.abs(tileL.getY() - heliTile.getY());
                int distR = Math.abs(tileR.getX() - heliTile.getX()) + Math.abs(tileR.getY() - heliTile.getY());

                return distL - distR;
            });

            for (UnitTile enemy : ingameController.unitTiles) {

                // Iterate over enemys that are not tanks.
                if (enemy.getLeader().equals(playerID) || enemy.getType().equals("Heavy Tank")) {
                    continue;
                }

                targets.add(enemy);
            }

            if (targets.isEmpty()) {
                // Heli idle to move at least one unit per turn.
                EnvironmentTile heliTile = ingameController.environmentTileMapById.get(heli.getPosition());
                Pair<Path, Integer> path = findClosestAccessibleField(heli, heliTile.getX(), heliTile.getY(), false);
                socket.moveUnit(heli.getId(), path.getKey().path);
                waitForSocket();
            }

            UnitTile target = targets.first();
            EnvironmentTile targetUnitTile = ingameController.environmentTileMapById.get(target.getPosition());

            String closest = null;
            int closestDistance = Integer.MAX_VALUE;

            for (String targetTile : possibleDestinations) {
                EnvironmentTile current = ingameController.environmentTileMapById.get(targetTile);

                int currentDistance = Math.abs(targetUnitTile.getX() - current.getX())
                        + Math.abs(targetUnitTile.getY() - current.getY());

                // Forbid walking onto the target.
                if (currentDistance < closestDistance && currentDistance > 0) {
                    closestDistance = currentDistance;
                    closest = current.getId();
                }
            }

            Path path = new Path();
            path.end = ingameController.environmentTileMapById.get(closest);
            path.start = ingameController.environmentTileMapById.get(heli.getPosition());
            path.distance = 0;

            LinkedList<String> pathList = new LinkedList<>();

            do {
                path.distance++;
                pathList.addFirst(closest);
                closest = previousTileMapByIdLocal.get(closest);
            } while (!closest.equals(heli.getPosition()));

            path.path = pathList.toArray(new String[0]);

            socket.moveUnit(heli.getId(), path.path);
            waitForSocket();

            // If we land next to the target, mark it for attack.
            if (closestDistance == 1) {
                toAttack.put(heli.getId(), target.getId());
            }
        }
    }

    protected void tankRetreat() {
        // TODO Evaluate if smarter Ideas are found
        tankReposition();
    }

    @SuppressWarnings("static-access")
    protected void tankMovementAttackAdditional() {
        for (Entry<String, String> position : tankPositions.entrySet()) {

            // Skip repositioning dead units or units that already did a safe attack.
            UnitTile tank = null;
            for (UnitTile tiles : ingameController.unitTiles) {
                if (tiles.getId().equals(position.getKey())) {
                    tank = tiles;
                }
            }

            if (tank == null || toAttack.containsKey(tank.getId())) {
                continue;
            }

            TreeMap<Path, UnitTile> attackable = findAllAttackableEnemies(tank);

            for (Entry<Path, UnitTile> toAttack : attackable.entrySet()) {
                int hp = projectedHP.get(toAttack.getValue());

                // Target has too many HP, don't leave cover to attack.
                if (hp <= 0 || hp > predictDamage(toAttack.getValue(), hp)) {
                    continue;
                }

                boolean foundEnemy = false;

                for (UnitTile enemy : ingameController.unitTiles) {
                    if (enemy.getLeader().equals(playerID) || toAttack.getValue().getId().equals(enemy.getId())) {
                        continue;
                    }

                    EnvironmentTile enemyTile = ingameController.environmentTileMapById.get(enemy.getPosition());
                    EnvironmentTile friendly = attackable.firstKey().start;

                    int distance = Math.abs(friendly.getX() - enemyTile.getX())
                        + Math.abs(friendly.getY() - enemyTile.getY());

                    if (distance < enemy.getMp()) {
                        foundEnemy = true;
                        break;
                    }
                }

                if (!foundEnemy) {
                    socket.moveUnit(tank.getId(), toAttack.getKey().path);
                    tank.setMpLeft(tank.getMpLeft() - toAttack.getKey().distance);
                    waitForSocket();
                    flagForAttackByHeavyTank(toAttack.getValue(), tank);
                }
            }
        }
    }

    @SuppressWarnings("static-access")
    protected void tankMovementAttackSafe() {
        for (Entry<String, String> position : tankPositions.entrySet()) {

            // Skip repositioning dead units.
            UnitTile tank = null;
            for (UnitTile tiles : ingameController.unitTiles) {
                if (tiles.getId().equals(position.getKey())) {
                    tank = tiles;
                }
            }

            if (tank == null) {
                continue;
            }

            TreeMap<Path, UnitTile> attackable = findAllAttackableEnemies(tank);

            if (attackable.isEmpty()) {
                continue;
            }

            if (attackable.firstKey().distance == 0) {
                flagForAttackByHeavyTank(attackable.firstEntry().getValue(), tank);
            } else if (attackable.firstKey().distance <= tank.getMpLeft() / 2) {
                socket.moveUnit(tank.getId(), attackable.firstKey().path);
                tank.setMpLeft(tank.getMpLeft() - attackable.firstKey().distance);
                waitForSocket();
                flagForAttackByHeavyTank(attackable.firstEntry().getValue(), tank);
            }
        }

    }

    protected void flagForAttackByHeavyTank(UnitTile target, UnitTile by) {
        toAttack.put(by.getId(), target.getId());
        int hp = projectedHP.get(target);
        hp -= predictDamage(target, hp);
        projectedHP.put(target, hp);
    }

    @SuppressWarnings("static-access")
    protected int predictDamage(UnitTile target, int hp) {
        EnvironmentTile field = ingameController.environmentTileMapById.get(target.getPosition());
        int fieldDefense = 0;

        switch (field.getName()) {
            case "Grass":
                fieldDefense = 1;
                break;
            case "Forest":
                fieldDefense = 3;
                break;
            case "Mountain":
                fieldDefense = 4;
                break;
            default:
                assert true;
        }

        int defense = hp * fieldDefense;

        int unitDmg = 0;

        switch (target.getType()) {
            case "Infantry":
                unitDmg = 105;
                break;
            case "Bazooka":
                unitDmg = 95;
                break;
            case "Jeep":
                unitDmg = 105;
                break;
            case "Light Tank":
                unitDmg = 85;
                break;
            case "Heavy Tank":
                unitDmg = 55;
                break;
            case "Chopper":
                unitDmg = 75;
                break;
            default:
                assert true;
        }

        float damage = ((float) (unitDmg - defense)) / 10.0f;

        return (damage < 1) ? 1 : (int) damage;
    }

    @SuppressWarnings("static-access")
    protected void tankReposition() {
        for (Entry<String, String> position : tankPositions.entrySet()) {

            // Skip repositioning dead units.
            UnitTile tile = null;
            for (UnitTile tiles : ingameController.unitTiles) {
                if (tiles.getId().equals(position.getKey())) {
                    tile = tiles;
                }
            }

            // Skip correctly positioned units.
            if (tile == null || tile.getPosition().equals(position.getValue())) {
                continue;
            }

            EnvironmentTile target = ingameController.environmentTileMapById.get(position.getValue());
            Pair<Path, Integer> path = findClosestAccessibleField(tile, target.getX(), target.getY(), true);

            if (path != null && path.getKey().distance != 0) {
                tile.setMpLeft(tile.getMpLeft() - path.getKey().distance);
                socket.moveUnit(tile.getId(), path.getKey().path);
                waitForSocket();
            }
        }
    }

    @Override
    public List<String> requestArmy() {
        String idChopper = "";
        String idHeavyTank = "";

        for (Unit unit : availableUnitTypes.values()) {
            if (unit.getType().equals("Chopper")) {
                idChopper = unit.getId();
            }

            if (unit.getType().equals("Heavy Tank")) {
                idHeavyTank = unit.getId();
            }
        }

        List<String> requestIds = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            if (i < AMOUNT_HEAVY_TANKS) {
                requestIds.add(idHeavyTank);
            } else {
                requestIds.add(idChopper);
            }
        }

        return requestIds;
    }

    class PairComperatorXY implements Comparator<Pair<Integer, Integer>> {

        protected int sideLength;

        public PairComperatorXY(int length) {
            sideLength = length;
        }

        @Override
        public int compare(Pair<Integer, Integer> pairL, Pair<Integer, Integer> pairR) {
            int diff = (pairL.getValue() - pairR.getValue()) * sideLength;
            diff += pairL.getKey() - pairR.getKey();

            return diff;
        }
    }
}