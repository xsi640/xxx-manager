val vers = rootProject.extra.get("vers") as Map<String, String>

plugins {
    id("kotlin-kapt")
}

dependencies {
    api(project(":${rootProject.name}-core"))
    implementation("org.mindrot:jbcrypt:${vers["jbcrypt"]}")
    implementation("io.jsonwebtoken:jjwt-api:${vers["jjwt"]}")
    implementation("io.jsonwebtoken:jjwt-impl:${vers["jjwt"]}")
    implementation("io.jsonwebtoken:jjwt-jackson:${vers["jjwt"]}")

    runtimeOnly("com.h2database:h2")

    kapt("com.querydsl:querydsl-apt:${vers["queryDSL"]}:jpa")
}