/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.bridge.serial.packet;

import java.util.Date;
import net.minisumo.serial.XBeeAPI;
import net.minisumo.serial.packet.Jpacket;
import net.minisumo.serial.packet.XBeeAddress;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Master
 */
@ServiceProvider(service = Jpacket.class)
public class TRobot extends Jpacket {

    public final static char DEAD_REKONING = 'D', ENGINE_SENSOR = 'X';
    public final static char ENGINE_LEFT = 'L', ENGINE_RIGHT = 'R';
    public final static char VELOCITY = 'V', CURRENT = 'C';
    public final static char RIFER_XYTH = 'Q';
    private String name = "T";
    private long timestamp;
    private byte[] data;
    private XBeeAddress address;
    private int rssi, ack = 0;
    private float posX, posY, posTh;
    private float riferX, riferY, riferTh;
    private boolean sensor, engine;
    private double wRiferL, wMisureL, contrL, wRiferR, wMisureR, contrR;
    private double v, w, vR, wR;
    private double engLC, engRC, Ctot, Vtot, tempAmbient;

    public TRobot() {
    }

    public TRobot(char detail, XBeeAddress address, long timestamp) {
        this(detail, address, true, timestamp);
    }

    public TRobot(char detail, XBeeAddress address, boolean ack, long timestamp) {
        this.address = address;
        this.timestamp = timestamp;
        name += detail;
        data = name.getBytes();
        if (ack && !address.isBroadcast()) {
            this.ack = 1 + (int) (Math.random() * 254);
        }
    }

    public TRobot(byte[] data, XBeeAddress address, int rssi, long timestamp) {
        this.data = data;
        this.address = address;
        this.rssi = rssi;
        this.timestamp = timestamp;
        this.ack = 0;
        name += new String(new byte[]{data[0]});
        switch (data[0]) {
            case DEAD_REKONING:                       //Odometria
                posX = XBeeAPI.byteArrayToFloat(data, 5);
                posY = XBeeAPI.byteArrayToFloat(data, 9);
                posTh = XBeeAPI.byteArrayToFloat(data, 1);
                break;

            case RIFER_XYTH:                          //Riferimenti controllo
                riferX = XBeeAPI.byteArrayToFloat(data, 5);
                riferY = XBeeAPI.byteArrayToFloat(data, 9);
                riferTh = XBeeAPI.byteArrayToFloat(data, 1);
                break;

            case ENGINE_SENSOR:                       //Stato motori e sensori
                if (data[1] == 1) {
                    sensor = true;
                } else {
                    sensor = false;
                }
                if (data[2] == 1) {
                    engine = true;
                } else {
                    engine = false;
                }
                break;

            case ENGINE_LEFT:
                wRiferL = XBeeAPI.byteArrayToInt(data, 1, 2);
                wMisureL = XBeeAPI.byteArrayToInt(data, 3, 2);
                contrL = percentualValue(XBeeAPI.byteArrayToInt(data, 5, 2));
                break;

            case ENGINE_RIGHT:
                wRiferR = XBeeAPI.byteArrayToInt(data, 1, 2);
                wMisureR = XBeeAPI.byteArrayToInt(data, 3, 2);
                contrR = percentualValue(XBeeAPI.byteArrayToInt(data, 5, 2));
                break;

            case VELOCITY:
                v = XBeeAPI.byteArrayToFloat(data, 1);
                w = XBeeAPI.byteArrayToFloat(data, 5);
                vR = XBeeAPI.byteArrayToFloat(data, 9);
                wR = XBeeAPI.byteArrayToFloat(data, 13);
                break;

            case CURRENT:
                engLC = XBeeAPI.byteArrayToInt(data, 1, 2);
                engRC = XBeeAPI.byteArrayToInt(data, 3, 2);
                Ctot = XBeeAPI.byteArrayToInt(data, 5, 4);
                Vtot = XBeeAPI.byteArrayToInt(data, 9, 4);
                tempAmbient = XBeeAPI.byteArrayToInt(data, 13, 4);
                break;
        }
    }

    private double percentualValue(float engineControl) {
        return (engineControl - 2049) / (2048 * 2);
    }

    public double[] getCurrent() {
        return new double[]{engLC, engRC, Ctot, Vtot, tempAmbient};
    }

    public double[] getVelocity() {
        return new double[]{v, w, vR, wR};
    }

    public double[] getEngine() {
        if (data[0] == ENGINE_LEFT) {
            return new double[]{wRiferL, wMisureL, contrL};
        } else {
            return new double[]{wRiferR, wMisureR, contrR};
        }
    }

    public boolean isEngine() {
        return engine;
    }

    public boolean isSensor() {
        return sensor;
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public float getPosTh() {
        return posTh;
    }

    public float getRiferTh() {
        return riferTh;
    }

    public float getRiferX() {
        return riferX;
    }

    public float getRiferY() {
        return riferY;
    }

    public int getRssi() {
        return rssi;
    }

    @Override
    public int getAck() {
        return ack;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean isCmdAT() {
        return false;
    }

    @Override
    public XBeeAddress getAddress() {
        return address;
    }

    @Override
    public byte[] getData() {
        return data;
    }

    @Override
    public String toString() {
        String export = "Command: " + name + "\n"
                + "Address: " + address.toString() + "\n"
                + "rssi: -" + rssi + " dbm \n"
                + "Time Stamp: " + df.format(new Date(timestamp));
        return export;
    }
}
