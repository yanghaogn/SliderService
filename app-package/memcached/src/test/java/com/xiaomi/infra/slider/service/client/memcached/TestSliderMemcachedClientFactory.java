package com.xiaomi.infra.slider.service.client.memcached;

import com.xiaomi.infra.slider.service.client.common.SliderServiceClientConfigKey;
import com.xiaomi.infra.slider.service.client.memcached.client.SliderMemcachedClient;
import com.xiaomi.infra.slider.service.common.RequestConfig;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by yang on 15-1-16.
 */
public class TestSliderMemcachedClientFactory {
  @Test
  public void testCreateSliderMemcachedClient() {
    try {
      System.setProperty(SliderServiceClientConfigKey.SERVICE_ADDRESS, "lg-hadoop-build01.bj:20050");
      System.setProperty(RequestConfig.USER, "h_yanghao3");
      String clusterName = "memcached1";
      SliderMemcachedClient client = SliderMemcachedClientFactory.createSliderMemcachedClient(clusterName);
      for (int i = 0; i < 500; i++) {
        client.set("yang", 1000, new Double(Math.random()).toString().getBytes());
        Thread.sleep(200);
        byte[] value = (byte[]) client.get("yang");
        System.out.println(i + " : " + (value == null ? null : new String(value)));
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
