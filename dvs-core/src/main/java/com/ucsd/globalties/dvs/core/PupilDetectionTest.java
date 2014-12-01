package com.ucsd.globalties.dvs.core;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import com.ucsd.globalties.dvs.core.Photo.PhotoType;
import com.ucsd.globalties.dvs.core.tools.Pair;

@Slf4j
public class PupilDetectionTest {
  private static final List<Pair<String,String>> TEST_PAIRS = Arrays.asList(new Pair<String,String>("Andrei_1.jpg","Andrei_2.jpg"),
                                                                  new Pair<String,String>("Sabit_1.jpg","Sabit_2.jpg"),
                                                                  new Pair<String,String>("Heather_1.jpg","Heather_2.jpg"),
                                                                  new Pair<String,String>("Rahul_1.jpg","Rahul_3.jpg"),
                                                                  new Pair<String,String>("Xiangcheng_1.jpg","Xiangcheng_2.jpg"),
                                                                  new Pair<String,String>("Shannon_1.jpg","Shannon_2.jpg"),
                                                                  new Pair<String,String>("Keith_1.jpg","Keith_2.jpg"),
                                                                  new Pair<String,String>("Teresa_1.jpg","Teresa_2.jpg"),
                                                                  new Pair<String,String>("Daniel_2.jpg","Daniel_3.jpg"),
                                                                  new Pair<String,String>("Gian_2.jpg","Gian_3.jpg"),
                                                                  new Pair<String,String>("Leon_2.jpg","Leon_3.jpg"));
  
  
  public static void main(String[] args) {
    Main.loadLibraryComponents();
    clean();
    run();
  }
  
  public static void clean() {
    Main.loadLibraryComponents();
    for (Iterator<Pair<String,String>> it = TEST_PAIRS.iterator(); it.hasNext();) {
      Pair<String, String> picPair = it.next();
      File fst = new File(PupilDetectionTest.class.getResource("/pics/" + picPair.getLeft()).getFile());
      if (!fst.exists()) {
        log.warn("pic file: " + fst.getAbsolutePath() + " does not exist");
        it.remove();
        continue;
      }
      File snd = new File(PupilDetectionTest.class.getResource("/pics/" + picPair.getRight()).getFile());
      if (!fst.exists()) {
        log.warn("pic file: " + snd.getAbsolutePath() + " does not exist");
        it.remove();
      }
    }
  }
  
  public static void run() {
    int found = 0;
    int total = 0;
    int[] results = new int[TEST_PAIRS.size()];
    for (int i = 0; i < TEST_PAIRS.size(); i++) {
      Pair<String, String> test = TEST_PAIRS.get(i);
      found = 0;
      try {
        File l = new File(PupilDetectionTest.class.getResource("/pics/" + test.getLeft()).getFile());
        File r = new File(PupilDetectionTest.class.getResource("/pics/" + test.getRight()).getFile());
        Photo pL = new Photo(l.getAbsolutePath(), PhotoType.VERTICAL);
        Photo pR = new Photo(r.getAbsolutePath(), PhotoType.HORIZONTAL);
        if (pL.getLeftEye().getPupil() != null) {
          found++;
        }
        if (pL.getRightEye().getPupil() != null) {
          found++;
        }
        if (pR.getLeftEye().getPupil() != null) {
          found++;
        }
        if (pR.getRightEye().getPupil() != null) {
          found++;
        }
        results[i] = found;
        total += found;
      }
      catch (Exception ex) {
        results[i] = -1;
        log.error(String.format("Exception raised for pair %s, %s",test.getLeft(),test.getRight(),ex.getStackTrace().toString()));
      }
    }
    for (int i = 0; i < results.length; i++) {
      log.info(String.format("Pupil test for pair %s, %s found %d/4 pupils",TEST_PAIRS.get(i).getLeft(),TEST_PAIRS.get(i).getRight(),results[i]));
    }
    log.info("Test complete. Double check all results by looking at the images.");
    log.info(String.format("Found: %d/%d",total, TEST_PAIRS.size()*4));
    if (total != TEST_PAIRS.size() * 4) {
      log.error("Expected: " + (TEST_PAIRS.size() * 4) + " images, found: " + total);
    }
  }
}
 