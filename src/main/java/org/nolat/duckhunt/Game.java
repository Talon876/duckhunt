package org.nolat.duckhunt;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Game {
    private Font font;
    private DuckSystem duckSystem;
    private int score;
    private int shotCount;
    private long lastTimeShot;
    private long timeBetweenShots;
    private BufferedImage backgroundImg;
    private BufferedImage grassImg;
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

        duckSystem = new DuckSystem(Framework.frameWidth, Framework.frameHeight, 333);

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
        duckSystem.reset();
        score = 0;
        shotCount = 0;

        lastTimeShot = 0;
        audio.playSound("play");

    }

    /**
     * update game logic.
     *
     * @param gameTime      gameTime of the game.
     * @param mousePosition current mouse position.
     */
    public void updateGame(long gameTime, Point mousePosition) {
        duckSystem.update();

        if (Canvas.mouseButtonState(MouseEvent.BUTTON1)) {

            if (System.nanoTime() - lastTimeShot >= timeBetweenShots) {
                audio.playSound("shotgunblast");
                shotCount++;
                DuckShotResult result = duckSystem.shootAt(mousePosition);
                if (result != null) {
                    killDuck(result.getPoints(), result.isHeadshot());
                }

                lastTimeShot = System.nanoTime();
            }
        }

        if (duckSystem.getAmountOfRunawayDucks() >= 15) {
            Framework.gameState = Framework.GameState.GAMEOVER;
        }
    }

    private void killDuck(int duckScore, boolean wasHeadshot) {
        if (wasHeadshot) {
            audio.playSound("headshot");
            score += duckScore * 1.3;
        } else {
            score += duckScore;
        }
        switch (duckSystem.getAmountOfKilledDucks()) {
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
        duckSystem.draw(g2d);

        g2d.drawImage(grassImg, 0, Framework.frameHeight - grassImg.getHeight(), Framework.frameWidth, grassImg.getHeight(), null);
        g2d.drawImage(sightImg, mousePosition.x - sightImgMiddleWidth, mousePosition.y - sightImgMiddleHeight, null);
        g2d.setFont(font);
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawString("RUNAWAY: " + duckSystem.getAmountOfRunawayDucks(), 10, 21);
        g2d.drawString("KILLS: " + duckSystem.getAmountOfKilledDucks(), 160, 21);
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
