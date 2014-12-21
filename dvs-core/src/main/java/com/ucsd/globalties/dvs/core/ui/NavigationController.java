package com.ucsd.globalties.dvs.core.ui;

import java.util.HashMap;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class NavigationController extends StackPane {
  //Holds the screens to be displayed
  private HashMap<String, Node> screens = new HashMap<>();
  private RootViewController rootViewController;

  public NavigationController(RootViewController rootViewController) {
    super();
    this.rootViewController = rootViewController;
  }

  //Add the screen to the collection
  public void addScreen(String name, Node screen) {
    screens.put(name, screen);
  }

  //Returns the Node with the appropriate name
  public Node getScreen(String name) {
    return screens.get(name);
  }

  //Loads the fxml file, add the screen to the screens collection and
  //finally injects the screenPane to the controller.
  public boolean loadScreen(String name, String resource) {
    try {
      FXMLLoader myLoader = new FXMLLoader(getClass().getResource(resource));
      Parent loadScreen = (Parent) myLoader.load();
      ControlledScreen myScreenController = ((ControlledScreen) myLoader.getController());
      myScreenController.setScreenParent(this);
      myScreenController.setRootView(rootViewController);
      addScreen(name, loadScreen);
      return true;
    } 
    catch (Exception e) {
      e.printStackTrace();
      log.error("Screen not loaded" + e);
      return false;
    }
  }

  //This method tries to displayed the screen with a predefined name.
  //First it makes sure the screen has been already loaded.  Then if there is more than
  //one screen the new screen is been added second, and then the current screen is removed.
  // If there isn't any screen being displayed, the new screen is just added to the root.
  public boolean setScreen(String name) {       
    if (screens.get(name) != null) {   //screen loaded
      if (!getChildren().isEmpty()) {    //if there is more than one screen
        getChildren().remove(0);                    //remove the displayed screen
        getChildren().add(0, screens.get(name));     //add the screen
      } 
      else {
        getChildren().add(screens.get(name));       //no other screen is displayed so show
      }
      return true;
    } 
    else {
      log.warn("Screen not found");
      return false;
    }
  }

  //This method will remove the screen with the given name from the collection of screens
  public boolean unloadScreen(String name) {
    if (screens.remove(name) == null) {
      log.warn("Screen does not exist");
      return false;
    } 
    else {
      return true;
    }
  }
}
