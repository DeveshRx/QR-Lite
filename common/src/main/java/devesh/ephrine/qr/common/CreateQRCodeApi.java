package devesh.ephrine.qr.common;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.util.Log;

import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CreateQRCodeApi {

     static int white = 0xFFFFFFFF;
     static int black = 0xFF000000;
     final static int WIDTH = 1000;

     Context mContext;
public CreateQRCodeApi(Context context){
    mContext = context;
}

    public Bitmap CreateWiFi(String WPA,
                             String ssid,
                             String password,
                             boolean isHidden,
                             String ttls,
                             String anon,
                             String identity,
                             String ph2) throws WriterException {
/*
WIFI:T:WPA;S:mynetwork;P:mypass;;
*/

        String text= "WIFI:";

        text=text+"T:"+WPA+";";

        text=text+"S:"+ssid+";";

        text=text+"P:"+password+";";
        text=text+"H:"+isHidden+";";

        if(WPA.equals("WPA2-EAP")){
            text=text+"E:"+ttls+";";
            text=text+"A:"+anon+";";
            text=text+"I:"+identity+";";
            text=text+"PH2:"+ph2+";";
        }


        return encodeAsBitmap(text);
    }

    public Bitmap CreateCalenderEvent(String startDate,String endDate,String SUMMARY) throws WriterException {
/*
BEGIN:VEVENT
SUMMARY:Summer+Vacation!
DTSTART:20180601T070000Z
DTEND:20180831T070000Z
END:VEVENT*/

        String text= "BEGIN:VEVENT" +
                "SUMMARY:"+SUMMARY +
                "DTSTART:"+startDate +
                "DTEND:"+endDate +
                "END:VEVENT";

        return encodeAsBitmap(text);
    }

    public Bitmap CreateGeoLocation(String Long,String lat) throws WriterException {
/*
geo:40.71872,-73.98905,100
*/

        String text="geo:"+lat+","+Long;

        return encodeAsBitmap(text);
    }

    public Bitmap CreateFaceTimeVideo(String id) throws WriterException {
/*
# FaceTime Video
facetime:+18005551212
facetime:me@icloud.com
*/

         String text="facetime:"+id;

         return encodeAsBitmap(text);
     }

    public Bitmap CreateFaceTimeAudio(String id) throws WriterException {
/*
# FaceTime Audio
facetime-audio:+18005551212
facetime-audio:me@icloud.com
*/

        String text="facetime-audio:"+id;

        return encodeAsBitmap(text);
    }

     public Bitmap CreateSMS(String phone, String body) throws WriterException {

         /*
         # Send an SMS/MMS to a number
sms:+18005551212

# Send an SMS/MMS to a number with pre-filled message.
sms:+18005551212:This%20is%20my%20text%20message.
         */
         String text="sms:"+phone;

         if(body!=null){
             text=text+":"+body;
             try {
                 text= URLEncoder.encode(text, "UTF-8");
             } catch (UnsupportedEncodingException e) {
                 e.printStackTrace();
             }

         }

         return encodeAsBitmap(text);
     }

     public Bitmap CreateVCard(VCard vCard) throws WriterException{

    /*
    BEGIN:VCARD
VERSION:3.0
N:Owen;Sean;;;
FN:Sean Owen
TITLE:Software Engineer
EMAIL;TYPE=INTERNET;TYPE=WORK;TYPE=PREF:srowen@google.com
URL;TYPE=Homepage:https://example.com
END:VCARD
*/
String text="BEGIN:VCARD" +
        "VERSION:3.0" +
        "FN:"+vCard.name +
        "TITLE"+vCard.title +
        "EMAIL;TYPE=INTERNET;TYPE=WORK;TYPE=PREF:"+vCard.email +
        "URL;TYPE=Homepage:"+vCard.url +
        "NOTE:"+vCard.note+
        "ORG:"+vCard.org+
        "ROLE:"+vCard.role+
        "END:VCARD";
    return encodeAsBitmap(text);
}

     public Bitmap CreateTelephone(String country, String phone) throws WriterException{
       /*  # NYC Directory assistance
         tel:+12125551212
         */
         String text="tel:"+country+phone;
         return encodeAsBitmap(text);

     }

    public Bitmap CreateEmail(String address,String body,String subject, String CC, String BCC) throws WriterException {
        /*
        # Address
mailto:someone@yoursite.com

# Address, subject
mailto:someone@yoursite.com?subject=Mail%20from%20Our%20Site

# Address, CC, BCC, subject
mailto:someone@yoursite.com?cc=someoneelse@theirsite.com,another@thatsite.com,me@mysite.com&bcc=lastperson@theirsite.com&subject=Big%20News

# Address, CC, BCC, subject, body
mailto:someone@yoursite.com?cc=someoneelse@theirsite.com,another@thatsite.com,me@mysite.com&bcc=lastperson@theirsite.com&subject=Big%20News&body=Body%20goes%20here.
*/
        String text="mailto:"+address+"?subject=";

        // Subject
        if(subject==null){
         subject=" ";
        }
        text=text+subject;

        // Body
        if(body==null){
            body=" ";
        }
        text=text+"&body="+body;

        // CC
        if(CC==null){
            CC=" ";
        }
        text=text+"&cc="+text;

        // BCC
        if(BCC==null){
            BCC=" ";
        }
        text=text+"&bcc="+BCC;


        try {
            text= URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeAsBitmap(text);
    }

     public Bitmap CreateQRFromText(String text) throws WriterException {
         return encodeAsBitmap(text);
     }


    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        Bitmap bitmap = null;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);

            int w = result.getWidth();
            int h = result.getHeight();
            int[] pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                int offset = y * w;
                for (int x = 0; x < w; x++) {
                    pixels[offset + x] = result.get(x, y) ? black : white;
                }
            }
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, WIDTH, 0, 0, w, h);
        } catch (Exception iae) {
            iae.printStackTrace();
            return null;
        }

        bitmap=drawTextToBitmap(mContext,bitmap,"Scan Using QRLite App");
        return bitmap;
    }

    Bitmap drawTextToBitmap(Context mContext, Bitmap resourceId, String mText) {
        try {
            Resources resources = mContext.getResources();
            float scale = resources.getDisplayMetrics().density;
            Bitmap bitmap = resourceId;
            android.graphics.Bitmap.Config bitmapConfig =   bitmap.getConfig();
            // set default bitmap config if none
            if(bitmapConfig == null) {
                bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
            }
            // resource bitmaps are imutable,
            // so we need to convert it to mutable one
            bitmap = bitmap.copy(bitmapConfig, true);

            Canvas canvas = new Canvas(bitmap);
            // new antialised Paint
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            // text color - #3D3D3D
            paint.setColor(Color.BLACK);
            // text size in pixels
            paint.setTextSize((int) (20 * scale));
            // text shadow
        //    paint.setShadowLayer(1f, 0f, 1f, Color.DKGRAY);

            // draw text to the Canvas center
            Rect bounds = new Rect();
            paint.getTextBounds(mText, 0, mText.length(), bounds);
            /*int x = (bitmap.getWidth() - bounds.width())/30;
            int y = (bitmap.getHeight() + bounds.height())/4;
*/
            int x = 200;
            int y = 970;

//            canvas.drawText(mText, x * scale, y * scale, paint);
            canvas.drawText(mText, x , y , paint);

            return bitmap;
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("TAG", "drawTextToBitmap: ",e );
            return null;
        }

    }

    public static class VCard{
    // https://en.wikipedia.org/wiki/VCard
    public String name;
   public String version="2.0";
        public String title;
        public String email;
        public String url;
        public String note;
        public String org;
        public String role;
    }

}




