/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.bridge.serial.packet;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.Date;
import net.minisumo.serial.packet.Jpacket;
import net.minisumo.serial.packet.XBeeAddress;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Raffaello
 */
@ServiceProvider(service = Jpacket.class)
public class DRobot<P> extends Jpacket {

    public final static char EN_ENGINE = 'E', EN_SENSOR = 'S';
    public final static char DRI_VELOCITY = 'V', COORDINATES = 'D';
    private String name = "D";
    private long timestamp;
    private int ack = 0;
    private byte[] data;
    private XBeeAddress address;
    private int rssi;

    public DRobot() {
    }

    public DRobot(char detail, P command, XBeeAddress address, long timestamp) {
        this(detail, command, address, true, timestamp);
    }

    public DRobot(char detail, P command, XBeeAddress address, boolean ack, long timestamp) {
        this.address = address;
        this.timestamp = timestamp;
        name += detail;
        data = name.getBytes();
        if (ack && !address.isBroadcast()) {
            this.ack = 1 + (int) (Math.random() * 254);
        }
        code(command);
    }

    public DRobot(byte[] data, XBeeAddress address, int rssi, long timestamp) {
        this.data = data;
        this.address = address;
        this.rssi = rssi;
        this.timestamp = timestamp;
        this.ack = 0;
        name += new String(new byte[]{data[0]});
    }

    private void code(P command) {
        byte[] datatemp;
        if (command.getClass().isArray()) {
            int length = Array.getLength(command);
            ByteBuffer dataTemp = ByteBuffer.allocate(data.length + length * 4);
            for (int i = 0; i < length; i++) {
                if (Array.get(command, i) instanceof Float) {
                    dataTemp.putFloat(data.length + i * 4, Array.getFloat(command, i));
                }
            }
            datatemp = dataTemp.array();
        } else {
            datatemp = new byte[data.length + 1];
            if (command.getClass().equals(Boolean.class)) {
                if (command.equals(true)) {
                    datatemp[data.length] = 1;
                } else {
                    datatemp[data.length] = 0;
                }
            }
        }
        System.arraycopy(data, 0, datatemp, 0, data.length);
        data = datatemp;
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
