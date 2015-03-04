package com.xiaomi.infra.slider.service.common;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class PojoMapper {

  //一旦初始化好后，就是线程安全的
  private static ObjectMapper mapper = new ObjectMapper();

  static {
    mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  public static <T> Object fromJson(String jsonAsString, Class<T> pojoClass)
      throws IOException {
    return mapper.readValue(jsonAsString, pojoClass);
  }

  public static <T> Object fromJson(File file, Class<T> pojoClass)
      throws IOException {
    return mapper.readValue(file, pojoClass);
  }

  public static void toJson(Object object, File file)
      throws IOException {
    mapper.writeValue(file, object);
  }

  public static String toJson(Object object)
      throws IOException {
    return mapper.writeValueAsString(object);
  }
}