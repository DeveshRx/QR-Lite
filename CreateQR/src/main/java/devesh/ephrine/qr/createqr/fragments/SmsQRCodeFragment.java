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
import devesh.ephrine.qr.createqr.databinding.FragmentGeoQrcodeBinding;
import devesh.ephrine.qr.createqr.databinding.FragmentSmsQrcodeBinding;
import devesh.ephrine.qr.database.QRCodeTypeConstants;

public class SmsQRCodeFragment extends BaseFragment {

FragmentSmsQrcodeBinding mBinding;
CreateQRCodeApi createQRCodeApi;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        createQRCodeApi=new CreateQRCodeApi(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentSmsQrcodeBinding.inflate(inflater, container, false);
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
            String phone=mBinding.PhoneNoET.getText().toString();
            String msg=mBinding.MessageET.getText().toString();

            qrSms.sms_phone=phone;
            qrSms.sms=msg;
            qrCodeFile.type= QRCodeTypeConstants.QRCODE_TYPE_SMS;
            qrCodeFile.qrSms=qrSms;
            qrCodeFile.epoch=System.currentTimeMillis();

            AppDB.qrcodeDao().insert(qrCodeFile);

            try {
                Bitmap bm=createQRCodeApi.CreateSMS(phone,msg);
                ((CreateQRCodeActivity)getActivity()).CreateQRCode(bm);

            } catch (WriterException e) {
                e.printStackTrace();
            }

        });

    }
}