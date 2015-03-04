package com.xiaomi.infra.slider.service.client.memcached;

import com.xiaomi.infra.slider.service.client.memcached.client.SliderMemcachedClient;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SliderMemcachedClientFactory {
  private static Logger log = Logger.getLogger(SliderMemcachedClientFactory.class);
  private static Map<String, SliderMemcachedClient> map = new HashMap<String, SliderMemcachedClient>();

  /**
   * 创建MdhMemcachedClient，每次创建的client仅调用者可以使用
   * @param clusterName
   * @return
   * @throws IOException
   */
  public static SliderMemcachedClient createSliderMemcachedClient(String clusterName) throws IOException {
    try {
      SliderMemcachedClient sliderMemcachedClient = new SliderMemcachedClient(clusterName);
      return sliderMemcachedClient;
    } catch (IOException e) {
      String error = "cannot establish connection with memcached cluster.";
      log.error(error, e);
      throw new IOException(error, e);
    }
  }

  /**
   * createMiMemcachedClient和这个方法是独立的，即前者创建的MiMemcachedClient，对这个方法不可见
   * 创建的MdhMemcached，以后还可以继续使用
   */
  
  public static SliderMemcachedClient getSliderMemcachedClient(String clusterName) throws Exception {
    SliderMemcachedClient sliderMemcachedClient = null;
    if (!map.containsKey(clusterName)) {
      synchronized (SliderMemcachedClient.class) {
        if (!map.containsKey(clusterName)) {
          sliderMemcachedClient = createSliderMemcachedClient(clusterName);
          map.put(clusterName, sliderMemcachedClient);
        }
      }
    }
    return sliderMemcachedClient;
  }
}
