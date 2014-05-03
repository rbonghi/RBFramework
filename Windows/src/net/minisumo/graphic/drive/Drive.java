/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.graphic.drive;

import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Hashtable;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.minisumo.util.DataInformation;
import net.minisumo.util.GBC;
import net.minisumo.world.AbstractRobot;
import net.minisumo.world.component.Component;
import org.openide.util.NbBundle;

/**
 *
 * @author Raffaello
 */
public class Drive extends JPanel implements KeyListener, PropertyChangeListener, ActionListener, ChangeListener, ItemListener {

    private static final int STEP_LABEL = 100, DECIMAL = 1;
    private static double STEPVEL = 0.1, STEPSTE = 0.1;
    private AbstractRobot robot;
    private JButton upButton, downButton, leftButton, rightButton;
    private JSlider velSlider, steSlider;
    private JCheckBox enable;
    private JLabel nameRobot;
    private double maxValueV, minValueV, maxValueS, minValueS;
    private Component drive;
    DataInformation velocityRifer, velocity, steeringRifer, steering;

    public Drive() {
        setFocusable(true);
        setLayout(new GridBagLayout());
        enable = new JCheckBox("Enable");
        enable.addItemListener(this);
        enable.setFocusable(false);
        JPanel panel = new JPanel(new GridLayout(1, 2));
        nameRobot = new JLabel("No selected robot");
        panel.add(enable);
        panel.add(nameRobot);
        add(panel, new GBC(0, 0).setAnchor(GBC.PAGE_START).setFill(GBC.HORIZONTAL).setSpan(2, 1).setIpad(40, 0));
        add(KeyPanel(), new GBC(0, 1).setFill(GBC.HORIZONTAL).setSpan(2, 1).setIpad(40, 0));
        velSlider = new JSlider(JSlider.VERTICAL, -100, 100, 0);
        steSlider = new JSlider(JSlider.VERTICAL, -100, 100, 0);
        add(SliderPanel(velSlider, "SliderPanel.border.title1"), new GBC(0, 2).setFill(GBC.HORIZONTAL).setWeight(0.5, 0));
        add(SliderPanel(steSlider, "SliderPanel.border.title2"), new GBC(1, 2).setFill(GBC.HORIZONTAL).setWeight(0.5, 0));
        velSlider.addChangeListener(this);
        steSlider.addChangeListener(this);
        enable.setEnabled(false);
        enableDrive(false);
        addKeyListener(this);
    }

    private JPanel KeyPanel() {
        JPanel keyPanel = new JPanel(new GridBagLayout());
        keyPanel.setBorder(BorderFactory.createTitledBorder(NbBundle.getMessage(Drive.class, "KeyPanel.border.title")));
        upButton = new JButton("Up");
        upButton.addActionListener(this);
        upButton.setFocusable(false);
        downButton = new JButton("Down");
        downButton.addActionListener(this);
        downButton.setFocusable(false);
        leftButton = new JButton("Left");
        leftButton.addActionListener(this);
        leftButton.setFocusable(false);
        rightButton = new JButton("Right");
        rightButton.addActionListener(this);
        rightButton.setFocusable(false);
        keyPanel.add(upButton, new GBC(1, 0).setFill(GBC.CENTER));
        keyPanel.add(leftButton, new GBC(0, 1).setAnchor(GBC.EAST));
        keyPanel.add(downButton, new GBC(1, 1));
        keyPanel.add(rightButton, new GBC(2, 1));
        return keyPanel;
    }

    private JPanel SliderPanel(JSlider slider, String text) {
        JPanel sliderPanel = new JPanel();
        sliderPanel.setBorder(BorderFactory.createTitledBorder(NbBundle.getMessage(DriveTopComponent.class, text)));
        sliderPanel.add(slider);
        //Turn on labels at major tick marks.
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setFocusable(false);
        return sliderPanel;
    }

    private void updateScale(double maxValue, double minValue, JSlider slider) {
        int maxValueI = (int) (maxValue * STEP_LABEL);
        int minValueI = (int) (minValue * STEP_LABEL);
        slider.setMaximum(maxValueI);
        slider.setMinimum(minValueI);
        Hashtable label = new Hashtable();
        label.put(new Integer(0), new JLabel(" Stop"));
        for (int i = 1; i < 3; i++) {
            label.put(new Integer(maxValueI / i), new JLabel(" " + maxValue / i));
            label.put(new Integer(minValueI / i), new JLabel(" " + minValue / i));
        }
        slider.setLabelTable(label);
    }

    private static double round(double value, int decimal) {
        double newValue = Math.pow(10, decimal);
        double round = (double) Math.round(value * newValue);
        return round / newValue;
    }

    private void enableDrive(boolean enableDrive) {
        velSlider.setEnabled(enableDrive);
        steSlider.setEnabled(enableDrive);
        upButton.setEnabled(enableDrive);
        downButton.setEnabled(enableDrive);
        leftButton.setEnabled(enableDrive);
        rightButton.setEnabled(enableDrive);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() == velocityRifer) {
            if (evt.getPropertyName().equals(DataInformation.PROP_MAXLIMIT)) {
                maxValueV = (Double) evt.getNewValue();
                updateScale(maxValueV, minValueV, velSlider);
            } else if (evt.getPropertyName().equals(DataInformation.PROP_MINLIMIT)) {
                minValueV = (Double) evt.getNewValue();
                updateScale(maxValueV, minValueV, velSlider);
            } else if (evt.getPropertyName().equals(DataInformation.PROP_DATA)) {
                velSlider.setValue((int) (((Double) evt.getNewValue()) * STEP_LABEL));
            }
        } else if (evt.getSource() == steeringRifer) {
            if (evt.getPropertyName().equals(DataInformation.PROP_MAXLIMIT)) {
                maxValueS = (Double) evt.getNewValue();
                updateScale(maxValueS, minValueS, steSlider);
            } else if (evt.getPropertyName().equals(DataInformation.PROP_MINLIMIT)) {
                minValueS = (Double) evt.getNewValue();
                updateScale(maxValueS, minValueS, steSlider);
            }else if (evt.getPropertyName().equals(DataInformation.PROP_DATA)) {
                steSlider.setValue((int) (((Double) evt.getNewValue()) * STEP_LABEL));
            }
        } else if (evt.getSource() == robot) {
            if (evt.getPropertyName().equals(AbstractRobot.PROP_NAME)) {
                nameRobot.setText((String) evt.getNewValue());
            } else if (evt.getPropertyName().equals(AbstractRobot.PROP_CONTROL)) {
                enable.setEnabled(!robot.isControl());
                enableDrive(!robot.isControl() && enable.isSelected());
            }
        } else if (evt.getSource() == drive) {
            if (evt.getPropertyName().equals("velocity")) {
                double velocityValue = (Double) evt.getNewValue();
                velSlider.setValue((int) (velocityValue * STEP_LABEL));
            } else if (evt.getPropertyName().equals("steering")) {
                double steeringValue = (Double) evt.getNewValue();
                steSlider.setValue((int) (steeringValue * STEP_LABEL));
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (robot != null) {
            if (e.getSource() == upButton) {
                velocityRifer.setData(round(velocityRifer.getLastData(), DECIMAL) + STEPVEL);
            } else if (e.getSource() == downButton) {
                velocityRifer.setData(round(velocityRifer.getLastData(), DECIMAL) - STEPVEL);
            } else if (e.getSource() == leftButton) {
                steeringRifer.setData(round(steeringRifer.getLastData(), DECIMAL) + STEPSTE);
            } else if (e.getSource() == rightButton) {
                steeringRifer.setData(round(steeringRifer.getLastData(), DECIMAL) - STEPSTE);
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (robot != null) {
            JSlider source = (JSlider) e.getSource();
//                if (!source.getValueIsAdjusting()) {
            if (source == velSlider) {
                velocityRifer.setData((double) source.getValue() / STEP_LABEL);
            } else if (source == steSlider) {
                steeringRifer.setData((double) source.getValue() / STEP_LABEL);
            }
//                }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == enable) {
            enableDrive(e.getStateChange() == ItemEvent.SELECTED);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (robot != null && enable.isSelected() && !robot.isControl()) {
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                upButton.setSelected(true);
                velocityRifer.setData(round(velocityRifer.getLastData(), DECIMAL) + STEPVEL);
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                downButton.setSelected(true);
                velocityRifer.setData(round(velocityRifer.getLastData(), DECIMAL) - STEPVEL);
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                leftButton.setSelected(true);
                steeringRifer.setData(round(steeringRifer.getLastData(), DECIMAL) + STEPSTE);
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                rightButton.setSelected(true);
                steeringRifer.setData(round(steeringRifer.getLastData(), DECIMAL) - STEPSTE);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        upButton.setSelected(false);
        downButton.setSelected(false);
        leftButton.setSelected(false);
        rightButton.setSelected(false);
    }

    public void setRobot(AbstractRobot robot) {
        if (this.robot != null) {
            robot.removePropertyChangeListener(this);
            if (drive != null) {
                drive.removePropertyChangeListener(this);
                velocityRifer.removePropertyChangeListener(this);
                steeringRifer.removePropertyChangeListener(this);
                velocity.removePropertyChangeListener(this);
                steering.removePropertyChangeListener(this);
            }
        }
        this.robot = robot;
        if (robot != null) {
            //Scrittura nome robot
            nameRobot.setText(robot.getName());
            enable.setEnabled(!robot.isControl());
            enableDrive(!robot.isControl() && enable.isSelected());
            robot.addPropertyChangeListener(this);
            //Recupero informazioni robot
            HashMap<String, Component> component = robot.getComponents();
            drive = component.get("Drive");
            if (drive != null) {
                drive.addPropertyChangeListener(this);
                velocityRifer = drive.getParameters().get("VelocityRifer");
                velocityRifer.addPropertyChangeListener(this);
                velocity = drive.getParameters().get("Velocity");
                velocity.addPropertyChangeListener(this);
                steeringRifer = drive.getParameters().get("SteeringRifer");
                steeringRifer.addPropertyChangeListener(this);
                steering = drive.getParameters().get("Steering");
                steering.addPropertyChangeListener(this);
                //Dimensionamento slider
                velSlider.setMajorTickSpacing(10);
                velSlider.setMinorTickSpacing(5);
                steSlider.setMajorTickSpacing(100);
                steSlider.setMinorTickSpacing(50);
                maxValueV = velocityRifer.getMaxLimit();
                minValueV = velocityRifer.getMinLimit();
                maxValueS = steeringRifer.getMaxLimit();
                minValueS = steeringRifer.getMinLimit();
                updateScale(maxValueV, minValueV, velSlider);
                updateScale(maxValueS, minValueS, steSlider);
                velSlider.setValue((int) (velocityRifer.getLastData() * STEP_LABEL));
                steSlider.setValue((int) (steeringRifer.getLastData() * STEP_LABEL));
            }
        }
    }
}
