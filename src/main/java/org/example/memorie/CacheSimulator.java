package org.example.memorie;

import java.util.LinkedList;
import java.util.Queue;

public class CacheSimulator {

    private CacheLine[] cache;
    private int cacheSize;
    private Queue<Integer> fifoQueue = new LinkedList<>();


    public EvictionRecord lastEviction;

    public CacheSimulator(int cacheSize) {
        this.cacheSize = cacheSize;
        cache = new CacheLine[cacheSize];
        for (int i = 0; i < cacheSize; i++) {
            cache[i] = new CacheLine();
        }
    }

    public int getCacheSize() {
        return cacheSize;
    }


    public int findTag(int tag) {
        for (int i = 0; i < cache.length; i++) {
            if (cache[i].valid && cache[i].tag == tag) {
                return i;
            }
        }
        return -1;
    }




    public boolean read(int address) {
        int index = address % cacheSize;
        int tag = address / cacheSize;

        if (cache[index].valid && cache[index].tag == tag) {
            lastEviction = null;
            return true;
        } else {
            int oldTag = cache[index].valid ? cache[index].tag : -1;

            cache[index].valid = true;
            cache[index].tag = tag;
            cache[index].data = address * 10;

            if (oldTag != -1) {
                lastEviction = new EvictionRecord(index, oldTag, tag, address);
            } else {
                lastEviction = null;
            }

            return false;
        }
    }


    public boolean readFIFO(int address) {
        int tag = address;


        for (CacheLine c : cache) {
            if (c.valid && c.tag == tag) {
                lastEviction = null;
                return true;
            }
        }

        int evictedIndex = -1;
        int oldTag = -1;


        if (fifoQueue.size() == cacheSize) {
            oldTag = fifoQueue.poll();
            for (int i = 0; i < cache.length; i++) {
                CacheLine c = cache[i];
                if (c.valid && c.tag == oldTag) {
                    c.valid = false;
                    evictedIndex = i;
                    break;
                }
            }
        }

        fifoQueue.add(tag);


        for (CacheLine c : cache) {
            if (!c.valid) {
                c.valid = true;
                c.tag = tag;
                c.data = address * 10;
                break;
            }
        }

        if (evictedIndex != -1) {
            lastEviction = new EvictionRecord(evictedIndex, oldTag, tag, address);
        } else {
            lastEviction = null;
        }

        return false;
    }

    public CacheLine[] getCacheLines() {
        return cache;
    }

    public class CacheLine {
        public int tag;
        public int data;
        public boolean valid;

        public CacheLine() {
            this.valid = false;
        }
    }


    public static class EvictionRecord {
        public final int index;
        public final int oldTag;
        public final int newTag;
        public final int accessAddress;

        public EvictionRecord(int index, int oldTag, int newTag, int accessAddress) {
            this.index = index;
            this.oldTag = oldTag;
            this.newTag = newTag;
            this.accessAddress = accessAddress;
        }
    }
}
