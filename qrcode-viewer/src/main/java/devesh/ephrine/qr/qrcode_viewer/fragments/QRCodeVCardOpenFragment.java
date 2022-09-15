package devesh.ephrine.qr.qrcode_viewer.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.mlkit.vision.barcode.common.Barcode;

import java.lang.reflect.Type;

import devesh.ephrine.qr.common.CreateQRCodeApi;
import devesh.ephrine.qr.createqr.databinding.FragmentVcardQrcodeBinding;
import devesh.ephrine.qr.database.type.QRvCard;
import devesh.ephrine.qr.qrcode_viewer.R;
import devesh.ephrine.qr.qrcode_viewer.databinding.FragmentQrcodeVcardopenBinding;


public class QRCodeVCardOpenFragment extends Fragment {

FragmentQrcodeVcardopenBinding mBinding;

    QRvCard qRvCard=new QRvCard();
Gson gson;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson=new Gson();
        if (getArguments() != null) {
    String jsonRaw= getArguments().getString("json");

            Type collectionType = new TypeToken<QRvCard>() {
            }.getType();
            qRvCard=gson.fromJson(jsonRaw,collectionType);

        }
    }

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        mBinding = FragmentQrcodeVcardopenBinding.inflate(inflater, container, false);
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


        mBinding.EmailTextView.setText("Email: "+qRvCard.card_email);
mBinding.NameTextView.setText("Name: "+qRvCard.name);
mBinding.NoteTextView.setText("Note: "+qRvCard.note);
mBinding.OrgTextView.setText("Organisation: "+qRvCard.vCard_org);
mBinding.RoleTextView.setText("Role: "+qRvCard.role);
mBinding.TitleTextView.setText("Title: "+qRvCard.title);
mBinding.URLTextView.setText("URL: "+qRvCard.vCard_url);

    }


}