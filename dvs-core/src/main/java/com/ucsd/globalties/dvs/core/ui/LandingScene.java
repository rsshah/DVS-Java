package com.ucsd.globalties.dvs.core.ui;

import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import com.ucsd.globalties.dvs.core.Controller;
import com.ucsd.globalties.dvs.core.EyeDisease;
import com.ucsd.globalties.dvs.core.Patient;

public class LandingScene {

  private static String[] sceneLabels = {"Name","Date of Birth","Gender","Ethnicity","Language","Room Number","School","Screening Comment"};
  private Map<String,String> inputValues = new HashMap<String,String>();
  private Controller controller;
  private Stage stage;

  public LandingScene(Stage stage, Controller controller) {
    this.controller = controller;
    this.stage = stage;
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/landing.fxml"));
    VBox vbox;
    try {
      vbox = (VBox) loader.load();
      Scene scene = new Scene(vbox);
      
      stage.setScene(scene);
      stage.show();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }
  
  private void submitInformation() {
    int i = 0;
    //TODO input validation
    controller.setPatient(Patient.builder()
        .name(inputValues.get(sceneLabels[i++]))
        .birth(inputValues.get(sceneLabels[i++]))
        .gender(inputValues.get(sceneLabels[i++]))
        .ethnicity(inputValues.get(sceneLabels[i++]))
        .language(inputValues.get(sceneLabels[i++]))
        .roomNumber(inputValues.get(sceneLabels[i++]))
        .school(inputValues.get(sceneLabels[i++]))
        .screeningComment(inputValues.get(sceneLabels[i++]))
        .medicalRecord(new EnumMap<EyeDisease, String>(EyeDisease.class))
        .build());
    PhotoScene photoScene = new PhotoScene(stage, controller);
  }

}
