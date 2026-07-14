group = "app.franticg33k"

patches {
    about {
        name = "Phioster's JellyWatch Patches"
        description = "JellyWatch patch, kept working on current app versions. " +
            "Fork of franticg33k's Morphe Patches, trimmed to JellyWatch only."
        source = "git@github.com:Phioster/morphe-patches.git"
        author = "Phioster (fork of franticg33k)"
        contact = "na"
        website = "https://github.com/Phioster/morphe-patches"
        license = "GPLv3"
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}

// Separate configuration so gson is available at runtime for the
// generatePatchesList task but never bundled into the APK.
val patchListGeneratorClasspath: Configuration by configurations.creating

dependencies {
    compileOnly(libs.gson)
    patchListGeneratorClasspath(libs.gson)
}

tasks {
    register<JavaExec>("generatePatchesList") {
        description = "Build patch with patch list"

        dependsOn(build)

        classpath = sourceSets["main"].runtimeClasspath + patchListGeneratorClasspath
        mainClass.set("util.PatchListGeneratorKt")
    }

    // Used by gradle-semantic-release-plugin.
    publish {
        dependsOn("generatePatchesList")
    }
}