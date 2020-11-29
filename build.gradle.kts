group = "com.github.openEDGN"
// 你可以修改此为自己的组织地址
version = "1.0"
// 你可以指定此为项目 版本号

buildscript {
    repositories{
        mavenLocal()
        maven { url = project.uri("https://maven.aliyun.com/repository/public/") }
        jcenter()
        mavenCentral()
        maven { url = project.uri("https://jitpack.io") }
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.10")
    }
}

allprojects {
    repositories{
        mavenLocal()
        maven { url = project.uri("https://maven.aliyun.com/repository/public/") }
        jcenter()
        mavenCentral()
        maven { url = project.uri("https://jitpack.io") }
    }}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
    for (childProject in childProjects.values) {
        delete(childProject.buildDir)
    }
}
