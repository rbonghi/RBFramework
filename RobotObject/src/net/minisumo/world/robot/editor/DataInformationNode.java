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
import net.minisumo.util.DataInformation;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Exceptions;

/**
 *
 * @author Raffaello
 */
public class DataInformationNode extends AbstractNode {

    private BeanInfo beanInfo;
    private String name;
    private boolean variable;

    public DataInformationNode(boolean variable) {
        super(Children.LEAF);
        if (variable) {
            name = "Variable";
        }else {
            name = "Parameters";
        }
        setDisplayName(name);
        try {
            beanInfo = Introspector.getBeanInfo(DataInformation.class);
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
        this.variable = variable;
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
        return new MyDataInformation(variable);
    }
}
