package devesh.ephrine.qr.qrcode_viewer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.mlkit.vision.barcode.common.Barcode;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
/*
Not USED !
* */
public class ViewQRCodeActivity extends AppCompatActivity {
String TAG="viewqrcode";
    Gson gson;
   // Barcode barcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_qrcode);

        gson=new Gson();
        Intent intent = getIntent();
        String bString = intent.getStringExtra("b");

        Type collectionType = new TypeToken<Barcode>() {}.getType();
//        barcode=gson.fromJson(bString,collectionType);
  //      Log.d(TAG, "onCreate: Barcode RAW: "+barcode.getRawValue());
    }



}