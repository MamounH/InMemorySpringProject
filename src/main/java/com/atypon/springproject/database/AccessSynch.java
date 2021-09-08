package com.atypon.springproject.database;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class AccessSynch<V>{



    private HashMap<V, ReentrantReadWriteLock> locks = new HashMap<V, ReentrantReadWriteLock>();
    private HashMap<V,Integer> lockThreadCounts = new HashMap<V, Integer>();
    private final Object lock = new Object();


    /**
     * Get the lock for an object.
     *
     * This method must only be called by one thread at a time. synchronization
     * is handled by other methods in this class.
     *
     * @param object
     *            object to lock.
     * @return lock corresponding to object. not ours
     */
    private ReentrantReadWriteLock getLock(final V object) {
        ReentrantReadWriteLock lock = locks.get(object);

        if (lock == null) {
            System.out.println("lock is null");
            lock = new ReentrantReadWriteLock(true);
            locks.put(object, lock);
        }
        System.out.println("hash is"+ object.hashCode());
        System.out.println("queue for "+object.toString()+ locks.get(object).getQueueLength());
        System.out.println("locks for "+object.toString()+ locks.get(object));
        System.out.println("thread count is "+ lockThreadCounts.get(object));
        System.out.println("lock sizes is "+ locks.size());
        return lock;
    }

    /**
     * Increment the thread count for an object.
     *
     * This method must only be called by one thread at a time. synchronization
     * is handled by other methods in this class.
     *
     * @param object not ours
     */
    private void incrementThreadCount(final V object) {
        Integer threadCount = lockThreadCounts.get(object);
        if (threadCount == null) {
            threadCount = Integer.valueOf(0);
        }
        threadCount = threadCount + 1;
        lockThreadCounts.put(object, threadCount);
    }

    /**
     * Decrement the thread count for an object. Also, when the thread count
     * reaches zero, the lock corresponding to this object is removed from the
     * locks map.
     *
     * This method must only be called by one thread at a time. synchronization
     * is handled by other methods in this class.
     *
     * @param object not ours
     */
    private void decrementThreadCount(final V object) {
        Integer threadCount = lockThreadCounts.get(object);
        if (threadCount == null) {
            throw new IllegalStateException(
                    "Trying to decrement thread count that does not exist.");
        }
        threadCount = threadCount - 1;
        lockThreadCounts.put(object, threadCount);

        if (threadCount == 0) {
            // no threads are using this lock anymore, cleanup
            locks.remove(object);
            lockThreadCounts.remove(object);
        }
    }

    /**
     * Acquire a read lock for an object.
     *
     * Callers MUST subsequently call releaseReadLock.
     *
     * @param object
     *            the object to lock for reading.
     * @throws InterruptedException if thread is interrupted  ours
     */
    public void acquireReadLock(final V object) throws InterruptedException {
        ReentrantReadWriteLock readLock;
        synchronized (lock) {
            readLock = getLock(object);
            incrementThreadCount(object);
        }
        // do this outside synchronized for concurrency
        readLock.readLock().lockInterruptibly();
    }

    /**
     * Check if the calling thread currently has a write lock for the object.
     *
     * @param object
     *            object to check.
     * @return true if the current thread currently holds a write lock, false
     *         otherwise.
     */
    public boolean haveWriteLock(final V object) {
        ReentrantReadWriteLock lock = locks.get(object);
        if (lock == null) {
            return false;
        }
        return lock.isWriteLockedByCurrentThread();
    }

    /**
     * Release a held read lock for an object.
     *
     * Callers MUST have previously called acquireReadLock(object).
     *
     * @param object
     *            the object to unlock for reading.
     */
    public void releaseReadLock(final V object) {
        synchronized (lock) {
            ReentrantReadWriteLock lock = getLock(object);
            decrementThreadCount(object);
            lock.readLock().unlock();
        }
    }

    /**
     * Acquire a write lock for an object.
     *
     * Callers MUST also call releaseWriteLock(object).
     *
     * @param object
     *            the object to lock for writing.
     * @throws InterruptedException if thread is interrupted
     */
    public void acquireWriteLock(final V object) throws InterruptedException {
        ReentrantReadWriteLock lock = null;
        synchronized (this.lock) {
            lock = getLock(object);
            incrementThreadCount(object);
        }
        // do this outside synchronized for concurrency
        lock.writeLock().lockInterruptibly();
//        lock.writeLock().lock();
    }

    /**
     * Release a held write lock for an object.
     *
     * Callers MUST have previously called acquireWriteLock(object).
     *
     * @param object
     *            the object to unlock for writing.
     */
    public void releaseWriteLock(final V object) {
        synchronized (lock) {
            ReentrantReadWriteLock lock = getLock(object);
            decrementThreadCount(object);
            lock.writeLock().unlock();
        }
    }

    /**
     * This is a synonym for acquireWriteLock, which is an exclusive lock for
     * this object.
     *
     * @param object
     *            the object to lock.
     * @throws InterruptedException if thread is interrupted
     */
    public void acquireLock(final V object) throws InterruptedException {
        acquireWriteLock(object);
    }

    /**
     * This is a synonym for releaseWriteLock, which is an exclusive lock for
     * this object.
     *
     * @param object
     *            the object to unlock.
     */
    public void releaseLock(final V object) {
        releaseWriteLock(object);
    }

}
