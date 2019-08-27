package de.uniks.se1ss19teamb.rbsg.util;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ThreadLocks {

    private static final ReentrantReadWriteLock lockPreviousTileMapById = new ReentrantReadWriteLock();
    private static final Lock readLockPreviousTileMapById = lockPreviousTileMapById.readLock();
    private static final Lock writeLockPreviousTileMapById = lockPreviousTileMapById.writeLock();

    private static final ReentrantReadWriteLock lockEnvironmentTileMapById = new ReentrantReadWriteLock();
    private static final Lock readEnvironmentTileMapById = lockEnvironmentTileMapById.readLock();


    private static final Lock writeEnvironmentTileMapById = lockEnvironmentTileMapById.writeLock();

    public static Lock getReadLockPreviousTileMapById() {
        return readLockPreviousTileMapById;
    }

    public static Lock getWriteLockPreviousTileMapById() {
        return writeLockPreviousTileMapById;
    }

    public static Lock getReadEnvironmentTileMapById() {
        return readEnvironmentTileMapById;
    }

    public static Lock getWriteEnvironmentTileMapById() {
        return writeEnvironmentTileMapById;
    }

}
