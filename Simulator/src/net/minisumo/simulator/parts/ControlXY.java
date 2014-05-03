/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.simulator.parts;

/**
 *
 * @author Raffaello
 */
public class ControlXY implements NonLinearControl<double[], double[]> {

    private double[] kControl;
    private transient double measureTh;

    public ControlXY(double[] kControl) {
        this.kControl = kControl;
    }

    @Override
    public double[] updateControl(double[] error) {
        double controlOutputV = Math.abs(kControl[0] * (error[0] * Math.cos(measureTh) + error[1] * Math.sin(measureTh)));
        double errorAngle = (Math.atan2(error[1], error[0]) - measureTh);
        if (errorAngle > Math.PI) {
            errorAngle -= 2 * Math.PI;
        } else if (errorAngle < -Math.PI) {
            errorAngle += 2 * Math.PI;
        }
        double controlOutputW = kControl[1] * errorAngle;
        return new double[]{controlOutputV, controlOutputW};
    }

    /**
     * Get the value of kControl
     *
     * @return the value of kControl
     */
    public double[] getKControl() {
        return kControl;
    }

    /**
     * Set the value of kControl
     *
     * @param kControl new value of kControl
     */
    public void setKControl(double[] kControl) {
        this.kControl = kControl;
    }

    /**
     * Get the value of kControl at specified index
     *
     * @param index
     * @return the value of kControl at specified index
     */
    public double getKControl(int index) {
        return this.kControl[index];
    }

    /**
     * Set the value of kControl at specified index.
     *
     * @param index
     * @param newKControl new value of kControl at specified index
     */
    public void setKControl(int index, double newKControl) {
        this.kControl[index] = newKControl;
    }


    public double getMeasureTh() {
        return measureTh;
    }

    public void setMeasureTh(double measureTh) {
        if (measureTh > Math.PI) {
            this.measureTh -= 2 * Math.PI;
        } else if (measureTh < -Math.PI) {
            this.measureTh += 2 * Math.PI;
        }
        this.measureTh = measureTh;
    }

    /**
     * 
     * @param rifer
     * @param state
     * @return azione di controllo
     */
    @Override
    public double[] updateControl(double[] rifer, double[] state) {
        setMeasureTh(state[2]);                             // state[2] = measureTh
        double[] error = new double[]{rifer[0] - state[0], rifer[1] - state[1]};
        return updateControl(error);
    }
}
