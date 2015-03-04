package com.xiaomi.infra.slider.server.provider;

import com.xiaomi.infra.slider.server.common.HttpRequest;
import com.xiaomi.infra.slider.server.information.Applications;
import com.xiaomi.infra.slider.server.yarn.YarnHandler;
import com.xiaomi.infra.slider.service.common.Request;
import com.xiaomi.infra.slider.service.common.RequestConfig;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;

import java.io.IOException;
import java.util.List;

/**
 * Created by yang on 15-1-16.
 */
public class RequestHandler implements Request.Iface {
  Logger log = Logger.getLogger(RequestHandler.class);

  @Override
  public String getJson(String type, String user, String clustername, String content) throws TException {
    log.info("type: " + type + ", user: " + user + ", clustername: " + clustername + ", content: " + content);
    if (RequestConfig.RequestType.DEFAULT.equals(type)) {
      return getDefaultResponse(user, clustername, content);
    }
    return "";
  }

  public String getDefaultResponse(String user, String clusterName, String content) {
    String result = "";
    List<Applications.Application> listApps = new YarnHandler().getLiveApplicationList(user, clusterName);
    if (listApps.size() == 0) {
      return result;
    }
    String url = listApps.get(0).getUrl();
    if (content != null && content.trim().length() > 0) {
      if (!content.endsWith(RequestConfig.URL_SUFFIX)) {
        content = content.trim() + RequestConfig.URL_SUFFIX;
      }
    }
    if (!url.endsWith("/") && content.startsWith("/")) {
      url += "/";
    }
    url += content;
    try {
      result = HttpRequest.getURLContent(url, RequestConfig.URL_CHARSET);
    } catch (IOException e) {
      log.error(e);
    }
    return result;
  }
}