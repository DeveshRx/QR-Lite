package devesh.ephrine.qr.database;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface QRCodeDao {
    @Query("SELECT * FROM qrcodefile ORDER BY epoch desc")
    List<QRCodeFile> getMyQRCodeAll();

    @Query("SELECT * FROM qrcodehistoryfile")
    List<QRCodeHistoryFile> getAllHistory();



  /*  @Query("SELECT * FROM qrcodefile WHERE uid IN (:userIds)")
    List<QRCodeFile> loadAllByIds(int[] userIds);
*/
   /* @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    User findByName(String first, String last);
*/
    @Insert
    void insertAll(QRCodeFile... qrfile);

    @Insert
    void insert(QRCodeFile qrfile);

    @Insert
    void insert(QRCodeHistoryFile qrfile);

    @Delete
    void delete(QRCodeFile user);

    @Query("DELETE FROM qrcodefile")
    void nukeTable();
}