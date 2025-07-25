package devesh.ephrine.qr.code;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import devesh.app.billing.GPlayBilling;
import devesh.ephrine.qr.code.databinding.ActivityMainBinding;
import devesh.ephrine.qr.code.fragment.CameraFragment;
import devesh.ephrine.qr.code.fragment.CreateQRFragment;
import devesh.ephrine.qr.code.fragment.HistoryFragment;
import devesh.ephrine.qr.code.fragment.SettingsFragment;
import devesh.ephrine.qr.common.AdMobAPI;
import devesh.ephrine.qr.common.AppAnalytics;
import devesh.ephrine.qr.common.AppReviewTask;
import devesh.ephrine.qr.common.BarcodeAPI;
import devesh.ephrine.qr.common.CachePref;
import devesh.ephrine.qr.createqr.CreateQRCodeActivity;
import devesh.ephrine.qr.database.AppDatabase;
import devesh.ephrine.qr.database.QRCodeFile;
import devesh.ephrine.qr.database.QRCodeTypeConstants;
import devesh.ephrine.qr.database.type.QRDrivLicId;
import devesh.ephrine.qr.database.type.QREmail;
import devesh.ephrine.qr.database.type.QREvent;
import devesh.ephrine.qr.database.type.QRGeoLoc;
import devesh.ephrine.qr.database.type.QRPhone;
import devesh.ephrine.qr.database.type.QRSms;
import devesh.ephrine.qr.database.type.QRText;
import devesh.ephrine.qr.database.type.QRWebsite;
import devesh.ephrine.qr.database.type.QRWifi;
import devesh.ephrine.qr.qrcode_viewer.BottomSheetFragments.CalenderQrCodeViewBottomSheetFrag;
import devesh.ephrine.qr.qrcode_viewer.BottomSheetFragments.DrivingLicQRCodeViewBottomSheetFrag;
import devesh.ephrine.qr.qrcode_viewer.BottomSheetFragments.EmailQRViewBottomSheetFrag;
import devesh.ephrine.qr.qrcode_viewer.BottomSheetFragments.GeoQrCodeViewerFragment;
import devesh.ephrine.qr.qrcode_viewer.BottomSheetFragments.PhoneQrCodeBottomSheetFrag;
import devesh.ephrine.qr.qrcode_viewer.BottomSheetFragments.RawTextQRCodeBottomSheetFrag;
import devesh.ephrine.qr.qrcode_viewer.BottomSheetFragments.URLQrCodeViewBottomSheetFrag;
import devesh.ephrine.qr.qrcode_viewer.BottomSheetFragments.WiFiQrCodeViewBottomSheetFrag;
import devesh.ephrine.qr.qrcode_viewer.fragments.CalenderQrCodeOpenFragment;
import devesh.ephrine.qr.qrcode_viewer.fragments.DrivingLicQRCodeOpenFragment;
import devesh.ephrine.qr.qrcode_viewer.fragments.EmailQROpenFragment;
import devesh.ephrine.qr.qrcode_viewer.fragments.GeoQrCodeOpenFragment;
import devesh.ephrine.qr.qrcode_viewer.fragments.PhoneQrCodeOpenFragment;
import devesh.ephrine.qr.qrcode_viewer.fragments.RawTextQRCodeOpenFragment;
import devesh.ephrine.qr.qrcode_viewer.fragments.URLQrCodeOpenFragment;
import devesh.ephrine.qr.qrcode_viewer.fragments.WiFiQrCodeOpenFragment;

public class MainActivity extends AppCompatActivity {

    static final int PERMISSION_REQUEST_CODE = 5050;
    final static boolean isAutoScan = true;
    String TAG = "MainAct";
    ActivityMainBinding mBinding;
    FragmentManager fragmentManager;
    Fragment fragmentScreen;
    Fragment oldFrag;
    BarcodeAPI barcodeAPI;
    int CurrentSelected;
    Gson gson;
    AppDatabase AppDB;
    String oldBarcodeRaw = "";
    CachePref cachePref;
    AppReviewTask appReviewTask;
    AdMobAPI adMobAPI;
    boolean isIntAdShowed;
    boolean isSubscribed;
    GPlayBilling gPlayBilling;

    AppAnalytics appAnalytics;
    ActivityResultLauncher<Intent> openGalleryApp = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //Bundle extras = result.getData().getExtras();
                    Log.d(TAG, "onActivityResult: " + result);
                    //Bitmap imageBitmap = (Bitmap) extras.get("data");
                    //   imageView1.setImageBitmap(imageBitmap);
                    //     Bundle extras = result.getData().getExtras();
                    //Bundle extras = result.getData().getExtras();
                    if (result.getData() != null) {
                        Intent i = result.getData();
                        Uri uri = i.getData();
                        Log.d(TAG, "onActivityResult: " + i.getData());

                        ShowLoadingView(false);

                        CropImage.activity(uri)
                                .setAutoZoomEnabled(true)
                                .setMultiTouchEnabled(true)
                                .start(MainActivity.this);


                    } else {
                        ShowLoadingView(false);

                    }


                }
            });
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isIntAdShowed = false;
        isSubscribed = false;
        //DynamicColors.applyToActivitiesIfAvailable(getApplication());
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Custom back press logic
                if (fragmentManager.findFragmentByTag("home") != null) {
                    if (fragmentManager.findFragmentByTag("home").isVisible()) {
                        fragmentManager.findFragmentByTag("home").onDestroy();
                        MainActivity.this.finish();
                    }

                }

                //     super.onBackPressed();

                if (fragmentManager != null) {
                    if (fragmentManager.findFragmentByTag("create") != null) {
                        if (fragmentManager.findFragmentByTag("create").isVisible()) {

                            MainActivity.this.finish();

                        }
                    }


                }

                if (fragmentManager.findFragmentByTag("history") != null) {
                    if (fragmentManager.findFragmentByTag("history").isVisible()) {
                        fragmentManager.findFragmentByTag("history").onDestroy();
                        if (adMobAPI.mInterstitialAd != null) {
                            adMobAPI.ShowInterstitialAd();
                        }
                    }

                }

                setEnabled(false);
                getOnBackPressedDispatcher().onBackPressed();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);



        gson = new Gson();
        barcodeAPI = new BarcodeAPI(this);
        fragmentScreen = new CameraFragment();
        fragmentManager = getSupportFragmentManager();
        CurrentSelected = R.id.menu_scan;
        cachePref = new CachePref(this);

        //   isAutoScan=cachePref.getBoolean(getString(devesh.ephrine.qr.common.R.string.Pref_AutoScan));

        //Initialization
        AppDB = Room.databaseBuilder(this,
                        AppDatabase.class, getString(devesh.ephrine.qr.database.R.string.DB_NAME))
                .fallbackToDestructiveMigration().allowMainThreadQueries().build();
        ReceiveShareIntent();
        appReviewTask = new AppReviewTask(this, this);

        adMobAPI = new AdMobAPI(this);
        RequestPermission();
        appAnalytics = new AppAnalytics(getApplication(), this);
        gPlayBilling = new GPlayBilling(this, (billingResult, list) -> {

        });

        Map<String, String> aa = new HashMap<>();
        aa.put("App_Open", "1");
        appAnalytics.logEvent(getString(devesh.ephrine.qr.common.R.string.ANALYTICS_EVENT_APP_OPEN), aa);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        Log.d(TAG, "FCM Token : "+token);

                    }
                });

        notification();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //    isAutoScan=cachePref.getBoolean(getString(devesh.ephrine.qr.common.R.string.Pref_AutoScan));
        BillingStart();

        if (fragmentScreen == null) {
            fragmentScreen = new CameraFragment();

        }

        if (fragmentManager.findFragmentByTag("home") == null) {

            if (fragmentManager.getFragments().isEmpty()) {
                fragmentManager.beginTransaction()
                        .add(mBinding.fragmentContainerView.getId(), fragmentScreen, "home")

                        .commit();

            }

        } else {

        }

        mBinding.bottomNavigation.setOnItemSelectedListener(item -> {

            oldBarcodeRaw = "";
            if (adMobAPI.mInterstitialAd != null && !isIntAdShowed && !isSubscribed) {
                adMobAPI.ShowInterstitialAd();

            }


            if (item.getItemId() == R.id.menu_scan) {
                setFragment(new CameraFragment(), null, "home");

            } else if (item.getItemId() == R.id.menu_create) {
                setFragment(new CreateQRFragment(), null, "create");


            } else if (item.getItemId() == R.id.menu_history) {
                setFragment(new HistoryFragment(), null, "history");


            } else if (item.getItemId() == R.id.menu_settings) {
                setFragment(new SettingsFragment(), null, "settings");

            }

            if (adMobAPI.mInterstitialAd == null && !isIntAdShowed && !isSubscribed) {
                adMobAPI.LoadInterstitialAd(this);
            }


            intAdListner();

            if (!appReviewTask.isAppReviewed()) {
                appReviewTask.requestAppReview();
            }

            Map<String, String> aa = new HashMap<>();
            aa.put(getString(devesh.ephrine.qr.common.R.string.ANALYTICS_EVENT_NAVIGATION_BAR), "1");
            appAnalytics.logEvent(getString(devesh.ephrine.qr.common.R.string.ANALYTICS_EVENT_NAVIGATION_BAR), aa);


            return true;
        });

        mBinding.bottomNavigation.setOnItemReselectedListener(item -> {


        });

    }



    void intAdListner() {
        if (adMobAPI.mInterstitialAd != null) {
            adMobAPI.mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    // Called when fullscreen content is dismissed.
                    Log.d("TAG", "The ad was dismissed.");
                    isIntAdShowed = true;
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    // Called when fullscreen content failed to show.
                    Log.d("TAG", "The ad failed to show.");
                    isIntAdShowed = true;
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    // Called when fullscreen content is shown.
                    // Make sure to set your reference to null so you don't
                    // show it a second time.
                    //  mInterstitialAd = null;
                    Log.d("TAG", "The ad was shown.");
                    isIntAdShowed = true;
                }
            });

        }
    }

    void setFragment(Fragment fragment, Bundle bundle, String tag) {
        oldFrag = fragmentScreen;
        fragmentScreen = fragment;
        if (bundle != null) {

            fragmentScreen.setArguments(bundle);

        }


        fragmentManager.beginTransaction()
                //  .hide(oldFrag)
                .replace(mBinding.fragmentContainerView.getId(), fragmentScreen, tag)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)

                //    .setReorderingAllowed(true)
                //  .addToBackStack("app")
                .commit();



        /*
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack("home")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(mBinding.fragmentContainerFrame.getId(), BottomSheetFragments, null)
                .commit();*/
    }

   /* public void analyzeIMG(InputImage image) {
       // @SuppressLint("UnsafeOptInUsageError")
      //  InputImage image = InputImage.fromMediaImage(imageProxy.getImage(), 0);
        barcodeScan(image);

        //imageProxy.close();


    }*/

    void setFragment(Fragment fragment, Bundle bundle, String tag, boolean add2stack) {
        oldFrag = fragmentScreen;
        fragmentScreen = fragment;
        if (bundle != null) {

            fragmentScreen.setArguments(bundle);

        }


        fragmentManager.beginTransaction()
                //   .hide(oldFrag)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .replace(mBinding.fragmentContainerView.getId(), fragmentScreen, tag)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)

                .setReorderingAllowed(true)
                .addToBackStack("app")
                .commit();



        /*
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack("home")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(mBinding.fragmentContainerFrame.getId(), BottomSheetFragments, null)
                .commit();*/
    }

    public void barcodeScan(InputImage image) {

        barcodeAPI.getScanner().process(image)
                .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                                          @Override
                                          public void onSuccess(List<Barcode> barcodes) {
                                              // Task completed successfully
                                              if (barcodes.isEmpty()) {
                                                  BarcodeNotFoundError();
                                                  Log.d(TAG, "onSuccess: barcodes.isEmpty()");
                                              }
                                              getBarcode(barcodes);
                                              ShowLoadingView(false);

                                          }
                                      }
                )
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                        // ...
                        Log.d(TAG, "onFailure: " + e);
                        BarcodeNotFoundError();
                        ShowLoadingView(false);
                    }
                });


    }

    public void getBarcode(List<Barcode> barcodes) {
        if (barcodes != null) {
            if (!barcodes.isEmpty()) {
                // Log.d(TAG, "getBarcode: barcode: "+barcodes.get(0).getRawBytes());
                Barcode barcode = barcodes.get(0);
                Log.d(TAG, "getBarcode: RAW Value: " + barcode.getRawValue());
                if (oldBarcodeRaw.equals(barcode.getRawValue())) {

                } else {
                    oldBarcodeRaw = barcode.getRawValue();

                    Rect bounds = barcode.getBoundingBox();
                    Point[] corners = barcode.getCornerPoints();

                    String rawValue = barcode.getRawValue();
                    // Log.d(TAG, "getBarcode: RAW Value: "+rawValue);
                    int valueType = barcode.getValueType();
                    // See API reference for complete list of supported types
                    switch (valueType) {
                        case Barcode.TYPE_EMAIL:
                            OpenFragmentEmail(barcode, true);
                            break;
                        case Barcode.TYPE_URL:
                            //    String title = barcode.getUrl().getTitle();
                            String url = barcode.getUrl().getUrl();
                            OpenFragmentURL(url, true);
                            break;

                        case Barcode.TYPE_TEXT:
                            String rawtext = barcode.getRawValue();
                            openFragmentTextQRCode(rawtext, true);
                            break;
                        case Barcode.TYPE_PHONE:
                            OpenFragmentPhone(barcode.getPhone(), true);
                            break;

                        case Barcode.TYPE_SMS:

                            OpenFragmentPhone(barcode.getSms(), true);
                            break;

                        case Barcode.TYPE_WIFI:

                            OpenFragmentWifi(barcode.getWifi(), true);
                            break;
                        case Barcode.TYPE_GEO:
                            openFragmentGeoQRCode(barcode.getGeoPoint(), true);
                            break;
                        case Barcode.TYPE_UNKNOWN:
                            openFragmentTextQRCode(barcode.getRawValue(), true);
                            break;
                        case Barcode.TYPE_CALENDAR_EVENT:
                            openFragmentCalenderQRCode(barcode.getCalendarEvent(), true);
                            break;
                        case Barcode.TYPE_DRIVER_LICENSE:
                            openFragmentDrivingLicQRCode(barcode.getDriverLicense(), true);
                            break;
                        default:
                            openFragmentTextQRCode(barcode.getRawValue(), true);
                            break;
                    }


                }


                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        oldBarcodeRaw = " ";
                        if (!appReviewTask.isAppReviewed()) {
                            appReviewTask.requestAppReview();
                        }

                    }
                }, 4000);

                Map<String, String> aa = new HashMap<>();
                aa.put(getString(devesh.ephrine.qr.common.R.string.ANALYTICS_EVENT_BARCODE_SCANNED), "1");
                appAnalytics.logEvent(getString(devesh.ephrine.qr.common.R.string.ANALYTICS_EVENT_BARCODE_SCANNED), aa);


   /* for (Barcode barcode: barcodes) {

        Rect bounds = barcode.getBoundingBox();
        Point[] corners = barcode.getCornerPoints();

        String rawValue = barcode.getRawValue();
        Log.d(TAG, "getBarcode: RAW Value: "+rawValue);
        int valueType = barcode.getValueType();
        // See API reference for complete list of supported types
        switch (valueType) {
            case Barcode.TYPE_WIFI:
                String ssid = barcode.getWifi().getSsid();
                String password = barcode.getWifi().getPassword();
                int type = barcode.getWifi().getEncryptionType();
                break;
            case Barcode.TYPE_URL:
                String title = barcode.getUrl().getTitle();
                String url = barcode.getUrl().getUrl();
                break;

            case Barcode.TYPE_TEXT:
                String rawtext = barcode.getRawValue();

                RawTextQRCodeBottomSheetFrag qview=new RawTextQRCodeBottomSheetFrag();
                Bundle bundle=new Bundle();
                bundle.putString("t",rawtext);
                qview.setArguments(bundle);
                qview.show(fragmentManager,"view");


                break;
        }
    }
*/


            }

        }

    }

    public void ShowLoadingView(boolean isShow) {
        runOnUiThread(() -> {
            Log.d(TAG, "ShowLoadingView: isShow: " + isShow);
            if (isShow) {
                mBinding.LoadingView.getRoot().setVisibility(View.VISIBLE);
            } else {
                mBinding.LoadingView.getRoot().setVisibility(View.GONE);
            }
        });

    }

    // Open QRCode Results Fragment
    void openFragmentTextQRCode(String text, boolean add2DB) {

        RawTextQRCodeBottomSheetFrag qview = new RawTextQRCodeBottomSheetFrag();
        Bundle bundle = new Bundle();
        bundle.putString("t", text);
        qview.setArguments(bundle);
        qview.show(fragmentManager, "view");

        if (add2DB) {

            QRText qrText = new QRText();
            qrText.text = text;
            QRCodeFile qrCodeFile = new QRCodeFile();
            qrCodeFile.epoch = System.currentTimeMillis();
            qrCodeFile.type = QRCodeTypeConstants.QRCODE_TYPE_TEXT;
            qrCodeFile.qrText = qrText;
            AppDB.qrcodeDao().insert(qrCodeFile);

        }


        Map<String, String> aa = new HashMap<>();
        aa.put(getString(devesh.ephrine.qr.common.R.string.ANALYTICS_EVENT_BARCODE_SCANNED_TEXT), "1");
        appAnalytics.logEvent(getString(devesh.ephrine.qr.common.R.string.ANALYTICS_EVENT_BARCODE_SCANNED_TEXT), aa);


    }

    void OpenFragmentURL(String url, boolean add2DB) {
        URLQrCodeViewBottomSheetFrag qview = new URLQrCodeViewBottomSheetFrag();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        qview.setArguments(bundle);
        qview.show(fragmentManager, "view");

//        val modalBottomSheetBehavior = (modalBottomSheet.dialog as BottomSheetDialog).behavior


        if (add2DB) {
            QRWebsite qrWebsite = new QRWebsite();
            qrWebsite.url = url;
            QRCodeFile qrCodeFile = new QRCodeFile();
            qrCodeFile.type = QRCodeTypeConstants.QRCODE_TYPE_WEBSITE;
            qrCodeFile.qrWebsite = qrWebsite;
            qrCodeFile.epoch = System.currentTimeMillis();

            AppDB.qrcodeDao().insert(qrCodeFile);

        }

        Map<String, String> aa = new HashMap<>();
        aa.put(getString(devesh.ephrine.qr.common.R.string.ANALYTICS_EVENT_BARCODE_SCANNED_URL), "1");
        appAnalytics.logEvent(getString(devesh.ephrine.qr.common.R.string.ANALYTICS_EVENT_BARCODE_SCANNED_URL), aa);


    }

    void OpenFragmentEmail(Barcode barcode, boolean add2DB) {
        EmailQRViewBottomSheetFrag qview = new EmailQRViewBottomSheetFrag();
        Bundle bundle = new Bundle();

        String email = barcode.getEmail().getAddress();
        String subject;
        if (barcode.getEmail().getSubject() != null || barcode.getEmail().getSubject() != "") {
            subject = barcode.getEmail().getSubject();
        } else {
            subject = "x";
        }

        String body;
        if (barcode.getEmail().getBody() != null || barcode.getEmail().getBody() != "") {
            body = barcode.getEmail().getBody();
        } else {
            body = "x";
        }
        int type = barcode.getEmail().getType();

        bundle.putString("email", email);
        bundle.putString("sub", subject);
        bundle.putString("body", body);
        bundle.putInt("type", type);
        qview.setArguments(bundle);
        qview.show(fragmentManager, "view");

        if (add2DB) {
            QREmail qrEmail = new QREmail();
            qrEmail.msg = body;
            qrEmail.email = email;
            qrEmail.Cc = "";
            qrEmail.Bcc = "";
            qrEmail.subject = subject;
            qrEmail.email_type = type;

            QRCodeFile qrCodeFile = new QRCodeFile();
            qrCodeFile.type = QRCodeTypeConstants.QRCODE_TYPE_EMAIL;
            qrCodeFile.email = qrEmail;
            qrCodeFile.epoch = System.currentTimeMillis();

            AppDB.qrcodeDao().insert(qrCodeFile);

        }

        Map<String, String> aa = new HashMap<>();
        aa.put(getString(devesh.ephrine.qr.common.R.string.ANALYTICS_EVENT_BARCODE_SCANNED_EMAIL), "1");
        appAnalytics.logEvent(getString(devesh.ephrine.qr.common.R.string.ANALYTICS_EVENT_BARCODE_SCANNED_EMAIL), aa);

    }

    void OpenFragmentPhone(Barcode.Phone phone, boolean add2DB) {
        int type = phone.getType();
        String ph = phone.getNumber();
        PhoneQrCodeBottomSheetFrag qview = new PhoneQrCodeBottomSheetFrag();
        Bundle bundle = new Bundle();
        bundle.putString("phone", ph);
        qview.setArguments(bundle);
        qview.show(fragmentManager, "view");

        if (add2DB) {
            QRPhone qrPhone = new QRPhone();
            qrPhone.Phone = ph;
            qrPhone.CountryCode = "";

            QRCodeFile qrCodeFile = new QRCodeFile();
            qrCodeFile.type = QRCodeTypeConstants.QRCODE_TYPE_Tele;
            qrCodeFile.qrPhone = qrPhone;
            qrCodeFile.epoch = System.currentTimeMillis();

            AppDB.qrcodeDao().insert(qrCodeFile);
        }
        Map<String, String> aa = new HashMap<>();
        aa.put(getString(devesh.ephrine.qr.common.R.string.ANALYTICS_EVENT_BARCODE_SCANNED_PHONE), "1");
        appAnalytics.logEvent(getString(devesh.ephrine.qr.common.R.string.ANALYTICS_EVENT_BARCODE_SCANNED_PHONE), aa);


    }

    void OpenFragmentPhone(Barcode.Sms sms, boolean add2DB) {
        String msg = sms.getMessage();
        String ph = sms.getPhoneNumber();
        PhoneQrCodeBottomSheetFrag qview = new PhoneQrCodeBottomSheetFrag();
        Bundle bundle = new Bundle();
        bundle.putString("phone", ph);
        bundle.putString("msg", msg);
        qview.setArguments(bundle);
        qview.show(fragmentManager, "view");

        if (add2DB) {
            QRSms qrSms = new QRSms();
            qrSms.sms_phone = ph;
            qrSms.sms = msg;
            QRCodeFile qrCodeFile = new QRCodeFile();
            qrCodeFile.type = QRCodeTypeConstants.QRCODE_TYPE_SMS;
            qrCodeFile.qrSms = qrSms;
            qrCodeFile.epoch = System.currentTimeMillis();

            AppDB.qrcodeDao().insert(qrCodeFile);
        }

        Map<String, String> aa = new HashMap<>();
        aa.put(getString(devesh.ephrine.qr.common.R.string.ANALYTICS_EVENT_BARCODE_SCANNED_PHONE_SMS), "1");
        appAnalytics.logEvent(getString(devesh.ephrine.qr.common.R.string.ANALYTICS_EVENT_BARCODE_SCANNED_PHONE_SMS), aa);


    }

    void OpenFragmentWifi(Barcode.WiFi wifi, boolean add2DB) {
        String ssid = wifi.getSsid();
        String pass = wifi.getPassword();
        int encType = wifi.getEncryptionType();
        WiFiQrCodeViewBottomSheetFrag qview = new WiFiQrCodeViewBottomSheetFrag();
        Bundle bundle = new Bundle();
        bundle.putString("ssid", ssid);
        bundle.putString("pass", pass);
        bundle.putInt("type", encType);

        qview.setArguments(bundle);
        qview.show(fragmentManager, "view");

        if (add2DB) {
            QRWifi qrWifi = new QRWifi();
            if (encType == WiFiQrCodeViewBottomSheetFrag.TYPE_OPEN) {
                qrWifi.WPA = "nopass";
            } else if (encType == WiFiQrCodeViewBottomSheetFrag.TYPE_WEP) {
                qrWifi.WPA = "WEP";
            } else if (encType == WiFiQrCodeViewBottomSheetFrag.TYPE_WPA) {
                qrWifi.WPA = "WPA";
            } else {
                qrWifi.WPA = "";
            }
            qrWifi.isHidden = false;
            qrWifi.Password = pass;
            qrWifi.SSID = ssid;

            QRCodeFile qrCodeFile = new QRCodeFile();
            qrCodeFile.type = QRCodeTypeConstants.QRCODE_TYPE_WIFI;
            qrCodeFile.qrWifi = qrWifi;
            qrCodeFile.epoch = System.currentTimeMillis();

            AppDB.qrcodeDao().insert(qrCodeFile);
        }

        Map<String, String> aa = new HashMap<>();
        aa.put(getString(devesh.ephrine.qr.common.R.string.ANALYTICS_EVENT_BARCODE_SCANNED_WIFI), "1");
        appAnalytics.logEvent(getString(devesh.ephrine.qr.common.R.string.ANALYTICS_EVENT_BARCODE_SCANNED_WIFI), aa);

    }

    void openFragmentGeoQRCode(Barcode.GeoPoint geo, boolean add2DB) {

        GeoQrCodeViewerFragment qview = new GeoQrCodeViewerFragment();
        Bundle bundle = new Bundle();
        bundle.putDouble("lat", geo.getLat());
        bundle.putDouble("lng", geo.getLng());
        qview.setArguments(bundle);
        qview.show(fragmentManager, "view");

        if (add2DB) {
            QRGeoLoc qrGeoLoc = new QRGeoLoc();
            qrGeoLoc.longt = String.valueOf(geo.getLng());
            qrGeoLoc.lat = String.valueOf(geo.getLat());

            QRCodeFile qrCodeFile = new QRCodeFile();
            qrCodeFile.type = QRCodeTypeConstants.QRCODE_TYPE_Geo;
            qrCodeFile.qrGeoLoc = qrGeoLoc;
            qrCodeFile.epoch = System.currentTimeMillis();

            AppDB.qrcodeDao().insert(qrCodeFile);
        }

        Map<String, String> aa = new HashMap<>();
        aa.put(getString(devesh.ephrine.qr.common.R.string.ANALYTICS_EVENT_BARCODE_SCANNED_Geo), "1");
        appAnalytics.logEvent(getString(devesh.ephrine.qr.common.R.string.ANALYTICS_EVENT_BARCODE_SCANNED_Geo), aa);


    }

    void openFragmentCalenderQRCode(Barcode.CalendarEvent cal, boolean add2DB) {
        CalenderQrCodeViewBottomSheetFrag qview = new CalenderQrCodeViewBottomSheetFrag();
        Bundle bundle = new Bundle();
        bundle.putString("json", gson.toJson(cal));
        qview.setArguments(bundle);
        qview.show(fragmentManager, "view");

        if (add2DB) {
            QREvent qrEvent = new QREvent();
            qrEvent.EndDateTime = cal.getEnd().getDay() + "-" + (cal.getEnd().getMonth() + 1) + "-" + cal.getEnd().getYear() + ", " + cal.getEnd().getHours() + ":" + cal.getEnd().getMinutes();
            qrEvent.StartDateTime = cal.getStart().getDay() + "-" + (cal.getStart().getMonth() + 1) + "-" + cal.getStart().getYear() + ", " + cal.getStart().getHours() + ":" + cal.getStart().getMinutes();
            qrEvent.Summary = cal.getSummary();
            qrEvent.Description = cal.getDescription();
            qrEvent.Org = cal.getOrganizer();
            qrEvent.Location = cal.getLocation();
            qrEvent.StartTimeRaw = cal.getStart().getRawValue();
            qrEvent.EndTimeRaw = cal.getEnd().getRawValue();

            QRCodeFile qrCodeFile = new QRCodeFile();
            qrCodeFile.type = QRCodeTypeConstants.QRCODE_TYPE_CALENDER;
            qrCodeFile.event = qrEvent;
            qrCodeFile.epoch = System.currentTimeMillis();

            AppDB.qrcodeDao().insert(qrCodeFile);
        }

        Map<String, String> aa = new HashMap<>();
        aa.put(getString(devesh.ephrine.qr.common.R.string.ANALYTICS_EVENT_BARCODE_SCANNED_Calender), "1");
        appAnalytics.logEvent(getString(devesh.ephrine.qr.common.R.string.ANALYTICS_EVENT_BARCODE_SCANNED_Calender), aa);


    }

    void openFragmentDrivingLicQRCode(Barcode.DriverLicense dLic, boolean add2DB) {
        DrivingLicQRCodeViewBottomSheetFrag qview = new DrivingLicQRCodeViewBottomSheetFrag();
        Bundle bundle = new Bundle();
        bundle.putString("json", gson.toJson(dLic));
        qview.setArguments(bundle);
        qview.show(fragmentManager, "view");

        if (add2DB) {
            QRDrivLicId qrDrivLicId = new QRDrivLicId();
            qrDrivLicId.addressCity = dLic.getAddressCity();
            qrDrivLicId.addressState = dLic.getAddressState();
            qrDrivLicId.addressStreet = dLic.getAddressStreet();
            qrDrivLicId.addressZip = dLic.getAddressZip();
            qrDrivLicId.birthDate = dLic.getBirthDate();
            qrDrivLicId.expiryDate = dLic.getExpiryDate();
            qrDrivLicId.firstName = dLic.getFirstName();
            qrDrivLicId.documentType = dLic.getDocumentType();
            qrDrivLicId.gender = dLic.getGender();
            qrDrivLicId.issueDate = dLic.getIssueDate();
            qrDrivLicId.issuingCountry = dLic.getIssuingCountry();
            qrDrivLicId.lastName = dLic.getLastName();
            qrDrivLicId.licenseNumber = dLic.getLicenseNumber();
            qrDrivLicId.middleName = dLic.getMiddleName();


            QRCodeFile qrCodeFile = new QRCodeFile();
            qrCodeFile.type = QRCodeTypeConstants.QRCODE_TYPE_DRIVING_LIC_ID;
            qrCodeFile.DrivingLicID = qrDrivLicId;
            qrCodeFile.epoch = System.currentTimeMillis();

            AppDB.qrcodeDao().insert(qrCodeFile);
        }

        Map<String, String> aa = new HashMap<>();
        aa.put(getString(devesh.ephrine.qr.common.R.string.ANALYTICS_EVENT_BARCODE_SCANNED_DrivingLic), "1");
        appAnalytics.logEvent(getString(devesh.ephrine.qr.common.R.string.ANALYTICS_EVENT_BARCODE_SCANNED_DrivingLic), aa);

    }

    // Create QR Code
    public void CreateQRCode(int i) {
        Intent intent = new Intent(this, CreateQRCodeActivity.class);
        intent.putExtra(CreateQRCodeActivity.QRCODE_TYPE_INTENT, i);
        startActivity(intent);

    }

    public List<QRCodeFile> getHistory() {
        return AppDB.qrcodeDao().getMyQRCodeAll();
    }

    // History tab
    public void HistoryDelete() {
        AppDB.qrcodeDao().nukeTable();
    }

    public void openHistoryrecord(QRCodeFile qrCodeFile) {
        int type = qrCodeFile.type;

        if (type == QRCodeTypeConstants.QRCODE_TYPE_DRIVING_LIC_ID) {
            Bundle bundle = new Bundle();
            bundle.putString("json", gson.toJson(qrCodeFile.DrivingLicID));
            setFragment(new DrivingLicQRCodeOpenFragment(), bundle, "history", true);

        } else if (type == QRCodeTypeConstants.QRCODE_TYPE_TEXT) {
            String text = qrCodeFile.qrText.text;
            Bundle bundle = new Bundle();
            bundle.putString("t", text);
            setFragment(new RawTextQRCodeOpenFragment(), bundle, "history", true);


        } else if (type == QRCodeTypeConstants.QRCODE_TYPE_EMAIL) {
            Bundle bundle = new Bundle();

            String email = qrCodeFile.email.email;
            String subject;
            if (qrCodeFile.email.subject != null || qrCodeFile.email.subject != "") {
                subject = qrCodeFile.email.subject;
            } else {
                subject = "x";
            }

            String body;
            if (qrCodeFile.email.msg != null || qrCodeFile.email.msg != "") {
                body = qrCodeFile.email.msg;
            } else {
                body = "x";
            }
            int email_type = qrCodeFile.email.email_type;

            bundle.putString("email", email);
            bundle.putString("sub", subject);
            bundle.putString("body", body);
            bundle.putInt("type", email_type);

            setFragment(new EmailQROpenFragment(), bundle, "history", true);


        } else if (type == QRCodeTypeConstants.QRCODE_TYPE_Facetime) {

            // Text
            String text = qrCodeFile.qrFaceTime.id;
            Bundle bundle = new Bundle();
            bundle.putString("t", text);
            setFragment(new RawTextQRCodeOpenFragment(), bundle, "history", true);


        } else if (type == QRCodeTypeConstants.QRCODE_TYPE_Geo) {
            Bundle bundle = new Bundle();
            bundle.putDouble("lat", Double.parseDouble(qrCodeFile.qrGeoLoc.lat));
            bundle.putDouble("lng", Double.parseDouble(qrCodeFile.qrGeoLoc.longt));
            setFragment(new GeoQrCodeOpenFragment(), bundle, "history", true);

        } else if (type == QRCodeTypeConstants.QRCODE_TYPE_SMS) {

            String ph = qrCodeFile.qrSms.sms_phone;
            String msg = qrCodeFile.qrSms.sms;
            Bundle bundle = new Bundle();
            bundle.putString("phone", ph);
            bundle.putString("msg", msg);
            setFragment(new PhoneQrCodeOpenFragment(), bundle, "history", true);

        } else if (type == QRCodeTypeConstants.QRCODE_TYPE_Tele) {
            String ph = qrCodeFile.qrPhone.CountryCode + qrCodeFile.qrPhone.Phone;
            Bundle bundle = new Bundle();
            bundle.putString("phone", ph);
            setFragment(new PhoneQrCodeOpenFragment(), bundle, "history", true);


        } else if (type == QRCodeTypeConstants.QRCODE_TYPE_VCARD) {


            Bundle bundle = new Bundle();

            bundle.putString("json", gson.toJson(qrCodeFile.qrvCard));

            setFragment(new RawTextQRCodeOpenFragment(), bundle, "history", true);

        } else if (type == QRCodeTypeConstants.QRCODE_TYPE_WIFI) {
            String ssid = qrCodeFile.qrWifi.SSID;
            String pass = qrCodeFile.qrWifi.Password;
            int encType = WiFiQrCodeOpenFragment.TYPE_OPEN;
            if (qrCodeFile.qrWifi.WPA.equals("WPA")) {
                encType = WiFiQrCodeOpenFragment.TYPE_WPA;
            } else if (qrCodeFile.qrWifi.WPA.equals("WEP")) {
                encType = WiFiQrCodeOpenFragment.TYPE_WEP;
            }
            Bundle bundle = new Bundle();
            bundle.putString("ssid", ssid);
            bundle.putString("pass", pass);
            bundle.putInt("type", encType);
            setFragment(new WiFiQrCodeOpenFragment(), bundle, "history", true);

        } else if (type == QRCodeTypeConstants.QRCODE_TYPE_CALENDER) {
            Bundle bundle = new Bundle();
            bundle.putString("json", gson.toJson(qrCodeFile.event));
            setFragment(new CalenderQrCodeOpenFragment(), bundle, "history", true);

        } else if (type == QRCodeTypeConstants.QRCODE_TYPE_WEBSITE) {

            String url = qrCodeFile.qrWebsite.url;
            Bundle bundle = new Bundle();
            bundle.putString("url", url);
            setFragment(new URLQrCodeOpenFragment(), bundle, "history", true);

        } else {
            String text = qrCodeFile.qrText.text;
            Bundle bundle = new Bundle();
            bundle.putString("t", text);
            setFragment(new RawTextQRCodeOpenFragment(), bundle, "history", true);

        }

        Map<String, String> aa = new HashMap<>();
        aa.put(getString(devesh.ephrine.qr.common.R.string.ANALYTICS_EVENT_OPEN_HISTORY), "1");
        appAnalytics.logEvent(getString(devesh.ephrine.qr.common.R.string.ANALYTICS_EVENT_BARCODE_SCANNED_DrivingLic), aa);


    }

    public void camCapture() {

        File file = new File(getFilesDir(), "img_cache.png");
        ShowLoadingView(false);

        CropImage.activity(Uri.fromFile(file))
                .setAutoZoomEnabled(true)
                .setMultiTouchEnabled(true)
                .start(this);

 /*       File mFile=new File(getFilesDir(), "img_cache.png");
        if(mFile.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(mFile.getPath());
            InputImage image = InputImage.fromBitmap(bitmap, 0);
            barcodeScan(image);
        }else{
            ShowLoadingView(false);
        }
*/
    }

    void ReceiveShareIntent() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.startsWith("image/")) {


                Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
                if (imageUri != null) {
                    // Update UI to reflect image being shared
                    //   ShowLoadingView(true);

                    CropImage.activity(imageUri)
                            .setAutoZoomEnabled(true)
                            .setMultiTouchEnabled(true)
                            .start(MainActivity.this);

                }

            }
        } else {
            // Handle other intents, such as being started from the home screen
        }

    }

    public void openGallery() {


        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        openGalleryApp.launch(Intent.createChooser(i, "Select Picture"));


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                //File file=new File(resultUri.toString());
                //   Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    ShowLoadingView(true);

                    InputImage image = InputImage.fromBitmap(bitmap, 0);
                    barcodeScan(image);


                } catch (IOException e) {
                    Log.d(TAG, "onActivityResult: " + e);
                    ShowLoadingView(false);

                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                ShowLoadingView(false);

            }
        } else {
            Log.e(TAG, "onActivityResult: #645");
            ShowLoadingView(false);

        }
    }

    public void BarcodeNotFoundError() {
        //isAutoScan=cachePref.getBoolean(getString(devesh.ephrine.qr.common.R.string.Pref_AutoScan));

        runOnUiThread(() -> {

            if (isAutoScan) {

            } else {
                Toast.makeText(this, "No Barcode Found", Toast.LENGTH_SHORT).show();
            }

        });
    }


    // Permission

    public void RequestPermission() {
// Camera
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            //performAction( );
        }
        /*else if (shouldShowRequestPermissionRationale(  )) {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected. In this UI,
            // include a "cancel" or "no thanks" button that allows the user to
            // continue using your app without granting the permission.
           // showInContextUI( );
        } */
        else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(
                    Manifest.permission.CAMERA);
        }

        // Location
/*   if (ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED) {
        // You can use the API that requires the permission.
        //performAction( );
    }
    else {
        // You can directly ask for the permission.
        // The registered ActivityResultCallback gets the result of this request.
        requestPermissionLauncher.launch(
                Manifest.permission.ACCESS_FINE_LOCATION);
    }*/

        // Notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.


                }
                return;
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    }

    boolean shouldShowRequestPermissionRationale() {
        return true;
    }

    // in-app Billing
    void BillingStart() {
        isSubscribed = cachePref.getBoolean(getString(devesh.ephrine.qr.common.R.string.Pref_isSubscribed));

        gPlayBilling.init(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {

            }

            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                gPlayBilling.fetchPayments((billingResult1, list) -> {
                    Log.d(TAG, "onBillingSetupFinished: " + list.toString());
                    if (list.isEmpty()) {
                        isSubscribed = false;
                    }

                    for (Purchase p : list) {
                        int state = p.getPurchaseState();
                        Log.d(TAG, "fetchOwnedPlans: " + p.getOrderId() + "\nState: " + p.getPurchaseState());
                        if (state == Purchase.PurchaseState.PURCHASED) {
                            Log.d(TAG, "fetchOwnedPlans: PURCHASED");
                            isSubscribed = true;
                        } else if (state == Purchase.PurchaseState.PENDING) {
                            Log.d(TAG, "fetchOwnedPlans: PENDING");
                            isSubscribed = true;

                        } else if (state == Purchase.PurchaseState.UNSPECIFIED_STATE) {
                            Log.d(TAG, "fetchOwnedPlans: UNSPECIFIED_STATE");
                            isSubscribed = false;
                        } else {
                            isSubscribed = false;
                            Log.d(TAG, "fetchOwnedPlans: UNSPECIFIED_STATE unknown");
                        }

                    }
                    cachePref.setBoolean(getString(devesh.ephrine.qr.common.R.string.Pref_isSubscribed), isSubscribed);
                    PremiumUserUI();

                });
            }
        });
        PremiumUserUI();

    }

    void PremiumUserUI() {

        if (isSubscribed) {
            Map<String, String> aa = new HashMap<>();
            aa.put(getString(devesh.ephrine.qr.common.R.string.ANALYTICS_EVENT_PREMIUM_MEMBER), "1");
            appAnalytics.logEvent(getString(devesh.ephrine.qr.common.R.string.ANALYTICS_EVENT_PREMIUM_MEMBER), aa);
        } else {
            Map<String, String> aa = new HashMap<>();
            aa.put(getString(devesh.ephrine.qr.common.R.string.ANALYTICS_EVENT_NON_PREMIUM_MEMBER), "1");
            appAnalytics.logEvent(getString(devesh.ephrine.qr.common.R.string.ANALYTICS_EVENT_NON_PREMIUM_MEMBER), aa);

        }

    }



    public void notification() {
        Intent intent = getIntent();

        // Check if the intent has extra data
        if (intent != null) {
            // Retrieve the data from the intent
            // String data = intent.getStringExtra("key");

            // Use the data as needed
            Log.d("MainActivity", "Received data from intent:");

            String intentDataString = (intent.getData() != null) ? intent.getData().toString() : "null";

            String notificationTitle = null;
            String notificationMessage = null;

            Bundle extras = intent.getExtras();
            if (extras != null) {
                notificationTitle = extras.getString("title");
                notificationMessage = extras.getString("message");
            }

            // Fallback if not found in extras (can happen depending on how notification was created)
            if (notificationTitle == null) {
                notificationTitle = intent.getStringExtra("title");
            }
            if (notificationMessage == null) {
                notificationMessage = intent.getStringExtra("message");
            }


            Log.d(TAG, "onCreate: " +
                    "Received data from intent:\n\n" +
                    intentDataString + "\n\n" +
                    "=====\n" +
                    "Received extra from intent:\n\n\n\n" +
                    "// Works for App Foreground\n" +
                    "notificationTitle: " + notificationTitle + "\n" +
                    "notificationMessage: " + notificationMessage + "\n\n\n" +
                    "notificationTitle: " + intent.getStringExtra("title") + "\n" + // Kept for direct comparison with Kotlin
                    "notificationMessage: " + intent.getStringExtra("message") + "\n\n\n" + // Kept for direct comparison
                    "// Works for App Background\n" +
                    "notificationTitle: " + intent.getStringExtra("title") + "\n" + // Kept for direct comparison
                    "notificationMessage: " + intent.getStringExtra("message") + "\n"
            );


            try {
                String mUrl = intent.getStringExtra("url");
                if (mUrl != null) {
                    if (!mUrl.isEmpty()) {
                        openChromeCustomTab(mUrl);
                    }
                }
            } catch (Exception e) {
                Log.d(TAG, "notification: ", e);
            }
        }
    }

    public void openChromeCustomTab(String url) {
        // firebaseAnalytics.logEvent(Analytics_EVENT_USER_OPEN_NOTIFICATION, new Bundle());
        Log.d(TAG, "openChromeCustomTab: Opening Chrome Tab : " + url);
        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

    /**
     DEPRECIATED
     @Override
     public void onBackPressed() {

     if (fragmentManager.findFragmentByTag("home") != null) {
     if (fragmentManager.findFragmentByTag("home").isVisible()) {
     fragmentManager.findFragmentByTag("home").onDestroy();
     MainActivity.this.finish();
     }

     }

     //     super.onBackPressed();

     if (fragmentManager != null) {
     if (fragmentManager.findFragmentByTag("create") != null) {
     if (fragmentManager.findFragmentByTag("create").isVisible()) {

     MainActivity.this.finish();

     }
     }


     }
     super.onBackPressed();

     if (fragmentManager.findFragmentByTag("history") != null) {
     if (fragmentManager.findFragmentByTag("history").isVisible()) {
     fragmentManager.findFragmentByTag("history").onDestroy();
     if (adMobAPI.mInterstitialAd != null) {
     adMobAPI.ShowInterstitialAd();
     }
     }

     }


     }
     */
}