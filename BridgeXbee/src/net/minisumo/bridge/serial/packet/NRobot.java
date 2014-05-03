/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.bridge.serial.packet;

import java.util.Date;
import net.minisumo.serial.packet.Jpacket;
import net.minisumo.serial.packet.XBeeAddress;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Master
 */
@ServiceProvider(service = Jpacket.class)
public class NRobot extends Jpacket {

    private final static String name = "N";
    private long timestamp;
    private byte[] data;
    private XBeeAddress address;
    private int rssi;
    private String nameRobot;
    private int ack = 0;

    public NRobot() {
    }

    public NRobot(XBeeAddress address, long timestamp) {
        this.address = address;
        this.timestamp = timestamp;
        data = name.getBytes();
        if(!address.isBroadcast()){
            this.ack = 1 + (int) (Math.random() * 254);
        }
    }

    public NRobot(XBeeAddress address, boolean ack, long timestamp) {
        this.address = address;
        this.timestamp = timestamp;
        data = name.getBytes();
        if (ack) {
            this.ack = 1 + (int) (Math.random() * 254);
        }
    }

    public NRobot(byte[] data, XBeeAddress address, int rssi, long timestamp) {
        this.data = data;
        this.address = address;
        this.rssi = rssi;
        this.timestamp = timestamp;
        this.nameRobot = new String(data);
        this.ack = 0;
    }

    public String getNameRobot() {
        return nameRobot;
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
                + "Name Robot: " + nameRobot + "\n"
                + "Address: " + address.toString() + "\n"
                + "rssi: -" + rssi + " dbm \n"
                + "Time Stamp: " + df.format(new Date(timestamp));
        return export;
    }
}
