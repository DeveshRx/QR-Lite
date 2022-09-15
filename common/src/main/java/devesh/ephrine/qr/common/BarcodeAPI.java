package devesh.ephrine.qr.common;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.List;

public class BarcodeAPI {
    Context mContext;
    BarcodeScannerOptions options;
  public BarcodeScanner scanner;
    public BarcodeAPI(Context context){
    mContext=context;
         options =
                new BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(
                                Barcode.FORMAT_ALL_FORMATS)
                        .build();
        scanner = BarcodeScanning.getClient(options);
    }

    public BarcodeScanner getScanner(){
        return scanner;
    }

    public Task<List<Barcode>> scan(InputImage image,
                                    OnSuccessListener onSuccessListener,
                                    OnFailureListener onFailureListener){

        Task<List<Barcode>> result= scanner.process(image)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);


        /*
        new OnSuccessListener<List<Barcode>>() {
                    @Override
                    public void onSuccess(List<Barcode> barcodes) {
                        // Task completed successfully
                        // ...
                    }
                }

         */

        /*
        new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                        // ...
                    }
                }
        */

        return  result;

    }


    /*
    for (Barcode barcode: barcodes) {
    Rect bounds = barcode.getBoundingBox();
    Point[] corners = barcode.getCornerPoints();

    String rawValue = barcode.getRawValue();

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
    }
}
    * */
}
