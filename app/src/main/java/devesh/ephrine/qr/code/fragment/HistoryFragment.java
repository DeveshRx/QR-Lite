package devesh.ephrine.qr.code.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import devesh.ephrine.qr.code.MainActivity;
import devesh.ephrine.qr.code.adapters.HistoryAdapter;
import devesh.ephrine.qr.code.databinding.FragmentHistoryBinding;
import devesh.ephrine.qr.common.AdMobAPI;
import devesh.ephrine.qr.database.QRCodeFile;


public class HistoryFragment extends Fragment {

    FragmentHistoryBinding mBinding;
    List<QRCodeFile> qrCodeFileList;
    AdMobAPI adMobAPI;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //          mParam1 = getArguments().getString(ARG_PARAM1);
            //          mParam2 = getArguments().getString(ARG_PARAM2);
        }
        qrCodeFileList = new ArrayList<>();
        adMobAPI = new AdMobAPI(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentHistoryBinding.inflate(inflater, container, false);
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
        LoadRecycleview();
        mBinding.DeleteButton.setOnClickListener(view -> {
            ((MainActivity) getActivity()).HistoryDelete();
            Toast.makeText(getActivity(), "History Deleted", Toast.LENGTH_SHORT).show();
            LoadRecycleview();
        });

    }

    void LoadRecycleview() {
        qrCodeFileList = ((MainActivity) getActivity()).getHistory();
        HistoryAdapter mAdapter = new HistoryAdapter(getActivity(), qrCodeFileList);

        if (qrCodeFileList.isEmpty()) {
            mBinding.EmptyHistoryTextView.setVisibility(View.VISIBLE);
            mBinding.qrCodeHistoryRecycleview.removeAllViews();
            mBinding.qrCodeHistoryRecycleview.removeAllViewsInLayout();

            mBinding.DeleteButton.setVisibility(View.GONE);
        } else {
            mBinding.EmptyHistoryTextView.setVisibility(View.GONE);
            mBinding.DeleteButton.setVisibility(View.VISIBLE);
        }
        mBinding.qrCodeHistoryRecycleview.setAdapter(mAdapter);

    }

    void loadAds() {
        adMobAPI.setAdaptiveBanner(mBinding.adFrame, getActivity());
    }
}