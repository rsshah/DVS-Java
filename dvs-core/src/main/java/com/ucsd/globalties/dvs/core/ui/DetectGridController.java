package com.ucsd.globalties.dvs.core.ui;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;

import com.ucsd.globalties.dvs.core.Main;

public class DetectGridController implements Initializable, ControlledScreen{
  private NavigationController navigationController;
  private RootViewController rootViewController;
  
  @FXML
  private GridPane root;
  @FXML
  private TilePane tilePaneLeft;
  @FXML
  private TilePane tilePaneRight;
  @FXML
  private Button btnPrev;
  @FXML
  private Button btnNext;

  @Override
  public void initialize(URL arg0, ResourceBundle arg1) {
    assert root != null : "fx:id=\"root\" was not injected: check your FXML file 'detect_grid.fxml'.";    
  }
  
  @Override
  public void setScreenParent(NavigationController navigationController) {
    this.navigationController = navigationController;    
  }
  
  @Override
  public void setRootView(RootViewController rootViewController) {
    this.rootViewController = rootViewController;    
  }
  
  @FXML
  private void goToPhotoGrid(ActionEvent event) {
    navigationController.setScreen(Main.photoGridID);
  }
  @FXML
  private void goToResultsGrid(ActionEvent event) {
    navigationController.setScreen(Main.resultGridID);
  }
}
