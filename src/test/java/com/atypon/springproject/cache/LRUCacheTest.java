package com.atypon.springproject.cache;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LRUCacheTest {


    @Test
    public void testCacheSize(){
        LRUCache<Integer, Integer> cache = new LRUCache<>(2);
        cache.put(1, 123);
        cache.put(2, 222);
        cache.put(3, 333);
        assertEquals(2,cache.size());
    }

    @Test
    public void testCacheEvict(){
        LRUCache<Integer, Integer> cache = new LRUCache<>(2);
        cache.put(1, 123);
        cache.put(2, 222);
        cache.evict(1);
        assertNull(cache.get(1));
    }


}