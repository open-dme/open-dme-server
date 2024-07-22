package io.github.opendme;

import io.github.opendme.server.entity.CallRepository;
import io.github.opendme.server.entity.CallResponseRepository;
import io.github.opendme.server.entity.DepartmentRepository;
import io.github.opendme.server.entity.MemberRepository;
import io.github.opendme.server.entity.SkillRepository;
import io.github.opendme.server.entity.VehicleRepository;
import io.github.opendme.server.service.KeycloakService;
import io.github.opendme.server.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootIntegrationTest
public class ITBase {
    @Autowired
    protected MockMvc mvc;
    @Autowired
    protected DepartmentRepository departmentRepository;
    @Autowired
    protected MemberRepository memberRepository;
    @Autowired
    protected VehicleRepository vehicleRepository;
    @Autowired
    protected SkillRepository skillRepository;
    @Autowired
    protected CallRepository callRepository;
    @Autowired
    protected CallResponseRepository callResponseRepository;

    @MockBean
    protected InMemoryClientRegistrationRepository inMemoryClientRegistrationRepository;
    @MockBean
    protected KeycloakService keycloakService;
    //Not in test scope
    @MockBean
    protected MailService mailService;

}
