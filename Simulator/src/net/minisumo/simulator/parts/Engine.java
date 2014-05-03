/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.simulator.parts;

/**
 *
 * @author Raffaello
 */
public class Engine extends Component<Double, Double> {

    //Stato Motori e PID Left
    private transient double oldVel = 0;
    private transient double oldTens = 0;
    private transient double vel = 0;
    private double A, B;
    private double maxTens;
    private boolean engine = true;
    private final double tcount;

    public Engine(double tcount, double tau, double maxTens) {
        this.maxTens = maxTens;
        this.tcount = ((double) tcount) / 1000;
        //Parametri Motore Left
        setTau(tau);
    }

    public void setTau(double tau) {
        A = (tcount - 2 * tau) / (tcount + 2 * tau);
        B = (tcount) / (tcount + 2 * tau);
    }

    public void setMaxTens(double maxTens) {
        this.maxTens = maxTens;
    }

    @Override
    public Double updateModel(Double tens) {
        tens = Math.max(tens, -maxTens);                //Saturazioni del motore
        tens = Math.min(tens, maxTens);
        if (!engine) {
            tens = 0.0;
        }
        vel = -A * oldVel + B * tens + B * oldTens;
        oldVel = vel;
        oldTens = tens;
        return vel;
    }

    /**
     * Resetta lo stato del robot
     */
    @Override
    public void resetModel() {
        vel = 0;
        oldVel = 0;
        oldTens = 0;
    }

    public boolean isEngine() {
        return engine;
    }

    public void setEngine(boolean engine) {
        this.engine = engine;
    }
}
