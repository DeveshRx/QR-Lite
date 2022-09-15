package devesh.ephrine.qr.createqr.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.room.Room;

import com.google.zxing.WriterException;

import java.util.Calendar;

import devesh.ephrine.qr.common.CreateQRCodeApi;
import devesh.ephrine.qr.createqr.CreateQRCodeActivity;
import devesh.ephrine.qr.createqr.R;
import devesh.ephrine.qr.createqr.databinding.FragmentCalenderQrcodeBinding;
import devesh.ephrine.qr.database.AppDatabase;
import devesh.ephrine.qr.database.QRCodeFile;
import devesh.ephrine.qr.database.QRCodeTypeConstants;
import devesh.ephrine.qr.database.type.QREvent;


public class CalenderQRCodeFragment extends BaseFragment {
    FragmentCalenderQrcodeBinding mBinding;
    String TAG = "CalQRFrag";

    SEDate startDate = new SEDate();
    SEDate endDate = new SEDate();

    SETime startTime = new SETime();
    SETime endTime = new SETime();

CreateQRCodeApi createQRCodeApi;
    // 1994-11-05T13:15:30Z
   // AppDatabase AppDB;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
            //          mParam2 = getArguments().getString(ARG_PARAM2);
        }
     createQRCodeApi=new CreateQRCodeApi(getActivity());

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = (c.get(Calendar.MONTH) + 1);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        startDate.day = day;
        startDate.month = month;
        startDate.year = year;
        startTime.hour = hour;
        startTime.min = minute;

        endDate.year = year;
        endDate.month = month;
        endDate.day = (day + 1);
        endTime.min = minute;
        endTime.hour = hour;


        //Initialization
  /*      AppDB = Room.databaseBuilder(getActivity(),
                AppDatabase.class, getString(devesh.ephrine.qr.database.R.string.DB_NAME))
                .fallbackToDestructiveMigration().allowMainThreadQueries().build();
*/


    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentCalenderQrcodeBinding.inflate(inflater, container, false);
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


        mBinding.StartTimeChip.setOnClickListener(view -> {
            DialogFragment newFragment = new StartTimePickerFragment();
            newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
        });

        mBinding.StartDateChip.setOnClickListener(view -> {
            DialogFragment newFragment = new StartDatePickerFragment();
            newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
        });

        mBinding.EndTimeChip.setOnClickListener(view -> {
            DialogFragment newFragment = new EndTimePickerFragment();
            newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");

        });

        mBinding.EndDateChip.setOnClickListener(view -> {
            DialogFragment newFragment = new EndDatePickerFragment();
            newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");

        });

        mBinding.CreateQRCodeButton.setOnClickListener(view -> {
            String summary = mBinding.EverySummaryET.getText().toString();
            // 1994-11-05T13:15:30Z
            String startDateTime=startDate.year+"-"+startDate.month+"-"+startDate.day+"T"+ startTime.hour+":"+startTime.min+"Z";
            String endDateTime=endDate.year+"-"+endDate.month+"-"+endDate.day+"T"+ endTime.hour+":"+endTime.min+"Z";

            if(summary==null || summary.equals("") || summary.equals(" ")){
                summary=" ";
            }
            Log.d(TAG, "onStart: " + summary+"\nstart: "+startDateTime+"\nend: "+endDateTime);
            Bitmap qrcode=null;
            try {
                 qrcode=createQRCodeApi.CreateCalenderEvent(startDateTime,endDateTime,summary);
            } catch (WriterException e) {
                e.printStackTrace();
            }
            qrEvent.Summary=summary;
            qrEvent.StartDateTime=startDateTime;
            qrEvent.EndDateTime=endDateTime;

            qrCodeFile.type= QRCodeTypeConstants.QRCODE_TYPE_CALENDER;
            qrCodeFile.event=qrEvent;
            qrCodeFile.epoch=System.currentTimeMillis();


            AppDB.qrcodeDao().insert(qrCodeFile);

            ((CreateQRCodeActivity)getActivity()).CreateQRCode(qrcode);
        });


        mBinding.StartDateChip.setText(startDate.day+"/"+startDate.month+"/"+startDate.year);
        mBinding.StartTimeChip.setText(startTime.hour+":"+startTime.min);

        mBinding.EndDateChip.setText(endDate.day+"/"+ endDate.month+"/"+endDate.year);
        mBinding.EndTimeChip.setText(endTime.hour+":"+endTime.min);

    }

    public void setStartTime(int hourOfDay, int minute) {
        Log.d(TAG, "setTime: " + hourOfDay + ":" + minute);
        startTime.hour = hourOfDay;
        startTime.min = minute;

        mBinding.StartTimeChip.setText(hourOfDay + ":" + minute);

    }

    public void setStartDate(int year, int month, int day) {
        Log.d(TAG, "setTime: " + day + " " + month + " " + year);
        startDate.day = day;
        startDate.month = month;
        startDate.year = year;
        mBinding.StartDateChip.setText(day + "/" + month + "/" + year);
    }

    public void setEndTime(int hourOfDay, int minute) {
        Log.d(TAG, "setTime: " + hourOfDay + ":" + minute);
        endTime.hour = hourOfDay;
        endTime.min = minute;
        mBinding.EndTimeChip.setText(hourOfDay + ":" + minute);
    }

    public void setEndDate(int year, int month, int day) {
        Log.d(TAG, "setTime: " + day + " " + month + " " + year);
        endDate.day = day;
        endDate.year = year;
        endDate.month = month;
        mBinding.EndDateChip.setText(day + "/" + month + "/" + year);
    }


    public static class StartTimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            FragmentManager fm = getParentFragmentManager();
            CalenderQRCodeFragment fragm = (CalenderQRCodeFragment) fm.findFragmentByTag("createqr");
            int min=minute;
            if(min<10){
                String f="0"+String.valueOf(min);
                 min=Integer.parseInt(f);
            }

            fragm.setStartTime(hourOfDay, min);
        }
    }

    public static class StartDatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of TimePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day
            );
        }


        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            FragmentManager fm = getParentFragmentManager();
            CalenderQRCodeFragment fragm = (CalenderQRCodeFragment) fm.findFragmentByTag("createqr");
            fragm.setStartDate(year, (month + 1), day);

        }
    }


    public static class EndTimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            FragmentManager fm = getParentFragmentManager();
            CalenderQRCodeFragment fragm = (CalenderQRCodeFragment) fm.findFragmentByTag("createqr");
            int min=minute;
            if(min<10){
                String f="0"+String.valueOf(min);
                min=Integer.parseInt(f);
            }
            fragm.setEndTime(hourOfDay, min);
        }
    }

    public static class EndDatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of TimePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day
            );
        }


        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            FragmentManager fm = getParentFragmentManager();
            CalenderQRCodeFragment fragm = (CalenderQRCodeFragment) fm.findFragmentByTag("createqr");
            fragm.setEndDate(year, (month + 1), day);

        }
    }

    class SEDate {
        public int day;
        public int month;
        public int year;
    }

    class SETime {
        public int hour;
        public int min;
    }

}