package devesh.ephrine.qr.database;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import devesh.ephrine.qr.database.type.QREmail;
import devesh.ephrine.qr.database.type.QREvent;

@Entity
public class QRCodeHistoryFile {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "type")
    public int type;

    @Embedded
    public QREmail email;

    @Embedded
    public QREvent calender;




}
