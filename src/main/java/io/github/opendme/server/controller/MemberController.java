package io.github.opendme.server.controller;

import io.github.opendme.server.entity.Member;
import io.github.opendme.server.entity.MemberDto;
import io.github.opendme.server.entity.MemberRepository;
import io.github.opendme.server.entity.Permission;
import io.github.opendme.server.entity.Status;
import io.github.opendme.server.service.MemberService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Set;

@RestController
public class MemberController {
    private static final Logger log = LogManager.getLogger(MemberController.class);
    private final MemberRepository memberRepository;
    private final MemberService service;

    public MemberController(MemberService service, MemberRepository memberRepository) {
        this.service = service;
        this.memberRepository = memberRepository;
    }

    @PostMapping(value = "/member", produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Member create(@RequestBody @Valid MemberDto dto) {
        Member member = service.create(dto);
        log.atInfo().log("Member created");

        return member;
    }

    @PatchMapping(value = "/member/{memberId}/status", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void setStatus(@PathVariable Long memberId, @RequestBody Status status) {
        service.setMemberStatus(memberId, status);
        log.atInfo().log("Member status patched");
    }

    @PatchMapping(value = "/member/{memberId}/permission", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void setPermissions(@PathVariable Long memberId, @RequestBody Set<Permission> permissions) {
        service.setMemberPermissions(memberId, permissions);
        log.info("Member permissions patched");
    }

    @GetMapping(value = "/member/{memberId}/permission", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Set<Permission> getPermissions(@PathVariable Long memberId) {
        Member member = memberRepository.findById(memberId)
                                        .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Member not found"));
        return member.getPermissions();
    }
}
