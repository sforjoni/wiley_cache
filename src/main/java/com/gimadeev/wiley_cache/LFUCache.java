package com.gimadeev.wiley_cache;

import java.util.*;

public class LFUCache<K, V> implements Cache<K, V>{

    private final Map<K, Node<K, V>> cache;
    private final List<LinkedHashSet> countList;
    private int minCount;
    private int maxCount;

    private final int size;

    public LFUCache(int size) {
        this.cache = new HashMap<>(size);
        this.countList = new ArrayList<>(size);
        this.minCount = 0;
        this.maxCount = size - 1;
        this.size = size;
        initCountList();
    }

    public void put(K key, V value) {
        Node<K, V> currentNode = cache.get(key);
        if (currentNode == null) {
            if (cache.size() == size) {
                doEviction();
            }
            LinkedHashSet<Node<K, V>> nodes = countList.get(0);
            currentNode = new Node(key, value, 0);
            nodes.add(currentNode);
            cache.put(key, currentNode);
            minCount = 0;
        } else {
            currentNode.value = value;
        }
    }

    public V get(K k) {
        Node<K, V> currentNode = cache.get(k);
        if (currentNode != null) {
            int currentCount = currentNode.count;
            if (currentCount < maxCount) {
                int nextCount = currentCount + 1;
                LinkedHashSet<Node<K, V>> currentNodes = countList.get(currentCount);
                LinkedHashSet<Node<K, V>> newNodes = countList.get(nextCount);
                moveToNextCount(currentNode, nextCount, currentNodes, newNodes);

                if (minCount == currentCount && currentNodes.isEmpty()) {
                    minCount = nextCount;
                }
            } else {
                LinkedHashSet<Node<K, V>> nodes = countList.get(currentCount);
                nodes.remove(currentNode);
                nodes.add(currentNode);
            }
            return currentNode.value;
        } else {
            return null;
        }
    }

    public V remove(K k) {
        Node<K, V> currentNode = cache.remove(k);
        if (currentNode != null) {
            LinkedHashSet<Node<K, V>> nodes = countList.get(currentNode.count);
            nodes.remove(currentNode);
            if (minCount == currentNode.count) {
                findNextMinCount();
            }
            return currentNode.value;
        } else {
            return null;
        }
    }

    public void clear() {
        countList.stream().forEach(LinkedHashSet::clear);
        cache.clear();
        minCount = 0;
    }

    public int size() {
        return cache.size();
    }

    private void initCountList() {
        for (int i = 0; i <= maxCount; ++i) {
            countList.add(new LinkedHashSet<Node<K, V>>(size));
        }
    }

    private void doEviction() {
        LinkedHashSet<Node<K, V>> nodes = countList.get(minCount);

        if (nodes.isEmpty()) {
            throw new IllegalStateException("Min count constraint violated!");
        } else {
            Node<K, V> node = nodes.iterator().next();
            nodes.remove(node);
            cache.remove(node.key);

            if (nodes.isEmpty()) {
                findNextMinCount();
            }
        }
    }

    private void moveToNextCount(Node<K, V> currentNode, int nextCount, LinkedHashSet<Node<K, V>> currentNodes, LinkedHashSet<Node<K, V>> newNodes) {
        currentNodes.remove(currentNode);
        newNodes.add(currentNode);
        currentNode.count = nextCount;
    }

    private void findNextMinCount() {
        while (minCount <= maxCount && countList.get(minCount).isEmpty()) {
            minCount++;
        }
        if (minCount > maxCount) {
            minCount = 0;
        }
    }

    private static class Node<K, V> {

        public final K key;
        public V value;
        public int count;

        public Node(K key, V value, int count) {
            this.key = key;
            this.value = value;
            this.count = count;
        }

    }
}
