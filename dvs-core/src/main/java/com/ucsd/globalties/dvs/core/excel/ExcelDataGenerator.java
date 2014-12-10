package com.ucsd.globalties.dvs.core.excel;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ucsd.globalties.dvs.core.Patient;
import com.ucsd.globalties.dvs.core.ui.MainController;
@Slf4j
public class ExcelDataGenerator {
  
  public static void exportPatientData(List<Patient> patientList) {
    if (patientList == null) {
      return;
    }
    try {
      SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/YYYY");
      sdf.format(Calendar.getInstance().getTime());
      FileOutputStream out = new FileOutputStream("DVS Date " +  sdf.toString() + ".xlsx");
      Workbook wb = new XSSFWorkbook();
      Sheet s = wb.createSheet("Patient Data");
      int rowNum = 0, cellNum = 0;
      Row fieldRow = s.createRow(rowNum++);
      
      for(String field : MainController.sceneLabels) {
        Cell c = fieldRow.createCell(cellNum++);
        c.setCellValue(field);
      }
      
      for (Patient p : patientList) {
        Row r = s.createRow(rowNum++);
        cellNum = 0;
        Map<String,String> patientData = p.getPatientData();
        for(String field : MainController.sceneLabels) {
          Cell c = r.createCell(cellNum++);
          c.setCellValue(patientData.get(field));
        }
      }
      wb.write(out);
      out.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
