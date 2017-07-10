package com.esp.socialintegration.utils;

import android.annotation.SuppressLint;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SdCardPath")
public class Storage {

    public static void verifyLogPath() throws IOException {
        File dir = new File(Config.DIR_LOG);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        dir = null;
    }

    /*public static void verifyMagicPath() throws IOException {
        File dir = new File(Config.DIR_MAGIC);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        dir = null;
    }
*/
    public static File verifyLogFile() throws IOException {
        File logFile = new File(Config.DIR_LOG + "/Inkskill_Log_"
                + new SimpleDateFormat("yyyy_MM_dd").format(new Date())
                + ".html");
        FileOutputStream fos = null;

        Storage.verifyLogPath();

        if (!logFile.exists()) {
            logFile.createNewFile();

            fos = new FileOutputStream(logFile);

            String str = "<TABLE style=\"width:100%;border=1px\" cellpadding=2 cellspacing=2 border=1><TR>"
                    + "<TD style=\"width:30%\"><B>Date n Time</B></TD>"
                    + "<TD style=\"width:20%\"><B>Title</B></TD>"
                    + "<TD style=\"width:50%\"><B>Description</B></TD></TR>";

            fos.write(str.getBytes());
        }

        if (fos != null) {
            fos.close();
        }

        fos = null;

        return logFile;
    }

    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

    public static void clearLog() {
        String files[] = null;
        File file = null;
        try {
            files = new File(Config.DIR_LOG).list();

            for (int ele = 0; ele < files.length; ele++) {
                file = new File(Config.DIR_LOG, files[ele]);

                file.delete();
            }

        } catch (Exception e) {
            Log.error(Storage.class + " :: clearLog :: ", String.valueOf(e));
        }

        files = null;
        files = null;
    }

    public static void copy_DB(String src, String dest) {
        FileInputStream fis = null;
        FileOutputStream out = null;
        int ch;

        try {
            fis = new FileInputStream(src);
            out = new FileOutputStream(dest);

            while ((ch = fis.read()) != -1) {
                out.write(ch);
            }

            fis.close();
            out.close();
        } catch (IOException e) {
            Log.error("Storage::copy()", String.valueOf(e));
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
            }

            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
            }
        }

        fis = null;
        out = null;
    }

    public static void verifyDataPath() throws IOException {
        File dir = new File(Config.DIR_USERDATA);

        if (!dir.exists()) {
            dir.mkdirs();
        }


        dir = null;
    }

    public static void verifyCategoryPath(String folder) throws IOException {
        File dir = new File(folder);

        if (!dir.exists()) {
            dir.mkdirs();
        }
        dir = null;
    }

    public static void copyDB() {
        try {
            verifyDataPath();
            Storage.copy_DB("/data/data/com.esp.inkskill/databases/"
                    + Config.DB_NAME, Config.DIR_USERDATA + "/" + Config.DB_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copy(String src, String dest) {

        FileInputStream in = null;
        FileOutputStream out = null;
        byte[] buf = null;
        int len;

        try {

            in = new FileInputStream(src);
            out = new FileOutputStream(dest);

            buf = new byte[1024];

            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
            }
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
            }
            in = null;
            out = null;
            buf = null;
            len = 0;
            System.gc();
        }
    }
}
