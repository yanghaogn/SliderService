package com.xiaomi.infra.slider.service.common;

import org.apache.log4j.Logger;

import java.io.*;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by yang on 15-1-16.
 */
public class PublicMethod {
  static Logger log = Logger.getLogger(PublicMethod.class);

  public static List<String> execCommand(String command, boolean wait) {
    Runtime run = Runtime.getRuntime();//返回与当前 Java 应用程序相关的运行时对
    Process process;
    List<String> response = new LinkedList<String>();
    try {
      process = run.exec(new String[]{"/bin/sh", "-c", command});
      response = loadStream(process.getInputStream());
      if (wait) {
        process.waitFor();
      }
    } catch (IOException e) {
      log.error(e);
    } catch (InterruptedException e) {
      log.error(e);
      Thread.currentThread().interrupt();
    }
    return response;
  }

  static List<String> loadStream(InputStream input) throws IOException {
    InputStreamReader inputStreamReader = new InputStreamReader(input, Charset.defaultCharset());
    BufferedReader in = new BufferedReader(inputStreamReader);
    List<String> result = new LinkedList<String>();
    String line;
    while ((line = in.readLine()) != null) {
      result.add(line);
    }
    return result;
  }

  public static boolean deleteFile(File file) {
    if (file.exists() && file.isFile()) {
      return file.delete();
    }
    return false;
  }

  //规格化url，原理同百度搜索请求时的关键词转换
  public static String regulateUrlContent(String content) {
    content = content.replaceAll("/", "%2F");
    content = content.replaceAll("\\?", "%3F");
    content = content.replaceAll("=", "%3D");
    return content;
  }
}
