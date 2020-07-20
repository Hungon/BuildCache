# BuildCache
the project to explorer for improving build performance.


#### Enable the Build Cache
By default, the build cache is not enabled. You can enable the build cache in a couple of ways:

Run with --build-cache on the command-line
Gradle will use the build cache for this build only.

Put org.gradle.caching=true in your gradle.properties
Gradle will try to reuse outputs from previous builds for all builds, unless explicitly disabled with --no-build-cache.

When the build cache is enabled, it will store build outputs in the Gradle user home. For configuring this directory or different kinds of build caches see Configure the Build Cache.

## Reference 
[android-cache-fix](https://plugins.gradle.org/plugin/org.gradle.android.cache-fix)
[Annotation processing with kotlin](https://kotlinlang.org/docs/reference/kapt.html)
[Improving build performance of gradle](https://guides.gradle.org/performance/)
