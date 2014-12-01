package com.ucsd.globalties.dvs.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Setter;

import com.ucsd.globalties.dvs.core.Photo.PhotoType;
import com.ucsd.globalties.dvs.core.excel.ExcelDataGenerator;


public class Controller {
	
  @Setter
  private Patient patient = null;
  private List<Patient> sessionPatients = null;
  
  public void setPatientPhotos(String hFilePath, String vFilePath) {
    List<Photo> photos = new ArrayList<Photo>();
    photos.add(new Photo(hFilePath, PhotoType.HORIZONTAL));
    photos.add(new Photo(vFilePath, PhotoType.VERTICAL));
    patient.setPhotos(photos);
  }
  
  public void finalizePatient() {
    if (sessionPatients == null) {
      sessionPatients = new ArrayList<Patient>();
    }
    sessionPatients.add(patient);
    patient = null;
  }
  
  public void diagnose() {
    patient.diagnose();
  }
  
  public Map<EyeDisease, String> getRecords() {
    return patient.getMedicalRecord();
  }

  public void exportData() {
    ExcelDataGenerator.exportPatientData(sessionPatients);
  }

}
