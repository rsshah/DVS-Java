package com.ucsd.globalties.dvs.core.ui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;

import com.ucsd.globalties.dvs.core.Main;

public class ResultGridController implements Initializable, ControlledScreen {
  private NavigationController navigationController;
  private RootViewController rootViewController;
  
  @FXML
  private GridPane root;
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
  private void goToInputGrid(ActionEvent event) {
    navigationController.setScreen(Main.inputScreenID);
  }

}
