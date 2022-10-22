package com.personia.supervisorassignment.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HierarchyRepository extends JpaRepository<EmployeeEntity, Long> {

    @Query("select e1.supervisorName as employeeName, e2.supervisorName  from EmployeeEntity e1 inner join EmployeeEntity e2 on e1.supervisorName = e2.employeeName where e1.employeeName = :employeeName")
    List<Object> findSupervisors(@Param("employeeName") String employeeName);
}

