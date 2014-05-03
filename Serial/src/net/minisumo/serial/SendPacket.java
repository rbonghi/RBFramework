/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.serial;

import java.util.concurrent.Callable;
import net.minisumo.serial.packet.Jpacket;
import net.minisumo.serial.packet.UnknownPacket;
import org.openide.util.Exceptions;

/**
 *
 * @author Raffaello
 */
public class SendPacket<P extends Jpacket> implements Callable<P>, EventJpacketListener {

    private P packetSend, packetRecived;
    private Barrier lock = new Barrier();

    public SendPacket(P packet) throws InterruptedException {
        this.packetSend = packet;
        SerialWriter.bufferOutput.add(packetSend);          //Add Packet
    }

    @Override
    public P call() {
        try {
            lock.block();
            return packetRecived;
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
            return null;
        }
    }

    @Override
    public void eventJpacketOccurred(EventJpacket evt) {
        packetRecived = (P) evt.getSource();
        if (packetSend.getClass().equals(packetRecived.getClass()) || (packetSend.getClass().equals(UnknownPacket.class)) && packetSend.getName().equals(packetRecived.getName())) {
            if (packetSend.isCmdAT()) {                                     //Se ATcommand
                SerialReader.bufferInput.remove(packetRecived);
                try {
                    lock.release();
                } catch (InterruptedException ex) {
                    Exceptions.printStackTrace(ex);
                }
            } else {
                if (packetSend.getAddress().isBroadcast()) {                //Se Broadcast
                    SerialReader.bufferInput.remove(packetRecived);
                    try {
                        lock.release();
                    } catch (InterruptedException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                } else {
                    if (packetSend.getAddress().equals(packetRecived.getAddress())) {   //Se corrisponde all'indirizzo richiesto
                        SerialReader.bufferInput.remove(packetRecived);
                        try {
                            lock.release();
                        } catch (InterruptedException ex) {
                            Exceptions.printStackTrace(ex);
                        }
                    }
                }
            }
        }
    }
}
