package media;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import org.freedesktop.gstreamer.Bin;
import org.freedesktop.gstreamer.Bus;
import org.freedesktop.gstreamer.Caps;
import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.elements.AppSink;
import org.freedesktop.gstreamer.elements.PlayBin;
import org.freedesktop.gstreamer.elements.PlayFlags;

import java.io.File;
import java.nio.ByteOrder;
import java.util.HashSet;

/**
 * This is for playing video
 */
public class GstreamerMediaPlayer {

    private final PlayBin playBin = new PlayBin("mediaplayer");

    /**
     * Map image in here;
     */
    private final ImageView imageView = new ImageView();

    /**
     * For looping if isrepeat is enable
     */
    private final Bus.EOS busEos = source -> playBin.seek(0);

    public GstreamerMediaPlayer(String videoPath, boolean isRepeat) {
        imageView.setFitHeight(300);
        imageView.setFitWidth(300);
        Bin bin = Gst.parseBinFromDescription("queue ! videoconvert ! appsink name=appsinkmediaplayer", true);
        AppSinkListener appSinkListener = new AppSinkListener();
        AppSink videoSink = (AppSink) bin.getElementByName("appsinkmediaplayer");
        videoSink.set("emit-signals", true);
        videoSink.connect(appSinkListener);
        videoSink.set("drop", true);

        StringBuilder caps = new StringBuilder("video/x-raw, ");

        //Resize current media player size into 480p
        caps.append("width=640, ");
        caps.append("height=360, ");

        // JNA creates ByteBuffer using native byte order, set masks according to that.
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            caps.append("format=BGRx");
        } else {
            caps.append("format=xRGB");
        }
        videoSink.setCaps(new Caps(caps.toString()));

        ImageContainer imageContainer = appSinkListener.getImageContainer();
        imageContainer.addListener((observable, oldValue, newValue) -> Platform.runLater(() -> imageView.setImage(newValue)));

        File file = new File(videoPath);
        playBin.setInputFile(file);
        playBin.setVideoSink(bin);

        if (isRepeat) {
            playBin.getBus().connect(busEos);
        }
        playBin.play();
    }

    public ImageView videoResult() {
        return imageView;
    }
}
