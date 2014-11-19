package com.ucsd.globalties.dvs.core;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Test;

import com.ucsd.globalties.dvs.core.Photo.PhotoType;

@Slf4j
public class PupilDetectionTest {
  private static final List<String> TEST_IMAGES = Arrays.asList("Andrei.JPG", "Daniel.JPG", "jt_h.jpg", "jt_v.jpg");
  
  
  @Before
  public void clean() {
    for (Iterator<String> it = TEST_IMAGES.iterator(); it.hasNext();) {
      String picName = it.next();
      File pic = new File(getClass().getResource("/pics/" + picName).getFile());
      if (!pic.exists()) {
        log.warn("pic file: " + pic.getAbsolutePath() + " does not exist");
        it.remove();
      }
    }
  }
  
  @Test
  public void run() {
    for (String test : TEST_IMAGES) {
      File f = new File(Main.class.getResource("/pics/" + test).getFile());
      Photo p = new Photo(f.getAbsolutePath(), PhotoType.VERTICAL);
      // touch white dot to invoke detection cascade from face to eye to pupil to white dot
      p.getLeftEye().getPupil().getWhiteDot();
      p.getRightEye().getPupil().getWhiteDot();
    }
  }
}
