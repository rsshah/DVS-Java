package com.ucsd.globalties.dvs.core;

import java.util.ArrayList;
import java.util.Comparator;
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

import com.ucsd.globalties.dvs.core.tools.Pair;

/**
 * Pupil class represents a detected pupil.
 * It has a white dot and a crescent, which are used for disease detection algorithms.
 * @author Rahul
 *
 */
@Slf4j
public class Pupil {
  
  @Getter
  private Eye eye; // the Eye from which this Pupil was derived
  @Getter
  private Mat mat;
  
  private WhiteDot whiteDot;
  
  public Pupil(Eye eye, Mat mat) {
    this.eye = eye;
    this.mat = mat;
  }

  /**
   * Detect the white dot here. The idea is to return a double value (or maybe a
   * simple object that describes the white dot's size/position/necessary
   * information to detect diseases. I don't think we really need to crop it out
   * of the image; the positional information will probably suffice.
   * 
   * @return a WhiteDot object identifying the white dot's positional information relative to the pupil
   */
  public WhiteDot getWhiteDot() {
    if (whiteDot != null) {
      return whiteDot;
    }
    //random code so that debug output will not override each other
    int code = (new Random()).nextInt();
    Mat src = new Mat();
    mat.copyTo(src);
    Mat gray = new Mat();
    Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
    //Highgui.imwrite("gray-test.jpg", gray);
    // threshold the image for white values
    Double thresh = Imgproc.threshold(gray, gray, 240, 255, Imgproc.THRESH_BINARY);
    //Highgui.imwrite("thresh-test.jpg", gray);
    List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
    Imgproc.findContours(gray.clone(), contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
    Imgproc.drawContours(gray, contours, -1, new Scalar(255, 255, 255), -1);
    if (contours.isEmpty()) {
      log.error("No contours found for this pupil.");
      return null;
    }
    java.awt.Point pupilCenter = new java.awt.Point(mat.width() / 2, mat.height() / 2);
    List<Pair<MatOfPoint, Double>> contourDistances = new ArrayList<>(contours.size());
    // Populate a List of Pairs, where pair.getLeft() is the contour and pair.getRight() is the contour's
    // distance from the center of the pupil
    for (int i = 0; i < contours.size(); i++) {
      Rect rect = Imgproc.boundingRect(contours.get(i));
      Core.circle(src, new Point(rect.x + rect.width / 2, rect.y + rect.width / 2), rect.width / 2, new Scalar(255, 0, 0), 1);
      int radius = rect.width / 2;
      java.awt.Point center = new java.awt.Point(rect.x + radius, rect.y + radius);
      contourDistances.add(new Pair<>(contours.get(i), pupilCenter.distanceSq(center)));
    }
    // sort the contours based on the distance from the center of the pupil (ascending)
    contourDistances.sort(contourCompare);
    Pair<MatOfPoint, Double> whiteDotPair = null;
    
    // Find the closest contour that matches certain criteria (currently checks for size)
    for (Pair<MatOfPoint, Double> pair : contourDistances) {
      double area = Imgproc.contourArea(pair.getLeft());
      log.info("whiteDot distance: {}, area: {}", pair.getRight(), area);
      
      if (area < 10 || area > 200.0) { // basic bounds checking, may need some tuning
        continue;
      }
      whiteDotPair = pair;
      log.info("selected pair with area: " + area);
      break;
    }
    if (whiteDotPair == null) {
      log.error("[WhiteDot Detection] Unable to find suitable white dot");
      return null;
    }
    MatOfPoint whiteDotContour = whiteDotPair.getLeft(); // assume white dot is the contour closest to the center of the image

    // debug test
    Rect rect = Imgproc.boundingRect(whiteDotContour);
    //Highgui.imwrite("test" + code + ".jpg", src);
    double wdarea = Math.PI * Math.pow(rect.width / 2, 2);
    //need: distance from white dot center to pupil center,
    //      angle between white dot center and pupil center
    int radius = rect.width / 2;
    java.awt.Point whiteDotCenter = new java.awt.Point(rect.x + radius, rect.y + radius);
    double distance = pupilCenter.distance(whiteDotCenter);
    double xDist = whiteDotCenter.x - pupilCenter.x;
    if (xDist > distance) {
      log.error("[WhiteDot Detection] unfulfilled invariant: adjacent edge of triangle is bigger than hypotenuse");
      return null;
    }
    log.info("[WhiteDot Detection] Computing angle for xDist: {}, dist: {}", xDist, distance);
    double angle = Math.acos(xDist / distance);
    
    log.info("[WhiteDot Detection] computed white dot with distance: {}, angle: {}, area: {}", distance, Math.toDegrees(angle), wdarea);
    this.whiteDot = new WhiteDot(distance, wdarea, angle);
    return whiteDot;
  }

  private static Comparator<Pair<MatOfPoint, Double>> contourCompare = new Comparator<Pair<MatOfPoint, Double>>() {
    public int compare(Pair<MatOfPoint, Double> p1, Pair<MatOfPoint, Double> p2) {
      return p1.getRight() < p2.getRight() ? -1 : 1;
    }
  };

  /**
   * Return crescent information.
   * TODO when better pictures are taken
   * @return
   */
  public double getCrescent() {
    return 0;
  }
  
  /**
   * Return the area of the DETECTED pupil.
   * This is NOT the area of the pupil itself, but usually of the iris.
   * This method does not return the exact value because we divide an int by 2,
   * but we need the area as a relative measure for the white dot, so the exact
   * value is not necessary as long as the white dot area is computed in the same inexact manner.
   * @return an approximation of the area of the Mat identifying this pupil.
   * Margin of error: [0, 0.5) (between 0 (inclusive) and 0.5 (exclusive))
   */
  public double getArea() {
    double radius = mat.width() / 2;
    return Math.PI * Math.pow(radius, 2);
  }
}
