package com.jhfactory.sample.googleapi.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

/**
 * A Project is a high-level Google Cloud Platform entity. It is a container for ACLs, APIs, App
 * Engine Apps, VMs, and other Google Cloud Platform resources.
 *
 * <p>JSON representation { "projectNumber": string, "projectId": string, "lifecycleState":
 * enum(LifecycleState), "name": string, "createTime": string, "labels": { string: string, ... },
 * "parent": { object(ResourceId) }, }
 */
public class Project implements Parcelable {

  private String projectNumber;
  private String projectId;
  private LifecycleState lifecycleState;
  private String name;
  private String createTime;
  private Map<String, String> labels;
  private ResourceId parent;

  private Project(Parcel in) {
    projectNumber = in.readString();
    projectId = in.readString();
    lifecycleState = LifecycleState.valueOf(in.readString());
    name = in.readString();
    createTime = in.readString();
    parent = in.readParcelable(ResourceId.class.getClassLoader());
  }

  public static final Creator<Project> CREATOR =
      new Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel in) {
          return new Project(in);
        }

        @Override
        public Project[] newArray(int size) {
          return new Project[size];
        }
      };

  public String getProjectNumber() {
    return projectNumber;
  }

  public String getProjectId() {
    return projectId;
  }

  public LifecycleState getLifecycleState() {
    return lifecycleState;
  }

  public String getName() {
    return name;
  }

  public String getCreateTime() {
    return createTime;
  }

  public Map<String, String> getLabels() {
    return labels;
  }

  public ResourceId getParent() {
    return parent;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(projectNumber);
    parcel.writeString(projectId);
    parcel.writeString(lifecycleState.name());
    parcel.writeString(name);
    parcel.writeString(createTime);
    parcel.writeParcelable(parent, i);
  }

  /** Project lifecycle states. */
  public enum LifecycleState {
    LIFECYCLE_STATE_UNSPECIFIED,
    ACTIVE,
    DELETE_REQUESTED,
    DELETE_IN_PROGRESS
  }

  /**
   * A container to reference an id for any resource type. A resource in Google Cloud Platform is a
   * generic term for something you (a developer) may want to interact with through one of our
   * API's. Some examples are an App Engine app, a Compute Engine instance, a Cloud SQL database,
   * and so on.
   *
   * <p>JSON representation { "type": string, "id": string, }
   */
  private static class ResourceId implements Parcelable {

    private String type;
    private String id;

    ResourceId(Parcel in) {
      type = in.readString();
      id = in.readString();
    }

    public static final Creator<ResourceId> CREATOR =
        new Creator<ResourceId>() {
          @Override
          public ResourceId createFromParcel(Parcel in) {
            return new ResourceId(in);
          }

          @Override
          public ResourceId[] newArray(int size) {
            return new ResourceId[size];
          }
        };

    public String getType() {
      return type;
    }

    public String getId() {
      return id;
    }

    @Override
    public int describeContents() {
      return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
      parcel.writeString(type);
      parcel.writeString(id);
    }
  }
}
