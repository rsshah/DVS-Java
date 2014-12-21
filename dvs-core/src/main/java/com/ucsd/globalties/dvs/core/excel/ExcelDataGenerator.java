package com.ucsd.globalties.dvs.core.excel;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ucsd.globalties.dvs.core.Main;
import com.ucsd.globalties.dvs.core.Patient;

/**
 * Basic excel export class that writes pateint information into an excel file
 * TODO make excel file prettier with formatting and colors
 * TODO add disease detection results to excel file
 * @author Sabit
 *
 */
@Slf4j
public class ExcelDataGenerator {
  
  public static void exportPatientData(List<Patient> patientList) {
    if (patientList == null) {
      log.info("nothing to export");
      return;
    }
    try {
      //Slashes and colons in file name will break writing to output
      SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-YYYY hm");
      FileOutputStream out = new FileOutputStream(Main.OUTPUT_FILE + "DVS Data " +  sdf.format(Calendar.getInstance().getTime()) + ".xlsx");
      Workbook wb = new XSSFWorkbook();
      Sheet s = wb.createSheet("Patient Data");
      int rowNum = 0, cellNum = 0;
      Row fieldRow = s.createRow(rowNum++);
      
      for(String field : Main.sceneLabels) {
        Cell c = fieldRow.createCell(cellNum++);
        c.setCellValue(field);
      }
      
      for (Patient p : patientList) {
        Row r = s.createRow(rowNum++);
        cellNum = 0;
        Map<String,String> patientData = p.getPatientData();
        for(String field : Main.sceneLabels) {
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
