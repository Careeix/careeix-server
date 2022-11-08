package com.example.careeix.domain.project.entity;

import java.util.List;

public interface ProjectMapping {
    Long getProjectId();
    String getTitle();
    String getStartDate();
    String getEndDate();
    int getIsProceed();
    String getClassification();
    String getIntroduction();
    List<ProjectDetailMapping> getProjectDetails();
}
