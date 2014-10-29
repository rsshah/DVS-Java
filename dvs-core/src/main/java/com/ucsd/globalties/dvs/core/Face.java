package com.ucsd.globalties.dvs.core;

import lombok.Setter;

public class Face {
  
  @Setter
  private Eye leftEye, rightEye;
  
  private Eye findEye(boolean left) {
    return new Eye();
  }
  
  public Eye getLeftEye() {
    if (leftEye == null) {
      leftEye = findEye(true);
    }
    return leftEye;
  }
  
  public Eye getRightEye() {
    if (rightEye == null) {
      rightEye = findEye(false);
    }
    return rightEye;
  }
}
