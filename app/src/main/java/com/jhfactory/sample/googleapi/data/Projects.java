package com.jhfactory.sample.googleapi.data;

import java.util.List;

/** JSON representation { "projects": [ { object(Project) } ], "nextPageToken": string, } */
public class Projects {

  private List<Project> projects;
  private String nextPageToken;

  public List<Project> getProjects() {
    return projects;
  }

  public String getNextPageToken() {
    return nextPageToken;
  }
}
