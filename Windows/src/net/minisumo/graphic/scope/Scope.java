/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.graphic.scope;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableSet;
import java.util.Set;
import net.minisumo.graphic.AbstractScope;
import net.minisumo.util.DataInformation;

/**
 *
 * @author Raffaello
 */
public class Scope extends AbstractScope {

    private final int RULE = 40;                 //Dimensione bordo righello
    private final int LEGEND = 20, LINE_LEGEND = 15;
    private final int numberLineH = 20;          //Numero di linee orizzontali
    private Graphics2D g2;
    private FontMetrics metrics;
    protected int width, height;
    protected double scale;
    protected double dimensionMax, dimensionMin;
    protected double origin;
    protected double accuracy = 0.1;
    protected Collection<DataInformation> allDataCollection;
    private int notch = 2;
    private String dimension = "", timeString = "";
    private int spacing = 0;

    public Scope() {
        setBackground(new Color(120, 120, 120));
        allDataCollection = new ArrayList<DataInformation>();
    }

    @Override
    protected void configRenderSize() {
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHints(rh);
        RenderingHints rh2 = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHints(rh2);
        Dimension size = getSize();
        width = size.width;
        height = size.height;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2 = (Graphics2D) g;
        metrics = g2.getFontMetrics();
        //Configura il rendering e la dimensione
        configRenderSize();
        //Disegna la griglia di sfondo
        background();
        spacing = RULE + 10;
        double allDataMax = 0, allDataMin = 0;
        ArrayList<DataInformation> allData = new ArrayList<DataInformation>(allDataCollection);
        for (int i = 0; i < allData.size(); i++) {
            DataInformation data = allData.get(i);
            if (data.getData() != null) {
                double[] element = getElement(data);
                if (i == 0) {
                    allDataMin = element[0];
                    allDataMax = element[1];
                } else {
                    allDataMin = Math.min(allDataMin, element[0]);
                    allDataMax = Math.max(allDataMax, element[1]);
                }
            }
            //Legenda
            legend(data);
        }
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(1));
        //Dimension String
        g2.drawString(dimension, (RULE - metrics.stringWidth(dimension)) / 2, height - LEGEND / 3);
        // Time information
        g2.draw(new Line2D.Double(width - 10, height - LEGEND / 2, width - 10 - width / numberLineH, height - LEGEND / 2));
        int stringWidth = metrics.stringWidth(timeString);
        g2.drawString(timeString, width - stringWidth - 20 - width / numberLineH, height - LEGEND / 3);
        //Autoset
        dimensionMin = allDataMin - notch * accuracy;
        dimensionMax = allDataMax + notch * accuracy;
    }

    /**
     * Disegno grafico elementi
     * @param data
     * @return 
     */
    protected double[] getElement(DataInformation data) {
        Color color = data.getColor();
        g2.setColor(color);
        g2.setStroke(new BasicStroke(3));
        double dimMax = 0, dimMin = 0;
        Set<Entry<Long, Double>> entrySet = data.getData().getMap().entrySet();
        boolean firstData = true;
        double totaltime = data.getData().getMap().lastKey() - data.getData().getMap().firstKey();
        double first = 0, firstMills = 0, lastMills = 0;
        for (Entry<Long, Double> entry : entrySet) {
            double second = entry.getValue();
            if (firstData) {
                first = second;
                firstData = false;
                dimMin = first;
                dimMax = first;
                firstMills = entry.getKey();
            }
            double thisMills = entry.getKey() - firstMills;
            g2.draw(new Line2D.Double(lastMills * (width - RULE) / totaltime + RULE, -(first - dimensionMax) * scale,
                    thisMills * (width - RULE) / totaltime + RULE, -(second - dimensionMax) * scale));
            dimMin = Math.min(dimMin, second);
            dimMax = Math.max(dimMax, second);
            first = second;
            lastMills = thisMills;
        }
        //Limiti dato
        if (data.isLimited()) {
            g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 150));
            if (dimensionMax * scale < (height - LEGEND) && dimensionMax >= data.getMaxLimit()) {
                g2.draw(new Line2D.Double(RULE - 4, -(data.getMaxLimit() - dimensionMax) * scale,
                        width, -(data.getMaxLimit() - dimensionMax) * scale));
            }
            if (data.getMinLimit() - dimensionMax >= -(height - LEGEND) / scale && dimensionMax > 0) {
                g2.draw(new Line2D.Double(RULE - 4, -(data.getMinLimit() - dimensionMax) * scale,
                        width, -(data.getMinLimit() - dimensionMax) * scale));
            }
        }
        return new double[]{dimMin, dimMax};
    }

    /**
     * Legenda
     */
    protected void legend(DataInformation data) {
        dimension = data.getDimension();
        double totalTime = data.getTotalTime();
        timeString = String.format("%.1f", totalTime / numberLineH) + "s";
        g2.setColor(data.getColor());
        g2.setStroke(new BasicStroke(3));
        g2.draw(new Line2D.Double(spacing, height - LEGEND / 2, LINE_LEGEND + spacing, height - LEGEND / 2));
        g2.setColor(Color.WHITE);
        int stringWidth = metrics.stringWidth(data.getName());
        g2.drawString(data.getName(), spacing + LINE_LEGEND + 5, height - LEGEND / 3);
        spacing += stringWidth + LINE_LEGEND + 20;
    }

    @Override
    protected void background() {
        //Linea
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.draw(new Line2D.Double(0, height - LEGEND, width, height - LEGEND));
        g2.draw(new Line2D.Double(RULE, 0, RULE, height));
        //Scala
        scale = (height - LEGEND) / (dimensionMax - dimensionMin);
        //Disegno griglia parallela ad X
        getGridX();
        //Disegno griglia parallela ad Y
        getGridTime();
        //Sfondo righello
        g2.setStroke(new BasicStroke(2));
        Rectangle2D rect = new Rectangle2D.Double(0, 0, RULE, height - LEGEND);
        g2.setColor(new Color(0, 0, 255, 100));
        g2.draw(rect);
        g2.setColor(new Color(0, 0, 255, 30));
        g2.fill(rect);
    }

    @Override
    protected void getGridX() {
        double err = (height - LEGEND) / scale - (dimensionMax - dimensionMin);
        double originRel = origin + dimensionMax + err / 2;
        //Assi paralleli ad X
        double errorX = Math.floor(-originRel) + originRel;
        for (double i = 0; (i + errorX) < (height - LEGEND) / scale; i += accuracy) {
            if ((i + errorX) > 0) {
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(1));
                g2.draw(new Line2D.Double(0, (i + errorX) * scale, width, (i + errorX) * scale));
                g2.drawString(String.format("%.1f", dimensionMax - (i + errorX)), RULE / 2, (int) ((i + errorX) * scale));
            }
        }
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(4));
        //Asse X
        if (dimensionMax * scale < (height - LEGEND) && dimensionMax * scale > 0) {
            g2.draw(new Line2D.Double(RULE - 4, originRel * scale, width, originRel * scale));
        }
    }

    protected void getGridTime() {
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(1));
        //Assi paralleli ad Y
        for (int i = 1; i < numberLineH; i++) {
            if (i * width / numberLineH >= RULE) {
                g2.draw(new Line2D.Double(i * width / numberLineH, 0, i * width / numberLineH, height - LEGEND));
            }
        }
    }

    public void setAllData(Collection<DataInformation> allData) {
        this.allDataCollection = allData;
    }
}
