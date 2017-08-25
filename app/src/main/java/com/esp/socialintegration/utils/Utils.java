package com.esp.socialintegration.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings.Secure;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class Utils {
    /**
     * Generate Device UDID.
     */

    public static void systemUpgrade(Context context) {
        DBHelper dbHelper = null;
        int level = 0;
        try {
            dbHelper = new DBHelper(context);
            level = Integer.parseInt(Pref.getValue(context, "LEVEL", "0"));
            System.out.println("=======Leval value=============" + level);
            if (level == 0) {
                System.out.println("=======Leval get=============");
                dbHelper.upgrade(level);
                // Create not confirmed order
                level++;
            } else if (level == 1) {
                dbHelper.upgrade(level);
                // Create not confirmed order
                level++;

            }
            Pref.setValue(context, "LEVEL", level + "");

        } catch (Exception e) {

        } finally {
            if (dbHelper != null)
                dbHelper.close();
            dbHelper = null;
            level = 0;
        }

    }

    public static String getDeviceID(Context context) {
        String udid = Secure.getString(context.getContentResolver(),
                Secure.ANDROID_ID);
        Pref.setValue(context, Config.PREF_UDID, udid);
        return udid;
    }

    /**
     * Check Connectivity of network.
     */
    public static boolean isOnline(Context context) {
        try {
            if (context == null)
                return false;

            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            if (cm != null) {
                if (cm.getActiveNetworkInfo() != null) {
                    return cm.getActiveNetworkInfo().isConnected();
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.error("Exception", String.valueOf(e));
            return false;
        }
    }

//
//    public static RestAdapter getAdapter() {
//        OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
//        httpBuilder.connectTimeout(120, TimeUnit.SECONDS);
//        OkHttpClient client = httpBuilder.build();
//        return new RestAdapter.Builder().setClient(new Ok3Client(client)).setEndpoint(Config.HOST_URL).build();
//    }

    public static Typeface getFont(Context context, int tag) {
        if (tag == 100) {
            return Typeface.createFromAsset(context.getAssets(),
                    "Roboto_Regular.ttf");
        } else if (tag == 200) {
            return Typeface.createFromAsset(context.getAssets(),
                    "Roboto_Bold.ttf");
        } else if (tag == 300) {
            return Typeface.createFromAsset(context.getAssets(),
                    "Roboto-Medium.ttf");
        } else if (tag == 400) {
            return Typeface.createFromAsset(context.getAssets(),
                    "Roboto-Light.ttf");
        } else if (tag == 500) {
            return Typeface.createFromAsset(context.getAssets(),
                    "olivier_demo.ttf");
        }
        return Typeface.DEFAULT;
    }

    @SuppressLint("SimpleDateFormat")
    public static Date convertStringToDate(String strDate, String parseFormat) {
        try {
            return new SimpleDateFormat(parseFormat).parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String convertDateToString(Date strDate, String parseFormat) {
        try {
            return new SimpleDateFormat(parseFormat).format(strDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Convert Current String to String formate with apply new date formate
     * Function
     */
    public static String convertDateStringToString(String strDate,
                                                   String currentFormat, String parseFormat) {
        try {
            return convertDateToString(
                    convertStringToDate(strDate, currentFormat), parseFormat);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public final static Pattern EMAIL_ADDRESS_PATTERN = Pattern
            .compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$");

    public final static Pattern Email_address_pattern = Pattern.
            compile("^[-a-zA-Z0-9~!$%^&*_=+}{\\'?]+" +
                    "(\\.[-a-zA-Z0-9~!$%^&*_=+}{\\'?]" +
                    "+)*@([a-zA-Z0-9_][-a-z0-9_]*(\\.[-a-zA-Z0-9_]" +
                    "+)*\\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|" +
                    "museum|name|net|org|pro|travel|mobi|[a-zA-Z][a-zA-Z])" +
                    "|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))" +
                    "(:[0-9]{1,5})?$");


    public static void freeMemory() {
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

    public static void verifyDataPath() throws IOException {

        File dir = new File(Config.DIR_USERDATA);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        dir = null;
    }

    public static void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    public static void ConvertImage(Bitmap bitmap, String name) {
        try {
            Storage.verifyDataPath();
            File imageFile = new File(Config.DIR_USERDATA, name);
            OutputStream os;
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
        }
    }

    public static Bitmap rotateBitmap(Bitmap b, int degrees) {

        Matrix m = new Matrix();
        if (degrees != 0) {
            // clockwise
            m.postRotate(degrees, (float) b.getWidth() / 2,
                    (float) b.getHeight() / 2);
        }
        try {
            Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
                    b.getHeight(), m, true);
            if (b != b2) {
                b.recycle();
                b = b2;
            }
        } catch (OutOfMemoryError ex) {
            // We have no memory to rotate. Return the original bitmap.
        }
        return b;
    }

    public static String compressImage(String imageUri, Context context, int IsPic) {
        String filePath = getRealPathFromURI(imageUri, context);
        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        // by setting this field as true, the actual bitmap pixels are not
        // loaded in the memory. Just the bounds are loaded. If
        // you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        // max Height and width values of the compressed image is taken as
        // 816x612


        float maxWidth = 0;
        float maxHeight = 0;
        if (IsPic == 1) {
            maxWidth = 300.0f;
            maxHeight = 300.0f;
        } else if (IsPic == 2) {
            maxWidth = 760.0f;
            maxHeight = 400.0f;
        }

        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        // width and height values are set maintaining the aspect ratio of the
        // image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }

        // setting inSampleSize value allows to load a scaled down version of
        // the original image
        options.inSampleSize = calculateInSampleSize(options, actualWidth,
                actualHeight);

        // inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

        // this options allow android to claim the bitmap memory if it runs low
        // on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            // load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,
                    Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2,
                middleY - bmp.getHeight() / 2, new Paint(
                        Paint.FILTER_BITMAP_FLAG));

        // check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = new File(imageUri).getPath();
        try {
            Storage.verifyCategoryPath(Config.DIR_USERDATA);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        try {
            out = new FileOutputStream(filename);
            // write the compressed bitmap at the destination specified by
            // filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return filename;
    }

    public static int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath) {
        int rotate = 0;
        try {
            context.getContentResolver().notifyChange(imageUri, null);
            File imageFile = new File(imagePath);

            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    private static String getRealPathFromURI(String contentURI, Context context) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = context.getContentResolver().query(contentUri, null,
                null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor
                    .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }
        return inSampleSize;
    }

    public static boolean isGotoCrop(Context context, Uri imgDestination, int minWidth, int minHeight) {
        boolean isGoto = false;
        try {
            Bitmap bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imgDestination);
            if (bm.getWidth() > minWidth && bm.getHeight() > minHeight) {
                isGoto = true;
            } else {
                Toast.makeText(context, "Minimum image dimension must be " + minWidth + " X " + minHeight, Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isGoto;
    }

    //created by Upen
    //For Hiding keyboard
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    //created by Upen
    //For show keyboard
    public static void showKeyboard(Activity activity) {

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    //created by Upen
    //For show Error Dialog
//    public static void errorOnDialog(Activity activity, String mesg, String heading) {
//        TextView txt_netdialog, btn_netdialog, txt_netheaddialog;
//        final Dialog dialog = new Dialog(activity);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.setContentView(R.layout.errordialog);
//        dialog.setCancelable(false);
//
//        txt_netheaddialog = (TextView) dialog.findViewById(R.id.txtHeader);
//        txt_netdialog = (TextView) dialog.findViewById(R.id.txt_netdialog);
//        btn_netdialog = (TextView) dialog.findViewById(R.id.btn_netdialog);
//        txt_netdialog.setText(mesg);
//        txt_netheaddialog.setText(heading);
//        btn_netdialog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
//    }

    public static int getToolbarHeight(Context context) {
        return 0;
    }

    public static String streamToString(InputStream is) throws IOException {
        String str = "";

        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is));

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                reader.close();
            } finally {
                is.close();
            }

            str = sb.toString();
        }

        return str;
    }
}