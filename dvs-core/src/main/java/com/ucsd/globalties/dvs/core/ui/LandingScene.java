package com.ucsd.globalties.dvs.core.ui;

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

public class LandingScene {
  
  private static String[]sceneLabels = {"Name","Date of Birth","Gender","Ethnicity","Language","Room Number","School","Screening Comment"};

  public LandingScene(Stage stage) {
    stage.setTitle("Digital Vision Screening");
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(25, 25, 25, 25));

    Text scenetitle = new Text("Welcome");
    scenetitle.setFont(Font.font("Calibri", FontWeight.NORMAL, 20));
    grid.add(scenetitle, 0, 0, 2, 1);
    
    //add a label and textfield for each scene label
    for (int i = 0; i < sceneLabels.length; i++) {
      String sceneLabel = sceneLabels[i];
      Label label = new Label(sceneLabel);
      grid.add(label, 0, i+1);
      
      TextField field = new TextField();
      grid.add(field, 1, i+1);
    }

    Button btn = new Button("Next");
    HBox hbBtn = new HBox(10);
    hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
    hbBtn.getChildren().add(btn);
    grid.add(hbBtn, 1, sceneLabels.length + 2);

    final Text actiontarget = new Text();
    grid.add(actiontarget, 1, 6);

    btn.setOnAction(new EventHandler<ActionEvent>() {

        @Override
        public void handle(ActionEvent e) {
            //handle next event
        }
    });

    Scene scene = new Scene(grid, 350, 350);
    stage.setScene(scene);
    stage.show();
  }

}
