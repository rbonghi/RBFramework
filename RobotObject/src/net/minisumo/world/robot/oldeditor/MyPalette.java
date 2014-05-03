/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world.robot.oldeditor;

import javax.swing.Action;
import org.netbeans.spi.palette.PaletteActions;
import org.openide.util.Lookup;

/**
 *
 * @author Raffaello
 */
public class MyPalette extends PaletteActions {

    @Override
    public Action[] getImportActions() {
        return new Action[0]; //TODO implement this
    }

    @Override
    public Action[] getCustomCategoryActions(Lookup category) {
        return new Action[0]; //TODO implement this
    }

    @Override
    public Action[] getCustomItemActions(Lookup item) {
        return new Action[0]; //TODO implement this
    }

    @Override
    public Action[] getCustomPaletteActions() {
        return new Action[0]; //TODO implement this
    }

    @Override
    public Action getPreferredAction(Lookup lkp) {
        return null;
    }
    
    
}
