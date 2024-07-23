import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependencymanagement)
    kotlin("jvm") version "2.0.0"
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
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation(libs.driver.postgres)
	implementation("org.keycloak", "keycloak-admin-client", "25.0.1")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")


    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("org.springframework.security:spring-security-oauth2-jose")

    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework:spring-context-support")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("com.google.zxing:core:3.3.0")
    implementation("com.google.zxing:javase:3.3.0")


    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation(testlibs.bundles.database.postgres)
}

tasks {
    test {
        useJUnitPlatform()
        testLogging {
            lifecycle {
                events = mutableSetOf(
                    TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent
                        .SKIPPED
                )
                exceptionFormat = TestExceptionFormat.FULL

                showExceptions = true
                showCauses = true
                showStackTraces = false
                showStandardStreams = false
            }
        }
    }
    named<Jar>("jar") {
        enabled = false
    }
}
