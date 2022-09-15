package devesh.ephrine.qr.qrcode_viewer.fragments;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import devesh.ephrine.qr.qrcode_viewer.databinding.FragmentRawTextQrcodeBinding;


public class RawTextQRCodeOpenFragment extends Fragment {

    String RawText="";

    FragmentRawTextQrcodeBinding mBinding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            RawText = getArguments().getString("t");
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        mBinding = FragmentRawTextQrcodeBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d("bottomsheet", "onDestroy: ");
    }

    @Override
    public void onStart() {
        super.onStart();

        mBinding.dragHandle.setVisibility(View.INVISIBLE);
        mBinding.qrtext.setText(RawText);
mBinding.CopyButton.setOnClickListener(view -> {
    CopyText(RawText);
});
    }





    void CopyText(String text) {

        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("QR Code", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getActivity(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();

    }
}