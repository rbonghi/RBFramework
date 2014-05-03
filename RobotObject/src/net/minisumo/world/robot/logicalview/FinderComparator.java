/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world.robot.logicalview;

import java.util.Comparator;
import java.util.Map.Entry;
import net.minisumo.util.DataInformation;
import net.minisumo.util.Finder;

/**
 *
 * @author Raffaello
 */
public class FinderComparator implements Comparator<Entry<Finder, DataInformation>> {

    @Override
    public int compare(Entry<Finder, DataInformation> o1, Entry<Finder, DataInformation> o2) {
        String name1, name2;
        if (o1.getKey().getComponent() != null && o2.getKey().getComponent() == null) {
            name1 = o1.getKey().getComponent().getName() + "-" + o1.getValue().getName();
            name2 = o2.getValue().getName();
        } else if (o1.getKey().getComponent() == null && o2.getKey().getComponent() != null) {
            name1 = o1.getValue().getName();
            name2 = o2.getKey().getComponent().getName() + "-" + o2.getValue().getName();
        } else if (o1.getKey().getComponent() != null && o2.getKey().getComponent() != null) {
            name1 = o1.getKey().getComponent().getName() + "-" + o1.getValue().getName();
            name2 = o2.getKey().getComponent().getName() + "-" + o2.getValue().getName();
        } else {
            name1 = o1.getValue().getName();
            name2 = o2.getValue().getName();
        }
        return name1.compareTo(name2);
    }
}
