/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.world.robot;

import au.com.bytecode.opencsv.CSVWriter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Map.Entry;
import net.minisumo.util.DataInformation;
import net.minisumo.util.Finder;
import net.minisumo.world.robot.logicalview.FinderComparator;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;

/**
 *
 * @author Raffaello
 */
public class Recorder implements PropertyChangeListener {

    private String EXTENSION = "csv";
    private Robot robot;
    private CSVWriter writer;
    private FileObject file;
    private long startTime;
    private ArrayList<String> firstLine = new ArrayList<String>();
    private ArrayList<DataInformation> parameters = new ArrayList<DataInformation>();
    private DateFormat dateFile = new SimpleDateFormat("ddMMyyyy HH-mm-ss");
    private DateFormat time = new SimpleDateFormat("ss.SSS");

    public Recorder(Robot robot, FileObject file) {
        this.robot = robot;
        this.file = file;
        robot.addPropertyChangeListener(this);
        if (robot.isLog()) {
            try {
                importData();
                createWriter();
                firstLine();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    private void importData() {
        ArrayList<Entry<Finder, DataInformation>> arrayList = new ArrayList<Entry<Finder, DataInformation>>(robot.getParameters().entrySet());
        Collections.sort(arrayList, new FinderComparator());
        firstLine.add("Time");
        for (Entry<Finder, DataInformation> entry : arrayList) {
            if (entry.getValue().isVariable()) {
                parameters.add(entry.getValue());
                firstLine.add(entry.getKey().toString() + " " + entry.getValue().getDimension());
                entry.getValue().addPropertyChangeListener(this);
            }
        }
        startTime = System.currentTimeMillis();
    }

    private void createWriter() throws IOException {
        String nameFile = "LOG " + dateFile.format(new Date()) + "." + EXTENSION;
        FileObject createData = file.createData(nameFile);
        File toFile = FileUtil.toFile(createData);
        writer = new CSVWriter(new FileWriter(toFile), ',', CSVWriter.NO_QUOTE_CHARACTER);
    }

    public void closeWriter() throws IOException {
        writer.close();
    }

    private void firstLine() {
        writer.writeNext(firstLine.toArray(new String[firstLine.size()]));
    }

    private void saveLine() {
        ArrayList<String> thisLine = new ArrayList<String>();
        long thisTime = System.currentTimeMillis() - startTime;
        thisLine.add("" + time.format(new Date(thisTime)));
        for (DataInformation data : parameters) {
            thisLine.add("" + data.getLastData());
        }
        writer.writeNext(thisLine.toArray(new String[thisLine.size()]));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() == robot) {
            if (evt.getPropertyName().equals(Robot.PROP_LOG)) {
                if ((Boolean) evt.getNewValue()) {
                    try {
                        importData();
                        createWriter();
                        firstLine();
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                } else {
                    try {
                        closeWriter();
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            }
        } else if (evt.getPropertyName().equals(DataInformation.PROP_DATA)) {
            saveLine();
        }
    }
}
