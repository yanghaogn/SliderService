package com.xiaomi.infra.slider.service.client;

import com.xiaomi.infra.slider.service.client.information.Servers;
import com.xiaomi.infra.slider.service.common.PojoMapper;
import com.xiaomi.infra.slider.service.common.RequestConfig;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by yang on 15-1-15.
 */
public class SliderServiceClient {
  Logger log = Logger.getLogger(SliderServiceClient.class);

  public boolean isRunning(String user, String clusterName) throws Exception {
    boolean result;
    String response = getResponse(RequestConfig.RequestType.DEFAULT, user, clusterName, "");
    result = (response != null) && (response.trim().length() > 0);
    return result;
  }

  /**
   * cluster启动之后，需要等待一段时间，才能获取正确的信息<br/>
   * 如果app只有一个component，则调用该方法<br/>
   *
   * @param user
   * @param clusterName
   * @return
   */
  public List<InetSocketAddress> getHostPortsForOneComponet(String user, String clusterName) throws Exception {
    Map<String, List<InetSocketAddress>> result = getHostPorts(user, clusterName);
    for (List<InetSocketAddress> key : result.values()) {
      return key;
    }
    return new LinkedList<InetSocketAddress>();
  }

  /**
   * 1. 返回map:<br/>
   * key: component name<br/>
   * value: host:port列表<br/>
   * 2. cluster启动后，需要等待一段时间，才能获取到正确的信息<br/>
   * 3. 你需要将host:port输出到ws/v1/slider/publisher/exports/servers才能使用，可参考"http://wiki.mioffice.cn/xmg/Metainfo.xml#"对metainfo.xml进行配置
   */
  public Map<String, List<InetSocketAddress>> getHostPorts(String user, String clusterName) throws IOException, TException {
    Map<String, List<InetSocketAddress>> result = new HashMap<String, List<InetSocketAddress>>();
    String appendContent = "ws/v1/slider/publisher/exports/servers";
    String response = getResponse(RequestConfig.RequestType.DEFAULT, user, clusterName, appendContent);
    Servers servers = (Servers) PojoMapper.fromJson(response, Servers.class);
    for (String componentName : servers.getEntries().keySet()) {
      List<InetSocketAddress> listAddress = new LinkedList<InetSocketAddress>();
      for (Servers.HOST_PORT hostport : servers.getEntries().get(componentName)) {
        String host = hostport.getValue().split(":")[0];
        int port = Integer.parseInt(hostport.getValue().split(":")[1]);
        InetSocketAddress address = new InetSocketAddress(host, port);
        listAddress.add(address);
      }
      result.put(componentName, listAddress);
    }
    
    return result;
  }

  public String getResponse(String type, String user, String clusterName, String content) throws IOException, TException {
    if (type == null || type.trim().length() == 0) {
      String error = "type为空, type=" + type;
      log.error(error);
      throw new NullPointerException(error);
    }
    if (user == null || user.trim().length() == 0) {
      String error = "user为空, user=" + user;
      log.error(error);
      throw new NullPointerException(error);
    }
    if (clusterName == null || clusterName.trim().length() == 0) {
      String error = "clusterName为空, clusterName=" + clusterName;
      log.error(error);
      throw new NullPointerException(error);
    }
    if (content != null && content.trim().length() > 0) {
      if (!content.endsWith(RequestConfig.URL_SUFFIX)) {
        content = content.trim() + RequestConfig.URL_SUFFIX;
      }
    }
    return new SliderServiceClientHandler().getResponse(type, user, clusterName, content);
  }
}
