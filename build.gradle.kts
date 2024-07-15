plugins {
	java
	id("org.springframework.boot")  version "3.3.1"
	id("io.spring.dependency-management") version "1.1.5"
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
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks {
	test {
		useJUnitPlatform()
	}
	named<Jar>("jar") {
		enabled = false
	}
}
