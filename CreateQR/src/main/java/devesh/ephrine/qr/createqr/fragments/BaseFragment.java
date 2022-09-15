package devesh.ephrine.qr.createqr.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import devesh.ephrine.qr.database.AppDatabase;
import devesh.ephrine.qr.database.QRCodeFile;
import devesh.ephrine.qr.database.type.QREmail;
import devesh.ephrine.qr.database.type.QREvent;
import devesh.ephrine.qr.database.type.QRFaceTime;
import devesh.ephrine.qr.database.type.QRGeoLoc;
import devesh.ephrine.qr.database.type.QRPhone;
import devesh.ephrine.qr.database.type.QRSms;
import devesh.ephrine.qr.database.type.QRText;
import devesh.ephrine.qr.database.type.QRWebsite;
import devesh.ephrine.qr.database.type.QRWifi;
import devesh.ephrine.qr.database.type.QRvCard;

public class BaseFragment extends Fragment {
public AppDatabase AppDB;
public QREvent qrEvent;
public QRCodeFile qrCodeFile;
    public QREmail qrEmail;
    public QRFaceTime qrFaceTime;
public QRGeoLoc qrGeoLoc;

public QRSms qrSms;
public QRPhone qrPhone;
public QRText qrText;
    public QRvCard qrvCard;
public QRWebsite qrWebsite;

public QRWifi qrWifi;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initialization
        AppDB = Room.databaseBuilder(getActivity(),
                        AppDatabase.class, getString(devesh.ephrine.qr.database.R.string.DB_NAME))
                .fallbackToDestructiveMigration().allowMainThreadQueries().build();

        qrEvent=new QREvent();
        qrCodeFile=new QRCodeFile();
        qrEmail=new QREmail();
        qrFaceTime=new QRFaceTime();
qrGeoLoc=new QRGeoLoc();
qrSms=new QRSms();
        qrPhone=new QRPhone();
        qrText=new QRText();
qrvCard=new QRvCard();
qrWebsite=new QRWebsite();
qrWifi=new QRWifi();


    }




}
