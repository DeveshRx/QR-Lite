package devesh.ephrine.qr.database;


import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


import devesh.ephrine.qr.database.type.QRDrivLicId;
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


@Entity
public class QRCodeFile {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "type")
    public int type;

    @ColumnInfo(name = "epoch")
    public long epoch;

    @Embedded
    public QREvent event;

    @Embedded
    public QREmail email;

    @Embedded
    public QRFaceTime qrFaceTime;

    @Embedded
    public QRGeoLoc qrGeoLoc;

    @Embedded
    public QRSms qrSms;

    @Embedded
    public QRPhone qrPhone;

    @Embedded
    public QRText qrText;

    @Embedded
    public QRvCard qrvCard;

    @Embedded
    public QRWebsite qrWebsite;

    @Embedded
    public QRWifi qrWifi;

    @Embedded
    public QRDrivLicId DrivingLicID;

 /*  @Ignore
    public Bitmap qrcode;
 */




}