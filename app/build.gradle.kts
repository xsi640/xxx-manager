import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar
import kotlin.collections.*

plugins {
    id("org.springframework.boot") version "2.4.2"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.0"
    kotlin("plugin.spring") version "1.5.0"
    kotlin("plugin.jpa") version "1.5.0"
    kotlin("kapt") version "1.5.0" apply false
}

allprojects {

    apply {
        plugin("idea")
        plugin("org.springframework.boot")
        plugin("io.spring.dependency-management")
        plugin("kotlin")
        plugin("kotlin-spring")
        plugin("kotlin-jpa")
        plugin("kotlin-allopen")
    }

    group = "com.github.xsi640"
    version = "1.0.0"
    java.sourceCompatibility = JavaVersion.VERSION_1_8
    java.targetCompatibility = JavaVersion.VERSION_1_8

    val user = System.getProperty("repoUser")
    val pwd = System.getProperty("repoPassword")

    repositories {
        mavenLocal()
        maven {
            credentials {
                username = user
                password = pwd
                isAllowInsecureProtocol = true
            }
            url = uri("http://172.16.11.231:8081/nexus/repository/maven2-group/")
//            url = uri("http://nexus.suyang.home/repository/maven-group/")
        }
    }

    val vers = mapOf(
        "commons_io" to "2.6",
        "commons_codec" to "1.15",
        "commons_lang" to "3.9",
        "jbcrypt" to "0.4",
        "caffeine" to "2.9.0",
        "jjwt" to "0.11.2",
        "queryDSL" to "4.4.0",
    )
    rootProject.extra.set("vers", vers)

    dependencies {
        implementation("commons-io:commons-io:${vers["commons_io"]}")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("org.jetbrains.kotlin:kotlin-allopen")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "1.8"
        }
    }

    val jar: Jar by tasks
    jar.enabled = true
    val bootJar: BootJar by tasks
    bootJar.enabled = false
}