/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.serial;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minisumo.serial.packet.Jpacket;
import net.minisumo.serial.packet.XBeeAddress;
import org.openide.util.Exceptions;

/**
 *
 * @author Raffaello
 */
public class SerialWriter implements Runnable {

    private final static int HEADER = 3;
    public static final ConcurrentLinkedQueue<Jpacket> bufferOutput = new ConcurrentLinkedQueue<Jpacket>();
    private OutputStream out;
    private byte[] data;
    private boolean running;
    private Barrier lock = new Barrier();

    public SerialWriter(OutputStream out) {
        this.out = out;
        running = true;
    }

    @Override
    public void run() {
        while (running) {
            try {
                lock.block();
                while (!bufferOutput.isEmpty()) {
                    Jpacket packetSend = bufferOutput.poll();
                    if (packetSend.isCmdAT()) {
                        data = ATCommand(packetSend.getAck(), packetSend.getName(), packetSend.getData());
                    } else {
                        XBeeAddress address = (XBeeAddress) packetSend.getAddress();
                        if (address.is16Address()) {
                            data = sendData16(packetSend.getAck(), address, (byte) 0x00, packetSend.getData());
                        } else {
                            data = sendData64(packetSend.getAck(), address, (byte) 0x00, packetSend.getData());
                        }
                    }
                    try {
                        out.write(createFrame(data));
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            } catch (InterruptedException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    /*
     * Riavvio thread di scrittura. Rilascio Barriera
     * 
     */
    public synchronized void activator() {
        try {
            lock.release();
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    /*
     * Chiusura thread Writer
     * 
     */
    public void close() throws IOException {
        activator();
        running = false;
        out.close();
    }

    /**
     * Costruzione frame da inviare
     * @param data
     * @return
     */
    private static byte[] createFrame(byte[] data) {
        byte[] dataSend = new byte[data.length + HEADER + 1];        //Dimensione del pacchetto
        byte[] lengthFrame = convertLength(data.length);             //salvataggio lunghezza pacchetto in byte
        dataSend[0] = XBeeAPI.START_DELIMITER;                    //Aggiunta dello START_DELIMITER
        dataSend[1] = lengthFrame[0];                                //Aggiunta dei due byte della lunghezza
        dataSend[2] = lengthFrame[1];
        System.arraycopy(data, 0, dataSend, HEADER, data.length);
        dataSend[data.length + HEADER] = (byte) (XBeeAPI.VERIFY_CKSUM - checkSumW(data));  //Calcolo checksum
        return dataSend;                                             //Ritorno pacchetto correttamente costruito
    }

    /**
     * Invio pacchetti di configurazione del XBee, modalità AT
     * @param ACK
     * @param ATCommand
     * @param cmdData
     * @return Array di byte con il pacchetto da spedire per comandi AT
     */
    private static byte[] ATCommand(int ACK, String ATCommand, byte[] cmdData) {                //Invio comandi AT
        byte[] dataSend = new byte[cmdData.length + 4];
        dataSend[0] = (byte) 0x08;
        dataSend[1] = (byte) ACK;
        dataSend[2] = (byte) ATCommand.charAt(0);
        dataSend[3] = (byte) ATCommand.charAt(1);
        System.arraycopy(cmdData, 0, dataSend, 4, cmdData.length);
        return dataSend;
    }

    /**
     * Invio pacchetti
     * @param ACK
     * @param destinationAdd
     * @param option
     * @param data
     * @return Array di byte con il dato da spedire
     */
    private static byte[] sendData16(int ACK, XBeeAddress destinationAdd, byte option, byte[] data) {    //Invio comando TX
        byte[] dataSend = new byte[data.length + 5];
        dataSend[0] = (byte) 0x01;
        dataSend[1] = (byte) ACK;
        dataSend[2] = (byte) (destinationAdd.AddressLow >> 8);
        dataSend[3] = (byte) (destinationAdd.AddressLow);
        dataSend[4] = option;
        System.arraycopy(data, 0, dataSend, 5, data.length);
        return dataSend;
    }

    /**
     * Invio pacchetti
     * @param ACK
     * @param destinationAdd
     * @param option
     * @param data
     * @return Array di byte con il dato da spedire
     */
    private static byte[] sendData64(int ACK, XBeeAddress destinationAdd, byte option, byte[] data) {    //Invio comando TX
        byte[] dataSend = new byte[data.length + 11];
        dataSend[0] = (byte) 0x00;
        dataSend[1] = (byte) ACK;
        dataSend[2] = (byte) (destinationAdd.AddressHigh >> 24);
        dataSend[3] = (byte) (destinationAdd.AddressHigh >> 16);
        dataSend[4] = (byte) (destinationAdd.AddressHigh >> 8);
        dataSend[5] = (byte) (destinationAdd.AddressHigh);
        dataSend[6] = (byte) (destinationAdd.AddressLow >> 24);
        dataSend[7] = (byte) (destinationAdd.AddressLow >> 16);
        dataSend[8] = (byte) (destinationAdd.AddressLow >> 8);
        dataSend[9] = (byte) (destinationAdd.AddressLow);
        dataSend[10] = option;
        System.arraycopy(data, 0, dataSend, 11, data.length);
        return dataSend;
    }

    /**
     * Creazione dell'intero con la lunghezza del pacchetto
     * @param lengthData
     * @return
     */
    private static byte[] convertLength(int lengthData) {                 //Funzione generazione byte lunghezza pacchetto
        byte numberbytes[] = new byte[2];
        numberbytes[0] = (byte) (lengthData >> 8);                    //MSB
        numberbytes[1] = (byte) (lengthData);                         //LSB
        return numberbytes;
    }

    /**
     * Calcolo del check Sum
     * @param datapkg
     * @return
     */
    private static byte checkSumW(byte[] datapkg) {                       //Costruzione checksum
        byte temp = 0;
        for (int i = 0; i < datapkg.length; i++) {
            temp += datapkg[i];                                        //Somma byte
        }
        return temp;
    }
}
