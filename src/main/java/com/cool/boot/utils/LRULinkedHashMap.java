package com.cool.boot.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class LRULinkedHashMap<K, V> extends LinkedHashMap<K, V> {

    private int capacity;
    private static final long serialVersionUID = 1L;

    public LRULinkedHashMap(int capacity) {

        super(16, 0.75f, true);
        this.capacity = capacity;
    }


    @Override
    public boolean removeEldestEntry(Map.Entry<K, V> eldest) {

        log.warn("LRU {} = {}", eldest.getKey(), eldest.getValue());
        return size() > capacity;
    }

    public static void main(String[] args) {
        LRULinkedHashMap<Integer, Integer> map = new LRULinkedHashMap<>(5);
        for (int i = 0; i < 10; i++) {
            map.put(i, i);
        }

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
    }
}
