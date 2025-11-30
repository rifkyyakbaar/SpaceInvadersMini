import java.io.File;
import javax.sound.sampled.*;

public class SoundPlayer {
    
    // Static supaya hanya ada 1 musik di seluruh game
    private static Clip bgmClip; 
    private static String currentTrack; // <--- INI OTAK BARUNYA (Mengingat nama lagu)

    public void playLoop(String filePath) {
        try {
            // 1. CEK CERDAS: Apakah lagu yang diminta SAMA dengan yang sedang main?
            if (currentTrack != null && currentTrack.equals(filePath) && bgmClip != null && bgmClip.isRunning()) {
                System.out.println("Lagu sama (" + filePath + "), lanjutkan...");
                return; // JANGAN lakukan apa-apa, biarkan musik lanjut!
            }

            // 2. Kalau lagunya BEDA (atau belum ada), matikan yang lama dulu
            stop(); 

            // 3. Mulai lagu baru
            File musicPath = new File(filePath);
            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                
                bgmClip = AudioSystem.getClip();
                bgmClip.open(audioInput);
                
                // Volume Background
                try {
                    FloatControl gainControl = (FloatControl) bgmClip.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(-10.0f); 
                } catch (Exception ex) {}

                bgmClip.start();
                bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
                
                // 4. Simpan nama lagu ini ke ingatan
                currentTrack = filePath; 
                
                System.out.println("Memutar Musik Baru: " + filePath);
            } else {
                System.out.println("File tidak ketemu: " + filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (bgmClip != null) {
            if (bgmClip.isRunning()) {
                bgmClip.stop();
            }
            bgmClip.flush();
            bgmClip.close();
            bgmClip = null;
            currentTrack = null; // Lupakan lagu lama
        }
    }

    // === SFX (EFEK SUARA) ===
    public void playSFX(String filePath) {
        try {
            File soundPath = new File(filePath);
            if (soundPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundPath);
                Clip sfxClip = AudioSystem.getClip(); 
                sfxClip.open(audioInput);
                try {
                    FloatControl gainControl = (FloatControl) sfxClip.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(-2.0f); 
                } catch (Exception ex) {}
                sfxClip.start();
            }
        } catch (Exception e) {}
    }
}