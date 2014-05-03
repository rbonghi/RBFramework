/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.simulator;

import java.beans.*;

/**
 *
 * @author Raffaello
 */
public class SimulatorBeanInfo extends SimpleBeanInfo {

    // Bean descriptor//GEN-FIRST:BeanDescriptor
    /*lazy BeanDescriptor*/
    private static BeanDescriptor getBdescriptor(){
        BeanDescriptor beanDescriptor = new BeanDescriptor  ( net.minisumo.simulator.Simulator.class , null ); // NOI18N//GEN-HEADEREND:BeanDescriptor

        // Here you can add code for customizing the BeanDescriptor.

        return beanDescriptor;     }//GEN-LAST:BeanDescriptor
    // Property identifiers//GEN-FIRST:Properties
    private static final int PROPERTY_maxTensL = 0;
    private static final int PROPERTY_maxTensR = 1;
    private static final int PROPERTY_tauL = 2;
    private static final int PROPERTY_tauR = 3;
    private static final int PROPERTY_timeUpdate = 4;

    // Property array 
    /*lazy PropertyDescriptor*/
    private static PropertyDescriptor[] getPdescriptor(){
        PropertyDescriptor[] properties = new PropertyDescriptor[5];
    
        try {
            properties[PROPERTY_maxTensL] = new PropertyDescriptor ( "maxTensL", net.minisumo.simulator.Simulator.class, "getMaxTensL", "setMaxTensL" ); // NOI18N
            properties[PROPERTY_maxTensR] = new PropertyDescriptor ( "maxTensR", net.minisumo.simulator.Simulator.class, "getMaxTensR", "setMaxTensR" ); // NOI18N
            properties[PROPERTY_tauL] = new PropertyDescriptor ( "tauL", net.minisumo.simulator.Simulator.class, "getTauL", "setTauL" ); // NOI18N
            properties[PROPERTY_tauR] = new PropertyDescriptor ( "tauR", net.minisumo.simulator.Simulator.class, "getTauR", "setTauR" ); // NOI18N
            properties[PROPERTY_timeUpdate] = new PropertyDescriptor ( "timeUpdate", net.minisumo.simulator.Simulator.class, "getTimeUpdate", "setTimeUpdate" ); // NOI18N
            properties[PROPERTY_timeUpdate].setExpert ( true );
        }
        catch(IntrospectionException e) {
            e.printStackTrace();
        }//GEN-HEADEREND:Properties

        // Here you can add code for customizing the properties array.

        return properties;     }//GEN-LAST:Properties
    // EventSet identifiers//GEN-FIRST:Events

    // EventSet array
    /*lazy EventSetDescriptor*/
    private static EventSetDescriptor[] getEdescriptor(){
        EventSetDescriptor[] eventSets = new EventSetDescriptor[0];//GEN-HEADEREND:Events

        // Here you can add code for customizing the event sets array.

        return eventSets;     }//GEN-LAST:Events
    // Method identifiers//GEN-FIRST:Methods
    private static final int METHOD_cancel0 = 0;
    private static final int METHOD_load1 = 1;
    private static final int METHOD_loadBridge2 = 2;
    private static final int METHOD_propertyChange3 = 3;
    private static final int METHOD_register4 = 4;
    private static final int METHOD_run5 = 5;
    private static final int METHOD_scheduledExecutionTime6 = 6;
    private static final int METHOD_stop7 = 7;

    // Method array 
    /*lazy MethodDescriptor*/
    private static MethodDescriptor[] getMdescriptor(){
        MethodDescriptor[] methods = new MethodDescriptor[8];
    
        try {
            methods[METHOD_cancel0] = new MethodDescriptor(java.util.TimerTask.class.getMethod("cancel", new Class[] {})); // NOI18N
            methods[METHOD_cancel0].setDisplayName ( "" );
            methods[METHOD_load1] = new MethodDescriptor(net.minisumo.bridges.Updater.class.getMethod("load", new Class[] {net.minisumo.bridges.Updater.class, java.util.HashMap.class})); // NOI18N
            methods[METHOD_load1].setDisplayName ( "" );
            methods[METHOD_loadBridge2] = new MethodDescriptor(net.minisumo.simulator.Simulator.class.getMethod("loadBridge", new Class[] {java.io.ObjectInputStream.class})); // NOI18N
            methods[METHOD_loadBridge2].setDisplayName ( "" );
            methods[METHOD_propertyChange3] = new MethodDescriptor(net.minisumo.simulator.Simulator.class.getMethod("propertyChange", new Class[] {java.beans.PropertyChangeEvent.class})); // NOI18N
            methods[METHOD_propertyChange3].setDisplayName ( "" );
            methods[METHOD_register4] = new MethodDescriptor(net.minisumo.simulator.Simulator.class.getMethod("register", new Class[] {net.minisumo.world.AbstractRobot.class})); // NOI18N
            methods[METHOD_register4].setDisplayName ( "" );
            methods[METHOD_run5] = new MethodDescriptor(net.minisumo.simulator.Simulator.class.getMethod("run", new Class[] {})); // NOI18N
            methods[METHOD_run5].setDisplayName ( "" );
            methods[METHOD_scheduledExecutionTime6] = new MethodDescriptor(java.util.TimerTask.class.getMethod("scheduledExecutionTime", new Class[] {})); // NOI18N
            methods[METHOD_scheduledExecutionTime6].setDisplayName ( "" );
            methods[METHOD_stop7] = new MethodDescriptor(net.minisumo.simulator.Simulator.class.getMethod("stop", new Class[] {})); // NOI18N
            methods[METHOD_stop7].setDisplayName ( "" );
        }
        catch( Exception e) {}//GEN-HEADEREND:Methods

        // Here you can add code for customizing the methods array.
        
        return methods;     }//GEN-LAST:Methods
    private static java.awt.Image iconColor16 = null;//GEN-BEGIN:IconsDef
    private static java.awt.Image iconColor32 = null;
    private static java.awt.Image iconMono16 = null;
    private static java.awt.Image iconMono32 = null;//GEN-END:IconsDef
    private static String iconNameC16 = "simulator.gif";//GEN-BEGIN:Icons
    private static String iconNameC32 = null;
    private static String iconNameM16 = null;
    private static String iconNameM32 = null;//GEN-END:Icons
    private static final int defaultPropertyIndex = -1;//GEN-BEGIN:Idx
    private static final int defaultEventIndex = -1;//GEN-END:Idx

//GEN-FIRST:Superclass
    // Here you can add code for customizing the Superclass BeanInfo.
//GEN-LAST:Superclass
    /**
     * Gets the bean's <code>BeanDescriptor</code>s.
     * 
     * @return BeanDescriptor describing the editable
     * properties of this bean.  May return null if the
     * information should be obtained by automatic analysis.
     */
    public BeanDescriptor getBeanDescriptor() {
        return getBdescriptor();
    }

    /**
     * Gets the bean's <code>PropertyDescriptor</code>s.
     * 
     * @return An array of PropertyDescriptors describing the editable
     * properties supported by this bean.  May return null if the
     * information should be obtained by automatic analysis.
     * <p>
     * If a property is indexed, then its entry in the result array will
     * belong to the IndexedPropertyDescriptor subclass of PropertyDescriptor.
     * A client of getPropertyDescriptors can use "instanceof" to check
     * if a given PropertyDescriptor is an IndexedPropertyDescriptor.
     */
    public PropertyDescriptor[] getPropertyDescriptors() {
        return getPdescriptor();
    }

    /**
     * Gets the bean's <code>EventSetDescriptor</code>s.
     * 
     * @return  An array of EventSetDescriptors describing the kinds of 
     * events fired by this bean.  May return null if the information
     * should be obtained by automatic analysis.
     */
    public EventSetDescriptor[] getEventSetDescriptors() {
        return getEdescriptor();
    }

    /**
     * Gets the bean's <code>MethodDescriptor</code>s.
     * 
     * @return  An array of MethodDescriptors describing the methods 
     * implemented by this bean.  May return null if the information
     * should be obtained by automatic analysis.
     */
    public MethodDescriptor[] getMethodDescriptors() {
        return getMdescriptor();
    }

    /**
     * A bean may have a "default" property that is the property that will
     * mostly commonly be initially chosen for update by human's who are 
     * customizing the bean.
     * @return  Index of default property in the PropertyDescriptor array
     * 		returned by getPropertyDescriptors.
     * <P>	Returns -1 if there is no default property.
     */
    public int getDefaultPropertyIndex() {
        return defaultPropertyIndex;
    }

    /**
     * A bean may have a "default" event that is the event that will
     * mostly commonly be used by human's when using the bean. 
     * @return Index of default event in the EventSetDescriptor array
     *		returned by getEventSetDescriptors.
     * <P>	Returns -1 if there is no default event.
     */
    public int getDefaultEventIndex() {
        return defaultEventIndex;
    }

    /**
     * This method returns an image object that can be used to
     * represent the bean in toolboxes, toolbars, etc.   Icon images
     * will typically be GIFs, but may in future include other formats.
     * <p>
     * Beans aren't required to provide icons and may return null from
     * this method.
     * <p>
     * There are four possible flavors of icons (16x16 color,
     * 32x32 color, 16x16 mono, 32x32 mono).  If a bean choses to only
     * support a single icon we recommend supporting 16x16 color.
     * <p>
     * We recommend that icons have a "transparent" background
     * so they can be rendered onto an existing background.
     *
     * @param  iconKind  The kind of icon requested.  This should be
     *    one of the constant values ICON_COLOR_16x16, ICON_COLOR_32x32, 
     *    ICON_MONO_16x16, or ICON_MONO_32x32.
     * @return  An image object representing the requested icon.  May
     *    return null if no suitable icon is available.
     */
    public java.awt.Image getIcon(int iconKind) {
        switch (iconKind) {
            case ICON_COLOR_16x16:
                if (iconNameC16 == null) {
                    return null;
                } else {
                    if (iconColor16 == null) {
                        iconColor16 = loadImage(iconNameC16);
                    }
                    return iconColor16;
                }
            case ICON_COLOR_32x32:
                if (iconNameC32 == null) {
                    return null;
                } else {
                    if (iconColor32 == null) {
                        iconColor32 = loadImage(iconNameC32);
                    }
                    return iconColor32;
                }
            case ICON_MONO_16x16:
                if (iconNameM16 == null) {
                    return null;
                } else {
                    if (iconMono16 == null) {
                        iconMono16 = loadImage(iconNameM16);
                    }
                    return iconMono16;
                }
            case ICON_MONO_32x32:
                if (iconNameM32 == null) {
                    return null;
                } else {
                    if (iconMono32 == null) {
                        iconMono32 = loadImage(iconNameM32);
                    }
                    return iconMono32;
                }
            default:
                return null;
        }
    }
}
