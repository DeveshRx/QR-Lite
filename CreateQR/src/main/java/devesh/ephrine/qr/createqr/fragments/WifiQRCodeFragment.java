package devesh.ephrine.qr.createqr.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.WriterException;

import devesh.ephrine.qr.common.CreateQRCodeApi;
import devesh.ephrine.qr.createqr.CreateQRCodeActivity;
import devesh.ephrine.qr.createqr.R;
import devesh.ephrine.qr.createqr.databinding.FragmentWebsiteQrcodeBinding;
import devesh.ephrine.qr.createqr.databinding.FragmentWifiQrcodeBinding;
import devesh.ephrine.qr.database.QRCodeTypeConstants;


public class WifiQRCodeFragment extends BaseFragment {

    FragmentWifiQrcodeBinding mBinding;
CreateQRCodeApi createQRCodeApi;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
 //           mParam2 = getArguments().getString(ARG_PARAM2);
        }
        createQRCodeApi=new CreateQRCodeApi(getActivity());
    }

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        mBinding = FragmentWifiQrcodeBinding.inflate(inflater, container, false);
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
String WPA=mBinding.WPAMenu.getText().toString();
if(WPA.equals("none")){
    WPA="nopass";
}
String ssid=mBinding.SsidET.getText().toString();
String password=mBinding.PasswordET.getText().toString();
boolean isHidden=mBinding.isHiddenswitch.isChecked();

qrWifi.SSID=ssid;
qrWifi.Password=password;
qrWifi.WPA=WPA;
qrWifi.isHidden=isHidden;

qrCodeFile.type= QRCodeTypeConstants.QRCODE_TYPE_WIFI;
qrCodeFile.qrWifi=qrWifi;
    qrCodeFile.epoch=System.currentTimeMillis();
AppDB.qrcodeDao().insert(qrCodeFile);

try {
        Bitmap bm=createQRCodeApi.CreateWiFi(WPA,ssid,password,isHidden,null,null,null,null);
        ((CreateQRCodeActivity)getActivity()).CreateQRCode(bm);

    } catch (WriterException e) {
        e.printStackTrace();
    }


});


    }
}