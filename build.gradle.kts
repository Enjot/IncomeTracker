import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("app.cash.sqldelight") version "2.0.0"
    id ("com.google.devtools.ksp") version "1.9.10-1.0.13"

}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()

}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)

    val voyagerVersion = "1.0.0-rc07"
    val koinVersion = "3.5.0"

    // Voyager
    implementation("cafe.adriel.voyager:voyager-navigator:$voyagerVersion")
    implementation("cafe.adriel.voyager:voyager-bottom-sheet-navigator:$voyagerVersion")
    implementation("cafe.adriel.voyager:voyager-tab-navigator:$voyagerVersion")
    implementation("cafe.adriel.voyager:voyager-transitions:$voyagerVersion")
    implementation ("cafe.adriel.voyager:voyager-koin:$voyagerVersion")
    
    // SQLDelight
    implementation("app.cash.sqldelight:sqlite-driver:2.0.0")
    implementation("app.cash.sqldelight:coroutines-extensions:2.0.0")

    // Koin
    implementation ("io.insert-koin:koin-core:$koinVersion")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "IncomeTracker"
            packageVersion = "1.0.0"
        }
    }
}

sqldelight {
    databases {
        create("Database") {
            packageName.set("com.example")
        }
    }
}