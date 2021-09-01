package com.atypon.springproject.dao;

import java.util.TreeMap;

public interface QuotesDao<K, V>  {

    boolean recordIsAdded(V book);
    boolean recordIsDeleted(K bookNum);
    boolean recordIsUpdated(V book, K bookID);
    TreeMap<K, V> loadRecords();
    boolean RecordsAreDeleted(K key);
}
