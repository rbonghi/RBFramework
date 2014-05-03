/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.serial.packet.atcommand;

import java.util.Date;
import net.minisumo.serial.XBeeAPI;
import net.minisumo.serial.packet.Jpacket;
import net.minisumo.serial.packet.XBeeAddress;

/**
 *
 * @author Master
 */
public class NDPacket extends Jpacket {

    private final static String name = "ND";
    private long timestamp;
    private int sourceAddress, serialHigh, serialLow, rssi;
    private String node;
    private byte[] data;
    private int ack = 0;

    public NDPacket(long currentTimeMillis) {
        this.timestamp = currentTimeMillis;
        this.data = new byte[0];
        this.ack = 1 + (int) (Math.random() * 254);
    }

    public NDPacket(byte[] cmdData, long currentTimeMillis) {
        this.timestamp = currentTimeMillis;
        this.data = cmdData;
        if (cmdData.length > 0) {
            sourceAddress = ((data[0] & 0xFF) << 8) + (data[1] & 0xFF);
            serialHigh = XBeeAPI.byteArrayToInt(cmdData, 2, 4);
            serialLow = XBeeAPI.byteArrayToInt(cmdData, 6, 4);
            rssi = (int) cmdData[10];
            byte[] temp = new byte[cmdData.length - 12];
            System.arraycopy(cmdData, 11, temp, 0, temp.length);
            node = new String(temp);
            //node = new String(cmdData, 12, cmdData.length - 1, "");
        }
    }

    public int getSourceAddress() {
        return sourceAddress;
    }

    public int getRssi() {
        return rssi;
    }

    public int getSerialHigh() {
        return serialHigh;
    }

    public int getSerialLow() {
        return serialLow;
    }

    public String getNode() {
        return node;
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
        String export;
        if (data.length > 0) {
            export = "Command: " + name + "\n"
                    + "Source Address: " + Integer.toHexString(sourceAddress).toUpperCase() + "\n"
                    + "Serial Number High: " + Integer.toHexString(serialHigh).toUpperCase() + "\n"
                    + "Serial Number Low: " + Integer.toHexString(serialLow).toUpperCase() + "\n"
                    + "Received Signal Strength: -" + rssi + "dBm\n"
                    + "Node Identifier: " + node + "\n"
                    + "Time Stamp: " + df.format(new Date(timestamp));
        } else {
            export = "Command: " + name + "\n"
                    + "Status: OK \n"
                    + "Time Stamp: " + df.format(new Date(timestamp));
        }
        return export;
    }
}
