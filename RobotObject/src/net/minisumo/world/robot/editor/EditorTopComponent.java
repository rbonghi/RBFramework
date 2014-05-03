/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world.robot.editor;

import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map.Entry;
import javax.swing.JScrollPane;
import net.minisumo.util.DataInformation;
import net.minisumo.util.Finder;
import net.minisumo.world.component.Component;
import net.minisumo.world.robot.Robot;
import net.minisumo.world.sensors.Sensor;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.netbeans.api.settings.ConvertAsProperties;
import org.netbeans.api.visual.action.AcceptProvider;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.layout.Layout;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.vmd.VMDGraphScene;
import org.netbeans.api.visual.vmd.VMDNodeWidget;
import org.netbeans.api.visual.vmd.VMDPinWidget;
import org.netbeans.spi.palette.PaletteActions;
import org.netbeans.spi.palette.PaletteController;
import org.netbeans.spi.palette.PaletteFactory;
import org.openide.awt.ActionID;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Node;
import org.openide.util.lookup.Lookups;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//net.minisumo.world.robot.editor//Editor//EN",
autostore = false)
@TopComponent.Description(preferredID = "EditorTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "net.minisumo.world.robot.editor.EditorTopComponent")
//@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_EditorAction",
preferredID = "EditorTopComponent")
public final class EditorTopComponent extends TopComponent implements PropertyChangeListener {

    private static Robot robot;
    private static int counter;
    private JScrollPane scenePane = new JScrollPane();
    private VMDGraphScene scene;

    public EditorTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(EditorTopComponent.class, "CTL_EditorTopComponent"));
        setToolTipText(NbBundle.getMessage(EditorTopComponent.class, "HINT_EditorTopComponent"));

        Node root = new AbstractNode(new GenreNodeContainer());
        PaletteActions a = new ComponentPalette();
        PaletteController p = PaletteFactory.createPalette(root, a);
        associateLookup(Lookups.fixed(p));

        Layout al = LayoutFactory.createVerticalFlowLayout();
        scene = new VMDGraphScene();
        scene.setLayout(al);
        scenePane.setViewportView(scene.createView());
        add(scenePane);

        WidgetAction createAcceptAction = ActionFactory.createAcceptAction(new AcceptProvider() {

            @Override
            public ConnectorState isAcceptable(Widget widget, Point point, Transferable t) {
                return t.isDataFlavorSupported(MyComponent.DATA_FLAVOR) ? ConnectorState.ACCEPT : ConnectorState.REJECT;
            }

            @Override
            public void accept(Widget widget, Point point, Transferable t) {
                try {
                    Object a = t.getTransferData(MyComponent.DATA_FLAVOR);
                    if (a instanceof MyComponent) {
                        MyComponent comp = (MyComponent) a;
                        String name = comp.getComponent().getSimpleName();
                        Object newInstance = comp.getComponent().newInstance();
                        if (newInstance instanceof Component) {
                            Component component = (Component) newInstance;
                            if (robot.getComponents().containsKey(name)) {
                                robot.addComponent(name + "-" + counter++, component);
                            } else {
                                robot.addComponent(name, component);
                            }
                        } else if (newInstance instanceof Sensor) {
                            Sensor sensor = (Sensor) newInstance;
                            if (robot.getSensors().containsKey(name)) {
                                robot.addSensor(name + "-" + counter++, sensor);
                            } else {
                                robot.addSensor(name, sensor);
                            }
                        }
                    } else if (a instanceof MyDataInformation) {
                        MyDataInformation data = (MyDataInformation) a;
                        String name = "data-" + counter;
                        DataInformation newInstance = DataInformation.class.newInstance();
                        newInstance.setVariable(data.isVariable());
                        robot.addParameters(name, newInstance);
                        counter++;
                    }
                } catch (Exception ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        });
        scene.getActions().addAction(createAcceptAction);
    }

    public void setRobot(Robot robot) {
        EditorTopComponent.robot = robot;
        setName(robot.getName() + " Editor");
        requestActive();
        robot.addPropertyChangeListener(this);
        VMDNodeWidget robotNode = (VMDNodeWidget) scene.addNode(robot.getName());
        robotNode.setNodeName(robot.getName());
        for (Entry<Finder, DataInformation> data : robot.getParameters().entrySet()) {
            Finder key = data.getKey();
            if (key.getComponent() == null) {
                VMDPinWidget pin = (VMDPinWidget) scene.addPin(robot.getName(), data.getValue().getName());
                pin.setPinName(data.getValue().getName());
            }
        }
        for (Entry<String, Component> component : robot.getComponents().entrySet()) {
            VMDNodeWidget componentNode = (VMDNodeWidget) scene.addNode(component.getKey());
            componentNode.setNodeName(component.getValue().getName());
            HashMap<String, DataInformation> parameters = component.getValue().getParameters();
            if (parameters != null) {
                for (Entry<String, DataInformation> data : parameters.entrySet()) {
                    String nodeName = component.getKey();
                    VMDPinWidget pin = (VMDPinWidget) scene.addPin(nodeName, nodeName + " - " + data.getKey());
                    pin.setPinName(data.getValue().getName());
                }
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }
}
