/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.simulator.parts;

/**
 *
 * @author Raffaello
 */
public class PIDControl implements Control<Double, Double> {

    private transient double pid = 0;
    private transient double oldError2 = 0;
    private transient double oldError1 = 0;
    //Parametri
    private double Tcount;
    private double[] kPID;
    private double PID_0, PID_1, PID_2;

    /**
     * 
     * @param kPID kPID[0]=Kp, kPID[1]=Ki, kPID[2]=Kd
     * @param Tcount 
     */
    public PIDControl(long Tcount, double[] kPID) {
        this.Tcount = ((double) Tcount) / 1000;
        this.kPID = kPID;
        //Parametri Controllo Left
        PID_0 = kPID[0] + kPID[2] / Tcount + kPID[1] * Tcount / 2;       //Definizione della costante P0
        PID_1 = -(kPID[0] + 2 * kPID[2] / Tcount - kPID[1] * Tcount / 2);  //Definizione della costante P1
        PID_2 = kPID[2] / Tcount;                      //Definizione della costante P2
    }

    /**
     * Setta i guadagni del controllore
     * @param kPID 
     */
    public void setKPID(double[] kPID) {
        this.kPID = kPID;
        updateGain();
    }

    /**
     * Restituisce i parametri del PID
     * @return 
     */
    public double[] getKPID() {
        return kPID;
    }

    /**
     * Get the value of string at specified index
     *
     * @param index
     * @return the value of string at specified index
     */
    public double getKPID(int index) {
        return this.kPID[index];
    }

    /**
     * Set the value of string at specified index.
     *
     * @param index
     * @param newString new value of string at specified index
     */
    public void setKPID(int index, double newString) {
        this.kPID[index] = newString;
        updateGain();
    }

    private void updateGain() {
        PID_0 = kPID[0] + kPID[2] / Tcount + kPID[1] * Tcount / 2;       //Definizione della costante P0
        PID_1 = -(kPID[0] + 2 * kPID[2] / Tcount - kPID[1] * Tcount / 2);  //Definizione della costante P1
        PID_2 = kPID[2] / Tcount;                      //Definizione della costante P2
    }

    /**
     * Azione di controllo PID
     * @param error
     * @return pid
     */
    @Override
    public Double updateControl(Double error) {
        //double error = vel - velR;                  //Calcolo errore di misura
        pid -= -PID_0 * error + PID_1 * oldError1 + PID_2 * oldError2;
        oldError2 = oldError1;
        oldError1 = -error;
        return pid;
    }
}
