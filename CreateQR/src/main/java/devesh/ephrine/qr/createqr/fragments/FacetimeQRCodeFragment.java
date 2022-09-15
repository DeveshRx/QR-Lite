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
import devesh.ephrine.qr.createqr.databinding.FragmentFacetimeQrcodeBinding;
import devesh.ephrine.qr.database.AppDatabase;
import devesh.ephrine.qr.database.QRCodeFile;
import devesh.ephrine.qr.database.QRCodeTypeConstants;
import devesh.ephrine.qr.database.type.QRFaceTime;


public class FacetimeQRCodeFragment extends BaseFragment {

    FragmentFacetimeQrcodeBinding mBinding;
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
        mBinding = FragmentFacetimeQrcodeBinding.inflate(inflater, container, false);
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
    String id=mBinding.AppleIdET.getText().toString();
boolean isAudio=mBinding.FacetimeAudioRadioButton.isChecked();
boolean isVideo=mBinding.FacetimeVideoRadioButton.isChecked();

Bitmap bm=null;
if(isVideo){
    try {
        bm=createQRCodeApi.CreateFaceTimeVideo(id);
    } catch (WriterException e) {
        e.printStackTrace();
    }
}else{
    try {
        bm=createQRCodeApi.CreateFaceTimeAudio(id);
    } catch (WriterException e) {
        e.printStackTrace();
    }
}
qrFaceTime.id=id;
qrFaceTime.isAudio=isAudio;

qrCodeFile.type= QRCodeTypeConstants.QRCODE_TYPE_Facetime;
qrCodeFile.qrFaceTime=qrFaceTime;
    qrCodeFile.epoch=System.currentTimeMillis();
AppDB.qrcodeDao().insert(qrCodeFile);

    ((CreateQRCodeActivity)getActivity()).CreateQRCode(bm);

});

    }
}