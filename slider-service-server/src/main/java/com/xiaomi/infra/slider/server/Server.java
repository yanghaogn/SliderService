package com.xiaomi.infra.slider.server;

import com.xiaomi.infra.slider.server.common.ConfigKey;
import com.xiaomi.infra.slider.server.provider.RequestHandler;
import com.xiaomi.infra.slider.service.common.Request;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.log4j.Logger;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by yang on 15-1-12.
 */
public class Server {
  static Logger log = Logger.getLogger(Server.class);
  int port;
  Executor executor = Executors.newSingleThreadExecutor();

  public void start() throws IOException {
    addKeytabResource();
    executor.execute(new Runnable() {
      @Override
      public void run() {
        RequestHandler handler = new RequestHandler();
        startMultiThreadsThriftServer(new Request.Processor<RequestHandler>(handler));
      }
    });
    System.out.println("start server on port: " + port + "......");
  }

  public void addKeytabResource() throws IOException {
    String user = System.getProperty(ConfigKey.User);
    String keytab = System.getProperty(ConfigKey.keyTabFile);
    UserGroupInformation.loginUserFromKeytab(user, keytab);
  }

  public void startMultiThreadsThriftServer(Request.Processor<RequestHandler> processor) {
    try {
      TServerTransport serverTransport = new TServerSocket(port);
      TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));
      server.serve();
    } catch (Exception e) {
      e.printStackTrace();
      log.error(e);
    }
  }

  public static void main(String[] args) {
    if (args.length != 3) {
      String error = "Usage: Server port username keytabfile";
      System.err.println(error);
      log.error(error);
      return;
    }
    if (!UserGroupInformation.isSecurityEnabled()) {
      String error = "请设置classpath, 其中需要包括hadoop配置";
      System.err.println(error);
      log.error(error);
    }
    if (System.getProperty(ConfigKey.KrbConf) == null) {
      String error = "Please set " + ConfigKey.KrbConf;
      System.err.println(error);
      log.error(error);
    }
    System.setProperty(ConfigKey.User, args[1]);
    System.setProperty(ConfigKey.keyTabFile, args[2]);
    try {
      Server server = new Server();
      server.port = Integer.parseInt(args[0]);
      server.start();
    } catch (IOException e) {
      e.printStackTrace();
      log.error(e);
    }
  }
}
