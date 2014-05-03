/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.serial.packet;

import java.util.Date;

/**
 *
 * @author Raffaello
 */
public class TxStatusPacket extends Jpacket {

    private final static String name = "89";
    private long timestamp;
    public static String[] cmdData = {"Success",
        "No ACK (Acknowledgement) received",
        "CCA failure",
        "Purged"};
    private byte[] data;
    private final int ack;

    public TxStatusPacket(byte frameID, byte cmdData, long currentTimeMillis) {
        this.ack = (int) (frameID & 0xFF);
        this.timestamp = currentTimeMillis;
        this.data = new byte[]{cmdData};
    }

    @Override
    public int getAck() {
        return ack;
    }

    public String getStatusS() {
        return cmdData[(int) data[0]];
    }

    public int getStatus() {
        return (int) data[0];
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
        return new XBeeAddress();
    }

    @Override
    public byte[] getData() {
        return data;
    }

    @Override
    public String toString() {
        String export = "Command: " + name + "\n"
                + "Frame ID: " + ack + "\n"
                + "Status: " + cmdData[(int) data[0]] + "\n"
                + "Time Stamp: " + df.format(new Date(timestamp));
        return export;
    }
}
