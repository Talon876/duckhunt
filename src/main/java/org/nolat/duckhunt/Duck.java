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

    public Rectangle getBodyHitbox() {
        return new Rectangle(x + 30, y + 30, 88, 25);
    }

    public Rectangle getHeadHitbox() {
        return new Rectangle(x + 18, y, 27, 30);
    }
}
