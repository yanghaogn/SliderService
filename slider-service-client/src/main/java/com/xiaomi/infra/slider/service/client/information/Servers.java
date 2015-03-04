package com.xiaomi.infra.slider.service.client.information;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yang on 14-12-10.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Servers {
  private String description = "description";
  private long updated = 100;
  private String updatedTime = "updatedTime";
  private Map<String, HOST_PORT[]> entries = new HashMap<String, HOST_PORT[]>();
  private boolean empty = false;

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public long getUpdated() {
    return updated;
  }

  public void setUpdated(long updated) {
    this.updated = updated;
  }

  public String getUpdatedTime() {
    return updatedTime;
  }

  public void setUpdatedTime(String updatedTime) {
    this.updatedTime = updatedTime;
  }

  public boolean isEmpty() {
    return empty;
  }

  public void setEmpty(boolean empty) {
    this.empty = empty;
  }

  public Map<String, HOST_PORT[]> getEntries() {
    return entries;
  }

  public void setEntries(Map<String, HOST_PORT[]> entries) {
    this.entries = entries;
  }

  public static class HOST_PORT {
    private String value;
    private String containerId;
    private String tag;
    private String level;
    private String updatedTime;

    public String getValue() {
      return value;
    }

    public void setValue(String value) {
      this.value = value;
    }

    public String getContainerId() {
      return containerId;
    }

    public void setContainerId(String containerId) {
      this.containerId = containerId;
    }

    public String getTag() {
      return tag;
    }

    public void setTag(String tag) {
      this.tag = tag;
    }

    public String getLevel() {
      return level;
    }

    public void setLevel(String level) {
      this.level = level;
    }

    public String getUpdatedTime() {
      return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
      this.updatedTime = updatedTime;
    }
  }
}
