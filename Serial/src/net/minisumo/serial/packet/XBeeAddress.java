/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.serial.packet;

import net.minisumo.services.Address;

/**
 *
 * @author Raffaello
 */
public class XBeeAddress implements Address {

    public static final byte BROADCAST = (byte) 0xFF;
    public int AddressHigh, AddressLow;

    public XBeeAddress() {
        AddressHigh = -1;
        AddressLow = -1;
    }

    public XBeeAddress(int AddressLow) {
        AddressHigh = 0;
        this.AddressLow = AddressLow;
    }

    public XBeeAddress(int AddressHigh, int AddressLow) {
        this.AddressHigh = AddressHigh;
        this.AddressLow = AddressLow;
    }

    @Override
    public boolean isBroadcast() {
        if (this.equals(new XBeeAddress(BROADCAST))) {
            return true;
        } else {
            return false;
        }
    }

    public int getAddressHigh() {
        return AddressHigh;
    }

    public void setAddressHigh(int AddressHigh) {
        this.AddressHigh = AddressHigh;
    }

    public int getAddressLow() {
        return AddressLow;
    }

    public void setAddressLow(int AddressLow) {
        this.AddressLow = AddressLow;
    }

    public boolean is16Address() {
        if (AddressHigh == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof XBeeAddress) {
            if (AddressHigh == ((XBeeAddress) obj).AddressHigh && AddressLow == ((XBeeAddress) obj).AddressLow) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.AddressHigh;
        hash = 89 * hash + this.AddressLow;
        return hash;
    }

    @Override
    public String toString() {
        return Integer.toHexString(AddressHigh).toUpperCase() + ":" + Integer.toHexString(AddressLow).toUpperCase();
    }
}
