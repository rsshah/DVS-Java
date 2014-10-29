package com.ucsd.globalties.dvs.core;

import com.ucsd.globalties.dvs.core.ui.LandingScene;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
  public static void main(String[] args) {
    //Patient.builder().gender("").birth("").build();
    launch(args);
  }

  @Override
  public void start(Stage stage) throws Exception {
    LandingScene landingScene = new LandingScene(stage);
    
  }
}
