package net.team.astera.impl.util.models;

public class Timer {
    private long lastMs;
    private long time = -1L;

    public boolean passedS(double s) {
        return this.getMs(System.nanoTime() - this.time) >= (long) (s * 1000.0);
    }

    public boolean passedM(double m) {
        return this.getMs(System.nanoTime() - this.time) >= (long) (m * 1000.0 * 60.0);
    }

    public boolean passedDms(double dms) {
        return this.getMs(System.nanoTime() - this.time) >= (long) (dms * 10.0);
    }

    public boolean passedDs(double ds) {
        return this.getMs(System.nanoTime() - this.time) >= (long) (ds * 100.0);
    }

    public Timer() {
        this.lastMs = System.currentTimeMillis();
    }

    public boolean passedMs(int ms) {
        return System.currentTimeMillis() - lastMs >= ms;
    }

    public void reset() {
        lastMs = System.currentTimeMillis();
    }

    public boolean passedNS(long ns) {
        return System.nanoTime() - this.time >= ns;
    }

    public void setMs(long ms) {
        this.time = System.nanoTime() - ms * 1000000L;
    }

    public long getPassedTimeMs() {
        return this.getMs(System.nanoTime() - this.time);
    }

    public long getMs(long time) {
        return time / 1000000L;
    }

}