import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.SourcesJar
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.mavenPublish)
}

android {
    namespace = "me.xechoz.localekit"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    api(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    testImplementation(libs.junit)
}

mavenPublishing {
    publishToMavenCentral(automaticRelease = true)
    signAllPublications()

    configureBasedOnAppliedPlugins(
        javadocJar = JavadocJar.Empty(),
        sourcesJar = SourcesJar.Sources(),
    )

    coordinates("io.github.xechoz", "locale", "1.0.0")

    pom {
        name = "LocaleKit"
        description = "Android per-app language and system language settings toolkit."
        inceptionYear = "2026"
        url = "https://github.com/xechoz/LocaleKit/"
        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "https://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
        developers {
            developer {
                id = "xechoz"
                name = "xechoz"
                url = "https://github.com/xechoz/"
            }
        }
        scm {
            url = "https://github.com/xechoz/LocaleKit/"
            connection = "scm:git:git://github.com/xechoz/LocaleKit.git"
            developerConnection = "scm:git:ssh://git@github.com/xechoz/LocaleKit.git"
        }
    }
}
