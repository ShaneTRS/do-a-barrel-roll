package nl.enjarai.doabarrelroll.config;

public class Sensitivity {
    public double pitch = 1;
    public double yaw = 1;
    public double roll = 0.25;

    public Sensitivity() {
    }

    public Sensitivity(double pitch, double yaw, double roll) {
        this.pitch = pitch;
        this.yaw = yaw;
        this.roll = roll;
    }
}
