package com.ucsd.globalties.dvs.core;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import org.opencv.core.Core;

@Slf4j
public class Main extends Application {
  private static final String HAAR_FACE = "/haarcascade_frontalface_alt.xml";
  private static final String HAAR_EYE = "/haarcascade_eye.xml";
  private static Controller controller;
  public static String HAAR_FACE_PATH;
  public static String HAAR_EYE_PATH;
  
  public static void main(String[] args) {
    controller = new Controller();
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
  
  public static Controller getController() {
    return controller;
  }

  @Override
  public void start(Stage stage) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main.fxml"));
    VBox vbox;
    try {
      vbox = (VBox) loader.load();
      stage.setScene(new Scene(vbox));
      stage.show();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
