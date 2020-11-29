import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    `maven-publish`
    id("org.openjfx.javafxplugin")
    application

}
java.sourceCompatibility = JavaVersion.VERSION_11

val compileKotlin: KotlinCompile by tasks
val compileJava: JavaCompile by tasks
compileJava.destinationDir = compileKotlin.destinationDir


application{
    mainClass.set("tech.openEdgn.test.MainKt")
}

dependencies {
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib"))
    implementation("com.github.openEDGN.FXUIManager:manager:1.0")
    implementation("com.github.OpenEdgn.Logger4K:logger-console:1.0.4")
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.2")
    implementation("com.jfoenix:jfoenix:9.0.10")
    implementation("com.google.code.gson:gson:2.8.5")
    implementation("org.json:json:20201115")
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




