package com.ucsd.globalties.dvs.core.ui;

import java.net.URL;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import lombok.extern.slf4j.Slf4j;

import com.ucsd.globalties.dvs.core.Controller;
import com.ucsd.globalties.dvs.core.EyeDisease;
import com.ucsd.globalties.dvs.core.Main;
import com.ucsd.globalties.dvs.core.Patient;
/**
 * Interaction logic for input grid
 * @author sabitn2
 *
 */
@Slf4j
public class InputGridController implements Initializable,ControlledScreen {
  public static String[] sceneLabels = {"Name","Date of Birth","Gender","Ethnicity","Language","Room Number","School","Screening Comment"};
  private Map<String,TextField> inputValues = new HashMap<String,TextField>();
  private NavigationController navigationController;
  private RootViewController rootViewController;
  
  @FXML
  private GridPane root;
  
  @Override
  public void initialize(URL url, ResourceBundle rsrc) {
    assert root != null : "fx:id=\"root\" was not injected: check your FXML file 'input_grid.fxml'.";
    setupInputGrid();
  }
  
  @Override
  public void setScreenParent(NavigationController navigationController) {
    this.navigationController = navigationController;    
  }
  
  /*
   * Sets up the initial input grid.
   */
  private void setupInputGrid() {
    for (int i = 0; i < sceneLabels.length; i++) {
      String sceneLabel = sceneLabels[i];
      Label label = new Label(sceneLabel);

      TextField field = new TextField();
      inputValues.put(sceneLabel, field);
      root.add(label, 0, i);
      root.add(field, 1, i);
    }
    Button btn = new Button("Next");
    HBox hbBtn = new HBox(10);
    hbBtn.setAlignment(Pos.BOTTOM_LEFT);
    hbBtn.getChildren().add(btn);
    root.add(hbBtn, 1, sceneLabels.length + 2);

    btn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
        goToPhotoGrid(e);
      }
    });
  }
  
  /**
   * TODO Definitely want to improve passing user input to controller
   * @param event
   */
  private void goToPhotoGrid(ActionEvent event) {
    int i = 0;
    rootViewController.getController().setPatient(Patient.builder()
        .name(inputValues.get(sceneLabels[i++]).getText())
        .birth(inputValues.get(sceneLabels[i++]).getText())
        .gender(inputValues.get(sceneLabels[i++]).getText())
        .ethnicity(inputValues.get(sceneLabels[i++]).getText())
        .language(inputValues.get(sceneLabels[i++]).getText())
        .roomNumber(inputValues.get(sceneLabels[i++]).getText())
        .school(inputValues.get(sceneLabels[i++]).getText())
        .screeningComment(inputValues.get(sceneLabels[i++]).getText())
        .medicalRecord(new EnumMap<EyeDisease, String>(EyeDisease.class))
        .build());
    navigationController.setScreen(Main.photoGridID);
  }

  @Override
  public void setRootView(RootViewController rootViewController) {
    this.rootViewController = rootViewController;    
  }
  
  @Override
  public void resetState() {
    for(Map.Entry<String, TextField> entry : inputValues.entrySet()) {
      entry.getValue().clear();
    }
  }

}
