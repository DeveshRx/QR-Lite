package devesh.ephrine.qr.code.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import devesh.app.billing.BillingActivity;
import devesh.ephrine.qr.code.R;
import devesh.ephrine.qr.code.databinding.FragmentSettingsBinding;
import devesh.ephrine.qr.common.CachePref;
import devesh.ephrine.qr.common.InstallSource;


public class SettingsFragment extends Fragment {
    FragmentSettingsBinding mBinding;
    CachePref cachePref;
    boolean isSubscription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //          mParam1 = getArguments().getString(ARG_PARAM1);
            //        mParam2 = getArguments().getString(ARG_PARAM2);
        }
        isSubscription = false;
        cachePref = new CachePref(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentSettingsBinding.inflate(inflater, container, false);
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

        Fragment frag = new MySettingsFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(mBinding.prefView.getId(), frag).commit();

        isSubscription = cachePref.getBoolean(getString(devesh.ephrine.qr.common.R.string.Pref_isSubscribed));
        if (isSubscription) {
            mBinding.PremiumMemberBadge.getRoot().setVisibility(View.VISIBLE);
        } else {
            mBinding.PremiumMemberBadge.getRoot().setVisibility(View.GONE);
        }

    }


    public static class MySettingsFragment extends PreferenceFragmentCompat {
        String TAG = "PrefSettings";
        CachePref cachePref;
        boolean isSub;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);
            isSub = false;

            cachePref = new CachePref(getActivity());

            isSub = cachePref.getBoolean(getString(devesh.ephrine.qr.common.R.string.Pref_isSubscribed));

            Preference SecPri = findPreference("secpri");
            SecPri.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Log.d(TAG, "onPreferenceClick: ");
                    String url = "https://deveshrx.com/privacy_policy";

                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    // Verify that the intent will resolve to an activity
                    startActivity(intent);

                    return true;
                }
            });

            Preference BuyPref = findPreference("buy");

            BuyPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {


                    Intent intent = new Intent(getActivity(), BillingActivity.class);
                    // Verify that the intent will resolve to an activity
                    startActivity(intent);

                    return true;
                }
            });
            if (isSub) {
                BuyPref.setSummary("");
            } else {
                BuyPref.setSummary("Get Ad-Free Experience");
            }

            Preference drxWebsite= findPreference("drx");
            drxWebsite
                    .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                        public boolean onPreferenceClick(Preference preference) {
                            Log.d(TAG, "onPreferenceClick: ");
                            String url = getString(R.string.deveshrx_website);
                            Uri uri = Uri.parse(url);
                           Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                          // Verify that the intent will resolve to an activity
                            startActivity(intent);

                            return true;
                       }
                   });

           // Preference MoreAppsPref = findPreference("MoreApps");

//            if (InstallSource.isGalaxyStore(getActivity())) {
//                MoreAppsPref.setVisible(false);
//            } else {
//                MoreAppsPref.setVisible(true);
//                MoreApps();
//
//            }


        }


//        void MoreApps() {
//
//
//            findPreference("textmaster")
//                    .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//                        public boolean onPreferenceClick(Preference preference) {
//                            Log.d(TAG, "onPreferenceClick: ");
//                            String url = getString(devesh.app.moreapps.R.string.MoreApps_TextMaster_url);
//                            Uri uri = Uri.parse(url);
//                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                            // Verify that the intent will resolve to an activity
//                            startActivity(intent);
//
//                            return true;
//                        }
//                    });
//
//
//            findPreference("smsdrive")
//                    .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//                        public boolean onPreferenceClick(Preference preference) {
//                            Log.d(TAG, "onPreferenceClick: ");
//                            String url = getString(devesh.app.moreapps.R.string.MoreApps_SMS_Drive_url);
//                            Uri uri = Uri.parse(url);
//                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                            // Verify that the intent will resolve to an activity
//                            startActivity(intent);
//
//                            return true;
//                        }
//                    });
//
//            findPreference("indra")
//                    .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//                        public boolean onPreferenceClick(Preference preference) {
//                            Log.d(TAG, "onPreferenceClick: ");
//                            String url = getString(devesh.app.moreapps.R.string.MoreApps_Indra_url);
//                            Uri uri = Uri.parse(url);
//                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                            // Verify that the intent will resolve to an activity
//                            startActivity(intent);
//
//                            return true;
//                        }
//                    });
//
//
//            findPreference("pharmahub")
//                    .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//                        public boolean onPreferenceClick(Preference preference) {
//                            Log.d(TAG, "onPreferenceClick: ");
//                            String url = getString(devesh.app.moreapps.R.string.MoreApps_PharmaHub_url);
//                            Uri uri = Uri.parse(url);
//                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                            // Verify that the intent will resolve to an activity
//                            startActivity(intent);
//
//                            return true;
//                        }
//                    });
//
//            findPreference("sht")
//                    .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//                        public boolean onPreferenceClick(Preference preference) {
//                            Log.d(TAG, "onPreferenceClick: ");
//                            String url = getString(devesh.app.moreapps.R.string.MoreApps_SHT_url);
//                            Uri uri = Uri.parse(url);
//                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                            // Verify that the intent will resolve to an activity
//                            startActivity(intent);
//
//                            return true;
//                        }
//                    });
//
//            findPreference("jinx")
//                    .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//                        public boolean onPreferenceClick(Preference preference) {
//                            Log.d(TAG, "onPreferenceClick: ");
//                            String url = getString(devesh.app.moreapps.R.string.MoreApps_Jinx_url);
//                            Uri uri = Uri.parse(url);
//                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                            // Verify that the intent will resolve to an activity
//                            startActivity(intent);
//
//                            return true;
//                        }
//                    });
//
//            findPreference("muzilla")
//                    .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//                        public boolean onPreferenceClick(Preference preference) {
//                            Log.d(TAG, "onPreferenceClick: ");
//                            String url = getString(devesh.app.moreapps.R.string.MoreApps_Muzilla_url);
//                            Uri uri = Uri.parse(url);
//                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                            // Verify that the intent will resolve to an activity
//                            startActivity(intent);
//
//                            return true;
//                        }
//                    });
//
//        /*    findPreference("qrlite")
//                    .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//                        public boolean onPreferenceClick(Preference preference) {
//                            Log.d(TAG, "onPreferenceClick: ");
//                            String url = getString(devesh.app.moreapps.R.string.MoreApps_QRLite_url);
//                            Uri uri = Uri.parse(url);
//                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                            // Verify that the intent will resolve to an activity
//                            startActivity(intent);
//
//                            return true;
//                        }
//                    });
//
//       */
//            findPreference("vlsd")
//                    .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//                        public boolean onPreferenceClick(Preference preference) {
//                            Log.d(TAG, "onPreferenceClick: ");
//                            String url = getString(devesh.app.moreapps.R.string.MoreApps_VirtualLSD_url);
//                            Uri uri = Uri.parse(url);
//                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                            // Verify that the intent will resolve to an activity
//                            startActivity(intent);
//
//                            return true;
//                        }
//                    });
//
//
//        }

    }


}