package com.example.careeix.domain.project.entity;


import java.util.List;

public interface ProjectDetailMapping {
    String getProjectDetailTitle();
    String getContent();
    List<ProjectNoteMapping> getProjectNotes();
}
