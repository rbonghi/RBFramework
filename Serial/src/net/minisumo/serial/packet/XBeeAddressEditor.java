/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.serial.packet;

import java.beans.PropertyEditorSupport;

/**
 *
 * @author Raffaello
 */
public class XBeeAddressEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        String[] stringAddress = text.split(":");
        if(stringAddress.length != 2) throw new IllegalArgumentException("Not Address");
        long AddressHigh = Long.parseLong(stringAddress[0], 16);
        long AddressLow = Long.parseLong(stringAddress[1], 16);
        XBeeAddress address = new XBeeAddress((int)AddressHigh, (int)AddressLow);
        if(address.isBroadcast()) throw new IllegalArgumentException("Is Broadcast");
        setValue(address);
    }

    @Override
    public String getAsText() {
        return getValue().toString();
    }
    
}
