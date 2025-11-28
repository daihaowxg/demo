package com.example.demo;

import java.util.*;

/**
 * 使用 Java 实现一个LRU，要求有过期时间，如果达到容量，随机删一个过期的，如果没有过期的，按LRU规则删，set时更新过期时间
 * @param <K>
 * @param <V>
 */
public class ExpiringLRUCache<K, V> {
    private final int capacity;
    private final long ttlMillis;
    private final Map<K, CacheNode<K, V>> map;
    private final Deque<CacheNode<K, V>> deque;

    public ExpiringLRUCache(int capacity, long ttlMillis) {
        this.capacity = capacity;
        this.ttlMillis = ttlMillis;
        this.map = new HashMap<>();
        this.deque = new LinkedList<>();
    }

    public synchronized V get(K key) {
        CacheNode<K, V> node = map.get(key);
        if (node == null || isExpired(node)) {
            removeNode(key);
            return null;
        }

        // Move node to front (most recently used)
        deque.remove(node);
        deque.addFirst(node);
        return node.value;
    }

    public synchronized void set(K key, V value) {
        long expiry = System.currentTimeMillis() + ttlMillis;
        if (map.containsKey(key)) {
            CacheNode<K, V> node = map.get(key);
            node.value = value;
            node.expiryTime = expiry;
            deque.remove(node);
            deque.addFirst(node);
        } else {
            if (map.size() >= capacity) {
                evictIfNecessary();
            }
            CacheNode<K, V> node = new CacheNode<>(key, value, expiry);
            map.put(key, node);
            deque.addFirst(node);
        }
    }

    private void evictIfNecessary() {
        // 先尝试随机删一个过期的
        List<CacheNode<K, V>> expiredNodes = new ArrayList<>();
        for (CacheNode<K, V> node : deque) {
            if (isExpired(node)) {
                expiredNodes.add(node);
            }
        }

        if (!expiredNodes.isEmpty()) {
            CacheNode<K, V> toRemove = expiredNodes.get(new Random().nextInt(expiredNodes.size()));
            removeNode(toRemove.key);
        } else {
            // 没有过期的，按LRU删（即从队尾删除）
            CacheNode<K, V> lruNode = deque.removeLast();
            map.remove(lruNode.key);
        }
    }

    private void removeNode(K key) {
        CacheNode<K, V> node = map.remove(key);
        if (node != null) {
            deque.remove(node);
        }
    }

    private boolean isExpired(CacheNode<K, V> node) {
        return System.currentTimeMillis() > node.expiryTime;
    }

    private static class CacheNode<K, V> {
        K key;
        V value;
        long expiryTime;

        CacheNode(K key, V value, long expiryTime) {
            this.key = key;
            this.value = value;
            this.expiryTime = expiryTime;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CacheNode)) return false;
            CacheNode<?, ?> that = (CacheNode<?, ?>) o;
            return Objects.equals(key, that.key);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key);
        }
    }
}