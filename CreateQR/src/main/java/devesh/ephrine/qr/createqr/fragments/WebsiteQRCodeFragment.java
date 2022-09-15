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
import devesh.ephrine.qr.createqr.databinding.FragmentTextQrcodeBinding;
import devesh.ephrine.qr.createqr.databinding.FragmentWebsiteQrcodeBinding;
import devesh.ephrine.qr.database.QRCodeTypeConstants;


public class WebsiteQRCodeFragment extends BaseFragment {
FragmentWebsiteQrcodeBinding mBinding;
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
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        mBinding = FragmentWebsiteQrcodeBinding.inflate(inflater, container, false);
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
        mBinding.CreateQRCodeButton.setOnClickListener(view -> {
            String website=mBinding.WebsiteLinkET.getText().toString();
            qrWebsite.url=website;

            qrCodeFile.type= QRCodeTypeConstants.QRCODE_TYPE_WEBSITE;
            qrCodeFile.qrWebsite=qrWebsite;
            qrCodeFile.epoch=System.currentTimeMillis();
            AppDB.qrcodeDao().insert(qrCodeFile);
            try {
                Bitmap bm=createQRCodeApi.CreateQRFromText(website);
                ((CreateQRCodeActivity)getActivity()).CreateQRCode(bm);
            } catch (WriterException e) {
                e.printStackTrace();
            }


        });
    }
}