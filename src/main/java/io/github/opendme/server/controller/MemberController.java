package io.github.opendme.server.controller;

import io.github.opendme.server.entity.Member;
import io.github.opendme.server.entity.MemberDto;
import io.github.opendme.server.service.MemberService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
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
    public Member create(@RequestBody @Valid MemberDto dto) {
        Member member = service.create(dto);
        log.atInfo().log("Member created");

        return member;
    }
}
