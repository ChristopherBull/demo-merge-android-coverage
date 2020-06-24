# Demo: Merging Android test coverage reports

[![Build Status](https://github.com/CaffeinatedAndroid/demo-merge-android-coverage/workflows/CI/badge.svg)](https://github.com/CaffeinatedAndroid/demo-merge-android-coverage/actions?query=workflow%3ACI)
[![Test Coverage](https://api.codeclimate.com/v1/badges/3561603f61a5ce2cc309/test_coverage)](https://codeclimate.com/github/CaffeinatedAndroid/demo-merge-android-coverage/test_coverage)
[![Maintainability](https://api.codeclimate.com/v1/badges/3561603f61a5ce2cc309/maintainability)](https://codeclimate.com/github/CaffeinatedAndroid/demo-merge-android-coverage/maintainability)

Example repository demonstrating combining Android unit and instrumented test coverage reports using [JaCoCo](https://www.jacoco.org/). It features:

- Combining Unit test coverage and Instrumented (emulator) test coverage reports.
- Supports Java, Kotlin, and Java+Kotlin projects.
- Supports usage in CI/CD workflows and pipelines.

## Code Coverage

Android Studio has built-in code coverage support, but coverage is only reported for Instrumented Tests, not Unit tests (or both combined). Further, external services that may combine reports for you (e.g., as part of your CI/CD workflow) prevent your from calculating full coverage reports locally, offline, internal to any CI workflows, and may have service costs associated with them. 

This is where this solution comes in.

### Usage

Run in a terminal (Linux, Mac, etc):

```shell
$ ./gradlew jacocoCombinedTestReports
```

Run in a terminal (Windows):

```shell
> gradlew jacocoCombinedTestReports
```

The generated HTML (and XML) reports will be located under: `app/build/reports/coverage/jacocoCombinedTestReports/`.
Note that `app/` is the default module name/directory, so the above path will work unless you have configured your projects to use a different module name (or have multiple modules).

An example usage in [a CI workflow using GitHub Actions is also available in this repository](.github/workflows/ci.yml), which uploads generated HTML as artifacts and uploads the combined JaCoCo xml report to a third-party service (demonstrated by the "test coverage" percentage badge at the top of this file).

### Configuration

The following `build.gradle` snippets should be merged into your existing `build.gradle` files (not replacing them). Complete minimal working examples are available within this repository.

In the [project-level `build.gradle`](build.gradle) file:

```gradle
buildscript {
    ext.jacocoVersion = '0.8.5'
    dependencies {
        classpath "org.jacoco:org.jacoco.core:$jacocoVersion"
    }
}
```

In your [module-level `build.gradle`](app/build.gradle) file (e.g., `/app/build.gradle`):

```gradle
apply plugin: 'jacoco'

jacoco {
    toolVersion = "$jacocoVersion"
    reportsDir = file("$buildDir/reports/coverage")
}

tasks.withType(Test) {
    jacoco.includeNoLocationClasses = true
}

task jacocoCombinedTestReports(type: JacocoReport, dependsOn: ['testDebugUnitTest', 'createDebugCoverageReport']) {
    group = "Verification"
    description = "Creates JaCoCo test coverage report for Unit and Instrumented Tests (combined) on the Debug build"
    reports {
        xml.enabled = true
        html.enabled = true
    }

    // Files to exclude:
    // Generated classes, platform classes, etc.
    def excludes = [
            '**/R.class',
            '**/R$*.class',
            '**/BuildConfig.*',
            '**/Manifest*.*',
            '**/*Test*.*',
            'android/**/*.*'
    ]

    // generated classes
    classDirectories.from = fileTree(
            dir: "$buildDir/intermediates/classes/debug",
            excludes: excludes
    ) + fileTree(
            dir: "$buildDir/tmp/kotlin-classes/debug",
            excludes: excludes
    )
    // sources
    sourceDirectories.from = [
            android.sourceSets.main.java.srcDirs,
            "src/main/kotlin"
    ]
    // Output and existing data
    // Combine Unit test and Instrumented test reports
    executionData.from = fileTree(dir: "$buildDir", includes: [
            // Unit tests coverage data
            "jacoco/testDebugUnitTest.exec",
            // Instrumented tests coverage data
            "outputs/code_coverage/debugAndroidTest/connected/*coverage.ec"
    ])
}

android {
    buildTypes {
        debug {
            testCoverageEnabled true
        }
    }
}
```

That's it, you are all set.
