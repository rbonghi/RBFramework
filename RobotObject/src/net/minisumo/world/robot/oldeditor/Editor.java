/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world.robot.oldeditor;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.openide.awt.UndoRedo;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Raffaello
 */
public class Editor extends JPanel implements MultiViewElement {

    private JToolBar toolbar = new JToolBar();
    private MultiViewElementCallback callback = null;

    public Editor() {
//        initComponents();
        add(new JLabel("aaa"));
//        toolbar.add(new Panel1ToolbarAction1());
//        toolbar.add(new Panel1ToolbarAction2());
    }

    @Override
    public JComponent getVisualRepresentation() {
        return this;
    }

    @Override
    public JComponent getToolbarRepresentation() {
        return toolbar;
    }

    @Override
    public Action[] getActions() {
        if (callback != null) {
            return callback.createDefaultActions();
        } else {
            return new Action[]{};
        }
    }

    @Override
    public Lookup getLookup() {
        return Lookups.singleton(this);
    }

    @Override
    public void componentOpened() {
        callback.updateTitle("View 1");
    }

    @Override
    public void componentClosed() {
    }

    @Override
    public void componentShowing() {
    }

    @Override
    public void componentHidden() {
    }

    @Override
    public void componentActivated() {
        callback.updateTitle("View 1");
    }

    @Override
    public void componentDeactivated() {
    }

    @Override
    public UndoRedo getUndoRedo() {
        return UndoRedo.NONE;
    }

    @Override
    public void setMultiViewCallback(MultiViewElementCallback c) {
        callback = c;
    }

    @Override
    public CloseOperationState canCloseElement() {
        return CloseOperationState.STATE_OK;
    }
}
