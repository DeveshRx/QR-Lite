package devesh.ephrine.qr.qrcode_viewer.BottomSheetFragments;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.mlkit.vision.barcode.common.Barcode;

import java.lang.reflect.Type;

import devesh.ephrine.qr.qrcode_viewer.databinding.FragmentDrivinglicQrcodeviewBinding;


public class DrivingLicQRCodeViewBottomSheetFrag extends BottomSheetDialogFragment {


    FragmentDrivinglicQrcodeviewBinding mBinding;
    Barcode.DriverLicense dLic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gson gson = new Gson();
        Type collectionType = new TypeToken<Barcode.DriverLicense>() {
        }.getType();

        if (getArguments() != null) {
            String json = getArguments().getString("json");
            dLic = gson.fromJson(json, collectionType);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentDrivinglicQrcodeviewBinding.inflate(inflater, container, false);
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


        mBinding.LicNoDL.setText("No: " + dLic.getLicenseNumber());
        mBinding.FirstNameDL.setText("Name: " + dLic.getFirstName() + " " + dLic.getMiddleName() + " " + dLic.getLastName());
        mBinding.AddressStreetDL.setText("Address: " + dLic.getAddressStreet() + "," + dLic.getAddressCity() + "," + dLic.getAddressState());
        mBinding.AddressZipDL.setText("Zip code: " + dLic.getAddressZip());

        mBinding.BirthdateDL.setText("Birthday: " + dLic.getBirthDate());
        mBinding.ExpiryDateDL.setText("Expiry Date: " + dLic.getExpiryDate());
        mBinding.IssueDateDL.setText("Issue Date: " + dLic.getIssueDate());
        mBinding.CountryDL.setText("Country: " + dLic.getIssuingCountry());

        String gender = dLic.getGender();
        if (gender.equals("1") || gender.equals("male")) {
            mBinding.GenderChip.setChipIcon(getActivity().getDrawable(devesh.ephrine.qr.common.R.drawable.ic_baseline_male_30));
            mBinding.GenderChip.setText("Male");
        } else if (gender.equals("2") || gender.equals("female")) {

            mBinding.GenderChip.setChipIcon(getActivity().getDrawable(devesh.ephrine.qr.common.R.drawable.ic_baseline_female_30));
            mBinding.GenderChip.setText("female");

        } else {
            mBinding.GenderChip.setVisibility(View.GONE);
            mBinding.GenderTx.setVisibility(View.GONE);
        }

        String type = dLic.getDocumentType();
        if (type.equals("DL")) {
            mBinding.TitleTextView.setText("Driving Licenses");
        } else if (type.equals("ID")) {
            mBinding.TitleTextView.setText("ID Card");
        }

        mBinding.CopyButton.setOnClickListener(view -> {
            CopyText(
                    "No: " + dLic.getLicenseNumber() + "\n" +
                            "Name: " + dLic.getFirstName() + " " + dLic.getMiddleName() + " " + dLic.getLastName() + "\n" +
                            "Gender: " + dLic.getGender() + "\n" +
                            "Birthday: " + dLic.getBirthDate() + "\n" +
                            "Address: " + dLic.getAddressStreet() + "," + dLic.getAddressCity() + "," + dLic.getAddressState() + "\n" +
                            "Zip code: " + dLic.getAddressZip() + "\n" +
                            "Country: " + dLic.getIssuingCountry() + "\n" +
                            "Expiry Date: " + dLic.getExpiryDate() + "\n" +
                            "Issue Date: " + dLic.getIssueDate() + "\n"


            );
        });


    }

    void CopyText(String text) {

        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("QR Code", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getActivity(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();

    }
}