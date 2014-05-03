/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.graphic.worldmap;

import java.util.Collection;
import java.util.logging.Logger;
import net.minisumo.world.JElement;
import org.openide.util.LookupEvent;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Lookup;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//net.minisumo.grahic.scope//Scope//EN",
autostore = false)
@TopComponent.Description(preferredID = "ScopeTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "editor", openAtStartup = true)
@ActionID(category = "Window", id = "net.minisumo.grahic.scope.ScopeTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_ScopeAction",
preferredID = "ScopeTopComponent")
public final class ScopeTopComponent extends TopComponent implements LookupListener {
    
    static final Logger logger = Logger.getLogger(ScopeTopComponent.class.getName());
    private Lookup.Result result = null;
    private WorldMap scope;

    public ScopeTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(ScopeTopComponent.class, "CTL_ScopeTopComponent"));
//        setToolTipText(NbBundle.getMessage(ScopeTopComponent.class, "HINT_ScopeTopComponent"));
        scope = new WorldMap();
        add(scope);       
//        scope.setScale(-0.5, 0.5);
//        scope.setComponent(true);
//        scope.setAutoset(true);
//        scope.setSquare(true);
//        scope.setRule(true);
//        scope.setShowText(true);
//        scope.setShowLine(true);
//        scope.setDynamic(true);
//        scope.setStoric(true);
//        scope.repaint();
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
        result = Utilities.actionsGlobalContext().lookupResult(JElement.class);
        result.addLookupListener(this);
    }

    @Override
    public void componentClosed() {
        result.removeLookupListener(this);
        result = null;
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
    public void resultChanged(LookupEvent lookupEvent) {
        Lookup.Result r = (Lookup.Result) lookupEvent.getSource();
        Collection c = r.allInstances();
        if (!c.isEmpty()) {
            Object d = c.iterator().next();
//                scope.setElement((JElement) d);
        }
    }
}