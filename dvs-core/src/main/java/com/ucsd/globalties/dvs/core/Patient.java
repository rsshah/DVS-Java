package com.ucsd.globalties.dvs.core;

import java.util.EnumMap;
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
  
  @Setter
  private List<Photo> photos;
  
  @Getter
  private Map<EyeDisease, String> medicalRecord = new EnumMap<>(EyeDisease.class);
  
  public void diagnose() {
    // test for all conditions
    // can parallelize this if necessary (probably not though)
    // can also parallelize patients
    for (EyeDisease disease : EyeDisease.values()) {
      disease.getDetector().detect(this);
    }
    log.info("Done detecting. Medical record: " + medicalRecord.toString());
  }
}
