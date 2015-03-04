package com.xiaomi.infra.slider.server.information;


import org.apache.hadoop.yarn.api.records.ApplicationReport;

/**
 * Created by yang on 15-1-12.
 */
public class Applications {
  private Application[] applications;

  public static class Application {
    private String applicationId;
    private String applicationAttemptId;
    private String user;
    private String queue;
    private String name;
    private String host;
    private int rpcPort;
    private String state;
    private String diagnostics;
    private String url;
    private long startTime;
    private long finishTime;
    private String finalStatus;
    private float progress;
    private String applicationType;

    Application() {
    }

    public Application(ApplicationReport report) {
      applicationId = report.getApplicationId().toString();
      applicationAttemptId = report.getCurrentApplicationAttemptId().toString();
      user = report.getUser().toString();
      queue = report.getQueue().toString();
      name = report.getName().toString();
      host = report.getHost().toString();
      rpcPort = report.getRpcPort();
      state = report.getYarnApplicationState().toString();
      diagnostics = report.getDiagnostics().toString();
      url = report.getTrackingUrl().toString();
      startTime = report.getStartTime();
      finishTime = report.getFinishTime();
      finalStatus = report.getFinalApplicationStatus().toString();
      progress = report.getProgress();
      applicationType = report.getApplicationType();
    }

    public String getApplicationId() {
      return applicationId;
    }

    public void setApplicationId(String applicationId) {
      this.applicationId = applicationId;
    }

    public String getApplicationAttemptId() {
      return applicationAttemptId;
    }

    public void setApplicationAttemptId(String applicationAttemptId) {
      this.applicationAttemptId = applicationAttemptId;
    }

    public String getUser() {
      return user;
    }

    public void setUser(String user) {
      this.user = user;
    }

    public String getQueue() {
      return queue;
    }

    public void setQueue(String queue) {
      this.queue = queue;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getHost() {
      return host;
    }

    public void setHost(String host) {
      this.host = host;
    }

    public int getRpcPort() {
      return rpcPort;
    }

    public void setRpcPort(int rpcPort) {
      this.rpcPort = rpcPort;
    }

    public String getState() {
      return state;
    }

    public void setState(String state) {
      this.state = state;
    }

    public String getDiagnostics() {
      return diagnostics;
    }

    public void setDiagnostics(String diagnostics) {
      this.diagnostics = diagnostics;
    }

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }

    public String getFinalStatus() {
      return finalStatus;
    }

    public void setFinalStatus(String finalStatus) {
      this.finalStatus = finalStatus;
    }

    public String getApplicationType() {
      return applicationType;
    }

    public void setApplicationType(String applicationType) {
      this.applicationType = applicationType;
    }

    public void setStartTime(long startTime) {
      this.startTime = startTime;
    }

    public long getStartTime() {
      return startTime;
    }

    public void setFinishTime(long finishTime) {
      this.finishTime = finishTime;
    }

    public long getFinishTime() {
      return finishTime;
    }

    public void setProgress(float progress) {
      this.progress = progress;
    }

    public float getProgress() {
      return progress;
    }
  }

  public Application[] getApplications() {
    return applications;
  }

  public void setApplications(Application[] applications) {
    this.applications = applications;
  }


}
