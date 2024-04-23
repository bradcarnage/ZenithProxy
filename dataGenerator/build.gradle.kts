plugins {
    id("fabric-loom") version "1.6-SNAPSHOT"
}

group = "com.zenith"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://maven.parchmentmc.org")
}

loom {
    accessWidenerPath = file("src/main/resources/zenithproxy.accesswidener")
    runs {
        getByName("server") {
            ideConfigGenerated(true)
            server()
            property("data.dir", project.layout.buildDirectory.file("data").get().asFile.absolutePath)
        }
    }
}

dependencies {
    minecraft("com.mojang:minecraft:1.20.5")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-1.20.4:2024.04.14@zip")
    })
    modImplementation("net.fabricmc:fabric-loader:0.15.10")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.97.5+1.20.5")
}
