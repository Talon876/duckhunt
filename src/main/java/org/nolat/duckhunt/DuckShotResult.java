package org.nolat.duckhunt;

public class DuckShotResult {
    private final int points;
    private final boolean isHeadshot;

    public DuckShotResult(int points, boolean isHeadshot) {
        this.points = points;
        this.isHeadshot = isHeadshot;
    }

    public int getPoints() {
        return points;
    }

    public boolean isHeadshot() {
        return isHeadshot;
    }
}
