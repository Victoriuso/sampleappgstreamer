package media;

import org.freedesktop.gstreamer.Bus;
import org.freedesktop.gstreamer.ElementFactory;
import org.freedesktop.gstreamer.elements.PlayBin;
import org.freedesktop.gstreamer.message.Message;

import java.io.File;

public class AudioPlayer {

    private PlayBin playBin;

    private final Bus.EOS busEos = source -> playBin.seek(0);

    public AudioPlayer() {
        playBin = new PlayBin("AudioPlayer");
        playBin.setVideoSink(ElementFactory.make("fakesink", "videosink"));
    }

    public void play(String path, boolean isRepeat) {
        try {
            playBin.getBus().disconnect(busEos);
            playBin.stop();
            playBin.setInputFile(new File(path));
            playBin.play();
            if (isRepeat) {
                playBin.getBus().connect(busEos);
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
