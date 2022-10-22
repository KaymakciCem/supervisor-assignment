package com.personia.supervisorassignment.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.personia.supervisorassignment.service.CompanyHierarchyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hierarchy")
public class CompanyHierarchyController {

    private final CompanyHierarchyService hierarchyService;

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping(produces = APPLICATION_JSON_VALUE)
    public String createHierarchy(@RequestBody final Map<String, String> hierarchyRequest) {
        return hierarchyService.createHierarchy(hierarchyRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping(value = "/supervisors/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Object> getSupervisor(@PathVariable("name") String name) {
        return hierarchyService.getSupervisor(name);
    }
}

