# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class devesh.ephrine.qr.common.R$string { *; }

-keep class devesh.ephrine.qr.common.R$string

-keep class devesh.ephrine.qr.common.R


-keep class devesh.ephrine.qr.common.AdMobAPI { *; }



# Keep AppAnalytics and its members
-keep class devesh.ephrine.qr.common.AppAnalytics { *; }

# Keep AppReviewTask and its members
-keep class devesh.ephrine.qr.common.AppReviewTask { *; }

# Keep BarcodeAPI and its members
-keep class devesh.ephrine.qr.common.BarcodeAPI { *; }

# Keep CachePref and its members
-keep class devesh.ephrine.qr.common.CachePref { *; }

# Keep CreateQRCodeApi and its members, including nested classes like VCard
-keep class devesh.ephrine.qr.common.CreateQRCodeApi { *; }
-keep class devesh.ephrine.qr.common.CreateQRCodeApi$* { *; } # Keeps all nested classes