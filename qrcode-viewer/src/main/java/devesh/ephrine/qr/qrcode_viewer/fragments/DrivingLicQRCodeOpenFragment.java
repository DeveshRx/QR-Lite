package devesh.ephrine.qr.qrcode_viewer.fragments;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.mlkit.vision.barcode.common.Barcode;

import java.lang.reflect.Type;

import devesh.ephrine.qr.database.type.QRDrivLicId;
import devesh.ephrine.qr.qrcode_viewer.databinding.FragmentDrivinglicQrcodeviewBinding;


public class DrivingLicQRCodeOpenFragment extends Fragment {


    FragmentDrivinglicQrcodeviewBinding mBinding;
    QRDrivLicId dLic;

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

        mBinding.dragHandle.setVisibility(View.INVISIBLE);

        mBinding.LicNoDL.setText("No: " + dLic.licenseNumber);
        mBinding.FirstNameDL.setText("Name: " + dLic.firstName + " " + dLic.middleName + " " + dLic.lastName);
        mBinding.AddressStreetDL.setText("Address: " + dLic.addressStreet + "," + dLic.addressCity + "," + dLic.addressState);
        mBinding.AddressZipDL.setText("Zip code: " + dLic.addressZip);

        mBinding.BirthdateDL.setText("Birthday: " + dLic.birthDate);
        mBinding.ExpiryDateDL.setText("Expiry Date: " + dLic.expiryDate);
        mBinding.IssueDateDL.setText("Issue Date: " + dLic.issueDate);
        mBinding.CountryDL.setText("Country: " + dLic.issuingCountry);

        String gender = dLic.gender;
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

        String type = dLic.documentType;
        if (type.equals("DL")) {
            mBinding.TitleTextView.setText("Driving Licenses");
        } else if (type.equals("ID")) {
            mBinding.TitleTextView.setText("ID Card");
        }

        mBinding.CopyButton.setOnClickListener(view -> {
            CopyText(
                    "No: " + dLic.licenseNumber + "\n" +
                            "Name: " + dLic.firstName + " " + dLic.middleName + " " + dLic.lastName + "\n" +
                            "Gender: " + dLic.gender + "\n" +
                            "Birthday: " + dLic.birthDate + "\n" +
                            "Address: " + dLic.addressStreet + "," + dLic.addressCity + "," + dLic.addressState + "\n" +
                            "Zip code: " + dLic.addressZip + "\n" +
                            "Country: " + dLic.issuingCountry + "\n" +
                            "Expiry Date: " + dLic.expiryDate + "\n" +
                            "Issue Date: " + dLic.issueDate + "\n"


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