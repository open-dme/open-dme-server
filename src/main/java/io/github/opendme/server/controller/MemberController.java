package io.github.opendme.server.controller;

import io.github.opendme.server.entity.Member;
import io.github.opendme.server.entity.MemberDto;
import io.github.opendme.server.entity.Status;
import io.github.opendme.server.service.MemberService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;

import java.security.Principal;

import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public MemberController(MemberService service) {
        this.service = service;
    }

    @PostMapping(value = "/member", produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @RolesAllowed("admin")
    public Member create(@RequestBody @Valid MemberDto dto) {
        Member member = service.create(dto);
        log.atInfo().log("Member created");

        return member;
    }

    @PatchMapping(value = "/member/{memberId}/status", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @RolesAllowed("admin")
    public void setStatus(@PathVariable Long memberId, @RequestBody Status status) {
        service.setMemberStatus(memberId, status);
        log.atInfo().log("Member status patched");

    }
}
