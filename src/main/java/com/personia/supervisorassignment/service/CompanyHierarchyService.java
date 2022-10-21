package com.personia.supervisorassignment.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.personia.supervisorassignment.data.EmployeeEntity;
import com.personia.supervisorassignment.data.HierarchyRepository;
import com.personia.supervisorassignment.error.CycleDetectedException;
import com.personia.supervisorassignment.error.MultipleRootsDetectedException;
import com.personia.supervisorassignment.model.Graph;
import com.personia.supervisorassignment.model.Vertex;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyHierarchyService {

    private final HierarchyRepository hierarchyRepository;

    public Set<String> createHierarchy(final Map<String, String> hierarchyRequest) {
        final Graph graph = createGraph(hierarchyRequest);
        boolean cyclicGraph = graph.hasCycle();

        if (cyclicGraph) {
            throw new CycleDetectedException("There is a cycle. Hierarchy could not be created.");
        }

        final List<EmployeeEntity> employeeEntities = createEmployeeEntities(graph);

        if (checkMultipleRootExists(employeeEntities)) {
            throw new MultipleRootsDetectedException("There are multiple roots in the request.");
        }

        final Optional<String> rootVal = employeeEntities.stream()
                                                         .filter(c -> Objects.isNull(c.getSupervisorName()))
                                                         .map(EmployeeEntity::getEmployeeName)
                                                         .findFirst();
        if (rootVal.isEmpty()) {
            return Collections.emptySet();
        }

        final Set<String> response = new LinkedHashSet<>();
        response.add(rootVal.get());
        createResponse(rootVal.get(), employeeEntities, response);

        hierarchyRepository.saveAll(employeeEntities);

        return response;
    }

    private void createResponse(final String name, final List<EmployeeEntity> employees, final Set<String> response) {
        final List<EmployeeEntity> neighbours = employees.stream()
                                                  .filter(c -> Objects.equals(c.getSupervisorName(), name))
                                                  .toList();

        if (CollectionUtils.isEmpty(neighbours)) {
            return;
        }
        
        if (neighbours.size() > 1) {
            StringBuilder val = new StringBuilder();
            neighbours.forEach(c -> val.append("\t").append(c.getEmployeeName()).append("\n"));
            response.add(val.toString());
        } else {
            response.add("\t" + neighbours.get(0).getEmployeeName());
        }

        createResponse(neighbours.get(0).getEmployeeName(), employees, response);
    }

    private List<EmployeeEntity> createEmployeeEntities(final Graph graph) {
        List<EmployeeEntity> employeeEntities = new ArrayList<>();

        for (Vertex item : graph.getVertices()) {
            EmployeeEntity employee = new EmployeeEntity();
            employee.setEmployeeName(item.getValue());
            if (!CollectionUtils.isEmpty(item.getAdjacencyList())) {
                employee.setSupervisorName(item.getAdjacencyList().get(0).getValue());
            }

            if (employeeEntities.stream().noneMatch(k -> k.getEmployeeName().equals(item.getValue()))) {
                employeeEntities.add(employee);
            }
        }
        return employeeEntities;
    }

    private Graph createGraph(final Map<String, String> map) {
        final Graph graph = new Graph();
        Map<String, Vertex> vertices = new HashMap<>();
        for (Map.Entry<String, String> item : map.entrySet()) {
            Vertex vertexFrom;
            Vertex vertexTo;
            if (!vertices.containsKey(item.getKey())) {
                vertexFrom = new Vertex(item.getKey());
                graph.addVertex(vertexFrom);
                vertices.put(vertexFrom.getValue(), vertexFrom);
            } else {
                vertexFrom = vertices.get(item.getKey());
            }

            if (!vertices.containsKey(item.getValue())) {
                vertexTo = new Vertex(item.getValue());
                graph.addVertex(vertexTo);
                vertices.put(vertexTo.getValue(), vertexTo);
            } else {
                vertexTo = vertices.get(item.getValue());
            }

            graph.addEdge(vertexFrom, vertexTo);
        }

        return graph;
    }

    private boolean checkMultipleRootExists(final List<EmployeeEntity> employeeEntities) {
        return employeeEntities.stream().filter(c-> Objects.isNull(c.getSupervisorName())).count() > 1;
    }

    public List<Object> getSupervisor(final String name) {
        return hierarchyRepository.findSupervisors(name);
    }
}
