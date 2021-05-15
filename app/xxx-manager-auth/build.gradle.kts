dependencies {
    api(project(":${rootProject.name}-core"))
    implementation("org.mindrot:jbcrypt:0.4")
    runtimeOnly("com.h2database:h2")
}