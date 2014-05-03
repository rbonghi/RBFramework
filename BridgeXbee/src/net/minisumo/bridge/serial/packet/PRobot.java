/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.bridge.serial.packet;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
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
public class PRobot<P> extends Jpacket {

    public final static char DEAD_PARAMETERS = 'O', PIDL = 'L', PIDR = 'R', DEAD_RECKONING = 'D';
    public final static char WRITE = 'W', READ = 'R';
    private String name = "P";
    private long timestamp;
    private byte[] data;
    private XBeeAddress address;
    private int rssi;
    private int ack = 0;
    private P dataInformation;
    private char detail;

    public PRobot() {
    }

    /**
     * Lettura parametri robot
     * @param detail
     * @param address
     * @param timestamp 
     */
    public PRobot(char detail, XBeeAddress address, long timestamp) {
        this(detail, address, true, timestamp);
    }

    /**
     * Lettura paremetri robot
     * @param detail
     * @param address
     * @param ack
     * @param timestamp 
     */
    public PRobot(char detail, XBeeAddress address, boolean ack, long timestamp) {
        this.address = address;
        this.timestamp = timestamp;
        name += new String(new char[]{READ, detail});
        data = name.getBytes();
        if (ack && !address.isBroadcast()) {
            this.ack = 1 + (int) (Math.random() * 254);
        }
    }

    /**
     * Scrittura parametri robot
     * @param detail
     * @param address
     * @param timestamp 
     */
    public PRobot(char detail, P dataTrasmit, XBeeAddress address, long timestamp) {
        this(detail, dataTrasmit, address, true, timestamp);
    }

    /**
     * Scrittura parametri robot
     * @param detail
     * @param dataTrasmit
     * @param address
     * @param ack
     * @param timestamp 
     */
    public PRobot(char detail, P dataTrasmit, XBeeAddress address, boolean ack, long timestamp) {
        this.address = address;
        this.timestamp = timestamp;
        this.detail = detail;
        name += new String(new char[]{WRITE, detail});
        if (ack && !address.isBroadcast()) {
            this.ack = 1 + (int) (Math.random() * 254);
        }
        code(dataTrasmit);
    }

    /**
     * Ricezione pacchetto
     * @param data
     * @param address
     * @param rssi
     * @param timestamp 
     */
    public PRobot(byte[] data, XBeeAddress address, int rssi, long timestamp) {
        this.data = data;
        this.address = address;
        this.rssi = rssi;
        this.timestamp = timestamp;
        this.ack = 0;
        name += new String(new byte[]{data[0], data[1]});
        if (data[0] == READ) {
            decode();
        }
    }

    private void code(P dataTransmit) {
        int length;
        ByteBuffer dataTemp = null;
        switch (detail) {
            case DEAD_RECKONING:
            case DEAD_PARAMETERS:
                length = Array.getLength(dataTransmit);
                dataTemp = ByteBuffer.allocate(name.length() + length * 4);
                for (int i = 0; i < length; i++) {
                    dataTemp.putFloat(name.length() + i * 4, Array.getFloat(dataTransmit, i));
                }
                break;

            case PIDL:
            case PIDR:
                length = Array.getLength(dataTransmit);
                dataTemp = ByteBuffer.allocate(name.length() + length * 2);
                for (int i = 0; i < length; i++) {
                    float temp = Array.getFloat(dataTransmit, i);
                    dataTemp.putInt(name.length() + i * 2, (int) (temp * Math.pow(2, 15)));
                }
                break;
        }
        dataTemp.put(name.getBytes());
        data = dataTemp.array();
    }

    private void decode() {
        int length;
        switch (data[1]) {
            case DEAD_RECKONING:                            //Stesse operazioni di decodifica
            case DEAD_PARAMETERS:
                length = (data.length - 2) / 4;
                dataInformation = (P) Array.newInstance(float.class, length);
                for (int i = 0; i < length; i++) {
                    float idata = ByteBuffer.wrap(data, i * 4 + 2, 4).getFloat();
                    Array.setFloat(dataInformation, i, idata);
                }
                break;

            case PIDL:
            case PIDR:
                length = (data.length - 2) / 2;
                dataInformation = (P) Array.newInstance(float.class, length);
                for (int i = 0; i < length; i++) {
                    float idata = (float) (XBeeAPI.byteArrayToInt(data, i * 2 + 2, 2) * Math.pow(2, -15));     //Q15 Transform
                    Array.setFloat(dataInformation, i, idata);
                }
                break;
        }
    }

    public P getDataInformation() {
        return dataInformation;
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
