package de.uniks.se1ss19teamb.rbsg.ai;


import de.uniks.se1ss19teamb.rbsg.model.Unit;
import de.uniks.se1ss19teamb.rbsg.sockets.GameSocket;
import de.uniks.se1ss19teamb.rbsg.ui.InGameController;

import java.util.ArrayList;
import java.util.List;


class Nagato extends AI {
	
	private static final int AMOUNT_HEAVY_TANKS = 7;

    /*
     * Nagato Strategy:
     * 
     * Pick 6 - 8 Heavy Tanks, 4 - 2 Choppers
     * Position Tanks at a forest -> Grass Border (if none in friendly half of map, mountain -> forest)
     * Move Choppers as Mobile Strike force, attacking Bazooka > Light Tank > Infantry > Jeep while keeping Clear of Heavy Tanks
     * Heavy Tanks:
     * If Enemy in Attack Range/2 of HT -> Attack and retreat
     * If Enemy downable with In-Range Tanks and no other enemy HT in Range, attack and retreat next round
     * Focus Order: Heavy Tank > Chopper > Light Tank > Bazooka > Infantry > Jeep
     * 
     */
    
    public Nagato() {
    }
    
    @Override
    public void initialize(String playerID, GameSocket socket, InGameController controller) {
	    super.initialize(playerID, socket, controller);
	    
	    //Calculate HT Position
    }

    @Override
    public void doTurn() {
    	//Move Tanks to Forest Edge
        tankReposition();
        //Move Tanks to Attack if they have enough MP to return
        tankMovementAttackSafe();
        //Determine if closest Enemy can be killed with available Force and potentially reenforce attack
        tankMovementAttackAdditional();
        
        //Move Helicopter Strike Force
        helicopterMovement();
        
        //Move To Attack Phase
        socket.nextPhase();
        waitForSocket();
        
        //TODO Check Phase Advancement - Tank Movement Needed? Concede?
        
        attackAvailable();
        
        //Move To 2nd Movement Phase
        socket.nextPhase();
        waitForSocket();
        
        tankRetreat();
        
        //End Turn
        socket.nextPhase();
        waitForSocket();      
    }

	private void attackAvailable() {
		// TODO Auto-generated method stub
		
	}

	private void helicopterMovement() {
		// TODO Auto-generated method stub
		
	}

    private void tankRetreat() {
		// TODO Auto-generated method stub
		
	}
	
	private void tankMovementAttackAdditional() {
		// TODO Auto-generated method stub
		
	}

	private void tankMovementAttackSafe() {
		// TODO Auto-generated method stub
		
	}

	private void tankReposition() {
		// TODO Auto-generated method stub
		
	}

	@Override
    public List<String> requestArmy() {
    	String idChopper = "";
    	String idHeavyTank = "";
    	
    	for (Unit unit : availableUnitTypes.values()) {
    		if (unit.getType() == "Chopper") {
    			idChopper = unit.getId();
    		}
    		if (unit.getType() == "Heavy Tank") {
    			idHeavyTank = unit.getId();
    		}
    	}
    	
    	List<String> requestIds = new ArrayList<String>();
    	
    	for(int i = 0; i < 10; i++) {
    		if(i < AMOUNT_HEAVY_TANKS) {
    			requestIds.add(idHeavyTank);    			
    		}
    		else {
    			requestIds.add(idChopper);
    		}
    	}
    	
        return requestIds;
    }
    
}
