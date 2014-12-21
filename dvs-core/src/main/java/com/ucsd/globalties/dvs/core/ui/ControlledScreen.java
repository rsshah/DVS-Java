package com.ucsd.globalties.dvs.core.ui;


public interface ControlledScreen {
  
  //This method will allow the injection of the Parent ScreenPane
  public void setScreenParent(NavigationController uiController);
  //Allows screens to access the controller to insert/retrieve data
  public void setRootView(RootViewController rootViewController);
  //allows to reset views to default state
  public void resetState();
}
