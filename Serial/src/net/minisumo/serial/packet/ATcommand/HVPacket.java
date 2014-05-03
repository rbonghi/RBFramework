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
public class HVPacket extends Jpacket {

    private final static String name = "HV";
    private long timestamp;
    private String hardware;
    private byte[] data;
    private int ack = 0;

    public HVPacket(long currentTimeMillis) {
        this.timestamp = currentTimeMillis;
        data = new byte[0];
        this.ack = 1 + (int) (Math.random() * 254);
    }

    public HVPacket(byte[] cmdData, long currentTimeMillis) {
        this.timestamp = currentTimeMillis;
        hardware = (Integer.toHexString((int) (cmdData[0] & 0xff)) + Integer.toHexString((int) (cmdData[1] & 0xff))).toUpperCase();
    }

    public String getHardware() {
        return hardware;
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
                + "Hardware: " + hardware + "\n"
                + "Time Stamp: " + df.format(new Date(timestamp));
        return export;
    }
}
