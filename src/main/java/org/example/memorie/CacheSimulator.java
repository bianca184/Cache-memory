package org.example.memorie;
import java.util.LinkedList;
import java.util.Queue;

public class CacheSimulator {
    private CacheLine[] cache;
    private int cacheSize;
    private Queue<Integer> fifoQueue=new LinkedList<>();

    public CacheSimulator(int cacheSize) {
        this.cacheSize = cacheSize;
        cache = new CacheLine[cacheSize];
        for (int i = 0; i < cacheSize; i++) {
            cache[i] = new CacheLine();
        }

    }

    public boolean read(int address)
    {
        int index = address%cacheSize;
        int tag = address/cacheSize;

        if (cache[index].valid && cache[index].tag == tag)

        {
           return true;
        }
        else{
            cache[index].valid = true;
            cache[index].tag = tag;
            cache[index].data=address*10;
            return false;
        }

    }

    public boolean readFIFO(int address) {
        int tag = address;
        for (CacheLine c : cache) {
            if (c.valid && c.tag == tag)
                return true;
        }

        if (fifoQueue.size() == cacheSize) {
            int oldTag = fifoQueue.poll();
            for (CacheLine c : cache) {
                if (c.tag == oldTag) {
                    c.valid = false;
                    break;
                }
            }
        }

        fifoQueue.add(tag);
        for(CacheLine c : cache) {
            if(!c.valid)
            {
                c.valid=true;
                c.tag=tag;
                c.data=address*10;
                break;
            }
        }
        return false;
    }

    public CacheLine[] getCacheLines()
    {
        return cache;
    }

   public class CacheLine{
       public int tag;
       public int data;
       public boolean valid;

        public CacheLine()
        {
            this.valid = false;
        }
    }
}
