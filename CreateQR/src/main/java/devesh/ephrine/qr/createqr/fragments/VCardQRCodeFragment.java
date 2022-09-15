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
import devesh.ephrine.qr.createqr.databinding.FragmentVcardQrcodeBinding;
import devesh.ephrine.qr.database.QRCodeTypeConstants;


public class VCardQRCodeFragment extends BaseFragment {

FragmentVcardQrcodeBinding mBinding;
CreateQRCodeApi createQRCodeApi;

CreateQRCodeApi.VCard vCard=new CreateQRCodeApi.VCard();
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
        mBinding = FragmentVcardQrcodeBinding.inflate(inflater, container, false);
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

            vCard.name=mBinding.NameET.getText().toString();
            vCard.title=mBinding.TitleET.getText().toString();
            vCard.role=mBinding.RoleET.getText().toString();
            vCard.org=mBinding.OrganisationET.getText().toString();
            vCard.url=mBinding.UrlET.getText().toString();
            vCard.note=mBinding.NoteET.getText().toString();
            vCard.email=mBinding.EmailIdET.getText().toString();

            qrvCard.card_email= vCard.email;
qrvCard.name= vCard.name;
qrvCard.vCard_url= vCard.url;
qrvCard.note= vCard.note;
qrvCard.vCard_org=vCard.org;
qrvCard.role= vCard.role;
qrvCard.title= vCard.title;

qrCodeFile.type= QRCodeTypeConstants.QRCODE_TYPE_VCARD;
qrCodeFile.qrvCard=qrvCard;
            qrCodeFile.epoch=System.currentTimeMillis();
AppDB.qrcodeDao().insert(qrCodeFile);
            try {
                Bitmap bm=createQRCodeApi.CreateVCard(vCard);
                ((CreateQRCodeActivity)getActivity()).CreateQRCode(bm);
            } catch (WriterException e) {
                e.printStackTrace();
            }


        });

    }
}