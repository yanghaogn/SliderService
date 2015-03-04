package com.xiaomi.infra.slider.server.common;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by yang on 14-12-10.
 */
public class HttpRequest {
  static Logger log = Logger.getLogger(HttpRequest.class);

  public static String getURLContent(String url, String charset) throws IOException {
    log.info("get response from " + url);
    HttpClient client = new HttpClient();
    GetMethod method = new GetMethod(url);
    method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
        new DefaultHttpMethodRetryHandler(3, false));
    int statusCode = client.executeMethod(method);
    if (statusCode != HttpStatus.SC_OK) {
      String error = "http request with" + url + ",status code isnot HttpStatus.SC_OK(" + HttpStatus.SC_OK + ")";
      log.error(error);
      method.releaseConnection();
      throw new IOException(error);
    }
    statusCode = client.executeMethod(method);
    if (statusCode != HttpStatus.SC_OK) {
      String error = "http request with" + url + ", status code isnot HttpStatus.SC_OK(" + HttpStatus.SC_OK + ")";
      log.error(error);
      method.releaseConnection();
      throw new IOException(error);
    }
    byte[] response = method.getResponseBody();
    method.releaseConnection();
    return response == null ? "" : new String(response, Charset.forName(charset));
  }

  public static boolean isReachable(String url, int retryTimes) {
    boolean isReachable = false;
    for (int i = 0; i < retryTimes && !isReachable; i++) {
      HttpClient client = new HttpClient();
      GetMethod method = new GetMethod(url);
      try {
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
            new DefaultHttpMethodRetryHandler(3, false));
        int statusCode = client.executeMethod(method);
        if (statusCode != HttpStatus.SC_OK) {
          log.error("http request with" + url + ", status code isnot HttpStatus.SC_OK(" + HttpStatus.SC_OK + ")");
          continue;
        }
        isReachable = true;
      } catch (HttpException e) {
        log.error("attempt to connect to " + url, e);
      } catch (IOException e) {
        log.error("attempt to connect to " + url, e);
      } finally {
        method.releaseConnection();
      }
    }
    return isReachable;
  }
}
