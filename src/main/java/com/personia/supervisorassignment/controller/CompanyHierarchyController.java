package com.personia.supervisorassignment.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.personia.supervisorassignment.service.CompanyHierarchyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hierarchy")
public class CompanyHierarchyController {

    private final CompanyHierarchyService hierarchyService;

    @PostMapping(produces = APPLICATION_JSON_VALUE)
    public String createHierarchy(@RequestBody final Map<String, String> hierarchyRequest) {
        return hierarchyService.createHierarchy(hierarchyRequest);
    }

    @GetMapping(value = "/supervisors/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Object> getSupervisor(@PathVariable("name") String name) {
        return hierarchyService.getSupervisor(name);
    }
}

