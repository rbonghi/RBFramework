/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world.robot.editor;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import net.minisumo.util.DataInformation;

/**
 *
 * @author Raffaello
 */
public class MyDataInformation implements Transferable {

    public static DataFlavor DATA_FLAVOR = new DataFlavor(MyComponent.class, DataInformation.class.getSimpleName());
    private final boolean variable;

    public MyDataInformation(boolean variable) {
        this.variable = variable;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{DATA_FLAVOR};
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor == DATA_FLAVOR;
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (flavor == DATA_FLAVOR) {
            return this;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }

    public boolean isVariable() {
        return variable;
    }
}
