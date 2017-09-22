# Proguard rules that are applied to your test apk/code.
-ignorewarnings

-keep class android.support.v7.widget.** { *; }
-keep class android.support.v4.widget.** { *; }
-keep class android.support.design.** { *; }
-keep class android.support.constraint.** { *; }
-keep class android.support.v4.** { *; }
-keep class android.support.v7.** { *; }

-keepattributes *Annotation*

-dontnote junit.framework.**
-dontnote junit.runner.**

-dontwarn android.test.**
-dontwarn android.support.test.**
-dontwarn org.junit.**
-dontwarn org.hamcrest.**
-dontwarn com.squareup.javawriter.JavaWriter
-dontwarn org.mockito.**