package devesh.ephrine.qr.qrcode_viewer.BottomSheetFragments;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import devesh.ephrine.qr.qrcode_viewer.databinding.FragmentPhoneQrCodeBinding;


public class PhoneQrCodeBottomSheetFrag extends BottomSheetDialogFragment {
    FragmentPhoneQrCodeBinding mBinding;
    String phoneNumber = "";
    String message = "";
    boolean isSMS;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            phoneNumber = getArguments().getString("phone");
            isSMS = false;
            if (getArguments().getString("msg") != null) {
                message = getArguments().getString("msg");
                isSMS = true;
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentPhoneQrCodeBinding.inflate(inflater, container, false);
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
        if (isSMS) {
            mBinding.titleTextView.setText("SMS Details");
            if (message != null) {
                mBinding.msgTx.setText("Message: " + message);
            }
            mBinding.PhoneCallButton.setVisibility(View.GONE);
        } else {
            mBinding.titleTextView.setText("Phone Details");
            mBinding.msgTx.setVisibility(View.INVISIBLE);
            mBinding.PhoneCallButton.setOnClickListener(view -> {
                dialPhoneNumber(phoneNumber);
            });
        }

        mBinding.phoneTx.setText(phoneNumber);
        mBinding.CopyButton.setOnClickListener(view -> {
            if(isSMS){CopyText("phone: "+phoneNumber+"\nMessage: "+message);}else{
                CopyText(phoneNumber);
            }
        });


        mBinding.PhoneMessageButton.setOnClickListener(view -> {
            composeMmsMessage();
        });
    }


    void CopyText(String text) {

        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("OCR", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getActivity(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();

    }

    public void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void composeMmsMessage() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.setData(Uri.parse("smsto:" + phoneNumber));  // This ensures only SMS apps respond
        intent.putExtra("sms_body", message);

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}