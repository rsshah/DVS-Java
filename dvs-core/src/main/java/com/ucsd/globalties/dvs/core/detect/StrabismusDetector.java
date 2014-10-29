package com.ucsd.globalties.dvs.core.detect;

import com.ucsd.globalties.dvs.core.EyeDisease;
import com.ucsd.globalties.dvs.core.Patient;

public class StrabismusDetector implements DiseaseDetector {
  public void detect(Patient p) {
    p.getMedicalRecord().put(EyeDisease.STRABISMUS, "Healthy");
  }
}
