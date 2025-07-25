package devesh.ephrine.qr.code.adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import devesh.ephrine.qr.code.MainActivity;
import devesh.ephrine.qr.code.R;
import devesh.ephrine.qr.database.QRCodeFile;
import devesh.ephrine.qr.database.QRCodeTypeConstants;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private final List<QRCodeFile> localDataSet;
    String TAG = "HistoryAdapt";
    Context mContext;

    public HistoryAdapter(Context context, List<QRCodeFile> dataSet) {
        localDataSet = dataSet;
        mContext = context;
    }

    // ==

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycleview_history_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {

        // Get element from your dataset at this i and replace the
        // contents of the view with that element
        // viewHolder.getqrHistoryTitle().setText(localDataSet[i]);

        String title = "";
        String subTitle = "";
        int thumbImg = 0;

        int type = localDataSet.get(i).type;
        try {
            if (type == QRCodeTypeConstants.QRCODE_TYPE_DRIVING_LIC_ID) {
                title = localDataSet.get(i).DrivingLicID.firstName + " " + localDataSet.get(i).DrivingLicID.lastName;
                thumbImg = devesh.ephrine.qr.common.R.drawable.ic_baseline_credit_card_40;
            } else if (type == QRCodeTypeConstants.QRCODE_TYPE_TEXT) {
                title = localDataSet.get(i).qrText.text;
                thumbImg = devesh.ephrine.qr.common.R.drawable.ic_baseline_text_fields_40;
            } else if (type == QRCodeTypeConstants.QRCODE_TYPE_EMAIL) {
                title = localDataSet.get(i).email.email;
                thumbImg = devesh.ephrine.qr.common.R.drawable.ic_baseline_email_40;
                //subTitle=localDataSet.get(i).email.subject;
            } else if (type == QRCodeTypeConstants.QRCODE_TYPE_Facetime) {
                title = localDataSet.get(i).qrFaceTime.id;
                thumbImg = devesh.ephrine.qr.common.R.drawable.ic_baseline_videocam_40;
            } else if (type == QRCodeTypeConstants.QRCODE_TYPE_Geo) {
                title = "Lat:" + localDataSet.get(i).qrGeoLoc.lat + ", Long:" + localDataSet.get(i).qrGeoLoc.longt;
                thumbImg = devesh.ephrine.qr.common.R.drawable.ic_baseline_explore_40;
            } else if (type == QRCodeTypeConstants.QRCODE_TYPE_SMS) {
                title = localDataSet.get(i).qrSms.sms_phone;
                subTitle = localDataSet.get(i).qrSms.sms;
                thumbImg = devesh.ephrine.qr.common.R.drawable.ic_baseline_message_40;

            } else if (type == QRCodeTypeConstants.QRCODE_TYPE_Tele) {
                title = localDataSet.get(i).qrPhone.CountryCode + localDataSet.get(i).qrPhone.Phone;
                thumbImg = devesh.ephrine.qr.common.R.drawable.ic_baseline_phone_40;
            } else if (type == QRCodeTypeConstants.QRCODE_TYPE_VCARD) {
                title = localDataSet.get(i).qrvCard.title;
                subTitle = localDataSet.get(i).qrvCard.name;
                thumbImg = devesh.ephrine.qr.common.R.drawable.ic_baseline_contact_emergency_40;
            } else if (type == QRCodeTypeConstants.QRCODE_TYPE_WIFI) {
                title = localDataSet.get(i).qrWifi.SSID;
                subTitle = localDataSet.get(i).qrWifi.WPA;
                thumbImg = devesh.ephrine.qr.common.R.drawable.ic_baseline_wifi_40;
            } else if (type == QRCodeTypeConstants.QRCODE_TYPE_CALENDER) {
                title = localDataSet.get(i).event.Summary;
                if (title.equals("") || title == null) {
                    title = localDataSet.get(i).event.Description;
                }
                subTitle = localDataSet.get(i).event.StartDateTime + " - " + localDataSet.get(i).event.EndDateTime;
                thumbImg = devesh.ephrine.qr.common.R.drawable.ic_baseline_calendar_month_40;
            } else if (type == QRCodeTypeConstants.QRCODE_TYPE_WEBSITE) {
                title = localDataSet.get(i).qrWebsite.url;
                thumbImg = devesh.ephrine.qr.common.R.drawable.ic_baseline_language_40;
            }


        } catch (Exception e) {
            Log.d(TAG, "onBindViewHolder: " + e);
        }


//        else if(type==QRCodeTypeConstants.QRCODE_TYPE_TEXT){}


        viewHolder.qrHistoryTitle.setText(title);
        viewHolder.qrHistorySub.setText(subTitle);
        viewHolder.getThumbIMG().setImageResource(thumbImg);

        if (title.equals("")) {
            viewHolder.qrHistoryTitle.setVisibility(View.GONE);
        } else if (subTitle.equals("")) {
            viewHolder.qrHistorySub.setVisibility(View.GONE);
        }

        viewHolder.LLHistory.setOnClickListener(view -> {
            if (mContext instanceof MainActivity) {

                ((MainActivity) mContext).openHistoryrecord(localDataSet.get(i));

            }
        });


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView qrHistoryTitle;
        private final TextView qrHistorySub;
        private final LinearLayout LLHistory;
        private final ImageView ThumbIMG;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            qrHistoryTitle = view.findViewById(R.id.qrHistoryTitle);
            qrHistorySub = view.findViewById(R.id.qrHistorySubTitle);
            LLHistory = view.findViewById(R.id.LLHistory);
            ThumbIMG = view.findViewById(R.id.QRThumbIMG);
        }

        public TextView getqrHistoryTitle() {
            return qrHistoryTitle;
        }

        public TextView getqrHistorySub() {
            return qrHistorySub;
        }

        public LinearLayout getLLHistory() {
            return LLHistory;
        }

        public ImageView getThumbIMG() {
            return ThumbIMG;
        }
    }
}

