plugins {
    java
    kotlin("jvm") version "1.3.61"
}

group = "dev.jycabello"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
val arrowVersion = "0.10.3"
val kotlinTestVersion = "3.4.1"
dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.arrow-kt", "arrow-core", arrowVersion)
    implementation("io.arrow-kt", "arrow-fx", arrowVersion)
    implementation("io.arrow-kt", "arrow-syntax", arrowVersion)
    testImplementation("io.kotlintest", "kotlintest-runner-junit5", kotlinTestVersion) {
        exclude("io.arrow-kt")
    }
    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}
