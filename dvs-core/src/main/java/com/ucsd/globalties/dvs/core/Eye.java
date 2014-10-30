package com.ucsd.globalties.dvs.core;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

@Slf4j
public class Eye {
  @Getter
  private Mat mat;
  private Pupil pupil;
  
  public Eye(Mat mat) {
    this.mat = mat;
  }
  
  public Pupil getPupil() {
    if (pupil == null) {
      pupil = findPupil();
    }
    return pupil;
  }
  
  private Pupil findPupil() {
    Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY);
    Mat circles = new Mat();
    Imgproc.HoughCircles(mat, circles, Imgproc.CV_HOUGH_GRADIENT, 1, 1);
    log.info("circles: " + circles + ", size: " + circles.size());
    for (int i = 0; i < circles.total(); i++) {
      System.out.println("i: " + i);
    }
    return null;
  }
}
