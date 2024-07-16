package io.github.opendme.server.controller;

import io.github.opendme.server.entity.Department;
import io.github.opendme.server.entity.DepartmentDto;
import io.github.opendme.server.service.DepartmentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DepartmentController {
    DepartmentService service;

    public DepartmentController(DepartmentService service) {
        this.service = service;
    }

    @PostMapping(value = "/department", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Department create(@RequestBody DepartmentDto department) {
        Department department1 = service.create(department);
        System.out.println("Department created");

        return department1;
    }
}
