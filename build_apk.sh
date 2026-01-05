#!/bin/bash

# Professional Build Script for Android APK
# This script ensures the environment is clean and builds a signed/obfuscated APK

PROJECT_DIR="android-app"

echo "🚀 Starting Professional Build Process..."

cd $PROJECT_DIR

# 1. Clean the project
echo "🧹 Cleaning project..."
./gradlew clean

# 2. Run Static Analysis
echo "🔍 Running Detekt static analysis..."
./gradlew detekt || { echo "❌ Detekt failed. Please fix code smells."; exit 1; }

# 3. Build Release APK
# Note: In a real production env, you would have signingConfigs set up in build.gradle
echo "📦 Building Release APK..."
./gradlew assembleRelease

if [ $? -eq 0 ]; then
    echo "✅ Build Successful!"
    echo "📍 APK Location: app/build/outputs/apk/release/app-release-unsigned.apk"
    echo "💡 Note: This APK is unsigned. For production, use 'bundleRelease' and sign with your keystore."
else
    echo "❌ Build Failed."
    exit 1
fi
