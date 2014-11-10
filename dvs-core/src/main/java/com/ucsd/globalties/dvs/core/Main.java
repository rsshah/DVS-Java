package com.ucsd.globalties.dvs.core;

import java.io.File;

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
    //Workaround for "/" bug, load as file then get filepath
    File fp = new File(Main.class.getResource(HAAR_FACE).getFile());
    File ep = new File(Main.class.getResource(HAAR_EYE).getFile());
    HAAR_FACE_PATH = fp.getAbsolutePath();
    HAAR_EYE_PATH = ep.getAbsolutePath();
    launch(args);
  }

  @Override
  public void start(Stage stage) throws Exception {
    LandingScene landingScene = new LandingScene(stage,controller);
    try {
      //Workaround for "/" bug, load as file then get filepath
      File fh = new File(Main.class.getResource("/pics/jt_h.jpg").getFile());
      File fv = new File(Main.class.getResource("/pics/jt_v.jpg").getFile());
      Photo ph = new Photo(fh.getAbsolutePath(), PhotoType.HORIZONTAL);
      Photo pv = new Photo(fv.getAbsolutePath(), PhotoType.VERTICAL);
      Eye leftEye_h = ph.getLeftEye();
      Eye leftEye_v = pv.getLeftEye();
      Pupil leftPupil_h = leftEye_h.getPupil();
      Pupil leftPupil_v = leftEye_v.getPupil();
      Eye rightEye_h = ph.getRightEye();
      Eye rightEye_v = pv.getRightEye();
      Pupil rightPupil_h = rightEye_h.getPupil();
      Pupil rightPupil_v = rightEye_v.getPupil();
    } catch (Exception e) {
      log.error("Error for img", e);
    }
  }
}
