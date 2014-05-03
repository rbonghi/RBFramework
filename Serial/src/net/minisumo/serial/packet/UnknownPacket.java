/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.serial.packet;

import java.util.Date;

/**
 *
 * @author Master
 */
public class UnknownPacket extends Jpacket {

    private String name;
    private byte[] data;
    private long timestamp;
    private boolean cmdAT;
    private int rssi, option;
    private XBeeAddress address;
    private int ack = 0;

    public UnknownPacket(String nomecomando, byte[] data, long timestamp) {
        this.name = nomecomando;
        this.data = data;
        this.timestamp = timestamp;
        this.address = new XBeeAddress();
        this.rssi = -1;
        this.cmdAT = true;
        this.ack = 1 + (int) (Math.random() * 254);
    }

    public UnknownPacket(String nomecomando, XBeeAddress address, long timestamp) {
        this.name = nomecomando;
        this.data = nomecomando.getBytes();
        this.timestamp = timestamp;
        this.address = address;
        this.cmdAT = false;
        if(!address.isBroadcast()){
            this.ack = 1 + (int) (Math.random() * 254);
        }
    }

    public UnknownPacket(String nomecomando, XBeeAddress address, boolean ack, long timestamp) {
        this.name = nomecomando;
        this.data = nomecomando.getBytes();
        this.timestamp = timestamp;
        this.address = address;
        this.cmdAT = false;
        if (ack) {
            this.ack = 1 + (int) (Math.random() * 254);
        }
    }

    public UnknownPacket(String nomecomando, byte data[], XBeeAddress address, long timestamp) {
        this.name = nomecomando;
        this.data = nomecomando.getBytes();
        this.timestamp = timestamp;
        this.address = address;
        this.cmdAT = false;
        if(!address.isBroadcast()){
            this.ack = 1 + (int) (Math.random() * 254);
        }
    }

    public UnknownPacket(String nomecomando, byte data[], XBeeAddress address, boolean ack, long timestamp) {
        this.name = nomecomando;
        this.data = nomecomando.getBytes();
        this.timestamp = timestamp;
        this.address = address;
        this.cmdAT = false;
        if (ack) {
            this.ack = 1 + (int) (Math.random() * 254);
        }
    }

    public UnknownPacket(String nomecomando, byte[] data, XBeeAddress address, int rssi, int option, long timestamp) {
        this.name = nomecomando;
        this.data = data;
        this.timestamp = timestamp;
        this.address = address;
        this.rssi = rssi;
        this.option = option;
        this.cmdAT = false;
        ack = 0;
    }

    @Override
    public int getAck() {
        return ack;
    }

    public int getOption() {
        return option;
    }

    @Override
    public byte[] getData() {
        return data;
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
        return cmdAT;
    }

    @Override
    public XBeeAddress getAddress() {
        return address;
    }

    public int getRssi() {
        return rssi;
    }

    @Override
    public String toString() {
        String export;
        if (cmdAT) {
            String temp = "";
            for (int i = 0; i < data.length; i++) {
                temp += Integer.toHexString((int) (data[i] & 0xff));
            }
            export = "Command: " + name + "\n"
                    + "Data: " + temp + "\n"
                    + "Time Stamp: " + df.format(new Date(timestamp));
        } else {
            export = "Command: " + name + "\n"
                    + "Data: " + new String(data) + "\n"
                    + "Address: " + address.toString() + "\n"
                    + "rssi: -" + rssi + " dbm \n"
                    + "Time Stamp: " + df.format(new Date(timestamp));
        }
        return export;
    }
}
