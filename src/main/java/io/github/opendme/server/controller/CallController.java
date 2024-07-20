package io.github.opendme.server.controller;

import io.github.opendme.server.entity.Call;
import io.github.opendme.server.entity.CallCreationDto;
import io.github.opendme.server.service.CallService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CallController {
    CallService callService;

    public CallController(CallService callService) {
        this.callService = callService;
    }

    @PostMapping("/call")
    @ResponseStatus(HttpStatus.CREATED)
    @RolesAllowed("admin")
    Call create(@RequestBody CallCreationDto dto) {
        return callService.createFrom(dto.getDepartmentId(), dto.getVehicleIds());
    }
}
