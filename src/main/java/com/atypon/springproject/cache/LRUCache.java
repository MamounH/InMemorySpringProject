package com.atypon.springproject.cache;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LRUCache<K, V> implements Iterable<LRUCache.Node<K, V>>{

    public static class Node<K, V>{
        private K key;
        private V value;
        private Node<K, V> prev, next;

        private Node(K key, V value){
            this.key = key;
            this.value = value;
        }

        public K getKey(){
            return key;
        }

        public V getValue(){
            return value;
        }
    }


    private class LinkedList {
        private Node<K, V> head, last;

        /**
         * Detaches the last node from the tail of the list and returns it.
         *         from the list. Returns null, if the list was empty.
         */
        private Node<K, V> poll(){
            if (head == null)
                return null;
            if (head == last){
                Node<K, V> node = last;
                head = last = null;
                return node;
            }
            Node<K, V> node = last;
            last = last.prev;
            last.next = null;
            node.prev = null;
            return node;
        }

        /**
         * Adds a node at the head of the list.
         */
        private void offer(Node<K, V> node){
            if (head == null){
                head = last = node;
                return;
            }
            head.prev = node;
            node.next = head;
            head = node;
        }

        /**
         * Removes a node from the linked list. Assumes that the node is a part
         */
        private void remove(Node<K, V> node){
            if (node == last){
                poll();
            } else {
                if (head == node)
                    head = node.next;
                else
                    node.prev.next = node.next;
                node.next.prev = node.prev;
                node.prev = null;
                node.next = null;
            }
        }

        /**
         * Removes all elements from the list. Currently, it only dereferences the
         */
        private void clear(){
            list.head = list.last = null;
        }
    }

    private LinkedList list;

    private HashMap<K, Node<K, V>> map;

    public int getMaxSize() {
        return maxSize;
    }

    private int maxSize;
    private int size;
    private EvictionListener<K, V> listener;
    private int modCount;

    public LRUCache(int capacity){
        maxSize = capacity;
        list = new LinkedList();
        map = new HashMap<>();

    }


    public V get(K key){
        modCount++;
        Node<K, V> node = map.get(key);
        if (node == null)
            return null;
        list.remove(node);
        list.offer(node);
        return node.value;
    }

    public void put(K key, V value){
        modCount++;
        Node<K, V> node = map.get(key);
        if (node == null){
            node = new Node<>(key, value);
            list.offer(node);
            map.put(key, node);
            if (size == maxSize){
                Node<K, V> removed = list.poll();
                if (listener != null)
                    listener.onEvict(removed.key, removed.value);
                map.remove(removed.key);
            } else
                size++;
        } else {
            node.value = value;
            list.remove(node);
            list.offer(node);
        }
    }

    public void evict(K key){
        modCount++;
        Node<K, V> node = map.get(key);
        if (node != null) {
            list.remove(node);
            map.remove(key);
            size--;
        }
    }

    public int size(){
        return size;
    }

    public void evictAll(){
        modCount++;
        list.clear();
        map.clear();
        size = 0;
    }

    @Override
    public String toString() {
        if (list.head == null)
            return "()";
        StringBuilder builder = new StringBuilder();
        builder.append('(');
        builder.append(list.head.key);

        Node node = list.head.next;
        while (node != null){
            builder.append(',');
            builder.append(node.key);
            node = node.next;
        }
        builder.append(')');
        return builder.toString();
    }

    @Override
    public Iterator<Node<K, V>> iterator() {

        final Node<K, V> placeholder = new Node<>(null, null);
        placeholder.next = list.head;

        return new Iterator<Node<K, V>>() {
            Node<K, V> node = placeholder;
            int currentModCount = modCount;

            @Override
            public boolean hasNext() {
                return node.next != null;
            }

            @Override
            public Node<K, V> next() {
                if (currentModCount != modCount)
                    throw new ConcurrentModificationException();
                if (!hasNext())
                    throw new NoSuchElementException();

                node = node.next;
                return node;
            }

            @Override
            public void remove() {
                if (node.key != null ) {
                    Node<K, V> placeholder = new Node<>(null, null);
                    placeholder.next = node.next;
                    evict(node.key);
                    node = placeholder;
                    currentModCount++;
                }
            }
        };
    }
}