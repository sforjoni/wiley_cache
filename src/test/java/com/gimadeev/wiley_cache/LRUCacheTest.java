package com.gimadeev.wiley_cache;

import org.junit.Test;

import static com.gimadeev.wiley_cache.CacheFactory.createCache;
import static com.gimadeev.wiley_cache.Strategies.LRU;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LRUCacheTest {

    @Test
    public void test() {
        Cache<String, String> cache = createCache(3, LRU);

        cache.put("1", "one");
        cache.put("2", "two");
        cache.put("3", "three");

        cache.get("1");
        cache.get("1");
        cache.get("1");
        cache.get("2");
        cache.get("3");

        cache.put("4", "four");

        assertNull(cache.get("1"));
        assertEquals("four", cache.get("4"));
        assertEquals("three", cache.get("3"));
        assertEquals("two", cache.get("2"));
    }
}