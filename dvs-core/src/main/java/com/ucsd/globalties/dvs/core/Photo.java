package com.ucsd.globalties.dvs.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.highgui.Highgui;
import org.opencv.objdetect.CascadeClassifier;

import com.ucsd.globalties.dvs.core.tools.Pair;

@Slf4j
public class Photo {
  public enum PhotoType {
    HORIZONTAL, VERTICAL;
  }

  private PhotoType type;
  private String path;

  private Pair<Eye, Eye> eyes;

  public Photo(String path, PhotoType type) {
    this.path = path;
    if (!new File(path).exists()) {
    	throw new RuntimeException("Invalid file specified: " + path);
    }
    this.type = type;
  }

  public Eye getLeftEye() {
    if (eyes == null) {
      eyes = findEyes();
    }
    return eyes.getLeft();
  }

  public Eye getRightEye() {
    if (eyes == null) {
      eyes = findEyes();
    }
    return eyes.getRight();
  }
  
  private Rect findFaceRoi(Mat image) {
    CascadeClassifier faceDetector = new CascadeClassifier(Main.HAAR_FACE_PATH);
    MatOfRect faceDetections = new MatOfRect();
    faceDetector.detectMultiScale(image, faceDetections);

    log.info(String.format("Detected %s faces for img: %s", faceDetections.toArray().length, path));
    if (faceDetections == null || faceDetections.toArray().length == 0) {
      // go straight into eye detection on current image if no face is found
      return null;
    } else if (faceDetections.toArray().length > 1) {
      log.error("More than 1 face detected... YOLOing it with first detected Rect");
    }
    Rect detectedFace = faceDetections.toArray()[0];
    Rect faceBox = new Rect(detectedFace.x, detectedFace.y, detectedFace.width, (detectedFace.height * 2) / 3);
    //Highgui.imwrite("face_out_" + type + ".jpg", new Mat(image, faceBox));
    return faceBox;
  }

  public Pair<Eye, Eye> findEyes() {
    Mat image = Highgui.imread(path);
    // find face
    Rect faceBox = findFaceRoi(image);
    // Detect eyes from cropped face image
    CascadeClassifier eyeDetector = new CascadeClassifier(Main.HAAR_EYE_PATH);
    MatOfRect eyeDetections = new MatOfRect();
    Mat faceImage = faceBox != null ? new Mat(image, faceBox) : image;
    eyeDetector.detectMultiScale(faceImage, eyeDetections);

    List<Rect> detectedEyes = eyeDetections.toList();
    log.info(String.format("Detected %s eyes for img: %s", detectedEyes.size(), path));
    List<Rect> eyes = new ArrayList<>(2);
    if (detectedEyes.size() > 2) { // found an extra eye or two
      detectedEyes.sort(new EyeAreaCompare());
      // we can safely get the last 2 biggest ones, because after the crop the eyes take up the most space
      eyes.add(detectedEyes.get(detectedEyes.size() - 1));
      eyes.add(detectedEyes.get(detectedEyes.size() - 2));
      // TODO maybe add some more criteria here and have criterion weights for more accurate behavior
    } else {
      eyes.addAll(eyeDetections.toList());
    }
    eyes.sort(new EyeXCompare()); // simple sort to know which eye is left and which is right
    Mat leftEyeMat = new Mat(faceImage, eyes.get(0));
    Highgui.imwrite("left_eye_" + type + ".jpg", leftEyeMat);
    Mat rightEyeMat = new Mat(faceImage, eyes.get(1));
    log.info("created left eye mat: " + leftEyeMat);
    Highgui.imwrite("right_eye_" + type + ".jpg", rightEyeMat);
    log.info("created right eye mat: " + rightEyeMat);
    return new Pair<Eye, Eye>(new Eye(leftEyeMat), new Eye(rightEyeMat));
  }

  private static class EyeAreaCompare implements Comparator<Rect> {
    public int compare(Rect r1, Rect r2) {
      int r1Area = r1.height * r1.width;
      int r2Area = r2.height * r2.width;
      return r1Area < r2Area ? -1 : r1Area == r2Area ? 0 : 1;
    }
  }

  private static class EyeXCompare implements Comparator<Rect> {
    public int compare(Rect r1, Rect r2) {
      return r1.x < r2.x ? -1 : r1.x == r2.x ? 0 : 1;
    }
  }
}
