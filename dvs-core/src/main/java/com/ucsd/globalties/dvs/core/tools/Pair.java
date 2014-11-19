package com.ucsd.globalties.dvs.core.tools;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class Pair<L, R> {
  @Getter @Setter
  private L left;
  
  @Getter @Setter
  private R right;
}
