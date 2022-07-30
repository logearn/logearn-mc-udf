package cn.xlystar.utils;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class LRUCache<K, V> {
    private Map<K, V> map;
    private final int cacheSize;

    public LRUCache(int initialCapacity) {
        map = new LinkedHashMap<K, V>(initialCapacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > cacheSize;
            }
        };
        this.cacheSize = initialCapacity;
    }

}
