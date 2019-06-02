package com.gimadeev.wiley_cache;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache<K, V> implements Cache<K, V> {

    private final int size;
    private final HashMap<K, V> cache;

    public LRUCache(int size) {
        this.cache = new LinkedHashMap<K, V>(LRUCache.this.size, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > LRUCache.this.size;
            }
        };
        this.size = size;
    }

    public void put(K key, V value) {
        cache.put(key, value);
    }

    public V get(K key) {
        return cache.get(key);
    }

    public V remove(K key) {
        return cache.remove(key);
    }

    public void clear() {
        cache.clear();
    }

    public int size() {
        return cache.size();
    }
}
