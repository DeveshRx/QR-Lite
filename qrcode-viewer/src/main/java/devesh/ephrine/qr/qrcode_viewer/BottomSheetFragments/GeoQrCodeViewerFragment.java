package devesh.ephrine.qr.qrcode_viewer.BottomSheetFragments;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import devesh.ephrine.qr.qrcode_viewer.databinding.FragmentGeoQrcodeViewBinding;


public class GeoQrCodeViewerFragment extends BottomSheetDialogFragment {
FragmentGeoQrcodeViewBinding mBinding;
double geoLat;
double geoLong;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            geoLat = getArguments().getDouble("lat");
            geoLong = getArguments().getDouble("lng");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentGeoQrcodeViewBinding.inflate(inflater, container, false);
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
mBinding.geoLatPoint.setText("Latitude: "+String.valueOf(geoLat));
mBinding.geoLongPoint.setText("Longitude: "+String.valueOf(geoLong));

        mBinding.CopyButton.setOnClickListener(view -> {
            CopyText(geoLat+","+geoLong);
        });

        mBinding.OpeninMapsButton.setOnClickListener(view -> {
            OpenMaps();
        });
    }

    void CopyText(String text) {

        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("QR Code", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getActivity(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();

    }

    void OpenMaps(){
        Uri gmmIntentUri = Uri.parse("geo:"+geoLat+","+geoLong);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);

  /*      if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(mapIntent);
        }else{
            Toast.makeText(getActivity(), "Google Maps not installed", Toast.LENGTH_SHORT).show();
        }
  */  }



}