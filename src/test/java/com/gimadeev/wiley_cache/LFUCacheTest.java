package com.gimadeev.wiley_cache;

import org.junit.Test;

import static com.gimadeev.wiley_cache.CacheFactory.createCache;
import static com.gimadeev.wiley_cache.Strategies.LFU;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LFUCacheTest {

    @Test
    public void test() {
        Cache<String, String> cache = createCache(3, LFU);

        cache.put("1", "one");
        cache.put("2", "two");
        cache.put("3", "three");

        cache.get("1");
        cache.get("1");
        cache.get("1");
        cache.get("3");
        cache.get("2");

        cache.put("4", "four");

        assertEquals("one", cache.get("1"));
        assertEquals("four", cache.get("4"));
        assertNull("three", cache.get("3"));
        assertEquals("two", cache.get("2"));
    }

    @Test
    public void testRemove() {
        Cache<String, String> cache = createCache(3, LFU);

        cache.put("1", "one");
        cache.put("2", "two");
        cache.put("3", "three");

        cache.get("1");
        cache.get("1");
        cache.get("1");
        cache.get("3");
        cache.get("2");

        cache.put("4", "four");

        cache.remove("2");

        assertEquals("one", cache.get("1"));
        assertEquals("four", cache.get("4"));
        assertNull(cache.get("3"));
        assertNull(cache.get("2"));
    }

    @Test
    public void testClear() {
        Cache<Integer, String> cache = createCache(3, LFU);

        cache.put(1, "one");
        cache.put(2, "two");
        cache.put(3, "three");
        cache.put(4, "four");

        cache.clear();

        assertEquals(0, cache.size());
    }


}