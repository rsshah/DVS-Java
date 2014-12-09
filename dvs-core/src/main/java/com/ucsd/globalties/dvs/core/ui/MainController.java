package com.ucsd.globalties.dvs.core.ui;

import java.io.File;
import java.net.URL;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
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
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import lombok.extern.slf4j.Slf4j;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialogs;

import com.ucsd.globalties.dvs.core.Controller;
import com.ucsd.globalties.dvs.core.EyeDisease;
import com.ucsd.globalties.dvs.core.Main;
import com.ucsd.globalties.dvs.core.Patient;

@Slf4j
public class MainController implements Initializable {
  private static String[] sceneLabels = {"Name","Date of Birth","Gender","Ethnicity","Language","Room Number","School","Screening Comment"};
  private Map<String,TextField> inputValues = new HashMap<String,TextField>();
  private String hFilePath, vFilePath;
  private Controller controller;
  private final FileChooser fileChooser = new FileChooser();

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
  private GridPane detectGrid;
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
    assert detectGrid != null : "fx:id=\"detectGrid\" was not injected: check your FXML file 'landing.fxml'.";
    assert resultGrid != null : "fx:id=\"resultGrid\" was not injected: check your FXML file 'landing.fxml'.";
    assert exportItem != null : "fx:id=\"exportItem\" was not injected: check your FXML file 'landing.fxml'.";

    // initialize logic here: all @FXML variables will have been injected
    this.controller = Main.getController();
    //bind menu item to export to excel action
    exportItem.setOnAction(new EventHandler<ActionEvent>() {
      public void handle(ActionEvent t) {
        controller.exportData();
      }
    });
    //clear everything and only show input grid
    stackPane.getChildren().clear();
    stackPane.getChildren().add(inputGrid);
    
    fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
    this.setupInputGrid();
    this.setupPhotoGrid();
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
  
  /*
   * Sets up the photo selection grid
   */
  private void setupPhotoGrid() {
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
    HBox vBox = new HBox(10);
    HBox hBox = new HBox(10);
    vBox.setAlignment(Pos.CENTER);
    hBox.setAlignment(Pos.CENTER);

    hImageView.setFitWidth(150);
    hImageView.setPreserveRatio(true);
    hImageView.setSmooth(true);
    hImageView.setCache(true);
    hBox.getChildren().add(hImageView);

    vImageView.setFitWidth(150);
    vImageView.setPreserveRatio(true);
    vImageView.setSmooth(true);
    vImageView.setCache(true);
    vBox.getChildren().add(vImageView);

    hOpenBtn.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(final ActionEvent e) {
            File file = fileChooser.showOpenDialog(root.getScene().getWindow());
            if (file!=null) {
              fileChooser.setInitialDirectory(file.getParentFile());
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
            if (file!=null) {
              fileChooser.setInitialDirectory(file.getParentFile());
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
          showDetectGrid();
        }
      }
    });

    photoGrid.add(hbHOpenBtn, 0, 0);
    photoGrid.add(vbVOpenBtn, 1, 0);
    photoGrid.add(hBox, 0, 1);
    photoGrid.add(vBox, 1, 1);
    photoGrid.add(hbBackBtn, 0, 2);
    photoGrid.add(hbNextBtn, 1, 2);
  }

  /*
   * Sets up the result display
   */
  private void setupResultGrid() {    
    //get results
    controller.diagnose();
    Map<EyeDisease, String> medicalRecord = controller.getRecords();
    //display results
    int index = 0;
    for (Map.Entry<EyeDisease, String> entry : medicalRecord.entrySet()) {
      Label diseaseLabel = new Label(entry.getKey().toString());
      Label commentLabel = new Label(entry.getValue());

      resultGrid.add(diseaseLabel, 0, index);
      resultGrid.add(commentLabel, 1, index);
      index++;
    }
    Button startOver = new Button("Start Over");
    startOver.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
        showInputGrid();
      }
    });
    resultGrid.add(startOver, 1, medicalRecord.size()+3);
  }
  private void setupDetectGrid() {
    Map<String,String> detected = controller.detectAll();    
    Button backBtn = new Button("Back");
    backBtn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent arg0) {
        returnToPhotoGrid(); 
      }
    });
    Button nextBtn = new Button("Next");
    nextBtn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent arg0) {
        showResultGrid();
      }
    });
    HBox hbBackBtn = new HBox(10);
    HBox hbNextBtn = new HBox(10);
    hbBackBtn.setAlignment(Pos.BOTTOM_RIGHT);
    hbBackBtn.getChildren().add(backBtn);
    hbNextBtn.setAlignment(Pos.BOTTOM_LEFT);
    hbNextBtn.getChildren().add(nextBtn);  
    TilePane hTiles = new TilePane(Orientation.VERTICAL);
    hTiles.setPrefRows(2);
    if (detected.get("left_eye_horizontal") != null) {
      ImageView iView = new ImageView();
      iView.setImage(new Image("file:"+detected.get("left_eye_horizontal")));
      iView.setFitHeight(100);
      iView.setPreserveRatio(true);
      iView.setSmooth(true);
      iView.setCache(true);
      hTiles.getChildren().add(iView);
    }    
    if (detected.get("left_eye_pupil_horizontal") != null) {
      ImageView iView = new ImageView();
      iView.setImage(new Image("file:"+detected.get("left_eye_pupil_horizontal")));
      iView.setFitHeight(100);
      iView.setPreserveRatio(true);
      iView.setSmooth(true);
      iView.setCache(true);
      hTiles.getChildren().add(iView);
    }    
    if (detected.get("right_eye_horizontal") != null) {
      ImageView iView = new ImageView();
      iView.setImage(new Image("file:"+detected.get("right_eye_horizontal")));
      iView.setFitHeight(100);
      iView.setPreserveRatio(true);
      iView.setSmooth(true);
      iView.setCache(true);
      hTiles.getChildren().add(iView);
    }    
    if (detected.get("right_eye_pupil_horizontal") != null) {
      ImageView iView = new ImageView();
      iView.setImage(new Image("file:"+detected.get("right_eye_pupil_horizontal")));
      iView.setFitHeight(100);
      iView.setPreserveRatio(true);
      iView.setSmooth(true);
      iView.setCache(true);
      hTiles.getChildren().add(iView);
    }
    TilePane vTiles = new TilePane(Orientation.VERTICAL);
    vTiles.setPrefRows(2);
    if (detected.get("left_eye_vertical") != null) {
      ImageView iView = new ImageView();
      iView.setImage(new Image("file:"+detected.get("left_eye_vertical")));
      iView.setFitHeight(100);
      iView.setPreserveRatio(true);
      iView.setSmooth(true);
      iView.setCache(true);
      vTiles.getChildren().add(iView);
    }    
    if (detected.get("left_eye_pupil_vertical") != null) {
      ImageView iView = new ImageView();
      iView.setImage(new Image("file:"+detected.get("left_eye_pupil_vertical")));
      iView.setFitHeight(100);
      iView.setPreserveRatio(true);
      iView.setSmooth(true);
      iView.setCache(true);
      vTiles.getChildren().add(iView);
    }    
    if (detected.get("right_eye_vertical") != null) {
      ImageView iView = new ImageView();
      iView.setImage(new Image("file:"+detected.get("right_eye_vertical")));
      iView.setFitHeight(100);
      iView.setPreserveRatio(true);
      iView.setSmooth(true);
      iView.setCache(true);
      vTiles.getChildren().add(iView);
    }    
    if (detected.get("right_eye_pupil_vertical") != null) {
      ImageView iView = new ImageView();
      iView.setImage(new Image("file:"+detected.get("right_eye_pupil_vertical")));
      iView.setFitHeight(100);
      iView.setPreserveRatio(true);
      iView.setSmooth(true);
      iView.setCache(true);
      vTiles.getChildren().add(iView);
    }
    vTiles.setAlignment(Pos.CENTER);
    hTiles.setAlignment(Pos.CENTER);
    detectGrid.add(hTiles, 0, 0);
    detectGrid.add(vTiles, 1, 0);
    //
    detectGrid.add(hbBackBtn, 0, 1);
    detectGrid.add(hbNextBtn, 1, 1);
  }
  
  /*
   * Transition from photo grid to detect grid
   * TODO find more elegant way to do transitions instead of clearing/setting up grids
   */
  private void showDetectGrid() {
    controller.setPatientPhotos(hFilePath, vFilePath);
    stackPane.getChildren().remove(photoGrid);
    this.setupDetectGrid();
    stackPane.getChildren().add(detectGrid);
  }

  /*
   * Transition into the result grid from detect grid
   * TODO find more elegant way to do transitions instead of clearing/setting up grids
   */
  private void showResultGrid() {
    stackPane.getChildren().remove(detectGrid);
    this.setupResultGrid();
    stackPane.getChildren().add(resultGrid);
  }

  /*
   * Transition from input grid to photo grid
   * Submits patient information to controller and moves to photo select grid
   * TODO find more elegant way to do transitions instead of clearing/setting up grids
   */
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
  
  /*
   * Transition from results to input for new patient.
   * CLear everything and sets it up again so form elements are reset
   * TODO find more elegant way to do transitions instead of clearing/setting up everything
   */
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
  
  private void returnToPhotoGrid() {
    stackPane.getChildren().remove(detectGrid);
    stackPane.getChildren().add(photoGrid);
  }

}
