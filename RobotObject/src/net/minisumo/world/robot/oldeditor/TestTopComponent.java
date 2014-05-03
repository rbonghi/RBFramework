/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world.robot.oldeditor;

import javax.swing.JScrollPane;
import net.minisumo.world.robot.editor.GenreNodeContainer;
import org.netbeans.spi.palette.PaletteController;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.netbeans.api.settings.ConvertAsProperties;
import org.netbeans.api.visual.vmd.VMDGraphScene;
import org.netbeans.api.visual.vmd.VMDNodeWidget;
import org.netbeans.api.visual.vmd.VMDPinWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.spi.palette.PaletteActions;
import org.netbeans.spi.palette.PaletteFactory;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Node;
import org.openide.util.lookup.Lookups;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//net.minisumo.world.robot.editor//Test//EN",
autostore = false)
@TopComponent.Description(preferredID = "TestTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "net.minisumo.world.robot.editor.TestTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_TestAction",
preferredID = "TestTopComponent")
public final class TestTopComponent extends TopComponent {

    private JScrollPane scenePane = new JScrollPane();
    private Scene sc = new Scene();

    public TestTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(TestTopComponent.class, "CTL_TestTopComponent"));
        setToolTipText(NbBundle.getMessage(TestTopComponent.class, "HINT_TestTopComponent"));
        VMDGraphScene scene = new VMDGraphScene();
        scenePane.setViewportView(scene.createView());
        VMDNodeWidget node1 = (VMDNodeWidget) scene.addNode("Node 1");
        node1.setNodeName("Node 1");
        VMDNodeWidget node2 = (VMDNodeWidget) scene.addNode("Node 2");
        node2.setNodeName("Node 2");
        VMDNodeWidget node3 = (VMDNodeWidget) scene.addNode("Node 3");
        node3.setNodeName("Node 3");
        VMDPinWidget p1 = (VMDPinWidget) scene.addPin("Node 1", "Pin 1");
        p1.setPinName("Pin 1");
        VMDPinWidget p2 = (VMDPinWidget) scene.addPin("Node 2", "Pin 2");
        p2.setPinName("Pin 2");
        VMDPinWidget p3 = (VMDPinWidget) scene.addPin("Node 2", "Pin 3");
        p3.setPinName("Pin 3");
        VMDPinWidget p4 = (VMDPinWidget) scene.addPin("Node 3", "Pin 4");
        p4.setPinName("Pin 4");
        scene.addEdge("Edge 1");
        scene.setEdgeSource("Edge 1", "Pin 1");
        scene.setEdgeTarget("Edge 1", "Pin 3");
        scene.addEdge("Edge 2");
        scene.setEdgeSource("Edge 2", "Pin 3");
        scene.setEdgeTarget("Edge 2", "Pin 4");
//        
//        MyGraphPinScene scene = new MyGraphPinScene();
//        scenePane.setViewportView(scene.createView());
//        scene.addNode("Node 1");
//        scene.addNode("Node 2");
//        scene.addNode("Node 3");
//        scene.addPin("Node 1", "p1");
//        scene.addPin("Node 2", "p2");
//        scene.addPin("Node 2", "p3");
//        scene.addPin("Node 3", "p4");
//        scene.addEdge("Edge 1");
//        scene.addEdge("Edge 2");
//        scene.setEdgeSource("Edge 1", "p1");
//        scene.setEdgeTarget("Edge 1", "p2");
//        scene.setEdgeSource("Edge 2", "p3");
//        scene.setEdgeTarget("Edge 2", "p4");
//        GridGraphLayout<String, String> layout = new GridGraphLayout<String, String>();
//        SceneLayout sceneLayout = LayoutFactory.createSceneGraphLayout(scene, layout);
//        sceneLayout.invokeLayout();
//        
//        scenePane.setViewportView(sc.createView());
//        LayerWidget layer1 = new LayerWidget(sc);
//        sc.addChild(layer1);
//        ImageWidget w1 = new ImageWidget(sc,
//                ImageUtilities.loadImage("net/minisumo/world/robot/editor/shopcart.png"));
//        layer1.addChild(w1);
//        ImageWidget w2 = new ImageWidget(sc,
//                ImageUtilities.loadImage("net/minisumo/world/robot/editor/shopcart.png"));
//        layer1.addChild(w2);
//        LayerWidget layer2 = new LayerWidget(sc);
//        sc.addChild(layer2);
//        ImageWidget w3 = new ImageWidget(sc,
//                ImageUtilities.loadImage("net/minisumo/world/robot/editor/shopcart.png"));
//        layer2.addChild(w3);
//        WidgetAction ma = ActionFactory.createMoveAction();
//        w1.getActions().addAction(ma);
//        w2.getActions().addAction(ma);
//        w3.getActions().addAction(ma);
//        add(new JLabel("aaa"));
        add(scenePane);
        //        org.netbeans.spi.palette.PaletteModule.
        Node root = new AbstractNode(new GenreNodeContainer());
        PaletteActions a = new MyPalette();
        PaletteController p = PaletteFactory.createPalette(root, a);
        associateLookup(Lookups.fixed(p));
//        PaletteController createPalette = net.minisumo.world.robot.editor.palette.ComponentPalette.createPalette();
//        associateLookup(Lookups.fixed(createPalette));
//        associateLookup(Lookups.fixed(PaletteFactory.createPalette(new AlbumNode("aaa"), new MyPalette())));
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
}
