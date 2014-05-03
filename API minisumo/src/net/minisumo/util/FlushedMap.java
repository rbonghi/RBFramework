/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.util;

import java.util.Collection;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author Raffaello
 */
public class FlushedMap<D> {

//    private TreeMap<Long, D> map;
    private ConcurrentSkipListMap<Long, D> map;
    private long storic;

    public FlushedMap(long storic) {
        setStoric(storic);
        map = new ConcurrentSkipListMap<Long, D>();
//        map = new TreeMap<Long, D>();
    }

    public final void setStoric(long storic) {
        this.storic = storic;
    }

    public long getStoric() {
        return storic;
    }

    public void putData(D data) {
        long currentTimeMillis = System.currentTimeMillis();
        for(long data2: map.keySet()){
            if((currentTimeMillis - data2) > storic){
                map.remove(data2);
            }
        }
//        if (map.firstEntry() != null && ((currentTimeMillis - map.firstKey()) > storic)) {
//            map.remove(map.firstKey());
//        }
        map.put(currentTimeMillis, data);
    }

    public Collection<D> getValue() {
        return map.values();
    }

    public ConcurrentSkipListMap<Long, D> getMap() {
        return map.clone();
//        return (TreeMap<Long, D>) map.clone();
    }
}
