/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.graphic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author Raffaello
 */
public abstract class AbstractScope extends JPanel implements ActionListener, Graphic {

    public AbstractScope() {
        new Timer(100, this).start();
    }

    protected abstract void configRenderSize();
    
    protected abstract void background();

    protected abstract void getGridX();

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
