/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.simulator;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import net.minisumo.simulator.parts.ControlXY;
import net.minisumo.simulator.parts.Unicycle;
import net.minisumo.simulator.parts.Engine;
import net.minisumo.simulator.parts.PIDControl;
import java.io.ObjectInputStream;
import java.util.Timer;
import net.minisumo.bridges.Bridge;
import net.minisumo.bridges.Updater;
import net.minisumo.util.DataInformation;
import net.minisumo.util.Finder;
import net.minisumo.world.AbstractRobot;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Raffaello
 */
@ServiceProvider(service = Bridge.class)
public class Simulator extends Updater {

    //Parametri simulatore
    private long timeUpdate = 10;
    private double tauL = 0.0005;
    private double tauR = 0.0005;
    private double maxTensL = 12;
    private double maxTensR = 12;
    private Unicycle unicycle;
    private Engine engineL, engineR;
    private PIDControl pidLeft, pidRight;
    private ControlXY controlXY;
    //Variabili intene
    private transient Timer timer;
    private transient HashMap<String, DataInformation> parameters;
    private transient double controlL, controlR;
    private transient double wLeftEngine, wRightEngine;
    private transient double minRadius;
    private transient double[] velEngineRifer, velUnicycleMeasure, velUnicycleRifer;
    private transient double[] posMeasure, posRifer;
    private transient int counter;
    private transient boolean highControl;

    @Override
    public void run() {
        updateSimulator();
        if (counter % 10 == 0) {
            counter = 0;
            //Update Dati
            parameters.get("x").setData(posMeasure[0]);
            parameters.get("y").setData(posMeasure[1]);
            parameters.get("th").setData(posMeasure[2]);
            parameters.get("HighControl.riferX").setData(posRifer[0]);
            parameters.get("HighControl.riferY").setData(posRifer[1]);
//            parameters.get("thR").setData(posRifer[2]);
            parameters.get("Drive.Velocity").setData(velUnicycleMeasure[0]);
            parameters.get("Drive.Steering").setData(velUnicycleMeasure[1]);
            parameters.get("Drive.VelocityRifer").setData(velUnicycleRifer[0]);
            parameters.get("Drive.SteeringRifer").setData(velUnicycleRifer[1]);
            parameters.get("PID Control left.Input").setData(velEngineRifer[0]);
            parameters.get("PID Control left.Measure").setData(wLeftEngine);
            parameters.get("PID Control left.Output").setData(controlL);
            parameters.get("PID Control right.Input").setData(velEngineRifer[1]);
            parameters.get("PID Control right.Measure").setData(wRightEngine);
            parameters.get("PID Control right.Output").setData(controlR);
        }
        counter++;
    }

    @Override
    public boolean sync() {
        return true;
    }

    private void updateSimulator() {
        if (highControl && robot.isControl()) {
            if (Math.hypot(posRifer[0] - posMeasure[0], posRifer[1] - posMeasure[1]) <= minRadius) {
                velUnicycleRifer = new double[]{0, 0};
                robot.setControl(false);
            } else {
                velUnicycleRifer = controlXY.updateControl(posRifer, posMeasure);
            }
            velEngineRifer = Unicycle.vwTOwlwr(velUnicycleRifer);
        } else {
            velEngineRifer = Unicycle.vwTOwlwr(velUnicycleRifer);
        }
        wLeftEngine = engineL.updateModel(controlL);
        controlL = pidLeft.updateControl(velEngineRifer[0] - wLeftEngine);
        wRightEngine = engineR.updateModel(controlR);
        controlR = pidRight.updateControl(velEngineRifer[1] - wRightEngine);
        velUnicycleMeasure = Unicycle.wlwrTOvw(new double[]{wLeftEngine, wRightEngine});
        posMeasure = unicycle.updateModel(velUnicycleMeasure);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        super.propertyChange(evt);
        if (evt.getPropertyName().equals(DataInformation.PROP_DATA)) {
            if (!robot.isControl() && evt.getSource() == parameters.get("Drive.VelocityRifer")) {
                velUnicycleRifer[0] = parameters.get("Drive.VelocityRifer").getLastData();
            } else if (!robot.isControl() && evt.getSource() == parameters.get("Drive.SteeringRifer")) {
                velUnicycleRifer[1] = parameters.get("Drive.SteeringRifer").getLastData();
            } else if (evt.getSource() == parameters.get("HighControl.riferX") || evt.getSource() == parameters.get("HighControl.riferY")) {
                posRifer = new double[]{parameters.get("HighControl.riferX").getLastData(),
                    parameters.get("HighControl.riferY").getLastData(),};
            } else if (evt.getSource() == parameters.get("HighControl.Gain1") || evt.getSource() == parameters.get("HighControl.Gain2")) {
                controlXY.setKControl(new double[]{
                            parameters.get("HighControl.Gain1").getLastData(),
                            parameters.get("HighControl.Gain2").getLastData()
                        });
            } else if (evt.getSource() == parameters.get("HighControl.Precision")) {
                minRadius = this.parameters.get("HighControl.Precision").getLastData();
            }
        }
    }

    @Override
    public void register(AbstractRobot robot) {
        this.robot = robot;
        velUnicycleRifer = new double[2];
        velEngineRifer = new double[2];
        posMeasure = new double[3];
        posRifer = new double[3];
        //Registrazione datainformation
        robot.addPropertyChangeListener(this);
        HashMap<Finder, DataInformation> parameters = robot.getParameters();
        this.parameters = Updater.load(this, parameters);
        unicycle = new Unicycle(timeUpdate,
                this.parameters.get("Wheel left.radius").getLastData(),
                this.parameters.get("Wheel right.radius").getLastData(),
                this.parameters.get("wheelBase").getLastData());
        engineL = new Engine(timeUpdate, tauL, maxTensL);
        engineL.setEngine(robot.isEngine());
        engineR = new Engine(timeUpdate, tauR, maxTensR);
        engineR.setEngine(robot.isEngine());
        double[] kPIDL = new double[]{
            this.parameters.get("PID Control left.Proportional").getLastData(),
            this.parameters.get("PID Control left.Integrative").getLastData(),
            this.parameters.get("PID Control left.Derivative").getLastData()};
        double[] kPIDR = new double[]{
            this.parameters.get("PID Control right.Proportional").getLastData(),
            this.parameters.get("PID Control right.Integrative").getLastData(),
            this.parameters.get("PID Control right.Derivative").getLastData()};
        pidLeft = new PIDControl(timeUpdate, kPIDL);
        pidRight = new PIDControl(timeUpdate, kPIDR);
        //Controllo alto livello
        highControl = robot.getComponents().get("HighControl") != null;
        if (highControl) {
            double[] kControl = new double[]{
                this.parameters.get("HighControl.Gain1").getLastData(),
                this.parameters.get("HighControl.Gain2").getLastData(),};
            minRadius = this.parameters.get("HighControl.Precision").getLastData();
            controlXY = new ControlXY(kControl);
        }
        //Start timer
        timer = new Timer();
        timer.scheduleAtFixedRate(this, timeUpdate, timeUpdate);
    }

    @Override
    public Bridge loadBridge(ObjectInputStream objectInputStream) {
        try {
            return (Simulator) objectInputStream.readObject();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public void stop() {
        cancel();
        timer.cancel();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    protected void setEngine(boolean engine) {
        engineL.setEngine(engine);
        engineR.setEngine(engine);
    }

    @Override
    protected void setSensor(boolean sensor) {
    }

    @Override
    protected void setControl(boolean control) {
    }

    /**
     * Get the value of timeUpdate
     *
     * @return the value of timeUpdate
     */
    public long getTimeUpdate() {
        return timeUpdate;
    }

    /**
     * Set the value of timeUpdate
     *
     * @param timeUpdate new value of timeUpdate
     */
    public void setTimeUpdate(long timeUpdate) {
        this.timeUpdate = timeUpdate;
    }

    /**
     * Get the value of tauL
     *
     * @return the value of tauL
     */
    public double getTauL() {
        return tauL;
    }

    /**
     * Set the value of tauL
     *
     * @param tauL new value of tauL
     */
    public void setTauL(double tauL) {
        this.tauL = tauL;
        engineL.setTau(tauL);
    }

    /**
     * Get the value of tauR
     *
     * @return the value of tauR
     */
    public double getTauR() {
        return tauR;
    }

    /**
     * Set the value of tauR
     *
     * @param tauR new value of tauR
     */
    public void setTauR(double tauR) {
        this.tauR = tauR;
        engineR.setTau(tauR);
    }

    /**
     * Get the value of maxTensL
     *
     * @return the value of maxTensL
     */
    public double getMaxTensL() {
        return maxTensL;
    }

    /**
     * Set the value of maxTensL
     *
     * @param maxTensL new value of maxTensL
     */
    public void setMaxTensL(double maxTensL) {
        this.maxTensL = maxTensL;
        engineL.setMaxTens(maxTensL);
    }

    /**
     * Get the value of maxTensR
     *
     * @return the value of maxTensR
     */
    public double getMaxTensR() {
        return maxTensR;
    }

    /**
     * Set the value of maxTensR
     *
     * @param maxTensR new value of maxTensR
     */
    public void setMaxTensR(double maxTensR) {
        this.maxTensR = maxTensR;
        engineR.setMaxTens(maxTensR);
    }
}
