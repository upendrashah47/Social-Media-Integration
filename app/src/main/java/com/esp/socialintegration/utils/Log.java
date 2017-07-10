package com.esp.socialintegration.utils;

public class Log {
    /* Logging and Console */
    public static boolean DO_SOP = true;

    public static void print(String mesg) {
        if (Log.DO_SOP) {
            System.out.println(mesg);
        }
    }

    public static void print(String title, String mesg) {
        if (Log.DO_SOP) {
            System.out.println(title + " :: " + mesg);
        }
    }

    public static void error(String title, String e) {
        if (Log.DO_SOP) {
            System.out.println("=========================" + title + "=========================");
            /*e.printStackTrace();*/
        }
    }

    public static void error(Exception e) {
        if (Log.DO_SOP) {
            e.printStackTrace();
        }
    }
}