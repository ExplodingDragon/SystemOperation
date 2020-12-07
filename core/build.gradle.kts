import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    `maven-publish`
    id("org.openjfx.javafxplugin")
    id("org.beryx.jlink")
    application

}
java.sourceCompatibility = JavaVersion.VERSION_11


val compileKotlin: KotlinCompile by tasks
val compileJava: JavaCompile by tasks
compileJava.destinationDir = compileKotlin.destinationDir

java {
    modularity.inferModulePath.set(true)
}


application {
    mainModule.set("test.system")
    mainClass.set("tech.openEdgn.test.MainKt")
}
tasks.register("archive", Zip::class) {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE // allow duplicates
}
dependencies {
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib"))
    implementation("com.github.OpenEdgn:FXUIManager:1614cec512"){
        exclude("com.github.OpenEdgn.Logger4K","core")
    }

//    implementation("com.github.openEDGN.FXUIManager:manager:1.0"){
//        exclude("com.github.OpenEdgn.Logger4K","core")
//    }
    implementation("com.github.OpenEdgn.Logger4K:core:0cda6f05f7")
    implementation("com.github.OpenEdgn.Logger4K:logger-console:0cda6f05f7")
    implementation("com.jfoenix:jfoenix:9.0.10")
    implementation("com.google.code.gson:gson:2.8.5")
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.2")
    testImplementation("org.junit.platform:junit-platform-launcher:1.6.2")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "11"
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = rootProject.group.toString()
            artifactId = project.name
            version = rootProject.version.toString()
            from(components["java"])
        }
    }
    repositories {
        mavenLocal()
    }
}


javafx {
    version = "15.0.1"
    modules("javafx.controls", "javafx.fxml")
}


jlink {
    launcher {
        name = "SystemOperation"
    }
    imageZip.set(project.file("${project.buildDir}/release/SystemOperation-release-$version.zip"))
}

