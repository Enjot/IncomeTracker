import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm") version "1.9.20"
    id("org.jetbrains.compose") version "1.5.10"
    id("app.cash.sqldelight") version "2.0.0"
    id ("com.google.devtools.ksp") version "1.9.20-1.0.13"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
    maven ("https://jitpack.io")
}

dependencies {
    implementation(compose.desktop.windows_x64)
    implementation(compose.material3)
    implementation(compose.materialIconsExtended)
    
    // JSystem Theme Detector
    implementation ("com.github.Dansoftowner:jSystemThemeDetector:3.8")
    
    // Voyager
    implementation("cafe.adriel.voyager:voyager-navigator:1.0.0-rc10")
    implementation("cafe.adriel.voyager:voyager-koin:1.0.0-rc10")
    
    // SQLDelight
    implementation("app.cash.sqldelight:sqlite-driver:2.0.0")
    implementation("app.cash.sqldelight:coroutines-extensions:2.0.0")

    // Koin
    implementation ("io.insert-koin:koin-core:3.5.0")

    //AAY-chart
    implementation("io.github.thechance101:chart:Beta-0.0.5")

}

compose.desktop {
    
    application {
        mainClass = "MainKt"

        nativeDistributions {
            
            modules("java.sql")
            targetFormats(TargetFormat.Dmg, TargetFormat.Exe, TargetFormat.Deb)
            packageName = "IncomeTracker"
            packageVersion = "1.0.0"
        }
        
    }
}

sqldelight {
    
    databases {
        linkSqlite.set(true)
        create("Database") {
            packageName.set("com.example")
//            srcDirs.setFrom("src/main/sqldelight")
//            srcDirs("src/main/sqldelight", "main/sqldelight")
//            dialect("app.cash.sqldelight:sqlite-3-38-dialect:2.0.0")
            
//            generateAsync.set(true)
        }
        
    }
}