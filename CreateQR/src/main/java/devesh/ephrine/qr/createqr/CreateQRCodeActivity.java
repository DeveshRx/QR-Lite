package devesh.ephrine.qr.createqr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import devesh.ephrine.qr.common.AdMobAPI;
import devesh.ephrine.qr.createqr.databinding.ActivityCreateQrcodeBinding;
import devesh.ephrine.qr.createqr.databinding.FragmentGenBinding;
import devesh.ephrine.qr.createqr.fragments.CalenderQRCodeFragment;
import devesh.ephrine.qr.createqr.fragments.EmailQRCodeFragment;
import devesh.ephrine.qr.createqr.fragments.FacetimeQRCodeFragment;
import devesh.ephrine.qr.createqr.fragments.GeoQRCodeFragment;
import devesh.ephrine.qr.createqr.fragments.SmsQRCodeFragment;
import devesh.ephrine.qr.createqr.fragments.TeleQRCodeFragment;
import devesh.ephrine.qr.createqr.fragments.TextQRCodeFragment;
import devesh.ephrine.qr.createqr.fragments.VCardQRCodeFragment;
import devesh.ephrine.qr.createqr.fragments.WebsiteQRCodeFragment;
import devesh.ephrine.qr.createqr.fragments.WifiQRCodeFragment;

public class CreateQRCodeActivity extends AppCompatActivity {

    public static final String QRCODE_TYPE_INTENT="qrcodetype";

    public static final int QRCODE_TYPE_TEXT=0;
    public static final int QRCODE_TYPE_EMAIL=1;
    public static final int QRCODE_TYPE_Facetime=2;
    public static final int QRCODE_TYPE_Geo=3;
    public static final int QRCODE_TYPE_SMS=4;
    public static final int QRCODE_TYPE_Tele=5;
    public static final int QRCODE_TYPE_VCARD=6;
    public static final int QRCODE_TYPE_WIFI=7;
    public static final int QRCODE_TYPE_CALENDER=8;
    public static final int QRCODE_TYPE_WEBSITE=9;

    int QRCodeType=0;

    ActivityCreateQrcodeBinding mBinding;
    String TAG = "CreateQRAct";

    FragmentManager fragmentManager;
    Fragment fragmentScreen;
    Fragment oldFrag;

    AdMobAPI adMobAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityCreateQrcodeBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);
        fragmentManager = getSupportFragmentManager();

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        QRCodeType = intent.getIntExtra(QRCODE_TYPE_INTENT,0);
adMobAPI=new AdMobAPI(this);
adMobAPI.LoadInterstitialAd(this);
        Log.d(TAG, "onCreate: type: "+QRCodeType);

        if(QRCodeType==QRCODE_TYPE_TEXT){
            fragmentScreen = new TextQRCodeFragment();
        }else if(QRCodeType==QRCODE_TYPE_EMAIL){
            fragmentScreen=new EmailQRCodeFragment();
        }else if(QRCodeType==QRCODE_TYPE_Facetime){
            fragmentScreen=new FacetimeQRCodeFragment();
        }else if(QRCodeType==QRCODE_TYPE_Geo){
            fragmentScreen=new GeoQRCodeFragment();
        }else if(QRCodeType==QRCODE_TYPE_SMS){
            fragmentScreen=new SmsQRCodeFragment();
        }else if(QRCodeType==QRCODE_TYPE_Tele){
            fragmentScreen=new TeleQRCodeFragment();
        }else if(QRCodeType==QRCODE_TYPE_VCARD){
            fragmentScreen=new VCardQRCodeFragment();
        }else if(QRCodeType==QRCODE_TYPE_WIFI){
            fragmentScreen=new WifiQRCodeFragment();
        }else if(QRCodeType==QRCODE_TYPE_CALENDER){
            fragmentScreen=new CalenderQRCodeFragment();
        }else if(QRCodeType==QRCODE_TYPE_WEBSITE){
            fragmentScreen=new WebsiteQRCodeFragment();
        }else{
            fragmentScreen = new TextQRCodeFragment();
        }




    }


    @Override
    protected void onStart() {
        super.onStart();

        adMobAPI.setAdaptiveBanner(mBinding.adFrame,this);

        if (fragmentScreen == null) {



            if(QRCodeType==QRCODE_TYPE_TEXT){
                fragmentScreen = new TextQRCodeFragment();
            }else if(QRCodeType==QRCODE_TYPE_EMAIL){
                fragmentScreen=new EmailQRCodeFragment();
            }else if(QRCodeType==QRCODE_TYPE_Facetime){
                fragmentScreen=new FacetimeQRCodeFragment();
            }else if(QRCodeType==QRCODE_TYPE_Geo){
                fragmentScreen=new GeoQRCodeFragment();
            }else if(QRCodeType==QRCODE_TYPE_SMS){
                fragmentScreen=new SmsQRCodeFragment();
            }else if(QRCodeType==QRCODE_TYPE_Tele){
                fragmentScreen=new TeleQRCodeFragment();
            }else if(QRCodeType==QRCODE_TYPE_VCARD){
                fragmentScreen=new VCardQRCodeFragment();
            }else if(QRCodeType==QRCODE_TYPE_WIFI){
                fragmentScreen=new WifiQRCodeFragment();
            }else if(QRCodeType==QRCODE_TYPE_CALENDER){
                fragmentScreen=new CalenderQRCodeFragment();
            }else if(QRCodeType==QRCODE_TYPE_WEBSITE){
                fragmentScreen=new WebsiteQRCodeFragment();
            }else{
                fragmentScreen = new TextQRCodeFragment();
            }

        }

        if (fragmentManager.findFragmentByTag("createqr") == null) {

            if (fragmentManager.getFragments().isEmpty() && fragmentScreen!=null) {
                fragmentManager.beginTransaction()
                        .add(mBinding.fragmentContainerView.getId(), fragmentScreen, "createqr")

                        .commit();

            }

        } else {

        }




    }

    @Override
    public void onBackPressed() {

        if(adMobAPI.mInterstitialAd!=null){
            adMobAPI.ShowInterstitialAd();
        }

        super.onBackPressed();


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
                .addToBackStack("createqr")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(mBinding.fragmentContainerFrame.getId(), BottomSheetFragments, null)
                .commit();*/
    }

    public Bitmap qrCodeBitmap;
    public void CreateQRCode(Bitmap qrcode){
        if(qrcode!=null){
            qrCodeBitmap=qrcode;
            setFragment(new QRCodeGen(),null,"gen");
        }


    }

    public Bitmap getQrCodeBitmap(){return qrCodeBitmap;}


    public void saveImage(Bitmap bitmap, @NonNull String name) throws IOException {

        boolean saved;
        OutputStream fos;
        String IMAGES_FOLDER_NAME="QRLite";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" + IMAGES_FOLDER_NAME);
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            fos = resolver.openOutputStream(imageUri);
        } else {
            String imagesDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM).toString() + File.separator + IMAGES_FOLDER_NAME;

            File file = new File(imagesDir);

            if (!file.exists()) {
                file.mkdir();
            }

            File image = new File(imagesDir, name + ".png");
            fos = new FileOutputStream(image);

        }

        saved = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        fos.flush();
        fos.close();

        runOnUiThread(() -> {
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        });
    }

   public void ShareQRCode(Bitmap bm){
       Intent shareIntent = new Intent();
       shareIntent.setAction(Intent.ACTION_SEND);
       shareIntent.putExtra(Intent.EXTRA_STREAM, getImageUri(bm));
       shareIntent.setType("image/*");
       startActivity(Intent.createChooser(shareIntent, null));
   }
    private Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), inImage, null, null);
        return Uri.parse(path);
    }

    // Generated QRCode Show
    public static class QRCodeGen extends Fragment {

        FragmentGenBinding mBinding;
        String RawText;
        Bitmap qrcode;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                RawText = getArguments().getString("t");
                //mParam2 = getArguments().getString(ARG_PARAM2);
            }
        }

        @Override
        public View onCreateView (LayoutInflater inflater,
                                  ViewGroup container,
                                  Bundle savedInstanceState) {
            mBinding = FragmentGenBinding.inflate(inflater, container, false);
            View view = mBinding.getRoot();
            return view;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            mBinding = null;
        }

        @Override
        public void onStart() {
            super.onStart();

             qrcode=((CreateQRCodeActivity)getActivity()).getQrCodeBitmap();
            mBinding.QRCodeImageView.setImageBitmap(qrcode);


            mBinding.SavePhoneButton.setOnClickListener(view -> {
                long s=System.currentTimeMillis();
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getActivity(), "Saving..", Toast.LENGTH_SHORT).show();
                });
                try {
                    ((CreateQRCodeActivity)getActivity()).saveImage(qrcode,"QRLite_"+s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            mBinding.ShareButton.setOnClickListener(view -> {
                ((CreateQRCodeActivity)getActivity()).ShareQRCode(qrcode);
            });

        }
    }


}
