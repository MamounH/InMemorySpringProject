package com.atypon.springproject.database;

import com.atypon.springproject.dao.BooksDao;
import com.atypon.springproject.dao.daoImp.BooksDaoImp;

import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;


public class BooksTable<K, V> extends Transactions<V> {



    private final TreeMap<K , V> table;
    BooksDao<K,V> booksDao = new BooksDaoImp<>();

    private final Logger logger = Logger.getLogger("In-Memory Database");

    private static BooksTable instance;
    private static final Integer CAPACITY = 15;

    public static synchronized BooksTable getInstance(){
        if (instance == null) {
            instance = new BooksTable<>();
        }

        return instance;
    }

    public BooksTable() {
        table =booksDao.loadRecords();
    }

    public TreeMap<K, V> getAll() {
            return table;

    }



    public V get(K key){
        if (table.containsKey(key)){
            try {
                acquireReadLock(table.get(key));
                return table.get(key);
            } catch (InterruptedException e) {
                logError(e);
            } finally {
                releaseReadLock(table.get(key));
            }
        }
        return null;
    }

    public void add(V value, K key){
        try {
            acquireWriteLock(value);
            if (!isStorageFull() && table.get(key)== null) {
                addNewRecord(value,key);
            } else
                logger.log(Level.INFO,"In-Memory Storage is full or Record already exists.");
        } catch (InterruptedException e) {
            logError(e);
        } finally {
            releaseWriteLock(value);
        }
    }

    private void addNewRecord(V value, K key) {
        if (booksDao.recordIsAdded(value)){
            table.put(key, value);
            logger.log(Level.INFO,"Successfully Added new Record to the In-Memory");}
        else {
            logger.log(Level.INFO,"Record couldn't be added... ");
        }
    }

    public void update(V value, K key) {

        try {
            acquireWriteLock(value);
            updateRecord(value, key);
        } catch (Exception e) {
            logError(e);
        } finally {
            releaseWriteLock(value);
        }
    }

    private void updateRecord(V value, K key) {

        if (booksDao.recordIsUpdated(value,key)){
                logger.log(Level.INFO,"Client Updated an existing record..");
        table.put(key, value);}
        else {
            logger.log(Level.INFO,"record couldn't be updated..");
        }
    }

    public void remove(K key) {
        try {
            acquireWriteLock(table.get(key));
            removeRecord(key);
        } catch (InterruptedException e) {
            logError(e);
        }

    }

    private void removeRecord(K key) {

        if (booksDao.recordIsDeleted(key)){
            table.remove(key);
        } else {
            logger.log(Level.INFO , "Couldn't delete record....");
        }
    }


    public int getDBSize(){
        return table.size();
    }


    private boolean isStorageFull() { return table.size() == CAPACITY; }

    public boolean recordExists(K key){
            return table.containsKey(key);
    }

    public int getCacheSize(){return (int) (0.3 * table.size());}


    private void logError (Exception e){
        logger.log(Level.SEVERE,e.getMessage());
    }

}
