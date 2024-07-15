rootProject.name = "server"

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version ("0.8.0")
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            library("driver-postgres", "org.postgresql:postgresql:42.7.3")

            plugin("spring-boot", "org.springframework.boot").version("3.3.1")
            plugin("spring-dependencymanagement", "io.spring.dependency-management").version("1.1.5")
        }

        create("testlibs") {
            version("junit", "5.10.2")
            library("junit-jupiter", "org.junit.jupiter", "junit-jupiter").versionRef("junit")
            library("junit-params", "org.junit.jupiter", "junit-jupiter-params").versionRef("junit")
            bundle("junit", listOf("junit-jupiter", "junit-params"))

            library("driver-postgres", "org.postgresql:postgresql:42.7.3")

            version("testcontainers", "1.19.8")
            library("testcontainers-postgres", "org.testcontainers", "postgresql").versionRef("testcontainers")
            library("testcontainers-core", "org.testcontainers", "testcontainers").versionRef("testcontainers")
            library("testcontainers-junit", "org.testcontainers", "junit-jupiter").versionRef("testcontainers")

            version("slf4j", "2.0.13")
            library("slf4j-noop", "org.slf4j", "slf4j-nop").versionRef("slf4j")

            bundle(
                "database-postgres",
                listOf("testcontainers-junit", "testcontainers-core", "testcontainers-postgres", "driver-postgres")
            )
        }
    }
}
