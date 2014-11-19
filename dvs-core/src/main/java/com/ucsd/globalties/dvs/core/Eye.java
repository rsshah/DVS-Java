package com.ucsd.globalties.dvs.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
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

  /**
   * Algorithm from
   * https://opencv-code.com/tutorials/pupil-detection-from-an-eye-image/
   * https://github.com/bsdnoobz/opencv-code/blob/master/pupil-detect.cpp
   * 
   * TODO return a Mat with just the pupil, not including the eye 
   * TODO Error check in case multiple contours match conditions 
   * TODO refine conditions to work for other pictures as well (if they do not already)
   */
  private Pupil findPupil() {
    int code = (new Random()).nextInt();
    Mat src = new Mat();
    Mat invt = new Mat();
    mat.copyTo(src);
    mat.copyTo(invt);
    Mat gray = new Mat();
    Mat invertcolormatrix = new Mat(src.rows(), src.cols(), src.type(), new Scalar(255, 255, 255));
    Core.subtract(invertcolormatrix, src, invt);
    Imgproc.cvtColor(invt, gray, Imgproc.COLOR_BGR2GRAY);
    // use 220,225 for ideal image
    Imgproc.threshold(gray, gray, 160, 180, Imgproc.THRESH_BINARY);
    List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
    Imgproc.findContours(gray.clone(), contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);
    Imgproc.drawContours(gray, contours, -1, new Scalar(255, 255, 255), -1);
    Mat pupilMat = null;
    for (int i = 0; i < contours.size(); i++) {
      Double area = Imgproc.contourArea(contours.get(i));
      Rect rect = Imgproc.boundingRect(contours.get(i));
      int radius = rect.width / 2;
      // Cond1: area of contour
      // Cond2: compare rectangular area so it is as close as possible to a
      // square. A rect that bounds a perfect circle is a perfect square
      // Cond3: Compare circular contour area to perfect circular area
      // Should refine last condition so it is usable condition, might be able
      // to use to center point perfectly
      double boundingArea = Math.abs(1 - ((double) rect.width / (double) rect.height));
      if (area >= 1000 && boundingArea <= 0.2) {
        double unk = Math.abs(1 - (area / (Math.PI * Math.pow(radius, 2))));
        log.info("Contour info: Area " + area + " first arg " + boundingArea + " second arg " + unk);
        if (pupilMat != null) {
          log.warn("Setting pupilMat after a valid one had already been found.");
        }
        pupilMat = new Mat(src, rect);
      }
    }
    if (pupilMat == null) {
      log.error("Unable to find an adequate pupil");
      return null;
    }
    Highgui.imwrite("pupil-" + code + ".jpg", pupilMat);
    return new Pupil(pupilMat);
  }
}
