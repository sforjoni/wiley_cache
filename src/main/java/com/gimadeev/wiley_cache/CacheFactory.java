package com.gimadeev.wiley_cache;

public class CacheFactory {

    public static <K, V> Cache<K, V> createCache(int size, Strategies strategy) {
        switch (strategy) {
            case LFU:
                return new LFUCache<>(size);
            case LRU:
                return new LRUCache<>(size);
            default:
                throw new IllegalArgumentException("Wrong cache strategy");
        }
    }
}
