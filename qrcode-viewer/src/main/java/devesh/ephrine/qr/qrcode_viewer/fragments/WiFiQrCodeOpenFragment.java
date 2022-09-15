package devesh.ephrine.qr.qrcode_viewer.fragments;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.thanosfisherman.wifiutils.WifiUtils;
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionErrorCode;
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionSuccessListener;

import devesh.ephrine.qr.qrcode_viewer.databinding.FragmentWifiQrcodeviewBinding;


public class WiFiQrCodeOpenFragment extends Fragment {
   public  static final int TYPE_OPEN = 1;
public    static final int TYPE_WPA = 2;
public     static final int TYPE_WEP = 3;
    FragmentWifiQrcodeviewBinding mBinding;
    String ssid;
    String pass;
    int WiFi_Type = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ssid = getArguments().getString("ssid");
            pass = getArguments().getString("pass");
            WiFi_Type = getArguments().getInt("type");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentWifiQrcodeviewBinding.inflate(inflater, container, false);
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
        mBinding.CopyButton.setOnClickListener(view -> {
            CopyText("WifI Details\n\nSSID: " + ssid + "\nPassword: " + pass);
        });

        mBinding.ssidTx.setText("SSID: " + ssid);
        mBinding.passwordTx.setText("Password: " + pass);
        if (WiFi_Type == TYPE_OPEN) {
            mBinding.encryptionTx.setText("Type: Open");
        } else if (WiFi_Type == TYPE_WEP) {
            mBinding.encryptionTx.setText("Type: WEP");
        } else if (WiFi_Type == TYPE_WPA) {
            mBinding.encryptionTx.setText("Type: WPA");
        } else {
            mBinding.encryptionTx.setText(" ");
        }

        mBinding.WifiConnectButton.setOnClickListener(view -> {

            ConnectWifiInit();
        });

    }


    void CopyText(String text) {

        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("QR Code", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getActivity(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();

    }

    void ConnectWifiInit() {
        showProgressBar(true);
        Toast.makeText(getActivity(), "Connecting to WiFi....", Toast.LENGTH_SHORT).show();
        WifiUtils.withContext(getActivity()).enableWifi(this::checkResult);

    }

    private void checkResult(boolean isSuccess) {
        if (isSuccess) {
            //Toast.makeText(getActivity(), "WIFI ENABLED", Toast.LENGTH_SHORT).show();
            Connect2WiFi();
        } else {
            showProgressBar(false);
            Toast.makeText(getActivity(), "COULDN'T ENABLE WIFI", Toast.LENGTH_SHORT).show();
        }
    }

    void Connect2WiFi() {
        WifiUtils.withContext(getActivity())
                .connectWith(ssid, pass)
                .setTimeout(60000)
                .onConnectionResult(new ConnectionSuccessListener() {
                    @Override
                    public void success() {
                        showProgressBar(false);
                        Toast.makeText(getActivity(), "Connected to WiFi!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void failed(@NonNull ConnectionErrorCode errorCode) {
                        showProgressBar(false);
                        Toast.makeText(getActivity(), "WiFi Connection Failed!" + errorCode, Toast.LENGTH_SHORT).show();
                    }
                })
                .start();

    }

    void showProgressBar(boolean isShow){
        getActivity().runOnUiThread(() -> {
            if(isShow){
                mBinding.progressBar.setVisibility(View.VISIBLE);
            }else{
                mBinding.progressBar.setVisibility(View.GONE);
            }

        });
    }
}