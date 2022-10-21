package com.personia.supervisorassignment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.personia.supervisorassignment.data.HierarchyRepository;
import com.personia.supervisorassignment.error.CycleDetectedException;
import com.personia.supervisorassignment.error.MultipleRootsDetectedException;

@ExtendWith(MockitoExtension.class)
class CompanyHierarchyServiceTest {

    private static final String EMPLOYEE_NAME = "Nick";

    @Mock
    private HierarchyRepository hierarchyRepository;

    @InjectMocks
    private CompanyHierarchyService hierarchyService;

    @Test
    void createHierarchy_fails_cycle_detected() {
        Exception exception = assertThrows(CycleDetectedException.class, () -> {
            hierarchyService.createHierarchy(Map.of("Pete","Nick","Nick","Sophie","Barbara","Nick","Sophie","Pete"));
        });
        assertThat(exception.getMessage()).contains("There is a cycle. Hierarchy could not be created.");
    }

    @Test
    void createHierarchy_fails_multiple_root_detected() {
        Exception exception = assertThrows(MultipleRootsDetectedException.class, () -> {
            hierarchyService.createHierarchy(Map.of("Pete","Nick","Nick","Sophie","Barbara","Nick","Sophie","Jonas", "Cem", "Elif"));
        });
        assertThat(exception.getMessage()).contains("There are multiple roots in the request.");
    }

    @Test
    void createHierarchy_success() {
        Set<String> hierarchy = hierarchyService.createHierarchy(Map.of("Pete", "Nick", "Nick", "Sophie", "Barbara", "Nick", "Sophie", "Jonas"));

        Set<String> expected = new LinkedHashSet();
        expected.add("Jonas");
        expected.add("\tSophie");
        expected.add("\tNick");
        expected.add("\tBarbara\n\tPete\n");

        assertThat(hierarchy).contains("Jonas");
        assertThat(hierarchy).contains("\tSophie");
        assertThat(hierarchy).contains("\tNick");
    }

    @Test
    void getSupervisor_no_result_in_db() {
        when(hierarchyRepository.findSupervisors(EMPLOYEE_NAME)).thenReturn(Collections.emptyList());
        List<Object> supervisor = hierarchyService.getSupervisor(EMPLOYEE_NAME);
        assertThat(supervisor).isEmpty();
    }

    @Test
    void getSupervisor_success() {
        when(hierarchyRepository.findSupervisors(EMPLOYEE_NAME)).thenReturn(List.of("Sophie, Jonas"));
        List<Object> supervisor = hierarchyService.getSupervisor(EMPLOYEE_NAME);
        assertThat(supervisor).isNotEmpty();
        assertThat(supervisor.get(0)).isEqualTo("Sophie, Jonas");
    }

}