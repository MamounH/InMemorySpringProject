package com.atypon.springproject.dao;

import java.util.TreeMap;

public interface BooksDao<K, V> {

     boolean recordIsAdded(V book);
     boolean recordIsDeleted(K bookNum);
     boolean recordIsUpdated(V book, K bookID);
     TreeMap<K, V> loadRecords();
}
