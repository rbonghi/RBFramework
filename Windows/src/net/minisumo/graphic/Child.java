/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.graphic;

import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minisumo.bridges.Bridge;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

/**
 *
 * @author Raffaello
 */
class Child extends ChildFactory implements PropertyChangeListener {

    static final Logger logger = Logger.getLogger(Child.class.getName());
    private List childs = new ArrayList();
    private Object obj;
    private PropertyDescriptor property;
    private EventSetDescriptor event;

    public Child(Object obj) {
        this.obj = obj;
        if (obj != null) {
            Class objClass = obj.getClass();
            if (objClass != Class.class) {
                if (objClass.isArray()) {
                    int n = Array.getLength(obj);
                    for (int i = 0; i < n; i++) {
                        childs.add(Array.get(obj, i));
                    }
                } else if (AbstractCollection.class.isInstance(obj)) {
                    childs.addAll((AbstractCollection) obj);
                } else if (AbstractMap.class.isInstance(obj)) {
                    HashMap p = (HashMap) obj;
                    childs.addAll(p.values());
                } else {
                    try {
                        BeanInfo bean = Introspector.getBeanInfo(objClass);
                        Class customClass = bean.getBeanDescriptor().getCustomizerClass();
                        if (customClass == null) {
                            PropertyDescriptor[] properties = Introspector.getBeanInfo(objClass).getPropertyDescriptors();
                            EventSetDescriptor[] events = Introspector.getBeanInfo(obj.getClass()).getEventSetDescriptors();
                            for (EventSetDescriptor ievent : events) {
                                if (PropertyChangeListener.class.equals(ievent.getListenerType())) {
                                    logger.log(Level.FINE, "{0}", ievent.getListenerType());
                                    event = ievent;
                                    ievent.getAddListenerMethod().invoke(obj, this);
                                }
                            }
                            for (PropertyDescriptor iprop : properties) {
                                Object thisObj = iprop.getReadMethod().invoke(obj, new Object[0]);
                                if (thisObj != null) {
                                    Class thisProperty = iprop.getPropertyType();
                                    if (thisProperty.isArray() && thisProperty.getComponentType().isPrimitive()) {
//                                    childs.add(thisObj);
                                    } else if (AbstractCollection.class.isInstance(thisObj)) {
                                        childs.addAll((AbstractCollection) thisObj);
                                    } else if (thisProperty.isInterface()) {
                                        childs.add(thisObj);
                                    } else if (AbstractMap.class.isInstance(thisObj)) {
                                        childs.add(thisObj);
                                    }
                                }
                            }
                        }
                    } catch (Exception ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            }
        }
    }

    @Override
    protected boolean createKeys(List list) {
        list.addAll(childs);
        return true;
    }

    @Override
    protected Node createNodeForKey(Object key) {
        try {
            return new BNode(key);
        } catch (IntrospectionException ex) {
            return null;
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (Bridge.class.isInstance(evt.getNewValue())) {
            if (evt.getNewValue() == null) {
                childs.remove(evt.getOldValue());
            } else if (evt.getOldValue() == null) {
                childs.add(evt.getNewValue());
            } else {
                for (int i = 0; i < childs.size(); i++) {
                    if (evt.getOldValue() == childs.get(i)) {
                        if (evt.getNewValue() != null) {
                            try {
                                event.getRemoveListenerMethod().invoke(obj, childs.get(i));
                            } catch (Exception ex) {
                                Exceptions.printStackTrace(ex);
                            }
                            childs.set(i, evt.getNewValue());
                            break;
                        }
                    }
                }
            }
            refresh(true);
        }
    }
}
