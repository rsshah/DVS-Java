package com.ucsd.globalties.dvs.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    data.put("Name", name);
    data.put("Date of Birth", birth);
    data.put("Gender", gender);
    data.put("Ethnicity", ethnicity);
    data.put("Language", language);
    data.put("Room Number", roomNumber);
    data.put("School", school);
    data.put("Screening Comment", screeningComment);
    return data;
  }
}
