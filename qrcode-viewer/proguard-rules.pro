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

-keep class devesh.ephrine.qr.qrcode_viewer.R$string { *; }

-keep class devesh.ephrine.qr.qrcode_viewer.R$string

-keep class devesh.ephrine.qr.qrcode_viewer.R



# Keep BottomSheetFragments and their members
-keep class devesh.ephrine.qr.qrcode_viewer.BottomSheetFragments.CalenderQrCodeViewBottomSheetFrag { *; }
-keep class devesh.ephrine.qr.qrcode_viewer.BottomSheetFragments.DrivingLicQRCodeViewBottomSheetFrag { *; }
-keep class devesh.ephrine.qr.qrcode_viewer.BottomSheetFragments.EmailQRViewBottomSheetFrag { *; }
-keep class devesh.ephrine.qr.qrcode_viewer.BottomSheetFragments.GeoQrCodeViewerFragment { *; }
# Add rules for any other fragments in this package if they are also being removed


-keep class devesh.ephrine.qr.qrcode_viewer.BottomSheetFragments.PhoneQrCodeBottomSheetFrag
-keep class  devesh.ephrine.qr.qrcode_viewer.BottomSheetFragments.RawTextQRCodeBottomSheetFrag
-keep class devesh.ephrine.qr.qrcode_viewer.BottomSheetFragments.URLQrCodeViewBottomSheetFrag
-keep class devesh.ephrine.qr.qrcode_viewer.BottomSheetFragments.WiFiQrCodeViewBottomSheetFrag
-keep class devesh.ephrine.qr.qrcode_viewer.fragments.CalenderQrCodeOpenFragment
-keep class devesh.ephrine.qr.qrcode_viewer.fragments.DrivingLicQRCodeOpenFragment
-keep class devesh.ephrine.qr.qrcode_viewer.fragments.EmailQROpenFragment
-keep class devesh.ephrine.qr.qrcode_viewer.fragments.GeoQrCodeOpenFragment
-keep class devesh.ephrine.qr.qrcode_viewer.fragments.PhoneQrCodeOpenFragment
-keep class devesh.ephrine.qr.qrcode_viewer.fragments.RawTextQRCodeOpenFragment
-keep class devesh.ephrine.qr.qrcode_viewer.fragments.URLQrCodeOpenFragment
-keep class devesh.ephrine.qr.qrcode_viewer.fragments.WiFiQrCodeOpenFragment