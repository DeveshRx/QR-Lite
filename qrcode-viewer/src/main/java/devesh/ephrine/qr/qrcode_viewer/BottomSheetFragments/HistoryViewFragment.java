package devesh.ephrine.qr.qrcode_viewer.BottomSheetFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import devesh.ephrine.qr.qrcode_viewer.R;


public class HistoryViewFragment extends Fragment {


    int QRCode_Type=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
      //      mParam1 = getArguments().getString(ARG_PARAM1);
        //    mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this BottomSheetFragments
        return inflater.inflate(R.layout.fragment_history_view, container, false);
    }
}