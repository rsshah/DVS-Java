package com.ucsd.globalties.dvs.core;

import org.opencv.core.Mat;

public class Pupil {
  private Mat pupil;

  public Pupil(Mat mat) {
    pupil = mat;
  }

  /**
   * Detect the white dot here. The idea is to return a double value (or maybe a
   * simple object that describes the white dot's size/position/necessary
   * information to detect diseases. I don't think we really need to crop it out
   * of the image; the positional information will probably suffice.
   * 
   * @return something identified this white dot
   */
  public double getWhiteDot() {
    return 0;
  }

  /**
   * {@link #getWhiteDot}
   * 
   * @return
   */
  public double getCrescent() {
    return 0;
  }

  public Mat getMat() {
    return pupil;
  }
}
