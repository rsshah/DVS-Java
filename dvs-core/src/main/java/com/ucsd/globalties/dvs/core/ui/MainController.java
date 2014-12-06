package com.ucsd.globalties.dvs.core.ui;

import java.io.File;
import java.net.URL;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialogs;

import com.ucsd.globalties.dvs.core.Controller;
import com.ucsd.globalties.dvs.core.EyeDisease;
import com.ucsd.globalties.dvs.core.Main;
import com.ucsd.globalties.dvs.core.Patient;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class MainController implements Initializable {
  private static String[] sceneLabels = {"Name","Date of Birth","Gender","Ethnicity","Language","Room Number","School","Screening Comment"};
  private Map<String,TextField> inputValues = new HashMap<String,TextField>();
  private Map<EyeDisease, String> medicalRecord = new HashMap<EyeDisease,String>();
  private String hFilePath, vFilePath;
  private Controller controller;

  //Value injected by FXMLLoader
  @FXML
  private VBox root;
  @FXML
  private StackPane stackPane;
  @FXML
  private GridPane inputGrid;
  @FXML
  private GridPane photoGrid;
  @FXML
  private GridPane resultGrid;
  @FXML
  private MenuItem exportItem;

  @Override // This method is called by the FXMLLoader when initialization is complete
  public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
    assert root != null : "fx:id=\"root\" was not injected: check your FXML file 'landing.fxml'.";
    assert stackPane != null : "fx:id=\"stackPane\" was not injected: check your FXML file 'landing.fxml'.";
    assert inputGrid != null : "fx:id=\"inputGrid\" was not injected: check your FXML file 'landing.fxml'.";
    assert photoGrid != null : "fx:id=\"photoGrid\" was not injected: check your FXML file 'landing.fxml'.";
    assert resultGrid != null : "fx:id=\"resultGrid\" was not injected: check your FXML file 'landing.fxml'.";
    assert exportItem != null : "fx:id=\"exportItem\" was not injected: check your FXML file 'landing.fxml'.";

    // initialize logic here: all @FXML variables will have been injected
    this.controller = Main.getController();
    exportItem.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent t) {
        controller.exportData();
      }
    });
    stackPane.getChildren().clear();
    stackPane.getChildren().add(inputGrid);

    this.setupInputGrid();
    this.setupPhotoGrid();
  }

  private void setupInputGrid() {
    for (int i = 0; i < sceneLabels.length; i++) {
      String sceneLabel = sceneLabels[i];
      Label label = new Label(sceneLabel);

      TextField field = new TextField();
      inputValues.put(sceneLabel, field);
      inputGrid.add(label, 0, i);
      inputGrid.add(field, 1, i);
    }
    Button btn = new Button("Next");
    HBox hbBtn = new HBox(10);
    hbBtn.setAlignment(Pos.BOTTOM_LEFT);
    hbBtn.getChildren().add(btn);
    inputGrid.add(hbBtn, 1, sceneLabels.length + 2);

    btn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
        showPhotoGrid();
      }
    });
  }

  private void setupPhotoGrid() {
    final FileChooser fileChooser = new FileChooser();
    fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));

    Button hOpenBtn = new Button("Select Horizontal Picture");    
    Button vOpenBtn = new Button("Select Vertical Picture");
    HBox hbHOpenBtn = new HBox(10);
    HBox vbVOpenBtn = new HBox(10);
    hbHOpenBtn.setAlignment(Pos.CENTER);
    hbHOpenBtn.getChildren().add(hOpenBtn);
    vbVOpenBtn.setAlignment(Pos.CENTER);
    vbVOpenBtn.getChildren().add(vOpenBtn);

    Button backBtn = new Button("Back");
    Button nextBtn = new Button("Next");
    HBox hbBackBtn = new HBox(10);
    HBox hbNextBtn = new HBox(10);
    hbBackBtn.setAlignment(Pos.BOTTOM_RIGHT);
    hbBackBtn.getChildren().add(backBtn);
    hbNextBtn.setAlignment(Pos.BOTTOM_LEFT);
    hbNextBtn.getChildren().add(nextBtn);  

    final ImageView hImageView = new ImageView();
    final ImageView vImageView = new ImageView();

    hImageView.setFitWidth(150);
    hImageView.setPreserveRatio(true);
    hImageView.setSmooth(true);
    hImageView.setCache(true);

    vImageView.setFitWidth(150);
    vImageView.setPreserveRatio(true);
    vImageView.setSmooth(true);
    vImageView.setCache(true);

    hOpenBtn.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(final ActionEvent e) {
            File file = fileChooser.showOpenDialog(root.getScene().getWindow());
            fileChooser.setInitialDirectory(file.getParentFile());
            if (file!=null) {
              hFilePath = file.getAbsolutePath();
              hImageView.setImage(new Image("file:///"+hFilePath));
            }
          }
        });

    vOpenBtn.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(final ActionEvent e) {
            File file = fileChooser.showOpenDialog(root.getScene().getWindow());
            fileChooser.setInitialDirectory(file.getParentFile());
            if (file!=null) {
              vFilePath = file.getAbsolutePath();
              vImageView.setImage(new Image("file:///"+vFilePath));
            }
          }
        });

    // Takes the user back to the landing page
    backBtn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {        
        returnToInputGrid();
      }
    });

    nextBtn.setOnAction(new EventHandler<ActionEvent>() {
      //TODO Input verification
      @Override
      public void handle(ActionEvent e) {
        if (vFilePath == null || hFilePath == null) {
          //controlfx dialogs is deprecated since it will be merged into java
          //planned for 8u40, sometime March 2015
          Action alert = Dialogs.create()
              .owner(root.getScene().getWindow())
              .title("Alert!")
              .message("Please select the appropriate images.")
              .showError();
        }
        else {
          showResultGrid();
        }
      }
    });

    photoGrid.add(hbHOpenBtn, 0, 0);
    photoGrid.add(vbVOpenBtn, 1, 0);
    photoGrid.add(hImageView, 0, 1);
    photoGrid.add(vImageView, 1, 1);
    photoGrid.add(hbBackBtn, 0, 2);
    photoGrid.add(hbNextBtn, 1, 2);
  }


  private void setupResultGrid() {
    Button startOver = new Button("Start Over");
    startOver.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
        showInputGrid();
      }
    });

    int index = 0;
    for (Map.Entry<EyeDisease, String> entry : medicalRecord.entrySet()) {
      Label diseaseLabel = new Label(entry.getKey().toString());
      Label commentLabel = new Label(entry.getValue());

      resultGrid.add(diseaseLabel, 0, index);
      resultGrid.add(commentLabel, 1, index);
      index++;
    }
    resultGrid.add(startOver, 1, medicalRecord.size()+3);
  }

  private void showResultGrid() {
    controller.setPatientPhotos(hFilePath, vFilePath);
    controller.diagnose();
    medicalRecord = controller.getRecords();
    stackPane.getChildren().remove(photoGrid);
    this.setupResultGrid();
    stackPane.getChildren().add(resultGrid);
  }

  private void showPhotoGrid() {
    int i = 0;
    controller.setPatient(Patient.builder()
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
    stackPane.getChildren().add(photoGrid);
    stackPane.getChildren().remove(inputGrid);
  }

  private void showInputGrid() {
    controller.finalizePatient();
    inputValues.clear();
    hFilePath = "";
    vFilePath = "";
    stackPane.getChildren().remove(resultGrid);
    inputGrid.getChildren().clear();
    photoGrid.getChildren().clear();
    resultGrid.getChildren().clear();
    this.setupInputGrid();
    this.setupPhotoGrid();
    stackPane.getChildren().add(inputGrid);
  }

  private void returnToInputGrid() {
    stackPane.getChildren().remove(photoGrid);
    stackPane.getChildren().add(inputGrid);
  }

}
