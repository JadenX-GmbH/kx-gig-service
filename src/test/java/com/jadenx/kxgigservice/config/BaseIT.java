package com.jadenx.kxgigservice.config;

import com.jadenx.kxgigservice.KxGigServiceApplication;
import com.jadenx.kxgigservice.repos.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.util.StreamUtils;
import org.testcontainers.containers.MariaDBContainer;

import java.nio.charset.Charset;


/**
 * Abstract base class to be extended by every IT test. Starts the Spring Boot context with a
 * Datasource connected to the Testcontainers Docker instance. The instance is reused for all tests,
 * with all data wiped out before each test.
 */
// CHECKSTYLE IGNORE check FOR NEXT 5 LINES
@SpringBootTest(
    classes = {KxGigServiceApplication.class, TestSecurityConfig.class},
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("it")
@Sql("/data/clearAll.sql")
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
public abstract class BaseIT {

    private static final MariaDBContainer mariadbContainer;

    static {
        mariadbContainer = (MariaDBContainer) (new MariaDBContainer("mariadb")
            .withUsername("testcontainers")
            .withPassword("Testcontain3rs!")
            .withReuse(true));
        mariadbContainer.start();
    }

    @Autowired
    public TestRestTemplate restTemplate;

    @Autowired
    public ContractRepository contractRepository;

    @Autowired
    public OfferRepository offerRepository;

    @Autowired
    public SlaStatementRepository slaStatementRepository;

    @Autowired
    public SkillsetRepository skillsetRepository;

    @Autowired
    public GigRepository gigRepository;

    @Autowired
    public DataOwnerRepository dataOwnerRepository;

    @Autowired
    public SpecialistRepository specialistRepository;

    @Autowired
    public CandidateSpecialistRepository candidateSpecialistRepository;

    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mariadbContainer::getJdbcUrl);
        registry.add("spring.datasource.password", mariadbContainer::getPassword);
        registry.add("spring.datasource.username", mariadbContainer::getUsername);
    }

    @SneakyThrows
    public String readResource(final String resourceName) {
        return StreamUtils.copyToString(getClass().getResourceAsStream(resourceName), Charset.forName("UTF-8"));
    }

    public HttpHeaders headers() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

}
