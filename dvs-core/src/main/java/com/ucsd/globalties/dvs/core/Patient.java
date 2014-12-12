package com.ucsd.globalties.dvs.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ucsd.globalties.dvs.core.ui.MainController;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder
@Slf4j
public class Patient {
  private String name, birth, gender, ethnicity, language, roomNumber, school, 
  screeningComment, referral;
  
  @Setter @Getter
  private List<Photo> photos;
  
  @Getter
  private Map<EyeDisease, String> medicalRecord;
  
  public void diagnose() {
    for (Iterator<Photo> it = photos.iterator(); it.hasNext();) {
      Photo p = it.next();
      Eye left = p.getLeftEye();
      Eye right = p.getRightEye();
      if (left == null || right == null) {
        it.remove();
      } else if (left.getPupil() == null || right.getPupil() == null) {
        it.remove();
      } else if (left.getPupil().getWhiteDot() == null || right.getPupil().getWhiteDot() == null) {
        log.warn("No white dot detected for " + p.getType() + " "
            + "photo. Patient either has severe strabismus, or the algorithm failed.");
        it.remove();
      }
    }
    if (photos.isEmpty()) {
      log.warn("Skipping diagnosis for patient " + name + " because no adequate photos exist.");
      return;
    } else if (photos.size() < 2) {
      log.warn("A photo was removed from " + name + " because not enough eyes/pupils/whitedots were found.");
    }
    for (EyeDisease disease : EyeDisease.values()) {
      disease.getDetector().detect(this);
    }
    log.info("Done detecting. Medical record: " + medicalRecord.toString());
  }
  
  public Map<String,String> getPatientData() {
    Map<String,String> data = new HashMap<String,String>();
    String[] labels = MainController.sceneLabels;
    data.put(labels[0], name);
    data.put(labels[1], birth);
    data.put(labels[2], gender);
    data.put(labels[3], ethnicity);
    data.put(labels[4], language);
    data.put(labels[5], roomNumber);
    data.put(labels[6], school);
    data.put(labels[7], screeningComment);
    return data;
  }
}
