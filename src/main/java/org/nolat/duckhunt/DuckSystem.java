package org.nolat.duckhunt;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DuckSystem {

    private Random random = new Random();
    private final List<Duck> ducks;
    private final int timeBetweenDucksMs;
    private final int[][] duckLines;

    private long lastDuckSpawnTime;
    private int nextDuckLine;
    private int runawayDucks;
    private int killedDucks;

    private static BufferedImage duckImg;

    public DuckSystem(int worldWidth, int worldHeight, int timeBetweenDucksMs) {
        this.duckLines = new int[][]{
                {worldWidth, (int) (worldHeight * 0.6f), -2, 20},
                {worldWidth, (int) (worldHeight * 0.65f), -2, 30},
                {worldWidth, (int) (worldHeight * 0.7f), -4, 40},
                {worldWidth, (int) (worldHeight * 0.78f), -5, 50},
        };
        this.ducks = new ArrayList<>();
        this.timeBetweenDucksMs = timeBetweenDucksMs;
        try {
            duckImg = ImageIO.read(this.getClass().getResource("/images/duck.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        reset();
    }

    public void reset() {
        this.ducks.clear();
        this.nextDuckLine = 0;
        this.runawayDucks = 0;
        this.killedDucks = 0;
    }

    public void spawnDuck() {
        Duck duck = new Duck(duckLines[nextDuckLine][0] + random.nextInt(200),
                duckLines[nextDuckLine][1],
                duckLines[nextDuckLine][2],
                duckLines[nextDuckLine][3],
                duckImg);
        ducks.add(duck);
        nextDuckLine++;
        if (nextDuckLine >= duckLines.length) {
            nextDuckLine = 0;
        }
        lastDuckSpawnTime = System.currentTimeMillis();
    }

    public int shootAt(Point point) {
        for (Duck duck : ducks) {
            if (duck.getHeadHitbox().contains(point)) {
                killDuck(duck);
                return (int) (duck.score * 1.3);
            } else if (duck.getBodyHitbox().contains(point)) {
                killDuck(duck);
                return duck.score;
            }
        }
        return 0;
    }

    private void killDuck(Duck toKill) {
        this.killedDucks++;
        ducks.remove(toKill);
    }

    public void update() {
        if (System.currentTimeMillis() - lastDuckSpawnTime >= timeBetweenDucksMs) {
            spawnDuck();
        }

        for (int i = 0; i < ducks.size(); i++) {
            ducks.get(i).update();
            if (ducks.get(i).x < 0 - duckImg.getWidth()) {
                ducks.remove(ducks.get(i));
                runawayDucks++;
            }
        }
    }

    public void draw(Graphics2D g2d) {
        this.ducks.forEach(duck -> duck.draw(g2d));
    }

    public int getAmountOfRunawayDucks() {
        return runawayDucks;
    }

    public int getAmountOfKilledDucks() {
        return killedDucks;
    }
}
