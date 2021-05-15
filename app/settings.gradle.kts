import java.nio.file.Paths

rootProject.name = "xxx-manager"

fun defineSubProject(name: String, dir: String = "") {
    val projectName = "xxx-manager-$name"
    include(projectName)
    val dir = if (dir.isEmpty()) Paths.get(projectName).toFile() else Paths.get(dir, projectName).toFile()
    if (!dir.exists())
        mkdir(dir)
    project(":$projectName").projectDir = dir
}

defineSubProject("core")
defineSubProject("auth")