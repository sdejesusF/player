-keep class com.centralway.player.** { *; }

-keep class android.support.v4.widget.DrawerLayout { *; }
-keep class android.support.test.espresso.IdlingResource { *; }
-keep class com.google.common.base.Preconditions { *; }
-keep class android.support.v7.widget.SearchView { *; }

-keep class android.support.v7.widget.** { *; }
-keep class android.support.v4.widget.** { *; }
-keep class android.support.design.** { *; }
-keep class android.support.constraint.** { *; }
-keep class android.support.v4.** { *; }
-keep class android.support.v7.** { *; }

# For Guava:
-dontwarn javax.annotation.**
-dontwarn javax.inject.**
-dontwarn sun.misc.Unsafe

# Proguard rules that are applied to your test apk/code.
-ignorewarnings

-keepattributes *Annotation*

-dontnote junit.framework.**
-dontnote junit.runner.**

-dontwarn android.test.**
-dontwarn android.support.test.**
-dontwarn org.junit.**
-dontwarn org.hamcrest.**
-dontwarn com.squareup.javawriter.JavaWriter

# Uncomment this if you use Mockito
-dontwarn org.mockito.**
