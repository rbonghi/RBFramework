/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.graphic;

import java.beans.IntrospectionException;
import java.util.logging.Logger;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Raffaello
 */
public class BNode extends BeanNode {

    static final Logger logger = Logger.getLogger(BNode.class.getName());
    private Object obj;

    public BNode(Object obj) throws IntrospectionException {
        super(obj, Children.create(new Child(obj), true), Lookups.singleton(obj));
        this.obj = obj;

    }
}
