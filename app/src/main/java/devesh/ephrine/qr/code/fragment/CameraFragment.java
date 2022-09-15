package devesh.ephrine.qr.code.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.FocusMeteringAction;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.MeteringPoint;
import androidx.camera.core.Preview;
import androidx.camera.core.TorchState;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import devesh.ephrine.qr.code.MainActivity;
import devesh.ephrine.qr.code.R;
import devesh.ephrine.qr.code.databinding.FragmentCameraBinding;
import devesh.ephrine.qr.common.AdMobAPI;
import devesh.ephrine.qr.common.BarcodeAPI;
import devesh.ephrine.qr.common.CachePref;

public class CameraFragment extends Fragment {
    FragmentCameraBinding mBinding;
    String TAG = "CameraFragment";
    ImageCapture imageCapture;
    ExecutorService cameraExecutor = Executors.newFixedThreadPool(2);
    Camera camera;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
     final static boolean isAutoScan=true;
CachePref cachePref;
BarcodeAPI barcodeAPI;
AdMobAPI adMobAPI;

boolean isSubscribed;

    public CameraFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentCameraBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //      mParam1 = getArguments().getString(ARG_PARAM1);
            //    mParam2 = getArguments().getString(ARG_PARAM2);
        }
isSubscribed=false;
        cameraProviderFuture = ProcessCameraProvider.getInstance(getActivity());
cachePref=new CachePref(getActivity());
adMobAPI=new AdMobAPI(getActivity());


//isAutoScan=cachePref.getBoolean(getString(devesh.ephrine.qr.common.R.string.Pref_AutoScan));
        barcodeAPI = new BarcodeAPI(getActivity());

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);

            } catch (ExecutionException | InterruptedException e) {
                // No errors need to be handled for this Future.
                // This should never be reached.
            }

        }, ContextCompat.getMainExecutor(getActivity()));

    }

    @Override
    public void onStart() {
        super.onStart();
        isSubscribed=cachePref.getBoolean(getString(devesh.ephrine.qr.common.R.string.Pref_isSubscribed));
        if(isSubscribed){
            mBinding.PremiumMemberBadge.getRoot().setVisibility(View.VISIBLE);
        }else{
            mBinding.PremiumMemberBadge.getRoot().setVisibility(View.GONE);
        }

        LoadAds();
     //   isAutoScan=cachePref.getBoolean(getString(devesh.ephrine.qr.common.R.string.Pref_AutoScan));

        mBinding.FlashChip.setOnClickListener(view -> {
            ToggleFlash();
        });

        mBinding.GalleryChip.setOnClickListener(view -> {
            ((MainActivity) getActivity()).openGallery();
        });




        if(isAutoScan){

            mBinding.CameraCaptureButton.setVisibility(View.GONE);
        }else{
            mBinding.CameraCaptureButton.setVisibility(View.VISIBLE);
            mBinding.CameraCaptureButton.setOnClickListener(view -> {
                onClick();
            });
        }

       /* mBinding.viewFinder.setOnClickListener(view -> {


        });*/


    }

    @Override
    public void onPause() {
        super.onPause();
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();
   /*     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            imageCapture =
                   new ImageCapture.Builder()
                           //.setTargetRotation(getActivity().getDisplay().getRotation())
                           .setTargetRotation(Surface.ROTATION_0)
                           .build();
        }else{
            imageCapture =
                    new ImageCapture.Builder()
                            //.setTargetRotation(getActivity().getDisplay().getRotation())
                            .build();
        }
*/


        ImageAnalysis imageAnalysis =
                new ImageAnalysis.Builder()
                        // enable the following line if RGBA output is needed.
                        //.setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                        .setTargetResolution(new Size(1280, 720))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .setTargetRotation(Surface.ROTATION_90)
                        .build();


        imageAnalysis.setAnalyzer(cameraExecutor, new ImageAnalysis.Analyzer() {
            @Override
            public void analyze(@NonNull ImageProxy imageProxy) {
                int rotationDegrees = imageProxy.getImageInfo().getRotationDegrees();
                // insert your code here.
               // @SuppressLint("UnsafeOptInUsageError")
               // InputImage image = InputImage.fromMediaImage(imageProxy.getImage(), rotationDegrees);

                analyzeIMG(imageProxy,rotationDegrees);

            //    imageProxy.close();



                // after done, release the ImageProxy object
               /* @SuppressLint("UnsafeOptInUsageError")
                Image image=imageProxy.getImage();
                if(image!=null){

                    previewBitmap=toBitmap(image);
                }*/
                //imageProxy.close();
            }
        });
        imageCapture =
                new ImageCapture.Builder()
                        //  .setTargetRotation(Surface.ROTATION_90)
                        //   .setTargetResolution(new Size(1920, 1080))
                        .setJpegQuality(100)

                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)

                        .build();

        preview.setSurfaceProvider(mBinding.viewFinder.getSurfaceProvider());

        try {
         //   camera = cameraProvider.bindToLifecycle(this, cameraSelector,imageAnalysis, imageCapture, preview);

            if(isAutoScan){
                camera = cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis, preview);
            }else{
                //camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector,imageCapture, preview);
                camera = cameraProvider.bindToLifecycle(this, cameraSelector, imageCapture, preview);
            }



        } catch (Exception e) {
            Log.e(TAG, "bindPreview: " + e);
        }


    }

    public void analyzeIMG(ImageProxy imageProxy, int rotationDegrees) {

          @SuppressLint("UnsafeOptInUsageError")
          InputImage image = InputImage.fromMediaImage(imageProxy.getImage(), rotationDegrees);
        barcodeAPI.getScanner().process(image)
                .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                                          @Override
                                          public void onSuccess(List<Barcode> barcodes) {
                                              // Task completed successfully
                                              if (barcodes.isEmpty()) {
                                                  if(getActivity() instanceof MainActivity){
                                                      ((MainActivity)getActivity()).BarcodeNotFoundError();

                                                  }
                                                  Log.d(TAG, "onSuccess: barcodes.isEmpty()");
                                              }
                                              if(isAutoScan){
                                                  if(getActivity() instanceof MainActivity){
                                                      ((MainActivity)getActivity()).getBarcode(barcodes);

                                                  }


                                              }
                                              if(getActivity() instanceof MainActivity){
                                                  ((MainActivity)getActivity()).ShowLoadingView(false);

                                              }

                                          }
                                      }
                )
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                        // ...
                        Log.d(TAG, "onFailure: "+e);
                        if(getActivity() instanceof MainActivity){

                            ((MainActivity)getActivity()).BarcodeNotFoundError();
                            ((MainActivity)getActivity()).ShowLoadingView(false);
                        }
                    }
                }).addOnCompleteListener(new OnCompleteListener<List<Barcode>>() {
                    @Override
                    public void onComplete(@NonNull Task<List<Barcode>> task) {

                        imageProxy.close();
                    }
                });;



       // imageProxy.close();


    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        cameraProviderFuture.cancel(true);

        try {
            adMobAPI.DestroyAds();
        }catch (Exception e){

        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    void ToggleFlash() {
        if (camera.getCameraInfo().getTorchState().getValue().equals(TorchState.ON)) {
            camera.getCameraControl().enableTorch(false);
            mBinding.FlashChip.setChipIcon(getActivity().getDrawable(devesh.ephrine.qr.common.R.drawable.ic_baseline_flash_on_24));

        } else {
            camera.getCameraControl().enableTorch(true);
            mBinding.FlashChip.setChipIcon(getActivity().getDrawable(devesh.ephrine.qr.common.R.drawable.ic_baseline_flash_off_24));
        }
    }


    public void onClick() {
        ((MainActivity) getActivity()).ShowLoadingView(true);
        ImageCapture.OutputFileOptions outputFileOptions =
                new ImageCapture.OutputFileOptions.Builder(new File(getActivity().getFilesDir(), "img_cache.png")).build();
        imageCapture.takePicture(outputFileOptions, cameraExecutor,
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(ImageCapture.OutputFileResults outputFileResults) {
                        // insert your code here.
                        Log.d(TAG, "onImageSaved: ");
                        ((MainActivity)getActivity()).camCapture();

                    }

                    @Override
                    public void onError(ImageCaptureException error) {
                        // insert your code here.
                        Log.e(TAG, "onError: " + error);
                        ((MainActivity) getActivity()).ShowLoadingView(false);
                    }
                }
        );
    }

    void LoadAds(){
        adMobAPI.loadNativeAd(mBinding.adFrame,getActivity());
    }


}