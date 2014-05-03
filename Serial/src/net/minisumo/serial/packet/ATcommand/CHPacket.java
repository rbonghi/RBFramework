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
public class CHPacket extends Jpacket {

    private final static String name = "CH";
    private long timestamp;
    private String channelNumber;
    private byte[] data;
    private int ack = 0;

    public CHPacket(long currentTimeMillis) {
        this.timestamp = currentTimeMillis;
        data = new byte[0];
        this.ack = 1 + (int) (Math.random() * 254);
    }

    public CHPacket(byte[] cmdData, long currentTimeMillis) {
        this.timestamp = currentTimeMillis;
        this.data = cmdData;
        if (cmdData.length != 0) {
            channelNumber = Integer.toHexString(cmdData[0]);
        }
    }

    public CHPacket(String channelNumber, long timestamp) {
        this.timestamp = timestamp;
        this.channelNumber = channelNumber;
        this.data = new byte[]{(byte) Integer.parseInt(channelNumber, 16)};
        this.ack = 1 + (int) (Math.random() * 254);
    }

    public String getChannel() {
        return channelNumber;
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
                + "Channel: " + channelNumber + "\n"
                + "Time Stamp: " + df.format(new Date(timestamp));
        return export;
    }
}
