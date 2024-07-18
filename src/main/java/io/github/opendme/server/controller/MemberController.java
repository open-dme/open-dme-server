package io.github.opendme.server.controller;

import io.github.opendme.server.entity.Member;
import io.github.opendme.server.entity.MemberDto;
import io.github.opendme.server.entity.Status;
import io.github.opendme.server.service.KeycloakService;
import io.github.opendme.server.service.MemberService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {
    private static final Logger log = LogManager.getLogger(MemberController.class);
    MemberService service;
    KeycloakService keycloakService;

    public MemberController(MemberService service) {
        this.service = service;
    }

    @PostMapping(value = "/member", produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Member create(@RequestBody @Valid MemberDto dto) {
        Member member = service.create(dto);
        log.atInfo().log("Member created");
        keycloakService.createUser(member);
        // TODO: Send creation mail with password c:

        return member;
    }

    @PatchMapping(value = "/member/{memberId}/status", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void setStatus(@PathVariable Long memberId, @RequestBody Status status) {
        service.setMemberStatus(memberId, status);
        log.atInfo().log("Member status patched");

    }
}
