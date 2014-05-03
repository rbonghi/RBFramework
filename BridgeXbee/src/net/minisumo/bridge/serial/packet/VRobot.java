/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.bridge.serial.packet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minisumo.serial.packet.Jpacket;
import net.minisumo.serial.packet.XBeeAddress;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Master
 */
@ServiceProvider(service = Jpacket.class)
public class VRobot extends Jpacket {

    private String name = "V";
    private long timestamp;
    private byte[] data;
    private XBeeAddress address;
    private int rssi;
    private GregorianCalendar version = new GregorianCalendar();
    private int ack = 0;

    public VRobot() {
    }

    public VRobot(XBeeAddress address, long timestamp) {
        this.address = address;
        this.timestamp = timestamp;
        if(!address.isBroadcast()){
            this.ack = 1 + (int) (Math.random() * 254);
        }
    }

    public VRobot(XBeeAddress address, boolean ack, long timestamp) {
        this.address = address;
        this.timestamp = timestamp;
        if (ack) {
            this.ack = 1 + (int) (Math.random() * 254);
        }
    }

    public VRobot(byte[] data, XBeeAddress address, int rssi, long timestamp) {
        this.data = data;
        this.address = address;
        this.rssi = rssi;
        this.timestamp = timestamp;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String miaData = new String(data);
        try {
            version.setTime(sdf.parse(miaData));
        } catch (ParseException ex) {
            Logger.getLogger(VRobot.class.getName()).log(Level.SEVERE, null, ex);
        }
        ack = 0;
    }

    public GregorianCalendar getVersion() {
        return version;
    }

    @Override
    public int getAck() {
        return ack;
    }

    public int getRssi() {
        return rssi;
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
                + "Versione del: " + version.get(GregorianCalendar.DATE) + "/" + (version.get(GregorianCalendar.MONTH) + 1) + "/" + version.get(GregorianCalendar.YEAR) + "\n"
                + "Address: " + address.toString() + "\n"
                + "rssi: -" + rssi + " dbm \n"
                + "Time Stamp: " + df.format(new Date(timestamp));
        return export;
    }
}
