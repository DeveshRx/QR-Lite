package devesh.ephrine.qr.code.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import devesh.ephrine.qr.code.MainActivity;
import devesh.ephrine.qr.code.databinding.FragmentCreateQrBinding;
import devesh.ephrine.qr.common.AdMobAPI;
import devesh.ephrine.qr.createqr.CreateQRCodeActivity;


public class CreateQRFragment extends Fragment {

    FragmentCreateQrBinding mBinding;
    String TAG = "CreateQR";
AdMobAPI adMobAPI;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //        mParam1 = getArguments().getString(ARG_PARAM1);
            //       mParam2 = getArguments().getString(ARG_PARAM2);
        }
        adMobAPI=new AdMobAPI(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentCreateQrBinding.inflate(inflater, container, false);
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
        loadAds();
        mBinding.CreateTextQRcardview.setOnClickListener(view -> {
            Log.d(TAG, "onStart: CreateTextQRcardview");
            ((MainActivity) getActivity()).CreateQRCode(CreateQRCodeActivity.QRCODE_TYPE_TEXT);
        });
        mBinding.CreateFacetimeQRcardview.setOnClickListener(view -> {
            ((MainActivity) getActivity()).CreateQRCode(CreateQRCodeActivity.QRCODE_TYPE_Facetime);

        });

        mBinding.CreateEventQRcardview.setOnClickListener(view -> {
            ((MainActivity) getActivity()).CreateQRCode(CreateQRCodeActivity.QRCODE_TYPE_CALENDER);

        });

        mBinding.CreateSMSQRcardview.setOnClickListener(view -> {
            ((MainActivity) getActivity()).CreateQRCode(CreateQRCodeActivity.QRCODE_TYPE_SMS);
        });

        mBinding.CreateVCardQRcardview.setOnClickListener(view -> {
            ((MainActivity) getActivity()).CreateQRCode(CreateQRCodeActivity.QRCODE_TYPE_VCARD);
        });

        mBinding.CreateEmailQRcardview.setOnClickListener(view -> {
            ((MainActivity) getActivity()).CreateQRCode(CreateQRCodeActivity.QRCODE_TYPE_EMAIL);
        });

        mBinding.CreateGeoLocQRcardview.setOnClickListener(view -> {
            ((MainActivity) getActivity()).CreateQRCode(CreateQRCodeActivity.QRCODE_TYPE_Geo);
        });

        mBinding.CreatePhoneNoQRcardview.setOnClickListener(view -> {
            ((MainActivity) getActivity()).CreateQRCode(CreateQRCodeActivity.QRCODE_TYPE_Tele);
        });

        mBinding.CreateWifiQRcardview.setOnClickListener(view -> {
            ((MainActivity) getActivity()).CreateQRCode(CreateQRCodeActivity.QRCODE_TYPE_WIFI);
        });

        mBinding.CreateWebsiteQRcardview.setOnClickListener(view -> {
            ((MainActivity) getActivity()).CreateQRCode(CreateQRCodeActivity.QRCODE_TYPE_WEBSITE);

        });


    }

    void loadAds(){
        adMobAPI.setAdaptiveBanner(mBinding.adFrame,getActivity());
    }


}