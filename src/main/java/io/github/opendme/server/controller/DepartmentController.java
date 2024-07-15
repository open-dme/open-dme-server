package io.github.opendme.server.controller;

import io.github.opendme.server.entity.Department;
import io.github.opendme.server.service.DepartmentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DepartmentController {
    DepartmentService service;

    public DepartmentController(DepartmentService service) {
        this.service = service;
    }

    @PostMapping("/department")
    Department create(@RequestBody Department department) {
        return service.create(department);
    }
}
