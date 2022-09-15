package devesh.ephrine.qr.createqr.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.WriterException;

import devesh.ephrine.qr.common.CreateQRCodeApi;
import devesh.ephrine.qr.createqr.CreateQRCodeActivity;
import devesh.ephrine.qr.createqr.R;
import devesh.ephrine.qr.createqr.databinding.FragmentEmailBinding;
import devesh.ephrine.qr.createqr.databinding.FragmentGenBinding;
import devesh.ephrine.qr.database.AppDatabase;
import devesh.ephrine.qr.database.QRCodeFile;
import devesh.ephrine.qr.database.QRCodeTypeConstants;
import devesh.ephrine.qr.database.type.QREmail;


public class EmailQRCodeFragment extends BaseFragment {
FragmentEmailBinding mBinding;

CreateQRCodeApi createQRCodeApi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
       //     mParam1 = getArguments().getString(ARG_PARAM1);
     //       mParam2 = getArguments().getString(ARG_PARAM2);
        }
        createQRCodeApi=new CreateQRCodeApi(getActivity());



    }

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        mBinding = FragmentEmailBinding.inflate(inflater, container, false);
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

            String email=mBinding.EmailAddressET.getText().toString();
        String subject=mBinding.SubjectET.getText().toString();
        String Cc=mBinding.CCET.getText().toString();
        String Bcc=mBinding.BCCET.getText().toString();
        String msg=mBinding.MessageET.getText().toString();

        qrEmail.email=email;
        qrEmail.subject=subject;
        qrEmail.Bcc=Bcc;
        qrEmail.Cc=Cc;
        qrEmail.msg=msg;

        qrCodeFile.type= QRCodeTypeConstants.QRCODE_TYPE_EMAIL;
        qrCodeFile.email=qrEmail;
            qrCodeFile.epoch=System.currentTimeMillis();
            Bitmap qcode= null;
            try {
                qcode = createQRCodeApi.CreateEmail(email,msg,subject,Cc,Bcc);

                AppDB.qrcodeDao().insert(qrCodeFile);

                ((CreateQRCodeActivity)getActivity()).CreateQRCode(qcode);
            } catch (WriterException e) {
                e.printStackTrace();
            }


        });


    }
}