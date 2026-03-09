# Kotlin Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** { *** Companion; }
-keepclasseswithmembers class kotlinx.serialization.json.** { kotlinx.serialization.KSerializer serializer(...); }
-keep,includedescriptorclasses class com.hapkonic.tailorapp.**$$serializer { *; }
-keepclassmembers class com.hapkonic.tailorapp.** {
    *** Companion;
}
-keepclasseswithmembers class com.hapkonic.tailorapp.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Koin
-keep class org.koin.** { *; }

# Firebase
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }
