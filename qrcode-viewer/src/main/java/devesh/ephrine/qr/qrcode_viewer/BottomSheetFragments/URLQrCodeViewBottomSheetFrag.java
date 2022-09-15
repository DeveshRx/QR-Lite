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

import androidx.browser.customtabs.CustomTabsIntent;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import devesh.ephrine.qr.qrcode_viewer.databinding.FragmentUrlQrcodeviewBinding;


public class URLQrCodeViewBottomSheetFrag extends BottomSheetDialogFragment {



    FragmentUrlQrcodeviewBinding mBinding;
    String mURL="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mURL = getArguments().getString("url");

        }
    }

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        mBinding = FragmentUrlQrcodeviewBinding.inflate(inflater, container, false);
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
        mBinding.qrtext.setText(mURL);
        mBinding.CopyButton.setOnClickListener(view -> {
            CopyText(mURL);
        });

        mBinding.UrlOpenButton.setOnClickListener(view -> {
            OpenURL();
        });
    }


    void CopyText(String text) {

        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("OCR", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getActivity(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();

    }

    void OpenURL(){
        String url = mURL;
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(getActivity(), Uri.parse(url));

        /*Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        // Verify that the intent will resolve to an activity
        startActivity(intent);*/
    }

}
