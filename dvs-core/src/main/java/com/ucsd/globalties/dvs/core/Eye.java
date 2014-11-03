package com.ucsd.globalties.dvs.core;

import java.util.Random;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
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
  
  private Pupil findPupil() {
    Mat invertcolormatrix= new Mat(mat.rows(),mat.cols(), mat.type(), new Scalar(255,255,255));
    Core.subtract(invertcolormatrix, mat, mat);
    Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY);
    Imgproc.threshold(mat, mat, 180, 255, Imgproc.THRESH_BINARY);
    Random r = new Random();
    Highgui.imwrite("test-" + r.nextInt() + ".jpg", mat);
    Imgproc.GaussianBlur(mat, mat, new Size(9, 9), 2);
    Imgproc.dilate(mat, mat, new Mat(), new Point(-1, -1), 2);
    Mat circles = new Mat();
    Imgproc.Canny(mat, mat, 20, 20);
    Imgproc.HoughCircles(mat, circles, Imgproc.CV_HOUGH_GRADIENT, 10, 1);
    log.info("circles: " + circles + ", size: " + circles.size());
    for (int i = 0; i < circles.cols(); i++) {
      Point center = new Point();
      center.x = circles.get(0, i)[0];
      center.y = circles.get(0, i)[1];
      Core.circle(mat, center, (int) circles.get(0, i)[2], new Scalar(100,100,100), 1);
    }
    Highgui.imwrite("mat" + (new Random()).nextInt() + ".jpg", mat);
    return null;
  }
}
