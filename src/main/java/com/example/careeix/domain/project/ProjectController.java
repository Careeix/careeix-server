package com.example.careeix.domain.project;

import com.example.careeix.domain.project.dto.PostProjectRequest;
import com.example.careeix.domain.project.dto.PostProjectResponse;
import com.example.careeix.domain.project.service.ProjectService;
import com.example.careeix.utils.dto.ApplicationResponse;
import com.example.careeix.utils.jwt.service.JwtService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/project")
@RequiredArgsConstructor
@Api(tags = "Project API")
public class ProjectController {

    private final JwtService jwtService;
    private final ProjectService projectService;


    /**
     * @param postProjectRequest
     * @return
     *
     */
    @ResponseBody
    @PostMapping("/create")
    public ApplicationResponse<PostProjectResponse> createProject(@RequestBody @Valid PostProjectRequest postProjectRequest) {

    }



}
