package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import media.AudioPlayer;
import media.GstreamerMediaPlayer;
import media.WebcamStream;
import org.freedesktop.gstreamer.Gst;

import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class IndexController implements Initializable {

    @FXML
    VBox vBoxContent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Gst.init();

        String videoPath = Paths.get("").toAbsolutePath().toString() + "/video/sky.mp4";
        String audioPath = Paths.get("").toAbsolutePath().toString() + "/audio/rain.mp3";
        GstreamerMediaPlayer mediaPlayer = new GstreamerMediaPlayer(videoPath, true);
        ImageView view = mediaPlayer.videoResult();

        WebcamStream webcamStream = new WebcamStream();
        ImageView webcam = webcamStream.getWebcamImageView();

        vBoxContent.getChildren().add(view);
        vBoxContent.getChildren().add(webcam);

        AudioPlayer audioPlayer = new AudioPlayer();
        audioPlayer.play(audioPath, true);
    }
}
