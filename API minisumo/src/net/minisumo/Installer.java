/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo;

import java.beans.PropertyEditorManager;
import net.minisumo.bridges.Bridge;
import net.minisumo.bridges.BridgeEditor;
import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall {

    //13A200:404A564E
    
    @Override
    public void restored() {
        PropertyEditorManager.registerEditor(Bridge.class, BridgeEditor.class);
    }
}
