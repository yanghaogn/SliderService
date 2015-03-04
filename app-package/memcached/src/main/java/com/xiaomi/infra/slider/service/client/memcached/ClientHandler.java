package com.xiaomi.infra.slider.service.client.memcached;


import com.xiaomi.infra.slider.service.client.SliderServiceClient;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by yang on 14-12-10.
 */
public class ClientHandler {
  private static Logger log = Logger.getLogger(ClientHandler.class);
  private volatile int retryTimes;
  private volatile int timeUnit;

  //默认的失败重连次数是3次，两次重连之间的间隔是2秒
  public ClientHandler() {
    this(3, 2000);
  }

  //重连次数为retryTimes，两次重连之间的时间间隔是timeUnit秒
  public ClientHandler(int retryTimes, int timeUnit) {
    this.retryTimes = retryTimes;
    this.timeUnit = timeUnit;
  }

  public List<String> getHostPortList(String user, String clusterName) throws IOException {
    List<String> hostPortList = new LinkedList<String>();
    int i = 0;
    boolean start = false;
    for (i = 0; i < retryTimes; i++) {
      try {
        List<InetSocketAddress> listAddress = new SliderServiceClient().getHostPortsForOneComponet(user, clusterName);
        if (listAddress != null && listAddress.size() > 0) {
          for (InetSocketAddress element : listAddress) {
            hostPortList.add(element.getHostName() + ":" + element.getPort());
          }
          start = true;
          break;
        }
        Thread.sleep(timeUnit);
      } catch (InterruptedException e) {
        log.error(e);
        Thread.currentThread().interrupt();
      } catch (IOException e) {
        log.error(e);
      } catch (Exception e) {
        log.error(e);
        e.printStackTrace();
      }
    }
    if (!start) {
      String error = "getHostPortList failed, maybe the memcached cluster(" + clusterName + ") has not started. Failed after trying " + i + " times,"
          + (i * timeUnit) + " milliseconds";
      log.error(error);
      throw new IOException(error);
    }
    return hostPortList;
  }
}
