/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.graphic.worldmap;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLayeredPane;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *
 * @author Raffaello
 */
public class PopupMenuElement {

    private JPopupMenu elementMenu = new JPopupMenu();
    private JMenuItem name, coord;

    public PopupMenuElement(JLayeredPane layeredPane) {
        name = new JMenuItem("Name: ");
        coord = new JMenuItem("Coordinate: ");
        elementMenu.add(name);
        elementMenu.add(coord);
        layeredPane.add(elementMenu);
        layeredPane.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    elementMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    elementMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }
}
