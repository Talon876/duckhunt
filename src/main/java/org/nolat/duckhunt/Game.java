package org.nolat.duckhunt;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class Game {
    private Random random;
    private Font font;
    private ArrayList<Duck> ducks;
    private int runawayDucks;
    private int killedDucks;
    private int score;
    private int shotCount;
    private long lastTimeShot;
    private long timeBetweenShots;
    private BufferedImage backgroundImg;
    private BufferedImage grassImg;
    private BufferedImage duckImg;
    private BufferedImage sightImg;
    private int sightImgMiddleWidth;
    private int sightImgMiddleHeight;
    private AudioManager audio;

    public Game() {
        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;

        Thread threadForInitGame = new Thread() {
            @Override
            public void run() {
                // Sets variables and objects for the game.
                initialize();
                // Load game files (images, sounds, ...)
                loadContent();

                Framework.gameState = Framework.GameState.PLAYING;
            }
        };
        threadForInitGame.start();
    }

    /**
     * Set variables and objects for the game.
     */
    private void initialize() {
        random = new Random();
        font = new Font("monospaced", Font.BOLD, 18);
        audio = new AudioManager();
        audio.addSound("dominating", "/sounds/dominating.wav");
        audio.addSound("doublekill", "/sounds/doublekill.wav");
        audio.addSound("firstblood", "/sounds/firstblood.wav");
        audio.addSound("godlike", "/sounds/godlike.wav");
        audio.addSound("headshot", "/sounds/headshot.wav");
        audio.addSound("holyshit", "/sounds/holyshit.wav");
        audio.addSound("killingspree", "/sounds/killingspree.wav");
        audio.addSound("megakill", "/sounds/megakill.wav");
        audio.addSound("monsterkill", "/sounds/monsterkill.wav");
        audio.addSound("multikill", "/sounds/multikill.wav");
        audio.addSound("play", "/sounds/play.wav");
        audio.addSound("rampage", "/sounds/rampage.wav");
        audio.addSound("ultrakill", "/sounds/ultrakill.wav");
        audio.addSound("unstoppable", "/sounds/unstoppable.wav");
        audio.addSound("wickedsick", "/sounds/wickedsick.wav");
        audio.addSound("dominating", "/sounds/dominating.wav");
        audio.addSound("shotgunblast", "/sounds/shotgunblast.wav");
        audio.addSound("background", "/sounds/background.wav");

        ducks = new ArrayList<Duck>();

        runawayDucks = 0;
        killedDucks = 0;
        score = 0;
        shotCount = 0;

        lastTimeShot = 0;
        timeBetweenShots = Framework.secInNanosec / 3; // 1 shot every 1/3 second
        audio.playSound("play");
        audio.playSound("background", true);
    }

    /**
     * Load game files - images, sounds, ...
     */
    private void loadContent() {
        try {
            backgroundImg = ImageIO.read(this.getClass().getResource("/images/background.jpg"));
            grassImg = ImageIO.read(this.getClass().getResource("/images/grass.png"));
            duckImg = ImageIO.read(this.getClass().getResource("/images/duck.png"));
            sightImg = ImageIO.read(this.getClass().getResource("/images/sight.png"));
            sightImgMiddleWidth = sightImg.getWidth() / 2;
            sightImgMiddleHeight = sightImg.getHeight() / 2;
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    /**
     * Restart game - reset some variables.
     */
    public void restartGame() {
        ducks.clear();
        Duck.lastDuckTime = 0;
        runawayDucks = 0;
        killedDucks = 0;
        score = 0;
        shotCount = 0;

        lastTimeShot = 0;
        audio.playSound("play");

    }

    /**
     * Update game logic.
     *
     * @param gameTime      gameTime of the game.
     * @param mousePosition current mouse position.
     */
    public void updateGame(long gameTime, Point mousePosition) {
        if (System.nanoTime() - Duck.lastDuckTime >= Duck.timeBetweenDucks) {
            Duck newDuck = new Duck(Duck.duckLines[Duck.nextDuckLines][0] + random.nextInt(200), Duck.duckLines[Duck.nextDuckLines][1], Duck.duckLines[Duck.nextDuckLines][2],
                    Duck.duckLines[Duck.nextDuckLines][3], duckImg);
            ducks.add(newDuck);
            Duck.nextDuckLines++;
            if (Duck.nextDuckLines >= Duck.duckLines.length)
                Duck.nextDuckLines = 0;

            Duck.lastDuckTime = System.nanoTime();
        }

        for (int i = 0; i < ducks.size(); i++) {
            ducks.get(i).Update();
            if (ducks.get(i).x < 0 - duckImg.getWidth()) {
                ducks.remove(ducks.get(i));
                runawayDucks++;
            }
        }

        if (Canvas.mouseButtonState(MouseEvent.BUTTON1)) {
            audio.playSound("shotgunblast");

            if (System.nanoTime() - lastTimeShot >= timeBetweenShots) {
                shotCount++;
                for (int i = 0; i < ducks.size(); i++) {
                    if (new Rectangle(ducks.get(i).x + 18, ducks.get(i).y, 27, 30).contains(mousePosition)) // head shot
                    {
                        killDuck(ducks.get(i), true);
                        break;
                    } else if (new Rectangle(ducks.get(i).x + 30, ducks.get(i).y + 30, 88, 25).contains(mousePosition)) // body shot
                    {
                        killDuck(ducks.get(i), false);
                        break;
                    }
                }
                lastTimeShot = System.nanoTime();
            }
        }

        if (runawayDucks >= 15) {
            Framework.gameState = Framework.gameState.GAMEOVER;
        }
    }

    private void killDuck(Duck toBeKilled, boolean wasHeadshot) {
        killedDucks++;

        ducks.remove(toBeKilled);
        if (wasHeadshot) {
            audio.playSound("headshot");
            score += (int) toBeKilled.score * 1.3;
        } else {
            score += toBeKilled.score;
        }
        switch (killedDucks) {
            case 1:
                audio.playSound("firstblood");
                break;
            case 2:
                audio.playSound("doublekill");
                break;
            case 4:
                audio.playSound("multikill");
                break;
            case 6:
                audio.playSound("megakill");
                break;
            case 7:
                audio.playSound("ultrakill");
                break;
            case 9:
                audio.playSound("dominating");
                break;
            case 13:
                audio.playSound("monsterkill");
                break;
            case 17:
                audio.playSound("rampage");
                break;
            case 20:
                audio.playSound("wickedsick");
                break;
            case 23:
                audio.playSound("unstoppable");
                break;
            case 25:
                audio.playSound("ludicrouskill");
                break;
            case 27:
                audio.playSound("holyshit");
                break;
            case 30:
                audio.playSound("godlike");
                break;

        }

    }

    /**
     * draw the game to the screen.
     *
     * @param g2d           Graphics2D
     * @param mousePosition current mouse position.
     */
    public void draw(Graphics2D g2d, Point mousePosition) {
        g2d.drawImage(backgroundImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
        for (Duck duck : ducks) {
            duck.Draw(g2d);
            // g2d.drawRect(duck.x + 18, duck.y, 27, 30);
            // g2d.drawRect(duck.x + 30, duck.y + 30, 88, 25);
        }

        g2d.drawImage(grassImg, 0, Framework.frameHeight - grassImg.getHeight(), Framework.frameWidth, grassImg.getHeight(), null);
        g2d.drawImage(sightImg, mousePosition.x - sightImgMiddleWidth, mousePosition.y - sightImgMiddleHeight, null);
        g2d.setFont(font);
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawString("RUNAWAY: " + runawayDucks, 10, 21);
        g2d.drawString("KILLS: " + killedDucks, 160, 21);
        g2d.drawString("SHOTS: " + shotCount, 299, 21);
        g2d.drawString("SCORE: " + score, 440, 21);
    }

    public void drawGameOver(Graphics2D g2d, Point mousePosition) {
        draw(g2d, mousePosition);

        // The first text is used for shade.
        g2d.setColor(Color.black);
        g2d.drawString("Game Over", Framework.frameWidth / 2 - 39, (int) (Framework.frameHeight * 0.65) + 1);
        g2d.drawString("Press space or enter to restart.", Framework.frameWidth / 2 - 149, (int) (Framework.frameHeight * 0.70) + 1);
        g2d.setColor(Color.red);
        g2d.drawString("Game Over", Framework.frameWidth / 2 - 40, (int) (Framework.frameHeight * 0.65));
        g2d.drawString("Press space or enter to restart.", Framework.frameWidth / 2 - 150, (int) (Framework.frameHeight * 0.70));
    }
}
