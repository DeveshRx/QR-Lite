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