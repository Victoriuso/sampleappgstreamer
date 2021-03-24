package media;

import javafx.scene.image.ImageView;
import org.freedesktop.gstreamer.*;
import org.freedesktop.gstreamer.elements.AppSink;

import java.nio.ByteOrder;

public class WebcamStream {

    final Pipeline pipe = new Pipeline();

    Element videoConvert;

    Element videoQueue;

    Element captureQueue;

    ImageView webcamImageView = new ImageView();

    public WebcamStream() {
        //Setting webcam USB Logitech
        webcamImageView.setFitWidth(200);
        webcamImageView.setFitHeight(200);
        AppSinkListener GstListener = new AppSinkListener();
        videoConvert = ElementFactory.make("videoconvert", "videoconvert");
        videoQueue = ElementFactory.make("queue", "videoqueue");
        captureQueue = ElementFactory.make("queue", "captureQueue");

        //Creating video converter, from format, width, and height
        StringBuilder caps = new StringBuilder("video/x-raw, ");
        Element source = ElementFactory.make("autovideosrc", "source");

        // JNA creates ByteBuffer using native byte order, set masks according to that.
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            caps.append("format=BGRx");
        } else {
            caps.append("format=xRGB");
        }

        AppSink videosink = new AppSink("GstVideoComponent");
        videosink.set("emit-signals", true);
        videosink.connect(GstListener);
        videosink.setCaps(new Caps(caps.toString()));
        videosink.set("max-buffers", 5000);
        videosink.set("drop", true);

        //Creating pipeline
        pipe.addMany(source, videoQueue, videoConvert, videosink, captureQueue);
        Pipeline.linkMany(source, videoQueue, videoConvert, videosink, captureQueue);

        //Set image to view
        ImageContainer imageContainer = GstListener.getImageContainer();
        imageContainer.addListener((observable, oldValue, newValue) -> webcamImageView.setImage(newValue));
        pipe.play();
    }

    public ImageView getWebcamImageView() {
        return webcamImageView;
    }
}
