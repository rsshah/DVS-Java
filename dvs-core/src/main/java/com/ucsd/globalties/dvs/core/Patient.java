package com.ucsd.globalties.dvs.core;

import java.util.HashMap;
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
    // test for all conditions
    // can parallelize this if necessary (probably not though)
    // can also parallelize patients
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
