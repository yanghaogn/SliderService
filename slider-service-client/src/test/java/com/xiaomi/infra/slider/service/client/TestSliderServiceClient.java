package com.xiaomi.infra.slider.service.client;

import org.junit.Assert;
import org.junit.Test;
import com.xiaomi.infra.slider.service.client.common.SliderServiceClientConfigKey;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by yang on 15-1-15.
 */
public class TestSliderServiceClient {
  @Test
  public void testIsRunning() {
    System.setProperty(SliderServiceClientConfigKey.SERVICE_ADDRESS, "lg-hadoop-build01.bj:20050");
    String user = "h_yanghao3";
    String clustername = "hbase1";
    try {
      boolean isRunning = new SliderServiceClient().isRunning(user, clustername);
      System.out.println(isRunning);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testPressure() {
    int numThread = 2000;
    System.setProperty(SliderServiceClientConfigKey.SERVICE_ADDRESS, "lg-hadoop-build01.bj:20050");
    final CountDownLatch latch = new CountDownLatch(numThread);
    final CyclicBarrier barrier = new CyclicBarrier(numThread);
    final AtomicLong startTime = new AtomicLong(System.currentTimeMillis());
    for (int i = 0; i < numThread; i++) {
      final int time = i;
      new Thread() {
        @Override
        public void run() {
          try {
            String user = "h_yanghao3";
            String clustername = "hbase1";
            barrier.await();
            boolean isRunning = new SliderServiceClient().isRunning(user, clustername);
            System.out.println(time + ":" + isRunning);
           // Assert.assertTrue(isRunning);
          } catch (Exception e) {
            Assert.assertNull( time + ":" + e, e);
          }finally {
            latch.countDown();
          }
        }
      }.start();
    }
    try {
      latch.await();
      final long endTime =System.currentTimeMillis();
      System.out.println("线程数：" + numThread + ", 总的运行时间: " + (endTime - startTime.get()) + "ms");
    } catch (InterruptedException e) {
      e.printStackTrace();
      Thread.currentThread().interrupt();
    }
  }
}
