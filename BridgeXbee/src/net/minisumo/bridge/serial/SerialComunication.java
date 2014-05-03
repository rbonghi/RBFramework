/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.bridge.serial;

import java.beans.PropertyChangeEvent;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Timer;
import java.util.logging.Logger;
import net.minisumo.bridge.serial.packet.DRobot;
import net.minisumo.bridge.serial.packet.PRobot;
import net.minisumo.bridge.serial.packet.TRobot;
import net.minisumo.bridges.Bridge;
import net.minisumo.bridges.Updater;
import net.minisumo.bridges.Wireless;
import net.minisumo.serial.ConnectionException;
import net.minisumo.serial.XBeeAPI;
import net.minisumo.serial.packet.XBeeAddress;
import net.minisumo.util.DataInformation;
import net.minisumo.world.AbstractRobot;
import net.minisumo.world.component.Component;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Raffaello
 */
@ServiceProvider(service = Bridge.class)
public class SerialComunication extends Updater implements Wireless {

    static final Logger logger = Logger.getLogger(SerialComunication.class.getName());
    //Variabili di comunicazione
    private transient boolean connect;
    private long timeUpdate = 100;
    private XBeeAddress address;
    private static final boolean ack = false;
    //Variabili di sync
    private double radiusL, radiusR, wheelbase;
    private transient boolean sensor, engine;
    //Variabili intene
    private transient int rssi;
    private transient Timer timer;
    private transient int numberError;
    private transient HashMap<String, DataInformation> parameters;
    private transient Component drive;
    private transient double[] posMeasure, velUnicycleMeasure;
    private transient double velRifer, steRifer;
    private transient int counter;
    private int totalCase = 2;

    public SerialComunication() {
//        address = new XBeeAddress();
        address = new XBeeAddress(1286656, 1078613582);
    }

    @Override
    public boolean sync() {
        return true;
    }

    @Override
    public void run() {
        if (connect) {
            switch (counter) {
                case 0:
                    posMeasure = measurePosition();
                    parameters.get("x").setData(posMeasure[0]);
                    parameters.get("y").setData(posMeasure[1]);
                    parameters.get("th").setData(posMeasure[2]);
                    break;
                case 1:
                    velUnicycleMeasure = getVelUnicycleMeasure();
                    parameters.get("Drive.Velocity").setData(velUnicycleMeasure[0]);
                    parameters.get("Drive.Steering").setData(velUnicycleMeasure[1]);
                    parameters.get("Drive.VelocityRifer").setData(velUnicycleMeasure[2]);
                    parameters.get("Drive.SteeringRifer").setData(velUnicycleMeasure[3]);
                    break;
            }
            counter++;
            if (counter >= totalCase) {
                counter = 0;
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        super.propertyChange(evt);
//        if (evt.getSource() == drive) {
        if (evt.getPropertyName().equals("velocity")) {
            velRifer = (Double) evt.getNewValue();
            setVelocity(velRifer, steRifer);
        } else if (evt.getPropertyName().equals("steering")) {
            steRifer = (Double) evt.getNewValue();
            setVelocity(velRifer, steRifer);
        }
//        }
//        if (evt.getPropertyName().equals(DataInformation.PROP_DATA)) {
//            if (evt.getSource() == parameters.get("Drive.VelocityRifer")) {
//                velRifer = parameters.get("Drive.VelocityRifer").getLastData();
//                setVelocity(velRifer, steRifer);
//            } else if (evt.getSource() == parameters.get("Drive.SteeringRifer")) {
//                steRifer = parameters.get("Drive.SteeringRifer").getLastData();
//                setVelocity(velRifer, steRifer);
//            }
//        }
    }

    @Override
    public void register(AbstractRobot robot) {
        this.robot = robot;
        posMeasure = new double[3];
        velUnicycleMeasure = new double[2];
        //Registrazione datainformation
        robot.addPropertyChangeListener(this);
        this.parameters = Updater.load(this, robot.getParameters());
        drive = robot.getComponents().get("Drive");
        drive.addPropertyChangeListener(this);
        //Start timer
        timer = new Timer();
        double temp = ((double) timeUpdate) / totalCase;
        long time = Math.round(temp);
        timer.scheduleAtFixedRate(this, time, time);
    }

    @Override
    public Bridge loadBridge(ObjectInputStream objectInputStream) {
        try {
            return (SerialComunication) objectInputStream.readObject();
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
    public int getSignal() {
        return rssi;
    }

    public int getNumberError() {
        return numberError;
    }

    @Override
    protected void setEngine(boolean engine) {
        try {
            DRobot<Boolean> recive = XBeeAPI.sendPacket(new DRobot<Boolean>(DRobot.EN_ENGINE, engine, address, ack, System.currentTimeMillis()), 100);
            rssi = recive.getRssi();
        } catch (ConnectionException ex) {
        } catch (Exception ex) {
            logger.info("Error n: " + numberError++);
        }
    }

    protected boolean isEngine() {
        try {
            TRobot recive = XBeeAPI.sendPacket(new TRobot(TRobot.ENGINE_SENSOR, address, ack, System.currentTimeMillis()), 100);
            sensor = recive.isSensor();
            engine = recive.isEngine();
            rssi = recive.getRssi();
            return engine;
        } catch (Exception ex) {
            logger.info("Error n: " + numberError++);
            return engine;
        }
    }

    @Override
    protected void setSensor(boolean sensor) {
        try {
            DRobot<Boolean> recive = XBeeAPI.sendPacket(new DRobot<Boolean>(DRobot.EN_SENSOR, sensor, address, ack, System.currentTimeMillis()), 100);
            rssi = recive.getRssi();
        } catch (ConnectionException ex) {
        } catch (Exception ex) {
            logger.info("Error n: " + numberError++);
        }
    }

    protected boolean isSensor() {
        try {
            TRobot recive = XBeeAPI.sendPacket(new TRobot(TRobot.ENGINE_SENSOR, address, ack, System.currentTimeMillis()), 100);
            sensor = recive.isSensor();
            engine = recive.isEngine();
            rssi = recive.getRssi();
            return sensor;
        } catch (Exception ex) {
            logger.info("Error n: " + numberError++);
            return sensor;
        }
    }

    @Override
    protected void setControl(boolean control) {
    }

    protected void setRadiusL(double radiusL) {
        try {
            this.radiusL = radiusL;
            float[] dataInformation = new float[]{(float) radiusL, (float) radiusR, (float) wheelbase};
            PRobot<float[]> recive = XBeeAPI.sendPacket(new PRobot<float[]>(PRobot.DEAD_PARAMETERS, dataInformation, address, ack, System.currentTimeMillis()), 100);
            rssi = recive.getRssi();
        } catch (ConnectionException ex) {
        } catch (Exception ex) {
            logger.info("Error n: " + numberError++);
        }
    }

    protected void setRadiusR(double radiusR) {
        try {
            this.radiusR = radiusR;
            float[] dataInformation = new float[]{(float) radiusL, (float) radiusR, (float) wheelbase};
            PRobot<float[]> recive = XBeeAPI.sendPacket(new PRobot<float[]>(PRobot.DEAD_PARAMETERS, dataInformation, address, ack, System.currentTimeMillis()), 100);
            rssi = recive.getRssi();
        } catch (ConnectionException ex) {
        } catch (Exception ex) {
            logger.info("Error n: " + numberError++);
        }
    }

    protected void setWheelbase(double wheelbase) {
        try {
            this.wheelbase = wheelbase;
            float[] dataInformation = new float[]{(float) radiusL, (float) radiusR, (float) wheelbase};
            PRobot<float[]> recive = XBeeAPI.sendPacket(new PRobot<float[]>(PRobot.DEAD_PARAMETERS, dataInformation, address, ack, System.currentTimeMillis()), 100);
            rssi = recive.getRssi();
        } catch (ConnectionException ex) {
        } catch (Exception ex) {
            logger.info("Error n: " + numberError++);
        }
    }

    protected void setVelocity(double velocity, double steering) {
        try {
            float[] dataInformation = new float[]{(float) velocity, (float) steering};
            DRobot<float[]> recive = XBeeAPI.sendPacket(new DRobot<float[]>(DRobot.DRI_VELOCITY, dataInformation, address, ack, System.currentTimeMillis()), 100);
            rssi = recive.getRssi();
        } catch (Exception ex) {
            logger.info("Error n: " + numberError++);
            ex.printStackTrace();
        }
    }

    protected double[] getVelUnicycleMeasure() {
        try {
            TRobot recive = XBeeAPI.sendPacket(new TRobot(TRobot.VELOCITY, address, ack, System.currentTimeMillis()), 100);
            velUnicycleMeasure = recive.getVelocity();
            rssi = recive.getRssi();
            return velUnicycleMeasure;
        } catch (Exception ex) {
            logger.info("Error n: " + numberError++);
            return velUnicycleMeasure;
        }
    }

    private double[] measurePosition() {
        try {
            TRobot recive = XBeeAPI.sendPacket(new TRobot(TRobot.DEAD_REKONING, address, ack, System.currentTimeMillis()), 100);
            posMeasure[0] = recive.getPosX();
            posMeasure[1] = recive.getPosY();
            posMeasure[2] = recive.getPosTh();
            rssi = recive.getRssi();
            return posMeasure;
        } catch (ConnectionException ex) {
            return posMeasure;
        } catch (Exception ex) {
            logger.info("Error n: " + numberError++);
            return posMeasure;
        }
    }

    protected void setPosition(double[] position) {
        try {
            float[] dataInformation = new float[]{(float) position[2], (float) position[0], (float) position[1]};
            PRobot<float[]> recive = XBeeAPI.sendPacket(new PRobot<float[]>(PRobot.DEAD_RECKONING, dataInformation, address, ack, System.currentTimeMillis()), 100);
            rssi = recive.getRssi();
        } catch (ConnectionException ex) {
        } catch (Exception ex) {
            logger.info("Error n: " + numberError++);
        }
    }

    protected void setKPIDL(double[] kPIDL) {
        try {
            float[] dataInformation = new float[]{(float) kPIDL[0], (float) kPIDL[1], (float) kPIDL[2]};
            PRobot<float[]> recive = XBeeAPI.sendPacket(new PRobot<float[]>(PRobot.PIDL, dataInformation, address, ack, System.currentTimeMillis()), 100);
            rssi = recive.getRssi();
        } catch (ConnectionException ex) {
        } catch (Exception ex) {
            logger.info("Error n: " + numberError++);
        }
    }

    protected double[] getPIDL() {
        double[] data = new double[3];
        try {
            PRobot<float[]> recive = XBeeAPI.sendPacket(new PRobot<float[]>(PRobot.PIDL, address, ack, System.currentTimeMillis()), 100);
            float[] dataT = recive.getDataInformation();
            rssi = recive.getRssi();
            for (int i = 0; i < data.length; i++) {
                data[i] = dataT[i];
            }
            return data;
        } catch (Exception ex) {
            logger.info("Error n: " + numberError++);
            return data;
        }
    }

    protected void setKPIDR(double[] kPIDR) {
        try {
            float[] dataInformation = new float[]{(float) kPIDR[0], (float) kPIDR[1], (float) kPIDR[2]};
            PRobot<float[]> recive = XBeeAPI.sendPacket(new PRobot<float[]>(PRobot.PIDR, dataInformation, address, ack, System.currentTimeMillis()), 100);
            rssi = recive.getRssi();
        } catch (ConnectionException ex) {
        } catch (Exception ex) {
            logger.info("Error n: " + numberError++);
        }
    }

    protected double[] getPIDR() {
        double[] data = new double[3];
        try {
            PRobot<float[]> recive = XBeeAPI.sendPacket(new PRobot<float[]>(PRobot.PIDR, address, ack, System.currentTimeMillis()), 100);
            float[] dataT = recive.getDataInformation();
            rssi = recive.getRssi();
            for (int i = 0; i < data.length; i++) {
                data[i] = dataT[i];
            }
            return data;
        } catch (Exception ex) {
            logger.info("Error n: " + numberError++);
            return data;
        }
    }

//    @Override
//    public void setRiferXYTh(Coordinate2D coord) {
//        try {
//            float[] dataInformation = new float[]{(float) coord.th, (float) coord.x, (float) coord.y};
//            DRobot<float[]> recive = XBeeReader.sendPacket(new DRobot<float[]>(DRobot.COORDINATES, dataInformation, address, ack, System.currentTimeMillis()));
//            rssi = recive.getRssi();
//        } catch (Exception ex) {
//            logger.info("Error n: " + count++);
//        }
//    }
//
//    @Override
//    public Coordinate2D getRiferXYTh() {
//        try {
//            TRobot recive = XBeeReader.sendPacket(new TRobot(TRobot.RIFER_XYTH, address, ack, System.currentTimeMillis()));
//            riferVector.x = recive.getPosX();
//            riferVector.y = recive.getPosY();
//            riferVector.th = recive.getPosTh();
//            rssi = recive.getRssi();
//            return riferVector;
//        } catch (Exception ex) {
//            logger.info("Error n: " + count++);
//            return riferVector;
//        }
//    }
//    @Override
//    public double[] getConfDead() {
//        double[] data = new double[3];
//        try {
//            PRobot<float[]> recive = XBeeReader.sendPacket(new PRobot<float[]>(PRobot.DEAD_PARAMETERS, address, ack, System.currentTimeMillis()));
//            float[] dataT = recive.getDataInformation();
//            rssi = recive.getRssi();
//            for (int i = 0; i < data.length; i++) {
//                data[i] = dataT[i];
//            }
//            return data;
//        } catch (Exception ex) {
//            logger.info("Error n: " + count++);
//            return data;
//        }
//    }
    /**
     * Get the value of connect
     *
     * @return the value of connect
     */
    public boolean isConnect() {
        return connect;
    }

    /**
     * Set the value of connect
     *
     * @param connect new value of connect
     */
    public void setConnect(boolean connect) {
        this.connect = connect;
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
     * Get the value of address
     *
     * @return the value of address
     */
    public XBeeAddress getAddress() {
        return address;
    }

    /**
     * Set the value of address
     *
     * @param address new value of address
     */
    public void setAddress(XBeeAddress address) {
        this.address = address;
    }
}
