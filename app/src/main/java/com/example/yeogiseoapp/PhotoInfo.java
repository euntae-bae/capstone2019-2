package com.example.yeogiseoapp;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class PhotoInfo {
    Uri uri;
    int id;
    String name = "name";
    long time;
    float latitude;
    float longitude;
    int orientation;
    String comment = "comment";

    PhotoInfo(){
        uri = null;
        id = -1;
        time = -1;
        latitude = -1;
        longitude = -1;
        orientation = 0;
    }

    PhotoInfo(Uri u){
        uri = u;
        id = -1;
        time = -1;
        latitude = -1;
        longitude = -1;
        orientation = 0;
    }
    PhotoInfo(Uri u, long t, float la, float lo){
        uri = u;
        time = t;
        latitude = la;
        longitude = lo;
    }

    PhotoInfo(Uri u, String t, String la, String lo){
        uri = u;
        time = convertToTime(t);
        latitude = convertToDegree(la);
        longitude = convertToDegree(lo);
    }
    public void setInfo(Uri u, long t, float la, float lo, int or){
        uri = u;
        time = t;
        latitude = la;
        longitude = lo;
        orientation = or;
    }

    public void setInfo(Uri u, String t, String la, String lo, int or){
        uri = u;
        time = convertToTime(t);
        latitude = convertToDegree(la);
        longitude = convertToDegree(lo);
        orientation = or;
    }

    public void setUri(Uri u){
        uri = u;
    }

    public void setTime(long t){
        time = t;
    }
    public void setTime(String t){
        time = convertToTime(t);
    }

    public void setLatitude(float l){
        latitude = l;
    }
    public void setLatitude(String l){
        latitude = convertToDegree(l);
    }

    public void setLongitude(float l){
        longitude = l;
    }
    public void setLongitude(String l){
        longitude = convertToDegree(l);
    }

    public void setOrientation(int or) { orientation = or; }
    public int getOrientation() { return orientation; }

    private float convertToDegree(String stringDMS){
        float result;
        if(stringDMS == null) {
            result = -1;
            return result;
        }
        String[] DMS = stringDMS.split(",", 3);

        String[] stringD = DMS[0].split("/", 2);
        float D0 = Float.parseFloat(stringD[0]);
        float D1 = Float.parseFloat(stringD[1]);
        float FloatD = D0/D1;

        String[] stringM = DMS[1].split("/", 2);
        float M0 = Float.parseFloat(stringM[0]);
        float M1 = Float.parseFloat(stringM[1]);
        float FloatM = M0/M1;

        String[] stringS = DMS[2].split("/", 2);
        float S0 = Float.parseFloat(stringS[0]);
        float S1 = Float.parseFloat(stringS[1]);
        float FloatS = S0/S1;

        result = FloatD + (FloatM/60) + (FloatS/3600);

        return result;
    };

    private long convertToTime(String stringDMS){
        long result = 0;
        String strRes = "";
        for(int i=0; i<stringDMS.length(); i++){
            if(stringDMS.charAt(i) != ':' && stringDMS.charAt(i) != ' ')
                strRes += stringDMS.charAt(i);
        }
        result = Long.parseLong(strRes);

        return result;
    };

    public Bitmap getRotatedBitmap(Context con, int width, int height){
        return rotateBitmap(decodeSampledBitmapFromUri(con, uri, width, height), orientation);
    }

    public Bitmap decodeSampledBitmapFromUri(Context context, Uri imageUri, int reqWidth, int reqHeight) {
        Bitmap bitmap = null;
        try {
            // Get input stream of the image
            final BitmapFactory.Options options = new BitmapFactory.Options();
            InputStream iStream = context.getContentResolver().openInputStream(imageUri);

            // First decode with inJustDecodeBounds=true to check dimensions
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(iStream, null, options);
            if (iStream != null) {
                iStream.close();
            }
            iStream = context.getContentResolver().openInputStream(imageUri);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeStream(iStream, null, options);
            if (iStream != null) {
                iStream.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public String getRealPathFromURI(Activity context) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = context.managedQuery(uri, projection, null,
                null, null);
        if (cursor == null)
            return null;
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        if (cursor.moveToFirst()) {
            String s = cursor.getString(column_index);
            // cursor.close();
            return s;
        }
        // cursor.close();
        return null;
    }
}
