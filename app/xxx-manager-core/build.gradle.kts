val vers = rootProject.extra.get("vers") as Map<String, String>

plugins {
    id("kotlin-kapt")
}

dependencies {
    api("org.springframework.boot:spring-boot-starter-data-jpa")
    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.boot:spring-boot-starter-validation")

    implementation("com.github.ben-manes.caffeine:caffeine:${vers["caffeine"]}")
    api("com.querydsl:querydsl-jpa:${vers["queryDSL"]}")

    kapt("com.querydsl:querydsl-apt:${vers["queryDSL"]}:jpa")
}