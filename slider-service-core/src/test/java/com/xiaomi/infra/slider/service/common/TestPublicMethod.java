package com.xiaomi.infra.slider.service.common;

import org.junit.Test;

/**
 * Created by yang on 15-1-23.
 */
public class TestPublicMethod {
  @Test
  public void testExecCommand(){
    String command = "ls -a .";
    for(String element: PublicMethod.execCommand(command, true)){
      System.out.println(element);
    }
  }
}
