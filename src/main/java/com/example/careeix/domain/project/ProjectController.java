package com.example.careeix.domain.project;

import com.example.careeix.domain.project.dto.PostProjectRequest;
import com.example.careeix.domain.project.dto.ProjectCreateResponse;
import com.example.careeix.domain.project.entity.Project;
import com.example.careeix.domain.project.entity.ProjectDetail;
import com.example.careeix.domain.project.entity.ProjectNote;
import com.example.careeix.domain.project.service.ProjectService;
import com.example.careeix.utils.jwt.service.JwtService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/project")
@RequiredArgsConstructor
@Api(tags = "Project API")
public class ProjectController {

    private final JwtService jwtService;
    private final ProjectService projectService;



    @ResponseBody
    @PostMapping("/create")
    public ResponseEntity<ProjectCreateResponse> createProject(@RequestBody @Valid PostProjectRequest postProjectRequest) {
        Project projectCreated = projectService.createProject(postProjectRequest.getProject());

        for (ProjectDetail pd : postProjectRequest.getProject().getProjectDetails()) {
            ProjectDetail projectDetailCreated = projectService.createProjectDetail(pd);

            for (ProjectNote pn : pd.getProjectNotes()) {
                ProjectNote projectNoteCreated = projectService.createProjectNote(pn);
            }
        }


        return projectCreated;
    }

}
