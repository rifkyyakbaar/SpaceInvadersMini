package main;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundPlayer {

    public static void play(String path) {
        // fire-and-forget sound playing in new thread to avoid blocking
        new Thread(() -> {
            try {
                File file = new File(path);
                if (!file.exists()) return;
                AudioInputStream audio = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audio);
                clip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                // ignore or print minimal info
                System.err.println("Sound err: " + e.getMessage());
            }
        }).start();
    }

    public static void loop(String path) {
        // optional background music looping
        new Thread(() -> {
            try {
                File file = new File(path);
                if (!file.exists()) return;
                AudioInputStream audio = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audio);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } catch (Exception e) {
                System.err.println("BGM err: " + e.getMessage());
            }
        }).start();
    }
}
