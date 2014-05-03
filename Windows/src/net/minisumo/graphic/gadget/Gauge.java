/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.graphic.gadget;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.Timer;
import net.minisumo.graphic.Graphic;
import net.minisumo.util.DataInformation;

/**
 *
 * @author Master
 */
public class Gauge extends Component implements ActionListener, Graphic {

    private int diameter;                          //Lunghezza
    private final double MIN_VALUE = Math.PI * 1 / 4;    //Valore minimo posizione lancetta
    private final double MAX_VALUE = Math.PI * 7 / 4;    //Valore massimo posizione lancetta
    private double step;                           //Step di divisione
    private double enlarge = 1;
    private boolean absolute;
    private int scale;
    private Graphics2D g2;
    private Collection<DataInformation> allData;
    private int index;
    private String dimString = "";
    private FontMetrics metrics;

    public Gauge() {
        allData = new ArrayList<DataInformation>();
        new Timer(100, this).start();
        step = (MAX_VALUE - MIN_VALUE) * enlarge / (2 * 5);
        setPreferredSize(new Dimension(200, 200));
    }

    public void setAllData(Collection<DataInformation> allData) {
        this.allData = allData;
    }

    @Override
    public void paint(Graphics g) {
        this.g2 = (Graphics2D) g;
        metrics = g2.getFontMetrics();
        config();
        background();
        index = 1;
        for (DataInformation data : allData) {
            drawGauge(data);
        }
        g2.setColor(Color.WHITE);
        g2.drawString(dimString, (diameter - metrics.stringWidth(dimString)) / 2, diameter / 2 + diameter / 4);
    }

    private void drawGauge(DataInformation data) {
        String dimension = data.getDimension();
        //Dimensioni fisiche della lancetta
        if (enlarge != 1) {
            dimString = "x" + String.format("%.1f", enlarge) + " " + dimension;
        } else {
            dimString = dimension;
        }
        g2.setColor(data.getColor());
        String nameString = data.getName();
        g2.drawString(nameString, (diameter - metrics.stringWidth(nameString)) / 2, diameter / 2 + index++ * (diameter / 12) + diameter / 4);

        //Proporzione valore, con scala
        double valueGauge;
        double lastData = 0;
        lastData = data.getLastData();
        if (absolute) {
            valueGauge = (lastData * step / enlarge) + MIN_VALUE;
        } else {
            valueGauge = (lastData * step / enlarge) + Math.PI;
        }
        if (valueGauge > MAX_VALUE + MIN_VALUE / 6) {
            valueGauge = MAX_VALUE + MIN_VALUE / 6;
        }
        if (valueGauge < MIN_VALUE * 5 / 6) {
            valueGauge = MIN_VALUE * 5 / 6;
        }
        //Lancetta
        AffineTransform saveXform = g2.getTransform();              //Salvataggio trasformazione
        g2.translate(diameter / 2, diameter / 2);
        g2.rotate(valueGauge);
        g2.setColor(data.getColor());
        int x1Points[] = {5, -5, 0};
        int y1Points[] = {0, 0, diameter / 2};
        GeneralPath polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD, x1Points.length);
        polygon.moveTo(x1Points[0], y1Points[0]);
        for (int i = 1; i < x1Points.length; i++) {
            polygon.lineTo(x1Points[i], y1Points[i]);
        }
        polygon.closePath();
        g2.fill(polygon);
        g2.setTransform(saveXform);                                 //Caricamento precedente trasformazione
    }

    private void background() {
        //Sfondo
        g2.setColor(Color.BLACK);
        g2.fill(new Ellipse2D.Double(0, 0, diameter, diameter));
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(4));
        g2.draw(new Ellipse2D.Double(0, 0, diameter, diameter));

        //Scala valori
        for (double i = MIN_VALUE; i <= MAX_VALUE; i += step) {
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(3));
            g2.draw(new Line2D.Double(diameter / 2 - 2 * diameter / 5 * Math.sin(i), diameter / 2 + 2 * diameter / 5 * Math.cos(i),
                    diameter / 2 - diameter / 2 * Math.sin(i), diameter / 2 + diameter / 2 * Math.cos(i)));
            if (absolute) {
                g2.drawString("" + (int) Math.round((i - MIN_VALUE) / step),
                        (float) (diameter / 2 - (Math.sin(i) * diameter * 3 / 10)),
                        (float) (diameter / 2 + (Math.cos(i) * diameter * 3 / 10)));

            } else {
                g2.drawString("" + ((int) Math.round((i - MIN_VALUE) / step) - (int) ((MAX_VALUE - MIN_VALUE) / (2 * step))),
                        diameter / 2 - (float) (Math.sin(i) * diameter * 3 / 10),
                        diameter / 2 + (float) (Math.cos(i) * diameter * 3 / 10));
            }
        }

        //Scala interna
        for (double i = MIN_VALUE; i <= MAX_VALUE; i = i + step / 2) {
            g2.setStroke(new BasicStroke(1));
            g2.draw(new Line2D.Double(diameter / 2 - 9 * diameter / 20 * Math.sin(i), diameter / 2 + 9 * diameter / 20 * Math.cos(i),
                    diameter / 2 - diameter / 2 * Math.sin(i), diameter / 2 + diameter / 2 * Math.cos(i)));
            g2.draw(new Line2D.Double(diameter / 2 - 2 * diameter / 9 * Math.sin(i), diameter / 2 + 2 * diameter / 9 * Math.cos(i),
                    diameter / 2 - diameter / 10 * Math.sin(i), diameter / 2 + diameter / 10 * Math.cos(i)));
        }
        g2.draw(new Arc2D.Double(0, 0, (double) diameter / 5, (double) diameter / 5, Math.PI / 2 + MIN_VALUE, Math.PI / 2 + MAX_VALUE, Arc2D.OPEN));

        //Blocco lancetta
        g2.setColor(Color.RED);
        g2.setStroke(new BasicStroke(3));
        Point2D pMin = new Point2D.Double(diameter / 2 - diameter * 2 / 5 * Math.sin(MIN_VALUE * 5 / 6), diameter / 2 + diameter * 2 / 5 * Math.cos(MIN_VALUE * 5 / 6));
        Point2D pMax = new Point2D.Double(diameter / 2 - diameter * 2 / 5 * Math.sin(MAX_VALUE + MIN_VALUE / 6), diameter / 2 + diameter * 2 / 5 * Math.cos(MAX_VALUE + MIN_VALUE / 6));
        g2.draw(new Line2D.Double(pMin, pMin));
        g2.draw(new Line2D.Double(pMax, pMax));

        //Linea rossa dello zero
        if (!absolute) {
            g2.setColor(Color.RED);
            g2.setStroke(new BasicStroke(3));
            g2.draw(new Line2D.Double(diameter / 2, diameter / 2 - 2 * diameter / 5, diameter / 2, diameter / 2 - diameter / 2));
        }
    }

    protected void config() {
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHints(rh);
        RenderingHints rh2 = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHints(rh2);
        Dimension size = getSize();
        if (size.width > size.height) {
            diameter = size.height;
        } else {
            diameter = size.width;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    public boolean isAbsolute() {
        return absolute;
    }

    public void setAbsolute(boolean absolute) {
        this.absolute = absolute;
    }
}
