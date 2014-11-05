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

    Text scenetitle = new Text("Welcome");
    scenetitle.setFont(Font.font("Calibri", FontWeight.NORMAL, 20));
    grid.add(scenetitle, 0, 0, 2, 1);

    Text sceneSubtitle = new Text("Select Patient Photos");
    sceneSubtitle.setFont(Font.font("Calibri", FontWeight.NORMAL,16));
    grid.add(sceneSubtitle, 1, 0, 2, 1);

    final FileChooser fileChooser = new FileChooser();

    Button hOpenBtn = new Button("Select Horizontal Picture");    
    Button vOpenBtn = new Button("Select Vertical Picture");

    Button btn = new Button("Next");
    HBox hbBtn = new HBox(10);
    hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
    hbBtn.getChildren().add(btn);    

    final ImageView hImageView = new ImageView();
    final ImageView vImageView = new ImageView();

    hImageView.setFitWidth(100);
    hImageView.setPreserveRatio(true);
    hImageView.setSmooth(true);
    hImageView.setCache(true);

    vImageView.setFitWidth(100);
    vImageView.setPreserveRatio(true);
    vImageView.setSmooth(true);
    vImageView.setCache(true);

    hOpenBtn.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(final ActionEvent e) {
            File file = fileChooser.showOpenDialog(stage);
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
            if (file!=null) {
              vFilePath = file.getAbsolutePath();
              vImageView.setImage(new Image("file:///"+vFilePath));
            }
          }
        });

    btn.setOnAction(new EventHandler<ActionEvent>() {
      //TODO Input verification
      @Override
      public void handle(ActionEvent e) {
        showResultScene();
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
  
  private void showResultScene() {
    controller.setPatientPhotos(hFilePath, vFilePath);
    ResultScene resultScene = new ResultScene(stage, controller);
  }

}
