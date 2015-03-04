package com.xiaomi.infra.slider.service.client;


import com.xiaomi.infra.slider.service.client.common.SliderServiceClientConfigKey;
import com.xiaomi.infra.slider.service.common.Request;
import com.xiaomi.infra.slider.service.common.RequestConfig;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by yang on 15-1-15.
 */
public class SliderServiceClientHandler {
  private static Logger log = Logger.getLogger(SliderServiceClientHandler.class);
  private TTransport transport = null;
  private Request.Client client = null;
  private static ArrayList<InetSocketAddress> serviceAddressList = new ArrayList<InetSocketAddress>(5);

  static {
    String addresses = System.getProperty(SliderServiceClientConfigKey.SERVICE_ADDRESS);
    if (addresses == null || addresses.trim().length() == 0) {
      log.error(SliderServiceClientConfigKey.SERVICE_ADDRESS + " not setted");
    } else {
      for (String address : addresses.split(",")) {
        String host = address.split(":")[0];
        int port = Integer.parseInt(address.split(":")[1]);
        serviceAddressList.add(new InetSocketAddress(host, port));
      }
    }
  }

  public String getResponse(String type, String user, String clusterName, String content) throws IOException, TException {
    if (transport != null) {
      transport.close();
    }
    if (serviceAddressList.size() == 0) {
      String error = SliderServiceClientConfigKey.SERVICE_ADDRESS + " not setted";
      log.error(error);
      throw new IOException(error);
    }
    //随机
    int randIndex = new Random().nextInt(serviceAddressList.size());
    refreshRequestClient(serviceAddressList.get(randIndex));
    for (int i = 1; i < serviceAddressList.size() && !isReachable(); i++) {
      int index = (i + randIndex) % serviceAddressList.size();
      if (index == randIndex) {
        continue;
      }
      refreshRequestClient(serviceAddressList.get(index));
    }
    if (!isReachable()) {
      String error = "cannot connect to any address in " + serviceAddressList.toString();
      log.error(error);
      throw new IOException(error);
    }
    String result = null;
    try {
      result = client.getJson(type, user, clusterName, content);
    } catch (TException e) {
      log.error(e);
      throw e;
    } finally {
      if (transport != null) {
        transport.close();
      }
    }
    return result;
  }

  public void refreshRequestClient(InetSocketAddress address) {
    try {
      transport = new TSocket(address.getHostName(), address.getPort());
      transport.open();
      TProtocol protocol = new TBinaryProtocol(transport);
      client = new Request.Client(protocol);
    } catch (TTransportException e) {
      e.printStackTrace();
      log.error(e);
    }
  }

  public boolean isReachable() {
    return transport != null && transport.isOpen();
  }
}
