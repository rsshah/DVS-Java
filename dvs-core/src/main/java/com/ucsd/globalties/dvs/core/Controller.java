package com.ucsd.globalties.dvs.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Setter;

import com.ucsd.globalties.dvs.core.Photo.PhotoType;


public class Controller {
	
  @Setter
  private Patient patient;
  
  public void setPatientPhotos(String hFilePath, String vFilePath) {
    List<Photo> photos = new ArrayList<Photo>();
    photos.add(new Photo(hFilePath, PhotoType.HORIZONTAL));
    photos.add(new Photo(vFilePath, PhotoType.VERTICAL));
    patient.setPhotos(photos);
  }
  
  public void diagnose() {
    patient.diagnose();
  }
  
  public Map<EyeDisease, String> getRecords() {
    return patient.getMedicalRecord();
  }

}
