/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.bridges;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.TimerTask;
import net.minisumo.util.DataInformation;
import net.minisumo.util.Finder;
import net.minisumo.world.AbstractRobot;

/**
 *
 * @author Raffaello
 */
public abstract class Updater extends TimerTask implements Bridge, PropertyChangeListener {

    protected transient AbstractRobot robot;

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() == robot) {
            if (evt.getPropertyName().equals(AbstractRobot.PROP_ENGINE)) {
                setEngine((Boolean) evt.getNewValue());
            } else if (evt.getPropertyName().equals(AbstractRobot.PROP_SENSOR)) {
                setSensor((Boolean) evt.getNewValue());
            } else if (evt.getPropertyName().equals(AbstractRobot.PROP_CONTROL)) {
                setControl((Boolean) evt.getNewValue());
            }
        }
    }

    protected abstract void setEngine(boolean engine);

    protected abstract void setSensor(boolean sensor);

    protected abstract void setControl(boolean control);

    public static HashMap<String, DataInformation> load(Updater updater, HashMap<Finder, DataInformation> data) {
        HashMap<String, DataInformation> parametersMap = Finder.parametersMap(data);
        for (DataInformation dataInformation : parametersMap.values()) {
            dataInformation.addPropertyChangeListener(updater);
        }
        return parametersMap;
    }
}
