package com.ucsd.globalties.dvs.core.ui;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialogs;

import com.ucsd.globalties.dvs.core.Controller;

public class PhotoScene {
  private Controller controller;
  private final Stage stage;
  private String hFilePath, vFilePath;

  public PhotoScene(final Stage stage, Controller controller) {
    this.controller = controller;
    this.stage = stage;
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(25, 25, 25, 25));

    Text sceneSubtitle = new Text("Select Patient Photos");
    sceneSubtitle.setFont(Font.font("Calibri", FontWeight.NORMAL,16));
    grid.add(sceneSubtitle, 0, 0, 2, 1);

    final FileChooser fileChooser = new FileChooser();
    fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));

    Button hOpenBtn = new Button("Select Horizontal Picture");    
    Button vOpenBtn = new Button("Select Vertical Picture");
    
    Button backBtn = new Button("Back");
    Button nextBtn = new Button("Next");
    HBox hbBtn = new HBox(10);
    hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
    hbBtn.getChildren().add(backBtn);
    hbBtn.getChildren().add(nextBtn);  

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
            File file = fileChooser.showOpenDialog(stage);
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
            File file = fileChooser.showOpenDialog(stage);
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
          showLandingScene();
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
              .owner(stage)
              .title("Alert!")
              .message("Please select the appropriate images.")
              .showError();
        }
        else {
          showResultScene();
        }
      }
    });

    grid.add(hOpenBtn, 0, 1);
    grid.add(vOpenBtn, 1, 1);
    grid.add(hImageView, 0, 2);
    grid.add(vImageView, 1, 2);
    grid.add(hbBtn, 1, 3);

    Scene scene = new Scene(grid, 350, 450);
    stage.setScene(scene);
    stage.show();
  }
  
  // 
  private void showResultScene() {
    controller.setPatientPhotos(hFilePath, vFilePath);
    ResultScene resultScene = new ResultScene(stage, controller);
  }
  
  private void showLandingScene() {
	  LandingScene landingScene = new LandingScene(stage, controller);
  }

}
