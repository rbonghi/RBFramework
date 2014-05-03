/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.serial;

import gnu.io.*;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.EventListenerList;
import net.minisumo.serial.packet.Jpacket;
import net.minisumo.serial.packet.StatusPacket;
import net.minisumo.serial.packet.TxStatusPacket;
import net.minisumo.serial.packet.UnknownPacket;
import net.minisumo.serial.packet.XBeeAddress;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;

/**
 *
 * @author Raffaello
 */
public class SerialReader implements SerialPortEventListener {

    public static final ConcurrentLinkedQueue<Jpacket> bufferInput = new ConcurrentLinkedQueue<Jpacket>();
    protected EventListenerList listenerList = new EventListenerList();
    private static final String POST_NAME_AT = "Packet";
    private static final String POST_NAME_ROBOT = "Robot";
    private final InputStream in;
    private final Set<Class<? extends Jpacket>> allClasses;
    private final String PACKAGE_NAME;
    private int statusDec = 0;         //Status avanzamento decodifica
    private boolean firstL = true;     //Primo byte lunghezza pacchetto
    private int lengthPkg;             //Lunghezza pacchetto
    private byte[] APIStructure;       //Pacchetto ricevuto
    private byte checksumR = 0;        //Checksum ricezione pacchetto
    private int counterAPIStr = 0;     //Contatore byte
    private Jpacket packet;            //Pacchetto
    private Class classPacket;         //Classe pacchetto

    public SerialReader(InputStream in) {
        this.in = in;
        PACKAGE_NAME = this.getClass().getPackage().getName();
        Result<Jpacket> lookupResult = Lookup.getDefault().lookupResult(Jpacket.class);
        allClasses = lookupResult.allClasses();
    }

    @Override
    public void serialEvent(SerialPortEvent spe) {
        try {
            int data;
            while ((data = in.read()) > -1) {
                if (readByt((byte) data)) {                                     //Buffering Packet
                    decodePkg(APIStructure.clone());                            //Decode Packet
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(SerialReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
     * Chiusura Thread reader
     */
    public void close() throws IOException {
        in.close();
    }

    private boolean readByt(byte bytePkg) {                                     //Lettura singolo byte
        switch (statusDec) {                                                    //Switch casi
            //Lettura START DELIMITER
            case 0:
                if (bytePkg == XBeeAPI.START_DELIMITER) {                    //verifica START_DELIMITER
                    statusDec++;                                                //Avanzamento decodifica
                } else {
                    throw new UnsupportedOperationException("Packet error");    //avvio funzione errore
                }
                break;

            //Lettura Lunghezza pacchetto
            case 1:
                if (firstL) {                                                   //se primo byte...
                    lengthPkg = (int) (bytePkg & 0xff) << 8;                    //Schift di 8 posizioni
                    firstL = false;                                             //ed attesa secondo byte
                } else {
                    lengthPkg += (int) (bytePkg & 0xff);                        //Calcolo lunghezza completa
                    statusDec++;                                                //Avanzamento decodifica
                    firstL = true;                                              //Riabilitata la lettura del primo byte
                    APIStructure = new byte[lengthPkg];                         //Creazione array dimensione pacchetto
                }
                break;

            //Lettura API e calcolo CHECKSUM
            case 2:
                checksumR += bytePkg;                                           //Calcolo Checksum
                if (counterAPIStr < lengthPkg) {                                //Attesa ultimo byte
                    APIStructure[counterAPIStr] = bytePkg;                      //salvataggio array
                    counterAPIStr++;                                            //incremento contatore
                } else {                                                        //Se ultimo byte
                    if (checksumR == XBeeAPI.VERIFY_CKSUM) {                 //verifica checksum
                        checksumR = 0;                                          //Reset checksum
                        statusDec = 0;                                          //Reset status
                        counterAPIStr = 0;                                      //Reset contatore
                        return true;
                    } else {
                        throw new UnsupportedOperationException("Packet error");//Errore checksum
                    }
                }
                break;

            default:
                throw new UnsupportedOperationException("Packet error");        //Errore status
        }
        return false;
    }

    private void decodePkg(byte[] APIStructure) {
        switch (APIStructure[0]) {
            case (byte) 0x8A:               //Modem status
                packet = new StatusPacket(APIStructure[1], System.currentTimeMillis());
                bufferInput.add(packet);
                break;

            case (byte) 0x80:               //Ricezione pacchetto da indirizzo a 64bit
                byte[] data = new byte[APIStructure.length - 12];
                for (int i = 0; i < data.length; i++) {
                    data[i] = APIStructure[i + 12];
                }
                String cmd = new String(new byte[]{APIStructure[11]});
                int addressHigh = XBeeAPI.byteArrayToInt(APIStructure, 1, 4);
                int addressLow = XBeeAPI.byteArrayToInt(APIStructure, 5, 4);
                int rssi = APIStructure[9];
                int option = APIStructure[10];
                try {
                    for (Class<? extends Jpacket> packetEx : allClasses) {
                        if (packetEx.getSimpleName().equals(cmd + POST_NAME_ROBOT)) {
                            packet = (Jpacket) packetEx.getDeclaredConstructor(byte[].class, XBeeAddress.class, int.class, long.class).newInstance(data, new XBeeAddress(addressHigh, addressLow), rssi, System.currentTimeMillis());
                            break;
                        }
                    }
                } catch (Exception ex) {
                    packet = new UnknownPacket(cmd, data, new XBeeAddress(addressHigh, addressLow), rssi, option, System.currentTimeMillis());
                }
                bufferInput.add(packet);
                break;

            case (byte) 0x81:               //Ricezione pacchetto da indirizzo a 16bit
                data = new byte[APIStructure.length - 6];
                for (int i = 0; i < data.length; i++) {
                    data[i] = APIStructure[i + 6];
                }
                cmd = new String(new byte[]{APIStructure[5]});
                int address = (APIStructure[1] << 8) + APIStructure[2];
                rssi = APIStructure[3];
                option = APIStructure[4];
                try {
                    classPacket = Class.forName(PACKAGE_NAME + ".packet.ATcommand." + cmd + POST_NAME_ROBOT);
                    packet = (Jpacket) classPacket.getDeclaredConstructor(byte[].class, XBeeAddress.class, int.class, long.class).newInstance(data, new XBeeAddress(address), rssi, System.currentTimeMillis());
                } catch (Exception ex) {
                    packet = new UnknownPacket(cmd, data, new XBeeAddress(address), rssi, option, System.currentTimeMillis());
                }
                break;

            case (byte) 0x88:               //Ricezione comando AT
                if (APIStructure[4] == 0) {
                    String nameAT = new String(new byte[]{APIStructure[2], APIStructure[3]});
                    byte[] cmdData = new byte[APIStructure.length - 5];
                    for (int i = 0; i < cmdData.length; i++) {
                        cmdData[i] = APIStructure[i + 5];
                    }
                    try {
                        classPacket = Class.forName(PACKAGE_NAME + ".packet.ATcommand." + nameAT + POST_NAME_AT);
                        packet = (Jpacket) classPacket.getDeclaredConstructor(byte[].class, long.class).newInstance(cmdData, System.currentTimeMillis());
                    } catch (Exception ex) {
                        packet = new UnknownPacket(nameAT, cmdData, System.currentTimeMillis());
                    }
                    bufferInput.add(packet);
                } else {
                    throw new UnsupportedOperationException("Decode error");
                }
                break;

            case (byte) 0x89:               //ACK trasmissione pacchetto
                packet = new TxStatusPacket(APIStructure[1], APIStructure[2], System.currentTimeMillis());
                bufferInput.add(packet);
                break;

            default:
                throw new UnsupportedOperationException("Decode error");
        }
        fireEventJpacket(new EventJpacket(packet));
    }

    // This methods allows classes to register for MyEvents
    public void addEventJpacketListener(EventJpacketListener listener) {
        listenerList.add(EventJpacketListener.class, listener);
    }

    // This methods allows classes to unregister for MyEvents
    public void removeEventJpacketListener(EventJpacketListener listener) {
        listenerList.remove(EventJpacketListener.class, listener);
    }

    // This private class is used to fire MyEvents
    void fireEventJpacket(EventJpacket evt) {
        Object[] listeners = listenerList.getListenerList();
        // Each listener occupies two elements - the first is the listener class
        // and the second is the listener instance
        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == EventJpacketListener.class) {
                ((EventJpacketListener) listeners[i + 1]).eventJpacketOccurred(evt);
            }
        }
    }
}
