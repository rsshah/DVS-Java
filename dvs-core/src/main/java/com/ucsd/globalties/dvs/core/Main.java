package com.ucsd.globalties.dvs.core;

import java.io.File;

import javafx.application.Application;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import org.opencv.core.Core;

import com.ucsd.globalties.dvs.core.ui.LandingScene;

@Slf4j
public class Main extends Application {
  private static final String HAAR_FACE = "/haarcascade_frontalface_alt.xml";
  private static final String HAAR_EYE = "/haarcascade_eye.xml";
  public static String HAAR_FACE_PATH;
  public static String HAAR_EYE_PATH;
  
  public static void main(String[] args) {
    loadLibraryComponents();
    launch(args);
  }
  
  public static void loadLibraryComponents() {
    // load OpenCV constants
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    //Workaround for "/" thing, load as file then get filepath
    File fp = new File(Main.class.getResource(HAAR_FACE).getFile());
    File ep = new File(Main.class.getResource(HAAR_EYE).getFile());
    HAAR_FACE_PATH = fp.getAbsolutePath();
    HAAR_EYE_PATH = ep.getAbsolutePath();
  }

  @Override
  public void start(Stage stage) throws Exception {
    LandingScene landingScene = new LandingScene(stage, new Controller());
  }
}
