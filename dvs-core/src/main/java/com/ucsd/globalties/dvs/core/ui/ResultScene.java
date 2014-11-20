package com.ucsd.globalties.dvs.core.ui;

import java.util.Map;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import com.ucsd.globalties.dvs.core.Controller;
import com.ucsd.globalties.dvs.core.EyeDisease;

public class ResultScene {
  private Controller controller;
  private Stage stage;
  
  private Map<EyeDisease, String> medicalRecord;

  public ResultScene(Stage stage, Controller controller) {
    this.controller = controller;
    this.stage = stage;
    controller.diagnose();
    medicalRecord = controller.getRecords();
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(25, 25, 25, 25));
    
    Text sceneSubtitle = new Text("Patient Results");
    sceneSubtitle.setFont(Font.font("Calibri", FontWeight.NORMAL,16));
    grid.add(sceneSubtitle, 0, 0, 2, 1);
    
    Button startOver = new Button("Start Over");
    startOver.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
       startOver(); 
      }
    });

    
    int index = 0;
    for (Map.Entry<EyeDisease, String> entry : medicalRecord.entrySet()) {
      Label diseaseLabel = new Label(entry.getKey().toString());
      Label commentLabel = new Label(entry.getValue());
      
      grid.add(diseaseLabel, 0, 2+index);
      grid.add(commentLabel, 1, 2+index);
      index++;
    }
    
    grid.add(startOver, 1, medicalRecord.size()+3);
    
    Scene scene = new Scene(grid, 350, 450);
    stage.setScene(scene);
    stage.show();
    
  }
  
  //We are done with this patient so show landing scene again and finalize this
  //patients results
  private void startOver() {
    controller.finalizePatient();
    LandingScene lScene = new LandingScene(stage,controller);
  }

}
