/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.graphic.worldmap;

import java.awt.event.ActionEvent;
import java.beans.IntrospectionException;
import net.minisumo.graphic.scope.XYScope;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import net.minisumo.bridges.Bridge;
import net.minisumo.bridges.Wireless;
import net.minisumo.graphic.gadget.RssiSignal;
import net.minisumo.util.DataInformation;
import net.minisumo.util.Finder;
import net.minisumo.world.AbstractRobot;
import net.minisumo.world.JElement;
import net.minisumo.world.World;
import net.minisumo.world.component.Component;
import net.minisumo.world.sensors.Sensor;
import org.openide.util.Exceptions;

/**
 *
 * @author Raffaello
 */
public class WorldMap extends XYScope implements PropertyChangeListener {

    private static final String CONTROL = "HighControl";
    private final int SPACE_LABEL = 10, SPACE_ANGLE = 80, SPACE_TEXTY = 15;
    protected Collection<JElement> worldCollection;
    private int counter = 0;
    private JPopupMenu menu = new JPopupMenu();
    private boolean storic = true;
    private FontMetrics metrics;
    private double xClick, yClick;
    private HashMap<JElement, PointSet> pointSet = new HashMap<JElement, PointSet>();
    private HashMap<JElement, JMenu> menuRobot = new HashMap<JElement, JMenu>();
    private HashMap<JElement, RssiSignal> signalRobot = new HashMap<JElement, RssiSignal>();
//    private HashMap<JElement, Shape> mapShapeElement = new HashMap<JElement, Shape>();

    public WorldMap() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBackground(new Color(120, 120, 120));
        worldCollection = World.getDefault().getWorld().values();
        World.getDefault().addPropertyChangeListener(this);
//        setDoubleBuffered(true);
        final JMenuItem coord = new JMenuItem();
        final JCheckBoxMenuItem enableClik = new JCheckBoxMenuItem("Enable Map");
        coord.setEnabled(false);
        menu.add(coord);
        menu.add(enableClik);
        menu.addSeparator();
        add(menu);
        addMouseListener(new MouseAdapter() {

//            private double xClick, yClick;
//            private static final String CONTROL = "HighControl";
            @Override
            public void mouseClicked(MouseEvent e) {
                if (enableClik.isSelected()) {
                    updateCoord(e);
                    JElement thisElement = World.getDefault().getThisElement();
                    if (thisElement instanceof AbstractRobot) {
                        AbstractRobot robot = (AbstractRobot) thisElement;
                        if (robot.getComponents().get(CONTROL) != null) {
                            robot.getComponents().get(CONTROL).getParameters().get("riferX").setData(xClick);
                            robot.getComponents().get(CONTROL).getParameters().get("riferY").setData(yClick);
                            robot.setControl(true);
                            pointSet.put(thisElement, new PointSet(xClick, yClick, robot.getColor()));
                        }
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                coord.setText(updateCoord(e));
                if (e.isPopupTrigger()) {
                    menu.show(e.getComponent(), e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                coord.setText(updateCoord(e));
                if (e.isPopupTrigger()) {
                    menu.show(e.getComponent(), e.getX(), e.getY());
                }
            }

            private String updateCoord(MouseEvent e) {
                xClick = originXrel + e.getX() / scaleW;
                yClick = originYrel - e.getY() / scaleH;
                return "x:" + String.format("%.4f", xClick) + " y:" + String.format("%.4f", yClick);
            }
        });
//        addMouseMotionListener(new MouseMotionAdapter() {
//
//            @Override
//            public void mouseDragged(MouseEvent e) {
//                mapShapeElement.entrySet();
//                for (Entry<JElement, Shape> poly : mapShapeElement.entrySet()) {
//                    if (poly.getValue().contains(e.getX(), e.getY())) {
//                        if(poly.getKey() instanceof AbstractRobot){
//                            AbstractRobot robot = (AbstractRobot) poly.getKey();
//                            DataInformation xData = robot.getParameters().get(new Finder(AbstractRobot.Coordinates.x.name()));
//                            DataInformation yData = robot.getParameters().get(new Finder(AbstractRobot.Coordinates.y.name()));
//                            xData.setData(originXrel + e.getX() / scaleW);
//                            yData.setData(originYrel - e.getY() / scaleH);
//                        }
//                    }
//                }
//            }
//        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        metrics = g2.getFontMetrics();
        double meanX = 0, meanY = 0;
        double dimMinX = 0, dimMaxX = 0, dimMinY = 0, dimMaxY = 0;
        counter = 0;
        ArrayList<JElement> world = new ArrayList(worldCollection);
        int counterDraw = 0;
        boolean first = true;
        for (int i = 0; i < world.size(); i++) {
            JElement element = world.get(i);
            if (element instanceof AbstractRobot) {
                counterDraw++;
                double[] position = drawRobot((AbstractRobot) element);
                meanX += position[0];
                meanY += position[1];
                if (first) {
                    dimMinX = position[3];
                    dimMaxX = position[4];
                    dimMinY = position[5];
                    dimMaxY = position[6];
                    first = false;
                } else {
                    dimMinX = Math.min(dimMinX, position[3]);
                    dimMaxX = Math.max(dimMaxX, position[4]);
                    dimMinY = Math.min(dimMinY, position[5]);
                    dimMaxY = Math.max(dimMaxY, position[6]);
                }
            } else {
                AffineTransform saveXform = g2.getTransform();              //Salvataggio trasformazione
                g2.translate(-originXrel * scaleH, originYrel * scaleW);
                g2.scale(scaleW, scaleH);
                g2.setColor(element.getColor());
                g2.fill(element.getPolygon());
                g2.setTransform(saveXform);                                 //Caricamento precedente trasformazione
            }
        }
        if (!world.isEmpty()) {
            originX = meanX / counterDraw;
            originY = meanY / counterDraw;
        }
        double lenghtX = dimMaxX - dimMinX;
        double lenghtY = dimMaxY - dimMinY;
        double lenght = Math.max(lenghtX, lenghtY);
        dimensionMinX = -lenght / 2 - notch * accuracyX;
        dimensionMaxX = lenght / 2 + notch * accuracyX;
        dimensionMinY = -lenght / 2 - notch * accuracyY;
        dimensionMaxY = lenght / 2 + notch * accuracyY;
        //Disegno PointSet
        for (PointSet point : pointSet.values()) {
            point.draw(g, originXrel, originYrel, scaleH);
        }
//        //Screen
//        Rectangle2D rectX = new Rectangle2D.Double(errX * scaleW / 2, errY * scaleH / 2, (dimensionMaxX - dimensionMinX) * scaleW, (dimensionMaxY - dimensionMinY) * scaleW);
//        Point2D center = new Point2D.Double(-(dimensionMinX - errX / 2) * scaleH, (dimensionMaxY + errY / 2) * scaleW);
//        Line2D line = new Line2D.Double(center, center);
//        g2.setStroke(new BasicStroke(6));
//        g2.setColor(Color.GREEN);
//        g2.draw(rectX);
//        g2.draw(line);
    }

    protected double[] drawRobot(AbstractRobot element) {
        double[] position = new double[3];
        double[] minMax = null;
        AffineTransform saveXform = g2.getTransform();              //Salvataggio trasformazione
        AbstractRobot robot = (AbstractRobot) element;
        HashMap<Finder, DataInformation> parameters = robot.getParameters();
        DataInformation xData = parameters.get(new Finder(AbstractRobot.Coordinates.x.name()));
        DataInformation yData = parameters.get(new Finder(AbstractRobot.Coordinates.y.name()));
        DataInformation thData = parameters.get(new Finder(AbstractRobot.Coordinates.th.name()));
        position[0] = xData.getLastData();
        position[1] = yData.getLastData();
        double posX = -(originXrel - xData.getLastData()) * scaleH;
        double posY = (originYrel - yData.getLastData()) * scaleW;
        double positionTh = thData.getLastData();
//        //Dimensioni e posizione poligono
//        AffineTransform affine = new AffineTransform();
//        affine.translate(posX, posY);
//        affine.rotate(-positionTh);
//        affine.scale(scaleW, scaleH);
//        mapShapeElement.put(robot, affine.createTransformedShape(element.getPolygon()));
        //Disegno storico
        if (xData.getData() != null && yData.getData() != null && storic) {
            g2.setColor(element.getColor());
            minMax = drawLine(xData, yData);            //Metodo da XYScope
        } else {
            minMax = new double[]{position[0], position[0], position[1], position[1]};
        }
        for (Component comp : robot.getComponents().values()) {
            if (comp.isDrawable()) {
                g2.translate(posX, posY);
                g2.rotate(-positionTh);
                g2.scale(scaleW, scaleH);
                Color color = comp.getColor();
//                g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 150));
                g2.setColor(color);
                g2.fill(comp.getPolygon());
                g2.setTransform(saveXform);                                 //Caricamento precedente trasformazione
            }
        }
        //Disegno sensori
        if (robot.isSensor()) {
            for (Sensor sensor : robot.getSensors().values()) {
                double angleSensor = -positionTh - sensor.getOrientation();
                Color colorSensor = sensor.getColor();
                AffineTransform saveXformR = g2.getTransform();              //Salvataggio trasformazione
                g2.translate(posX + scaleH * sensor.getPosition() * Math.cos(angleSensor), posY + scaleH * sensor.getPosition() * Math.sin(angleSensor));
                g2.rotate(angleSensor);
                g2.setStroke(new BasicStroke(1));
                g2.setColor(new Color(colorSensor.getRed(), colorSensor.getGreen(), colorSensor.getBlue(), 150));
                g2.fill(sensor.polygon(scaleH));
                g2.setTransform(saveXformR);                                 //Caricamento precedente trasformazione
            }
        }
        //Disegno linea valori
        if (showLine) {
            getLine(xData.getLastData(), yData.getLastData(), positionTh, robot.getColor());
        }
        //Draw name and Coordinate Robot
        g2.setColor(robot.getColor());
        String nameString = element.getName();
        String xString = xData.toString();
        String yString = yData.toString();
        g2.drawString(nameString, width - metrics.stringWidth(nameString + xString + yString) - SPACE_ANGLE - 4 * SPACE_LABEL, SPACE_TEXTY + counter * SPACE_TEXTY);
        g2.drawString(xString, width - metrics.stringWidth(xString + yString) - SPACE_ANGLE - 3 * SPACE_LABEL, SPACE_TEXTY + counter * SPACE_TEXTY);
        g2.drawString(yString, width - metrics.stringWidth(yString) - SPACE_ANGLE - 2 * SPACE_LABEL, SPACE_TEXTY + counter * SPACE_TEXTY);
        g2.drawString(thData.getName() + ": " + toDegree(positionTh),
                width - SPACE_ANGLE - SPACE_LABEL, SPACE_TEXTY + counter * SPACE_TEXTY);
        counter++;
        double[] total = new double[7];
        if (position != null) {
            total = new double[position.length + minMax.length];
            System.arraycopy(position, 0, total, 0, position.length);
            System.arraycopy(minMax, 0, total, position.length, minMax.length);
        }
        return total;
    }

    /**
     * Aggiunge le linee di posizione robot
     * @param position
     * @param color 
     */
    protected void getLine(double positionX, double positionY, double positionTh, Color color) {
        //Linee X e Y
        super.getLine(positionX, positionY, color);
        double posX = -(originXrel - positionX) * scaleH;
        double posY = (originYrel - positionY) * scaleW;
        double posTh = positionTh;
        //Arco angolo
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1));
        g2.draw(new Arc2D.Double(posX - lineAx / 2, posY - lineAx / 2, lineAx, lineAx, 0, Math.toDegrees(posTh), Arc2D.OPEN));
        g2.draw(new Line2D.Double(posX, posY, posX + lineAx * Math.cos(-posTh), posY + lineAx * Math.sin(-posTh)));
        g2.drawString(Math.round(Math.toDegrees(posTh)) + "°",
                (int) (posX + lineAx * Math.cos(-posTh / 2)),
                (int) (posY + lineAx * Math.sin(-posTh / 2)));

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() == World.getDefault()) {
            if (evt.getPropertyName().equals(World.PROP_WORLD)) {
                JElement element = (JElement) evt.getNewValue();
                if (element instanceof AbstractRobot) {
                    final AbstractRobot robot = (AbstractRobot) element;
                    JMenu robotMenu = new JMenu(robot.getName());
                    JMenuItem goHere = new JMenuItem("Go here");
                    goHere.setIcon(new ImageIcon("Windows/src/net/minisumo/graphic/worldmap/Maps-icon.png"));
                    goHere.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (robot.getComponents().get(CONTROL) != null) {
                                robot.getComponents().get(CONTROL).getParameters().get("riferX").setData(xClick);
                                robot.getComponents().get(CONTROL).getParameters().get("riferY").setData(yClick);
                                robot.setControl(true);
                                pointSet.put(robot, new PointSet(xClick, yClick, robot.getColor()));
                            }
                        }
                    });
                    menuRobot.put(element, robotMenu);
                    menu.add(robotMenu);
                    robotMenu.add(goHere);
                    try {
                        robotMenu.setIcon(new ImageIcon(Introspector.getBeanInfo(robot.getClass()).getIcon(BeanInfo.ICON_COLOR_16x16)));
                    } catch (IntrospectionException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                    robot.addPropertyChangeListener(this);
                } else if(element == null){
                    JElement oldValue = (JElement) evt.getOldValue();
                    if(oldValue instanceof AbstractRobot){
                        JMenu get = menuRobot.get(oldValue);
                        menu.remove(get);
                        menuRobot.remove(oldValue);
                        ((AbstractRobot)oldValue).removePropertyChangeListener(this);
                    }
                }
            }
        } else if (evt.getPropertyName().equals(AbstractRobot.PROP_CONTROL)) {
            //Abilitazione/disabilitazione icona cordinate di arrivo
            if (!(Boolean) evt.getNewValue()) {
                pointSet.remove((AbstractRobot) evt.getSource());
            }
        } else if (evt.getPropertyName().equals(AbstractRobot.PROP_NAME)) {
        }
//        else if (evt.getPropertyName().equals(AbstractRobot.PROP_BRIDGE)) {
//            if (evt.getNewValue() instanceof Wireless) {
//                Wireless wire = (Wireless) evt.getNewValue();
//                RssiSignal rssi = new RssiSignal();
//                signalRobot.put((AbstractRobot) evt.getSource(), rssi);
//                rssi.setSignal(wire.getSignal());
//                add(rssi);
//                rssi.setLocation(20, 20);
//            }
//        }
    }

    /**
     * converte un angolo da radianti in gradi, minuti secondi
     * @param value
     * @return 
     */
    private String toDegree(double value) {
        int degree = (int) Math.toDegrees(value);
        int minuti = (int) ((Math.toDegrees(value) - degree) * 60);
        int second = (int) (((Math.toDegrees(value) - degree) * 60 - minuti) * 60);
        return "" + degree + "° " + minuti + "' " + second + "''";
    }
}
