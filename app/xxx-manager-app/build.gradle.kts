import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation(project(":${rootProject.name}-auth"))
}

val bootJar:BootJar by tasks
bootJar.enabled = true