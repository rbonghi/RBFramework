/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.serial;

import gnu.io.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import net.minisumo.serial.packet.Jpacket;
import net.minisumo.services.Service;
import org.openide.util.Exceptions;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Raffaello
 */
@ServiceProvider(service = Service.class)
public class XBeeAPI implements Service, PropertyChangeListener {

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    public static final byte START_DELIMITER = (byte) 0x7E;
    public static final byte VERIFY_CKSUM = (byte) 0xFF;
    private static SerialWriter writer;
    private static SerialReader reader;
    private static String[] portList;
    private CommPort commPort;
    //Property Connection
    private static boolean connect;
    public static final String PROP_CONNECT = "connect";
    private String portName = "COM2";
    public static final String PROP_PORTNAME = "portName";
    private int baudRate = 57600;
    public static final String PROP_BAUDRATE = "baudRate";
    private int databits = SerialPort.DATABITS_8;
    public static final String PROP_DATABITS = "databits";
    private int stopBits = SerialPort.STOPBITS_1;
    public static final String PROP_STOPBITS = "stopBits";
    private int parity = SerialPort.PARITY_NONE;
    public static final String PROP_PARITY = "parity";

    public XBeeAPI() {
        addPropertyChangeListener(this);
        //Create list serial port
        HashSet<CommPortIdentifier> comm = availableSerialPorts();
        portList = new String[comm.size()];
        int j = 0;
        for (CommPortIdentifier i : comm) {
            portList[j++] = i.getName();
        }
    }

    public void connect() throws Exception {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if (portIdentifier.isCurrentlyOwned()) {
            System.err.println("Error: Port is currently in use");
        } else {
            commPort = portIdentifier.open(this.getClass().getName(), 2000);
            if (commPort instanceof SerialPort) {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(baudRate, databits, stopBits, parity);

                InputStream in = serialPort.getInputStream();
                OutputStream out = serialPort.getOutputStream();

                writer = new SerialWriter(out);
                (new Thread(writer, "Serial Writer")).start();

                reader = new SerialReader(in);
                serialPort.addEventListener(reader);
                serialPort.notifyOnDataAvailable(true);

            } else {
                System.err.println("Error: Only serial ports are handled by this example.");
            }
        }
    }

    public void close() throws IOException {
        writer.close();
        reader.close();
        commPort.close();
    }

    /**
     * Get the value of portName
     *
     * @return the value of portName
     */
    public String getPortName() {
        return portName;
    }

    /**
     * Set the value of portName
     *
     * @param portName new value of portName
     */
    public void setPortName(String portName) {
        String oldPortName = this.portName;
        this.portName = portName;
        propertyChangeSupport.firePropertyChange(PROP_PORTNAME, oldPortName, portName);
    }

    /**
     * Get the value of baudRate
     *
     * @return the value of baudRate
     */
    public int getBaudRate() {
        return baudRate;
    }

    /**
     * Set the value of baudRate
     *
     * @param baudRate new value of baudRate
     */
    public void setBaudRate(int baudRate) {
        int oldBaudRate = this.baudRate;
        this.baudRate = baudRate;
        propertyChangeSupport.firePropertyChange(PROP_BAUDRATE, oldBaudRate, baudRate);
    }

    /**
     * Get the value of databits
     *
     * @return the value of databits
     */
    public int getDatabits() {
        return databits;
    }

    /**
     * Set the value of databits
     *
     * @param databits new value of databits
     */
    public void setDatabits(int databits) {
        int oldDatabits = this.databits;
        this.databits = databits;
        propertyChangeSupport.firePropertyChange(PROP_DATABITS, oldDatabits, databits);
    }

    /**
     * Get the value of stopBits
     *
     * @return the value of stopBits
     */
    public int getStopBits() {
        return stopBits;
    }

    /**
     * Set the value of stopBits
     *
     * @param stopBits new value of stopBits
     */
    public void setStopBits(int stopBits) {
        int oldStopBits = this.stopBits;
        this.stopBits = stopBits;
        propertyChangeSupport.firePropertyChange(PROP_STOPBITS, oldStopBits, stopBits);
    }

    /**
     * Get the value of parity
     *
     * @return the value of parity
     */
    public int getParity() {
        return parity;
    }

    /**
     * Set the value of parity
     *
     * @param parity new value of parity
     */
    public void setParity(int parity) {
        int oldParity = this.parity;
        this.parity = parity;
        propertyChangeSupport.firePropertyChange(PROP_PARITY, oldParity, parity);
    }

    /**
     * Get the value of connect
     *
     * @return the value of connect
     */
    @Override
    public boolean isConnect() {
        return connect;
    }

    /**
     * Set the value of connect
     *
     * @param connect new value of connect
     */
    public void setConnect(boolean connect) {
        boolean oldConnect = XBeeAPI.connect;
        XBeeAPI.connect = connect;
        propertyChangeSupport.firePropertyChange(PROP_CONNECT, oldConnect, connect);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() == this) {
            if (evt.getPropertyName().equals(PROP_CONNECT)) {
                try {
                    if ((Boolean) evt.getNewValue()) {
                        connect();
                    } else {
                        close();
                    }
                } catch (Exception ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
    }

    public static <P extends Jpacket> P sendPacket(P packet, long timeout) throws InterruptedException, ExecutionException, ConnectionException {
        if (connect) {
            SendPacket<P> sendPacket = new SendPacket<P>(packet);
            reader.addEventJpacketListener(sendPacket);
            FutureTask<P> reciveData = new FutureTask<P>(sendPacket);
            Thread t = new Thread(reciveData, packet.getName());
            t.start();
            writer.activator();
            try {
                return reciveData.get(timeout, TimeUnit.MILLISECONDS);
            } catch (TimeoutException ex) {
//                Exceptions.printStackTrace(ex);
                return null;
            }
        } else {
            throw new ConnectionException("No connected");
        }
    }

    /**
     * 
     * @return A HashSet containing the CommPortIdentifier for all serial ports that are not currently being used.
     */
    private static HashSet<CommPortIdentifier> availableSerialPorts() {
        HashSet<CommPortIdentifier> h = new HashSet<CommPortIdentifier>();
        Enumeration thePorts = CommPortIdentifier.getPortIdentifiers();
        while (thePorts.hasMoreElements()) {
            CommPortIdentifier com = (CommPortIdentifier) thePorts.nextElement();
            switch (com.getPortType()) {
                case CommPortIdentifier.PORT_SERIAL:
                    try {
                        CommPort thePort = com.open("CommUtil", 50);
                        thePort.close();
                        h.add(com);
                    } catch (PortInUseException e) {
                        System.out.println("Port, " + com.getName() + ", is in use.");
                    } catch (Exception e) {
                        System.err.println("Failed to open port " + com.getName());
                    }
            }
        }
        return h;
    }

    public static String[] getPortList() {
        return portList;
    }

    public static SerialReader getReader() {
        return reader;
    }

    /**
     * Conversione da byte a int
     * @param b
     * @param offset
     * @return int
     */
    public static int byteArrayToInt(byte[] b, int offset, int totByte) {
        int value = 0;
        for (int i = 0; i < totByte; i++) {
            int shift = (totByte - 1 - i) * 8;
            value += (b[i + offset] & 0xFF) << shift;
        }
        return value;
    }

    /**
     * Conversione da int a byte
     * @param value
     * @return
     */
    public static byte[] intToByteArray(int value, int totByte) {
        byte[] b = new byte[totByte];
        for (int i = 0; i < totByte; i++) {
            int offset = (b.length - 1 - i) * 8;
            b[i] = (byte) ((value >>> offset) & 0xFF);
        }
        return b;
    }

    /**
     * Conversione da byte a float
     * @param b
     * @param offset
     * @return float
     */
    public static float byteArrayToFloat(byte[] b, int offset) {
        ByteBuffer buf = ByteBuffer.wrap(b, offset, 4);
        return buf.getFloat();
    }

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public static class PortNameEditor extends PropertyEditorSupport {

        private String[] options;

        public PortNameEditor() {
            options = XBeeAPI.portList;
        }

        @Override
        public String[] getTags() {
            return options;
        }

        @Override
        public String getJavaInitializationString() {
            return "" + getValue();
        }

        @Override
        public String getAsText() {
            return (String) getValue();
        }

        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            setValue(text);
        }
    }

    public static class BaudRateEditor extends PropertyEditorSupport {

        private String[] options = {"9600", "19200", "38400", "57600", "115200"};

        @Override
        public String[] getTags() {
            return options;
        }

        @Override
        public String getJavaInitializationString() {
            return "" + getValue();
        }

        @Override
        public String getAsText() {
            return "" + getValue();
        }

        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            for (int i = 0; i < options.length; i++) {
                if (options[i].equals(text)) {
                    setValue(Integer.parseInt(text));
                    return;
                }
            }
        }
    }

    public static class DataBitsEditor extends PropertyEditorSupport {

        private String[] options = {"5", "6", "7", "8"};

        @Override
        public String[] getTags() {
            return options;
        }

        @Override
        public String getJavaInitializationString() {
            return "" + getValue();
        }

        @Override
        public String getAsText() {
            int value = (Integer) getValue();
            return options[value - 5];
        }

        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            for (int i = 0; i < options.length; i++) {
                if (options[i].equals(text)) {
                    setValue(i + 5);
                    return;
                }
            }
        }
    }

    public static class ParityEditor extends PropertyEditorSupport {

        private String[] options = {"NONE", "ODD", "EVEN", "MARK", "SPACE"};

        @Override
        public String[] getTags() {
            return options;
        }

        @Override
        public String getJavaInitializationString() {
            return "" + getValue();
        }

        @Override
        public String getAsText() {
            int value = (Integer) getValue();
            return options[value];
        }

        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            for (int i = 0; i < options.length; i++) {
                if (options[i].equals(text)) {
                    setValue(i);
                    return;
                }
            }
        }
    }

    public static class StopBitsEditor extends PropertyEditorSupport {

        private String[] options = {"1", "2", "1.5"};

        @Override
        public String[] getTags() {
            return options;
        }

        @Override
        public String getJavaInitializationString() {
            return "" + getValue();
        }

        @Override
        public String getAsText() {
            int value = (Integer) getValue();
            return options[value - 1];
        }

        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            for (int i = 0; i < options.length; i++) {
                if (options[i].equals(text)) {
                    setValue(i + 1);
                    return;
                }
            }
        }
    }
}
