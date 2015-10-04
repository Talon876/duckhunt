package org.nolat.duckhunt;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Duck {
    public static long timeBetweenDucks = Framework.secInNanosec / 3;
    public static long lastDuckTime;

    public static int[][] duckLines = {
            {Framework.frameWidth, (int) (Framework.frameHeight * .60), -2, 20},
            {Framework.frameWidth, (int) (Framework.frameHeight * .65), -2, 30},
            {Framework.frameWidth, (int) (Framework.frameHeight * .70), -4, 40},
            {Framework.frameWidth, (int) (Framework.frameHeight * .78), -5, 50}
    };
    public static int nextDuckLines = 0;

    public int x;
    public int y;
    private int speed;
    public int score;
    private BufferedImage duckImg;

    public Duck(int x, int y, int speed, int score, BufferedImage duckImg) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.score = score;
        this.duckImg = duckImg;
    }

    public void Update() {
        x += speed;
    }

    public void Draw(Graphics2D g2d) {
        g2d.drawImage(duckImg, x, y, null);
    }
}
