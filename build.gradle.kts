plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependencymanagement)
}

group = "io.github.opendme"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(22)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.driver.postgres)
	implementation("org.keycloak", "keycloak-admin-client", "25.0.1")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation(testlibs.bundles.database.postgres)
}

tasks {
    test {
        useJUnitPlatform()
    }
    named<Jar>("jar") {
        enabled = false
    }
}
