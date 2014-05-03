/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.graphic.gadget;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import net.minisumo.graphic.Graphic;

/**
 *
 * @author Master
 */
public class RssiSignal extends Component implements Graphic {

    public static final int MAX_SIGNAL = 23, MIN_SIGNAL = 94;
    private static final int DELTA_SIGNAL = MIN_SIGNAL - MAX_SIGNAL;
    private int width, height;
    private int spacingLine;
    private int numberLine = 8;
    private Graphics2D g2;
    private int signal = MAX_SIGNAL;

    public RssiSignal() {
        spacingLine = 3;
        setPreferredSize(new Dimension(200, 200));
    }

    @Override
    public void paint(Graphics g) {
        this.g2 = (Graphics2D) g;
        g2.setBackground(new Color(200, 200, 200, 100));
        g2.setStroke(new BasicStroke(3));
        Dimension size = getSize();
        width = size.width;
        height = size.height;
        for (int i = 0; i < numberLine; i++) {
            if (i * DELTA_SIGNAL >= (signal - MAX_SIGNAL) * numberLine) {
                g2.setColor(new Color(255 - i * 255 / numberLine, 0, i * 255 / numberLine));
                g2.fill(new Line2D.Double(i * spacingLine + 4, i * (height - 28) / (numberLine - 1) + 4, width - 4, i * (height - 28) / (numberLine - 1) + 4));
            }
        }
        g2.setColor(Color.BLUE);
        g2.drawString("-" + signal + " dbm", width / 2, height);
    }

    public void setSignal(int signal) {
        this.signal = signal;
    }

    public int getSignal() {
        return signal;
    }
}
