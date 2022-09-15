package devesh.ephrine.qr.qrcode_viewer.BottomSheetFragments;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;

import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.mlkit.vision.barcode.common.Barcode;

import java.lang.reflect.Type;

import devesh.ephrine.qr.qrcode_viewer.databinding.FragmentCalenderQrcodeviewBinding;


public class CalenderQrCodeViewBottomSheetFrag extends BottomSheetDialogFragment {

FragmentCalenderQrcodeviewBinding mBinding;
    Barcode.CalendarEvent cal;
    String des;
    String location;
    String Org;
    String status;
    String summary;

    String startDate;
    String startDateRaw;
    String startTime;

    String endDate;
    String endDateRaw;
    String endTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gson gson=new Gson();
        Type collectionType = new TypeToken<Barcode.CalendarEvent>(){}.getType();

        if (getArguments() != null) {
            String json = getArguments().getString("json");
    //        mParam1 = getArguments().getString(ARG_PARAM1);
      //      mParam2 = getArguments().getString(ARG_PARAM2);
            cal=gson.fromJson(json,collectionType);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentCalenderQrcodeviewBinding.inflate(inflater, container, false);
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

         des=cal.getDescription();
         location=cal.getLocation();
         Org=cal.getOrganizer();
         status=cal.getStatus();
         summary=cal.getSummary();

         startDate=getDate(cal.getStart());
         startTime=getTime(cal.getStart());
         startDateRaw=cal.getStart().getRawValue();

         endDate=getDate(cal.getEnd());
         endTime=getTime(cal.getEnd());
         endDateRaw=cal.getEnd().getRawValue();

if(des!=null){
    mBinding.calenderDesc.setText("Description: "+des);
}else{}

if(location!=null){
    mBinding.calenderLocation.setText("Location: "+location);
}

if(status!=null){
    mBinding.calenderStatus.setText("Status: "+status);
}

if(summary!=null){
    mBinding.calenderSummary.setText("Summary: "+summary);
}

    mBinding.calenderStartDate.setText("Start Date: "+startDate+" @ "+ startTime);


         mBinding.calenderEndDate.setText("End Date: "+endDate+" @ "+endTime);


         mBinding.SaveCalenderButton.setOnClickListener(view -> {
             Save2Calender();
         });
         mBinding.CopyButton.setOnClickListener(view -> {
             CopyText("Description: "+des+"\n"+
                     "Location: "+location+"\n"+
                     "Status: "+status+"\n"+
                     "Summary: "+summary+"\n"+
                     "Start Date: "+startDate+" @ "+ startTime+"\n"+
                     "End Date: "+endDate+" @ "+endTime
             );
         });

    }

    String getDate(Barcode.CalendarDateTime calc){
        int hour=calc.getHours();
        int min=calc.getMinutes();

        int day=calc.getDay();
        int month=calc.getMonth();
        int year= calc.getYear();

        String d=day+"-"+month+"-"+year;

        return  d;
    }

    String getTime(Barcode.CalendarDateTime calc){
        int hour=calc.getHours();
        int min=calc.getMinutes();

        int day=calc.getDay();
        int month=calc.getMonth();
        int year= calc.getYear();

        String d=hour+":"+min;

        return  d;
    }

    void Save2Calender(){
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, summary)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, location)
                .putExtra(CalendarContract.Events.DESCRIPTION, des)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startDateRaw)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endDateRaw);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    void CopyText(String text) {

        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("QR Code", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getActivity(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();

    }
}