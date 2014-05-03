/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.serial.graphic;

import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.concurrent.ExecutionException;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import net.minisumo.serial.ConnectionException;
import net.minisumo.serial.EventJpacket;
import net.minisumo.serial.EventJpacketListener;
import net.minisumo.serial.XBeeAPI;
import net.minisumo.serial.packet.Jpacket;
import net.minisumo.serial.packet.UnknownPacket;
import net.minisumo.serial.packet.XBeeAddress;
import net.minisumo.util.GBC;
import org.openide.util.Exceptions;

/**
 *
 * @author Raffaello
 */
public class SendPacket extends JPanel implements ActionListener {

    private ButtonGroup group;
    private JRadioButton atPacket, standardPacket;
    private JButton send;
    private JTextArea area;
    private JLabel addressLabel;
    private JTextField name, address;

    public SendPacket() {
        super(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Send Packet"));
        //Property Packet
        JPanel property = new JPanel(new GridBagLayout());
        property.setBorder(BorderFactory.createTitledBorder("Property"));
        name = new JTextField();
        name.addActionListener(this);
        JLabel nameLabel = new JLabel("Name: ");
        property.add(nameLabel, new GBC(0, 0).setFill(GBC.HORIZONTAL).setAnchor(GBC.FIRST_LINE_START));
        property.add(name, new GBC(1, 0).setFill(GBC.HORIZONTAL));
        JLabel dataLabel = new JLabel("Data: ");
        area = new JTextArea();
        property.add(dataLabel, new GBC(0, 1).setFill(GBC.HORIZONTAL));
        property.add(area, new GBC(1, 1).setWeight(100, 100).setInsets(3).setFill(GBC.BOTH));
        addressLabel = new JLabel("Address: ");
        addressLabel.setVisible(false);
        address = new JTextField();
        address.setVisible(false);
        property.add(addressLabel, new GBC(0, 2).setFill(GBC.HORIZONTAL));
        property.add(address, new GBC(1, 2).setFill(GBC.HORIZONTAL));
        add(property, new GBC(0, 0).setSpan(1, 2).setWeight(100, 100).setFill(GBC.BOTH));
        //Select Packet
        JPanel radioPanel = new JPanel(new GridLayout(0, 1));
        radioPanel.setBorder(BorderFactory.createTitledBorder("Packet"));
        atPacket = new JRadioButton("AT");
        atPacket.addActionListener(this);
        radioPanel.add(atPacket);
        atPacket.setSelected(true);
        standardPacket = new JRadioButton("Standard");
        standardPacket.addActionListener(this);
        radioPanel.add(standardPacket);
        group = new ButtonGroup();
        group.add(atPacket);
        group.add(standardPacket);
        add(radioPanel, new GBC(1, 0).setFill(GBC.HORIZONTAL));
        //Button Send
        send = new JButton("Send");
        send.addActionListener(this);
        add(send, new GBC(1, 1).setFill(GBC.HORIZONTAL));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == send || e.getSource() == name) {
            UnknownPacket packet;
            String text = name.getText();
            if (group.isSelected(atPacket.getModel())) {
                packet = new UnknownPacket(text, new byte[0], System.currentTimeMillis());
            } else {
                String[] stringAddress = address.getText().split(":");
                if (stringAddress.length != 2) {
                    throw new IllegalArgumentException("Not Address");
                }
                long AddressHigh = Long.parseLong(stringAddress[0], 16);
                long AddressLow = Long.parseLong(stringAddress[1], 16);
                XBeeAddress add = new XBeeAddress((int) AddressHigh, (int) AddressLow);
                packet = new UnknownPacket(text, new byte[0], add, false, System.currentTimeMillis());
            }
            try {
                XBeeAPI.sendPacket((Jpacket) packet, 2000);
            } catch (InterruptedException ex) {
                Exceptions.printStackTrace(ex);
            } catch (ExecutionException ex) {
                Exceptions.printStackTrace(ex);
            } catch (ConnectionException ex) {
                Exceptions.printStackTrace(ex);
            }
        } else if (e.getSource() == atPacket) {
            address.setVisible(false);
            addressLabel.setVisible(false);
        } else if (e.getSource() == standardPacket) {
            address.setVisible(true);
            addressLabel.setVisible(true);
        }
    }
}
