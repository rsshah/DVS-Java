package com.ucsd.globalties.dvs.core.tools;

import lombok.Getter;
import lombok.Setter;

public class Pair<L, R> {
  @Getter @Setter
  private L left;
  
  @Getter @Setter
  private R right;
  
  public Pair(L left, R right) {
    this.left = left;
    this.right = right;
  }
}
