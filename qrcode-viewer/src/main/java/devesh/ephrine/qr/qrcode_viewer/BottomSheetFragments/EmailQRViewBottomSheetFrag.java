package devesh.ephrine.qr.qrcode_viewer.BottomSheetFragments;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import devesh.ephrine.qr.qrcode_viewer.databinding.FragmentEmailQrviewBinding;


public class EmailQRViewBottomSheetFrag extends BottomSheetDialogFragment {


    FragmentEmailQrviewBinding mBinding;
    String emailAddress="";
    String emailBody="";
    String emailSubject="";
    int emailType=0;

    static final int EMAIL_TYPE_UNKNOWN=0;
    static final int EMAIL_TYPE_WORK=1;
    static final int EMAIL_TYPE_HOME=2;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            emailAddress = getArguments().getString("email");
            emailSubject = getArguments().getString("sub");
            emailBody = getArguments().getString("body");
            emailType=getArguments().getInt("type");
      //      mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        mBinding = FragmentEmailQrviewBinding.inflate(inflater, container, false);
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
        mBinding.emailAddressTx.setText("Address: "+emailAddress);
        mBinding.CopyButton.setOnClickListener(view -> {
            CopyText(emailAddress);
        });

        mBinding.EmailSendButton.setOnClickListener(view -> {
            SendEmail();
        });

if(emailSubject!="x"){
    mBinding.emailSubjectTx.setText("Subject: "+emailSubject);
}

if(emailBody!="x"){
    mBinding.emailBodyTx.setText("Message: "+emailBody);
}

if(emailType==EMAIL_TYPE_HOME){
    mBinding.emailTypeChip.setText("Home");
    mBinding.emailTypeChip.setChipIcon(getActivity().getDrawable(devesh.ephrine.qr.common.R.drawable.ic_baseline_home_30));
}else if(emailType==EMAIL_TYPE_WORK){
    mBinding.emailTypeChip.setText("Work");
    mBinding.emailTypeChip.setChipIcon(getActivity().getDrawable(devesh.ephrine.qr.common.R.drawable.ic_baseline_work_30));
}else{
    mBinding.emailTypeChip.setText("Email");
    mBinding.emailTypeChip.setChipIcon(getActivity().getDrawable(devesh.ephrine.qr.common.R.drawable.ic_baseline_alternate_email_30));
}


    }

    void CopyText(String text) {

        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("OCR", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getActivity(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();

    }

    void SendEmail(){
        String[] addressess=new String[]{emailAddress};


        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_EMAIL, addressess);
        intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
        intent.putExtra(Intent.EXTRA_TEXT, emailBody);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}