package org.nolat.duckhunt;

import java.applet.Applet;
import java.applet.AudioClip;
import java.util.HashMap;

public class AudioManager extends Thread {
    static HashMap<String, AudioClip> sounds;

    private static boolean running = false;
    private static boolean soundEnabled = true;

    static Thread audioThread;
    private String track;

    public AudioManager() {
        sounds = new HashMap<String, AudioClip>();
    }

    /**
     * Adds a sound to the AudioManager
     *
     * @param trackName      This is what you want the track to be named (it will be how you refer to it in the <code>playSound(String track)</code> method.
     * @param pathToResource This is the path to the resource (it probably starts with /sounds/...) Note: The leading / is important.
     */
    public void addSound(String trackName, String pathToResource) {
        try {
            sounds.put(trackName, Applet.newAudioClip(this.getClass().getResource(pathToResource)));
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public void startThread() {
        if (soundEnabled) {
            if (running == false) {
                audioThread = new Thread(this);
                audioThread.start();
            }
            running = true;
        }
    }

    public void run() {
        while (running) {
            sounds.get(getTrack()).play();
            clearTrack();
            running = false;
        }
    }

    private void clearTrack() {
        this.track = "";
    }

    public void playSound(String track) {
        playSound(track, false);
    }

    public void playSound(String track, boolean loop) {
        this.track = track;
        if (loop) {
            sounds.get(getTrack()).loop();
        } else {
            startThread();
        }
    }

    public void stopSound(String track) {
        sounds.get(track).stop();
    }

    public String getTrack() {
        return this.track;
    }
}
