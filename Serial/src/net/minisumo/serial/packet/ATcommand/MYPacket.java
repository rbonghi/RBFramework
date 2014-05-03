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
public class MYPacket extends Jpacket {

    private final static String name = "MY";
    private long timestamp;
    private int myaddress;
    private byte[] data;
    private int ack = 0;

    public MYPacket(long currentTimeMillis) {
        this.timestamp = currentTimeMillis;
        data = new byte[0];
        this.ack = 1 + (int) (Math.random() * 254);
    }

    public MYPacket(byte[] cmdData, long currentTimeMillis) {
        this.timestamp = currentTimeMillis;
        this.data = cmdData;
        if (cmdData.length != 0) {
            myaddress = ((data[0] & 0xFF) << 8) + (data[1] & 0xFF);
        }
    }

    public MYPacket(int myaddress, long timestamp) {
        this.timestamp = timestamp;
        this.myaddress = myaddress;
        this.data = new byte[]{(byte) (myaddress >> 8), (byte) myaddress};
        this.ack = 1 + (int) (Math.random() * 254);
    }

    public int getMYAddress() {
        return myaddress;
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
                + "MY Address: " + Integer.toHexString(myaddress).toUpperCase() + "\n"
                + "Time Stamp: " + df.format(new Date(timestamp));
        return export;
    }
}
