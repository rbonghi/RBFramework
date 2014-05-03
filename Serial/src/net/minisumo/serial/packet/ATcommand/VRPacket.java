/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.serial.packet.atcommand;

import java.util.Date;
import net.minisumo.serial.packet.Jpacket;
import net.minisumo.serial.packet.XBeeAddress;

/**
 *
 * @author Master
 */
public class VRPacket extends Jpacket {

    private final static String name = "VR";
    private long timestamp;
    private String version;
    private byte[] data;
    private int ack = 0;

    public VRPacket(long currentTimeMillis) {
        this.timestamp = currentTimeMillis;
        data = new byte[0];
        this.ack = 1 + (int) (Math.random() * 254);
    }

    public VRPacket(byte[] cmdData, long currentTimeMillis) {
        this.timestamp = currentTimeMillis;
        version = (Integer.toHexString((int) (cmdData[0] & 0xff)) + Integer.toHexString((int) (cmdData[1] & 0xff))).toUpperCase();
    }

    @Override
    public int getAck() {
        return ack;
    }

    public String getVersion() {
        return version;
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
        return true;
    }

    @Override
    public XBeeAddress getAddress() {
        return new XBeeAddress();
    }

    @Override
    public byte[] getData() {
        return data;
    }

    @Override
    public String toString() {
        String export = "Command: " + name + "\n"
                + "Version: " + version + "\n"
                + "Time Stamp: " + df.format(new Date(timestamp));
        return export;
    }
}
