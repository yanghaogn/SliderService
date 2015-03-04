package com.xiaomi.infra.slider.service.common;

/**
 * Created by yang on 15-1-21.
 */
public interface RequestConfig {
  public static String URL_SUFFIX = "?proxyapproved=true";
  public static String URL_CHARSET = "UTF-8";
  public static String USER = "user";

  public static interface RequestType {
    public static String DEFAULT = "default";
  }
}
