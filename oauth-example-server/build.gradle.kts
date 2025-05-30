plugins {
    java
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "io.github.siujamo.playground"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    runtimeOnly(libs.dbDriver.pg)
    implementation(libs.hikari.core)
    implementation(platform(libs.devkit.bom))
    implementation(libs.devkit.utils)
    implementation(libs.devkit.guid)
    implementation(libs.devkit.simpleJwt.facade)
    implementation(libs.devkit.simpleJwt.auth0)
    implementation(libs.devkit.simpleJwt.starter)
    implementation(libs.mybatisFlex.starter)
    implementation(libs.springBoot.starter.web)
    implementation(libs.springBoot.starter.webflux)
    implementation(libs.springBoot.starter.redis)
    implementation(libs.springBoot.starter.security)
    compileOnly(libs.springBoot.core.configurationProcessor)
    annotationProcessor(libs.springBoot.core.configurationProcessor)
    testImplementation(libs.test.reactor)
    testImplementation(libs.test.springSecurity)
    testImplementation(libs.test.springBoot)
    testRuntimeOnly(libs.test.junit)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
