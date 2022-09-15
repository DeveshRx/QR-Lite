package devesh.ephrine.qr.database;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {QRCodeFile.class,QRCodeHistoryFile.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract QRCodeDao qrcodeDao();
}