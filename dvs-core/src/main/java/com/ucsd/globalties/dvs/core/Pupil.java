package com.ucsd.globalties.dvs.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import lombok.AllArgsConstructor;
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

import com.ucsd.globalties.dvs.core.tools.Pair;

@Slf4j
@AllArgsConstructor
public class Pupil {
  @Getter
  private Mat mat;
  @Getter
  private double area;

  /**
   * Detect the white dot here. The idea is to return a double value (or maybe a
   * simple object that describes the white dot's size/position/necessary
   * information to detect diseases. I don't think we really need to crop it out
   * of the image; the positional information will probably suffice.
   * 
   * @return something identified this white dot
   */
  public double getWhiteDot() {
    Mat src = new Mat();
    mat.copyTo(src);
    Mat gray = new Mat();
    // removed inverted image, can just use Imgproc.THRESH_BINARY_INV to invert
    Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
    Double thresh = Imgproc.threshold(gray, gray, 0, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
    List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
    Imgproc.findContours(gray.clone(), contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
    Imgproc.drawContours(gray, contours, -1, new Scalar(255, 255, 255), -1);
    if (contours.isEmpty()) {
      log.error("No contours found for this pupil.");
      return Double.MIN_VALUE;
    }
    java.awt.Point mid = new java.awt.Point(mat.width() / 2, mat.height() / 2);
    List<Pair<MatOfPoint, Double>> contourDistances = new ArrayList<>(contours.size());
    for (int i = 0; i < contours.size(); i++) {
      Rect rect = Imgproc.boundingRect(contours.get(i));
      int radius = rect.width / 2;
      java.awt.Point center = new java.awt.Point(rect.x + radius, rect.y + radius);
      contourDistances.add(new Pair<>(contours.get(i), mid.distanceSq(center)));
    }
    Collections.sort(contourDistances, contourCompare);
    Pair<MatOfPoint, Double> whiteDotPair = null;
    for (Pair<MatOfPoint, Double> pair : contourDistances) {
      double area = Imgproc.contourArea(pair.getLeft());
      if (area > 200.0) { // 250 as iniital value; needs more refinement when we have more test cases
        continue;
      }
      whiteDotPair = pair;
      break;
    }
    if (whiteDotPair == null) {
      log.error("[WhiteDot Detection] Unable to find suitable white dot");
    }
    MatOfPoint whiteDot = whiteDotPair.getLeft(); // assume white dot is the contour closest to the center of the image

    // debug test
    Rect rect = Imgproc.boundingRect(whiteDot);
    Core.circle(src, new Point(rect.x + rect.width / 2, rect.y + rect.width / 2), rect.width / 2, new Scalar(255, 0, 0), 2);
    Highgui.imwrite("test1.jpg", src);
    log.info("[WhiteDot Detection] Found whiteDot with distance: " + whiteDotPair.getRight() + " and area of: " + Imgproc.contourArea(whiteDot));
    return 0;
  }

  private static Comparator<Pair<MatOfPoint, Double>> contourCompare = new Comparator<Pair<MatOfPoint, Double>>() {
    public int compare(Pair<MatOfPoint, Double> p1, Pair<MatOfPoint, Double> p2) {
      return p1.getRight() < p2.getRight() ? -1 : 1;
    }
  };

  /**
   * {@link #getWhiteDot}
   * 
   * @return
   */
  public double getCrescent() {
    return 0;
  }
}
