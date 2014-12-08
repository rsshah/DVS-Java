package com.ucsd.globalties.dvs.core;

import java.util.Random;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
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
  
  /*
   * Finds pupil in eye
   * 
   * Basically uses the same algorithm from 
   * http://docs.opencv.org/trunk/modules/imgproc/doc/feature_detection.html?highlight=cvhoughcircles#houghcircles
   * with small value tweaks.
   */
  private Pupil findPupil() {
    int code = Math.abs((new Random()).nextInt());
    Mat src = new Mat();
    Mat gray = new Mat();
    Mat circles = new Mat();
    //make copy of eye mat so we do not modify it
    mat.copyTo(src);
    //convert to grayscale since HoughCircles, and other algorithms, require this
    Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
    //smooth out image to prevent false circles
    Imgproc.GaussianBlur(gray, gray, new Size(9,9), 2.0, 2.0);
    //Finds circles
    //See http://docs.opencv.org/trunk/modules/imgproc/doc/feature_detection.html?highlight=cvhoughcircles#houghcircles
    //for parameter information
    Imgproc.HoughCircles(gray, circles, Imgproc.CV_HOUGH_GRADIENT, 2.0, (gray.height()/4.0), 150.0, 20.0, (gray.height()/8), (gray.height()/2));
    //if none found return null
    if (circles.total() == 0) {
      log.info("Pupil not found.");
      return null;
    }
    
    //iterate over all circles and output circles/rects for debugging
    /*for(int i = 0; i < circles.total(); i++ )
    {
      //circle info is stored as [x,y,radius] with [x,y] forming the center of the circle
      //double[] circle = circles.get(0, i); 
      //log.info("x: " + circle[0] + " y: " + circle[1] + " r: " + circle[2]);
      //Core.rectangle(src, new Point(circle[0],circle[1]), new Point(circle[0]+2*circle[2],circle[1]+2*circle[2]), new Scalar(0,255,0),2);
      //Core.circle(src, new Point(circle[0], circle[1]), (int) circle[2], new Scalar(255,0,0),2);
    }*/
    //Highgui.imwrite(""+code+"_pupil_src.jpg", src);
    
    //pick the most center circle as Pupil. HoughCircles returns circles in decreasing order from center
    //if you suspect this is not the case, just sort the Mat using pythagorean theorem to get the distance from center of circle to center of eye image.
    double[] finalPupil = circles.get(0, 0);
    double area = Math.PI * Math.pow(finalPupil[2], 2);
    
    /*Mat fsrc = new Mat();
    mat.copyTo(fsrc);    
    Core.rectangle(fsrc, new Point(finalPupil[0],finalPupil[1]), new Point(finalPupil[0]+2*finalPupil[2],finalPupil[1]+2*finalPupil[2]), new Scalar(0,255,0),2);
    Core.circle(fsrc, new Point(finalPupil[0], finalPupil[1]), (int) finalPupil[2], new Scalar(255,0,0),2);*/
    
    log.info("Pupil found: x: " + finalPupil[0] + " y: " + finalPupil[1] + " r: " + finalPupil[2]);
    //Crop eye mat and create pupil mat
    Mat pupilMat = new Mat(src, new Rect(new Point(finalPupil[0]-finalPupil[2],finalPupil[1]-finalPupil[2]), new Point(finalPupil[0]+finalPupil[2],finalPupil[1]+finalPupil[2])));
    return new Pupil(pupilMat, area);
  }
}
