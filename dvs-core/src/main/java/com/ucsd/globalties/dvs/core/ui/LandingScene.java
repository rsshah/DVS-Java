package com.ucsd.globalties.dvs.core.ui;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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
    stage.setTitle("Digital Vision Screening");
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(25, 25, 25, 25));

    Text scenetitle = new Text("Welcome");
    scenetitle.setFont(Font.font("Calibri", FontWeight.NORMAL, 20));
    grid.add(scenetitle, 0, 0, 2, 1);

    Text sceneSubtitle = new Text("Enter Patient Information");
    sceneSubtitle.setFont(Font.font("Calibri", FontWeight.NORMAL,16));
    grid.add(sceneSubtitle, 1, 0, 2, 1);

    //add a label and textfield for each scene label
    for (int i = 0; i < sceneLabels.length; i++) {
      final String sceneLabel = sceneLabels[i];
      Label label = new Label(sceneLabel);
      grid.add(label, 0, i+1);

      final TextField field = new TextField();
      field.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent arg0) {
          inputValues.put(sceneLabel, field.getText());
        }        
      });
      grid.add(field, 1, i+1);
    }

    Button btn = new Button("Next");
    HBox hbBtn = new HBox(10);
    hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
    hbBtn.getChildren().add(btn);
    grid.add(hbBtn, 1, sceneLabels.length + 2);

    btn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
       submitInformation(); 
      }
    });

    Scene scene = new Scene(grid, 350, 450);
    stage.setScene(scene);
    stage.show();
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
