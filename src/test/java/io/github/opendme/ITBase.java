package io.github.opendme;

import io.github.opendme.server.entity.DepartmentRepository;
import io.github.opendme.server.entity.MemberRepository;
import io.github.opendme.server.entity.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootIntegrationTest
public class ITBase {
    @Autowired
    protected MockMvc mvc;
    @Autowired
    protected DepartmentRepository departmentRepository;
    @Autowired
    protected MemberRepository memberRepository;
    @Autowired
    protected SkillRepository skillRepository;

    @Container
    @ServiceConnection
    private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

}
