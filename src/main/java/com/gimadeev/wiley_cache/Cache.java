package com.gimadeev.wiley_cache;

public interface Cache<K, V> {

    void put(K key, V value);
    V get(K k);
    V remove(K k);
    void clear();
    int size();
}
