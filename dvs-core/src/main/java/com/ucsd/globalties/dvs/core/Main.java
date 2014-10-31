package com.ucsd.globalties.dvs.core;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import org.opencv.core.Core;

import com.ucsd.globalties.dvs.core.Photo.PhotoType;
import com.ucsd.globalties.dvs.core.ui.LandingScene;

@Slf4j
public class Main extends Application {
  private static final String HAAR_FACE = "/haarcascade_frontalface_alt.xml";
  private static final String HAAR_EYE = "/haarcascade_eye.xml";
  static Controller controller;
  public static String HAAR_FACE_PATH;
  public static String HAAR_EYE_PATH;
  
  public static void main(String[] args) {
    controller = new Controller();
    // load OpenCV constants
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    HAAR_FACE_PATH = Main.class.getResource(HAAR_FACE).getPath();
    if (HAAR_FACE_PATH.startsWith("/")) { // weird java bug that returns it prefixed with '/'
      HAAR_FACE_PATH = HAAR_FACE_PATH.substring(1);
    }
    HAAR_EYE_PATH = Main.class.getResource(HAAR_EYE).getPath();
    if (HAAR_EYE_PATH.startsWith("/")) {
      HAAR_EYE_PATH  = HAAR_EYE_PATH.substring(1);
    }
    launch(args);
  }

  @Override
  public void start(Stage stage) throws Exception {
    LandingScene landingScene = new LandingScene(stage,controller);
    List<String> imgs = new ArrayList<>(14);
    for (int i = 1; i < 10; i++) {
      if (i == 2) continue;
      imgs.add("pics/Red0" + i + ".jpg");
    }
    imgs.add("pics/redface1.jpg");
    imgs.add("pics/redface3.jpg");
    imgs.add("pics/redface3.PNG");
    imgs.add("pics/redface4.PNG");
    imgs.add("pics/redface6.PNG");
    imgs.add("pics/redface10.PNG");
    for (String img : imgs) {
      try {
        String path = Main.class.getResource("/" + img).getPath();
        final Photo p = new Photo(path.substring(1), PhotoType.VERTICAL);
        Eye leftEye = p.getLeftEye();
        Pupil leftPupil = leftEye.getPupil();
      } catch (Exception e) {
        log.error("Error for img: " + img);
      }
    }
  }
}
