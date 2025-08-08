# JitPack Usage Instructions

After pushing a tagged release to GitHub, the Kruiser library will be available through JitPack.

## Usage

### 1. Add JitPack repository to your project

**For Gradle (build.gradle.kts)**:
```kotlin
repositories {
    mavenCentral()
    maven("https://jitpack.io")
}
```

**For Gradle (build.gradle)**:
```groovy
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}
```

### 2. Add the dependency

Replace `TAG` with your actual release tag (e.g., `v1.0.0`):

**For Kotlin Multiplatform projects**:
```kotlin
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation("com.github.gaw1ik:kruiser-kmm:TAG")
            }
        }
    }
}
```

**For Android projects**:
```kotlin
dependencies {
    implementation("com.github.gaw1ik:kruiser-kmm:TAG")
}
```

**For Desktop/JVM projects**:
```kotlin
dependencies {
    implementation("com.github.gaw1ik:kruiser-kmm-desktop:TAG")
}
```

### 3. Platform-specific usage

- **Android**: Use the `android` classifier
- **iOS**: Use the `ios` classifier (or generate XCFramework)
- **Desktop/JVM**: Use the `desktop` classifier

## Creating a Release

1. Create and push a git tag:
```bash
git tag v1.0.0
git push origin v1.0.0
```

2. JitPack will automatically build the library when someone requests it for the first time.

3. Check build status at: `https://jitpack.io/#gaw1ik/kruiser-kmm`

## Example Usage in Your App

```kotlin
// In your composable
BackstackView(
    backstack = backstack,
    enterTransition = { slideInHorizontally { it } },
    exitTransition = { slideOutHorizontally { -it } }
)
```

## Notes

- The library will be built with Java 21
- All platforms (Android, iOS, Desktop) are supported
- Source code and documentation will be included in the published artifacts