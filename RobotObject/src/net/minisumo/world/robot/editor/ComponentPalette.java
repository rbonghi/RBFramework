/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world.robot.editor;

import java.io.IOException;
import javax.swing.Action;
import org.netbeans.spi.palette.PaletteActions;
import org.netbeans.spi.palette.PaletteController;
import org.netbeans.spi.palette.PaletteFactory;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author Raffaello
 */
public class ComponentPalette extends PaletteActions {

    //Add new buttons to the Palette Manager here:
    @Override
    public Action[] getImportActions() {
        return null;
    }

    //Add new contextual menu items to the palette here:
    @Override
    public Action[] getCustomPaletteActions() {
        return null;
    }

    //Add new contextual menu items to the categories here:
    @Override
    public Action[] getCustomCategoryActions(Lookup arg0) {
        return null;
    }

    //Add new contextual menu items to the items here:
    @Override
    public Action[] getCustomItemActions(Lookup arg0) {
        return null;
    }

    //Define the default action here:
    @Override
    public Action getPreferredAction(Lookup arg0) {
        return null;
    }
}
