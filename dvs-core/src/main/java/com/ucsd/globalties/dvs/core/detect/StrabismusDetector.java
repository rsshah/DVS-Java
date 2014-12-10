package com.ucsd.globalties.dvs.core.detect;

import com.ucsd.globalties.dvs.core.EyeDisease;
import com.ucsd.globalties.dvs.core.Patient;
import com.ucsd.globalties.dvs.core.Photo;
import com.ucsd.globalties.dvs.core.WhiteDot;

public class StrabismusDetector implements DiseaseDetector {
  private static final double DISTANCE_THRESHOLD = 10;
  private static final double ANGLE_THRESHOLD = Math.PI / 2d;
  
  public void detect(Patient p) {
    StringBuilder msg = new StringBuilder();
    for (Photo photo : p.getPhotos()) {
      msg.append("\nStats for " + photo.getType().toString().toLowerCase() + " photo:\n");
      boolean distWarning = false, angleWarning = false;
      WhiteDot leftDot = photo.getLeftEye().getPupil().getWhiteDot();
      WhiteDot rightDot = photo.getRightEye().getPupil().getWhiteDot();
      // Compare difference between the distances of the two white dots
      double distDiff = Math.abs(leftDot.getDistance() - rightDot.getDistance());
      if (distDiff > DISTANCE_THRESHOLD) {
        msg.append(String.format("\tDistance of %.2f detected when allowed limit is %.2f\n",distDiff, DISTANCE_THRESHOLD));
        distWarning = true;
      }
      double angleDiff = Math.abs(leftDot.getAngle() - rightDot.getAngle());
      if (angleDiff > ANGLE_THRESHOLD) {
        msg.append(String.format("\tAngle of %.2f detected when allowed limit is %.2f\n", angleDiff, ANGLE_THRESHOLD));
        angleWarning = true;
      }
      if (distWarning && angleWarning) {
        msg.append("Patient has Strabismus");
      } else if (distWarning || angleWarning) {
        msg.append("Patient may have Strabismus");
      } else {
        msg.append("Healthy");
      }
    }
    p.getMedicalRecord().put(EyeDisease.STRABISMUS, msg.toString());
  }
}
