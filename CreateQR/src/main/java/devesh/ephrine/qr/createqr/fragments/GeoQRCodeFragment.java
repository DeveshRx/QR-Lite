package devesh.ephrine.qr.createqr.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.WriterException;

import devesh.ephrine.qr.common.CreateQRCodeApi;
import devesh.ephrine.qr.createqr.CreateQRCodeActivity;
import devesh.ephrine.qr.createqr.R;
import devesh.ephrine.qr.createqr.databinding.FragmentGeoQrcodeBinding;
import devesh.ephrine.qr.database.AppDatabase;
import devesh.ephrine.qr.database.QRCodeTypeConstants;


public class GeoQRCodeFragment extends BaseFragment {

FragmentGeoQrcodeBinding mBinding;

CreateQRCodeApi createQRCodeApi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
    //        mParam1 = getArguments().getString(ARG_PARAM1);
      //      mParam2 = getArguments().getString(ARG_PARAM2);
        }
        createQRCodeApi=new CreateQRCodeApi(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentGeoQrcodeBinding.inflate(inflater, container, false);
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

        mBinding.CreateQRButton.setOnClickListener(view -> {
            String lat=mBinding.LatitudeET.getText().toString();
            String longt=mBinding.LongitudeET.getText().toString();
qrGeoLoc.lat=lat;
qrGeoLoc.longt=longt;

qrCodeFile.type= QRCodeTypeConstants.QRCODE_TYPE_Geo;
qrCodeFile.qrGeoLoc=qrGeoLoc;
            qrCodeFile.epoch=System.currentTimeMillis();
            AppDB.qrcodeDao().insert(qrCodeFile);

            try {
                Bitmap bm=createQRCodeApi.CreateGeoLocation(longt,lat);
                ((CreateQRCodeActivity)getActivity()).CreateQRCode(bm);

            } catch (WriterException e) {
                e.printStackTrace();
            }


        });

    }
}