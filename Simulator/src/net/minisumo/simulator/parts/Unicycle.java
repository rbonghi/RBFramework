/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minisumo.simulator.parts;

/**
 *
 * @author Raffaello
 */
public class Unicycle extends Component<double[], double[]> {

    private transient double[] pos;                    //X, Y, TH
    private double Tcount;
    private static double radiusL, radiusR, wheelbase;

    public Unicycle(long Tcount, double radiusL, double radiusR, double wheelbase) {
        this.Tcount = ((double) Tcount) / 1000;
        pos = new double[3];
        Unicycle.radiusL = radiusL;
        Unicycle.radiusR = radiusR;
        Unicycle.wheelbase = wheelbase;
    }

    public double[] getPos() {
        return pos;
    }

    public void setPos(double[] pos) {
        this.pos = pos;
    }

    public static double getRadiusL() {
        return radiusL;
    }

    public static void setRadiusL(double radiusL) {
        Unicycle.radiusL = radiusL;
    }

    public static double getRadiusR() {
        return radiusR;
    }

    public static void setRadiusR(double radiusR) {
        Unicycle.radiusR = radiusR;
    }

    public static double getWheelbase() {
        return wheelbase;
    }

    public static void setWheelbase(double wheelbase) {
        Unicycle.wheelbase = wheelbase;
    }

    /**
     * Converte da velocità ruota destra/sinistra in velocità Lineare/Angolare
     * @param wlwr
     * @return vw
     */
    public static double[] wlwrTOvw(double[] wlwr) {
        double[] vw = new double[2];
        vw[0] = (radiusR * wlwr[1] + radiusL * wlwr[0]) / 2;
        vw[1] = radiusL / wheelbase * wlwr[0] - radiusR / wheelbase * wlwr[1];
        return vw;
    }

    /**
     * Converte da velocità Lineare/Angolare in velocità ruota destra/sinistra
     * @param vw
     * @return wlwr
     */
    public static double[] vwTOwlwr(double[] vw) {
        double[] wlwr = new double[2];
        wlwr[0] = vw[0] / radiusL + wheelbase / (2 * radiusL) * vw[1];
        wlwr[1] = vw[0] / radiusR - wheelbase / (2 * radiusR) * vw[1];
        return wlwr;
    }

    /**
     * 
     * @param vel primo parametro velocità lineare, secondo velocità angolare
     * @return 
     */
    @Override
    public double[] updateModel(double[] vel) {
        if (Math.abs(vel[1]) > 0.001) {
            double thold = pos[2];
            pos[2] += vel[1] * Tcount;
            pos[0] += (vel[0] / vel[1]) * (Math.sin(pos[2]) - Math.sin(thold));
            pos[1] += -(vel[0] / vel[1]) * (Math.cos(pos[2]) - Math.cos(thold));
        } else {
            pos[0] += vel[0] * Math.cos(pos[2]) * Tcount;
            pos[1] += vel[0] * Math.sin(pos[2]) * Tcount;
            pos[2] += vel[1] * Tcount;
        }
        if (pos[2] > 2 * Math.PI) {
            pos[2] -= 2 * Math.PI;
        } else if (pos[2] < -2 * Math.PI) {
            pos[2] += 2 * Math.PI;
        }
        return pos;
    }

    /**
     * Resetta le coordinate del robot
     */
    @Override
    public void resetModel() {
        pos = new double[3];
    }
}
