/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world.robot.oldeditor;

import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.graph.GraphPinScene;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.router.RouterFactory;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.ImageWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Raffaello
 */
public class MyGraphPinScene extends GraphPinScene<String, String, String> {

    private LayerWidget mainLayer;
    private LayerWidget connectionLayer;

    public MyGraphPinScene() {
        mainLayer = new LayerWidget(this);
        addChild(mainLayer);
        connectionLayer = new LayerWidget(this);
        addChild(connectionLayer);
    }

    protected Widget attachNodeWidget(String node) {
        IconNodeWidget widget = new IconNodeWidget(this);
        widget.setImage(
                ImageUtilities.loadImage("net/minisumo/world/robot/editor/shopcart.png"));
        widget.getLabelWidget().setLayout(
                LayoutFactory.createHorizontalFlowLayout(
                LayoutFactory.SerialAlignment.JUSTIFY, 5));
        widget.getActions().addAction(ActionFactory.createMoveAction());
        mainLayer.addChild(widget);
        return widget;
    }

    protected Widget attachEdgeWidget(String edge) {
        ConnectionWidget widget = new ConnectionWidget(this);
        widget.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
        widget.setRouter(RouterFactory.createOrthogonalSearchRouter(
                mainLayer, connectionLayer));
        connectionLayer.addChild(widget);
        return widget;
    }

    protected Widget attachPinWidget(String node, String pin) {
        ImageWidget widget = new ImageWidget(this,
                ImageUtilities.loadImage("com/galileo/netbeans/module/pin.gif"));
        IconNodeWidget n = (IconNodeWidget) findWidget(node);
        n.getLabelWidget().addChild(widget);
        return widget;
    }

    protected void attachEdgeSourceAnchor(String edge, String oldPin, String pin) {
        ConnectionWidget c = (ConnectionWidget) findWidget(edge);
        Widget widget = findWidget(pin);
        Anchor a = AnchorFactory.createRectangularAnchor(widget);
        c.setSourceAnchor(a);
    }

    protected void attachEdgeTargetAnchor(String edge, String oldPin, String pin) {
        ConnectionWidget c = (ConnectionWidget) findWidget(edge);
        Widget widget = findWidget(pin);
        Anchor a = AnchorFactory.createRectangularAnchor(widget);
        c.setTargetAnchor(a);
    }
}