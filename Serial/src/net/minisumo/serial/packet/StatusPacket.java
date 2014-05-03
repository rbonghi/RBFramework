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
public class StatusPacket extends Jpacket {

    private final static String name = "8A";
    private long timestamp;
    private String[] cmdData = {"Hardware reset",
        "Watchdog timer reset",
        "Associated",
        "Disassociated",
        "Synchronization Lost (Beacon-enabled only )",
        "Coordinator realignment",
        "Coordinator started"};
    private byte[] data;

    public StatusPacket(byte cmdData, long currentTimeMillis) {
        this.timestamp = currentTimeMillis;
        this.data = new byte[]{cmdData};
    }

    @Override
    public int getAck() {
        return 1;
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
                + "Status: " + cmdData[(int) data[0]] + "\n"
                + "Time Stamp: " + df.format(new Date(timestamp));
        return export;
    }
}
