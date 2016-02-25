package org.nolat.duckhunt;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Duck {
    public int x;
    public final int y;
    private final int speed;
    public final int score;
    private final BufferedImage duckImg;

    public Duck(int x, int y, int speed, int score, BufferedImage duckImg) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.score = score;
        this.duckImg = duckImg;
    }

    public void update() {
        x += speed;
    }

    public void draw(Graphics2D g2d) {
        g2d.drawImage(duckImg, x, y, null);
    }
}
