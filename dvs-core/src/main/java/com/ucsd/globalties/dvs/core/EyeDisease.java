package com.ucsd.globalties.dvs.core;

import com.ucsd.globalties.dvs.core.detect.AnisometropiaDetector;
import com.ucsd.globalties.dvs.core.detect.AstigmatismDetector;
import com.ucsd.globalties.dvs.core.detect.CataractsDetector;
import com.ucsd.globalties.dvs.core.detect.DiseaseDetector;
import com.ucsd.globalties.dvs.core.detect.StrabismusDetector;

public enum EyeDisease {
  ASTIGMATISM(new AstigmatismDetector()),
  STRABISMUS(new StrabismusDetector()),
  CATARACTS(new CataractsDetector()),
  ANISOMETROPIA(new AnisometropiaDetector());
  
  private DiseaseDetector detector;
  
  private EyeDisease(DiseaseDetector detector) {
    this.detector = detector;
  }
  
  public DiseaseDetector getDetector() {
    return detector;
  }
}
