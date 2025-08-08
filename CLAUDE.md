# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Kruiser is a Kotlin Multiplatform navigation library that provides a composable backstack-based navigation system for Android, iOS, and Desktop platforms using Compose Multiplatform. The library consists of:

- **Core navigation components**: BackStackEntry, Destination interface, and BackstackView with animated transitions
- **Sample application**: Demonstrates usage across all supported platforms
- **Platform-specific implementations**: Android library, iOS framework, and JVM desktop support

## Project Structure

```
kruiser-kmm/
├── core/                    # Main navigation library (KMP module)
│   └── src/
│       ├── commonMain/      # Shared navigation logic
│       │   ├── core/        # BackStackEntry, Destination, utilities
│       │   └── ui/          # BackstackView, CompositionLocals, ViewModelStoreHolder
│       ├── androidMain/     # Android-specific implementations
│       ├── iosMain/         # iOS-specific implementations
│       └── desktopMain/     # Desktop-specific implementations
├── sample/                  # Demo application using the core library
├── iosApp/                  # iOS app wrapper (Xcode project)
└── gradle/                  # Gradle configuration and dependencies
```

## Development Commands

### Build Commands
```bash
# Build all modules and platforms
./gradlew build

# Build specific modules
./gradlew :core:build
./gradlew :sample:build

# Build iOS framework
./gradlew :core:linkXcFramework
```

### Test Commands
```bash
# Run all tests
./gradlew test

# Run Android instrumented tests
./gradlew :core:connectedAndroidDeviceTest

# Run specific platform tests
./gradlew :core:desktopTest
./gradlew :core:androidHostTest
```

### Platform-Specific Development
```bash
# Run Android sample
./gradlew :sample:installDebug

# Run desktop sample
./gradlew :sample:runDistributable

# For iOS development
open iosApp/iosApp.xcodeproj
```

### Clean and Refresh
```bash
# Clean build artifacts
./gradlew clean

# Refresh dependencies
./gradlew --refresh-dependencies
```

## Key Architecture Concepts

### Navigation System
- **Destination**: Interface representing any navigable screen/dialog with a `@Composable Content()` function
- **BackStackEntry**: Immutable data class wrapping a Destination with a unique key for state management
- **BackstackView**: Composable that renders the navigation stack with customizable animations

### State Management
- Uses `SaveableStateHolder` keyed by BackStackEntry.key for state preservation
- Automatic cleanup of saved states when entries are removed from backstack
- ViewModel scoping tied to BackStackEntry keys via `LocalBackstackEntry`

### Animation System
- **BackstackEvent**: GROW (push), SHRINK (pop), IDLE states
- Customizable enter/exit transitions for push and pop operations
- Default slide animations with horizontal transitions

## Platform Configuration

### Android
- Minimum SDK: 24, Target SDK: 36
- Namespace: `net.gaw.kruiser` (library), `net.gaw.kruiser.sample` (sample)
- Uses Android Kotlin Multiplatform Library plugin

### iOS
- Generates XCFramework named `kruiser-libKit`
- Supports iOS (arm64), iOS Simulator (arm64), and iOS x64
- Static framework binaries for easier integration

### Desktop
- JVM target with Compose Desktop
- Main class: `net.gaw.kruiser.sample.MainKt`
- Supports DMG, MSI, and DEB package formats

## Dependencies

### Core Library
- Kotlin 2.2.0 with Compose Multiplatform 1.9.0-beta01
- AndroidX Lifecycle ViewModel and Runtime Compose
- Kotlinx Coroutines with Swing support for desktop

### Build Tools
- Android Gradle Plugin 8.11.1
- Kotlin Gradle Plugin with KMP and Compose compiler support
- Hot Reload plugin for development (1.0.0-beta04)