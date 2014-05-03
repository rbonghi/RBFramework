/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world.robot.editor;

import java.awt.Image;
import java.awt.datatransfer.Transferable;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.io.IOException;
import net.minisumo.world.component.Component;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Exceptions;

/**
 *
 * @author Raffaello
 */
public class ComponentNode extends AbstractNode {

    private BeanInfo beanInfo;
    private MyComponent myComponent;
    private String name;

    public ComponentNode() {
        super(Children.LEAF);
    }

    public ComponentNode(Class component) {
        super(Children.LEAF);
        name = component.getSimpleName();
        myComponent = new MyComponent(component);
        setDisplayName(name);
        try {
            beanInfo = Introspector.getBeanInfo(component);
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public String getHtmlDisplayName() {
        return name;
    }

    @Override
    public Image getIcon(int type) {
        return beanInfo.getIcon(type);
    }

    @Override
    public Transferable drag() throws IOException {
        return myComponent;
    }
}
