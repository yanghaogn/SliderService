package com.xiaomi.infra.slider.server.yarn;


import com.xiaomi.infra.slider.server.information.Applications;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.apache.log4j.Logger;
import org.apache.slider.client.SliderYarnClientImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yang on 15-1-12.
 */
public class YarnHandler {
  public final static Logger log = Logger.getLogger(YarnHandler.class);
  private static SliderYarnClientImpl yarnClient = null;

  static  {
    yarnClient = new SliderYarnClientImpl();
    yarnClient.init(new Configuration());
    yarnClient.start();
  }

  public List<Applications.Application> getLiveApplicationList(String user, String name) {
    ArrayList<Applications.Application> listApps = new ArrayList<Applications.Application>();
    try {
      for (ApplicationReport report : yarnClient.findAllLiveInstances(user, name)) {
        listApps.add(new Applications.Application(report));
      }
    } catch (YarnException e) {
      log.error(e);
    } catch (IOException e) {
      log.error(e);
    }
    return listApps;
  }

}
