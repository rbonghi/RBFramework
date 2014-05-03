/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.util;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author Raffaello
 */
public class FlushedQueue<E> extends AbstractQueue<E> {

    private int capacity;
    private ConcurrentLinkedQueue<E> queue;

    public FlushedQueue(int capacity) {
        this.capacity = capacity;
        queue = new ConcurrentLinkedQueue<E>();
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public Iterator<E> iterator() {
        return queue.iterator();
    }

    @Override
    public int size() {
        return capacity;
    }

    @Override
    public boolean offer(E e) {
        if (queue.size() > capacity) {
            queue.poll();
        }
        queue.offer(e);
        return true;
    }

    @Override
    public E poll() {
        return queue.poll();
    }

    @Override
    public E peek() {
        return queue.peek();
    }
}
