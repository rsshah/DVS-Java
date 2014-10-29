package com.ucsd.globalties.dvs.core;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Photo {
  public enum PhotoType {
    HORIZONTAL,
    VERTICAL;
  }
  
  private PhotoType type;
  private Image src;
  
  
  public Photo(File f, PhotoType type) {
    try {
      src = ImageIO.read(f);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
