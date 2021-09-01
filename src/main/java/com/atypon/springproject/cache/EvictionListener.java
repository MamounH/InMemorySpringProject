package com.atypon.springproject.cache;

public interface EvictionListener<K, V> {
    void onEvict(K key, V value);
}
