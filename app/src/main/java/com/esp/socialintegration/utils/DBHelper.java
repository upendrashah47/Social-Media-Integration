package com.esp.socialintegration.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by admin on 11/7/16.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Zebra.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void upgrade(int level) {
        System.out.println("============uugrade==before========" + level);
        switch (level) {
            case 0:
                System.out.println("============uugrade=======after=====");
                doUpdate1();
                break;
            case 1:


            case 2:

                // doUpdate3();
                break;
            case 3:
                // doUpdate4();
        }
    }

    private void doUpdate1() {


        System.out.println("============doUpdate1=============");
        this.execute("CREATE TABLE IF NOT EXISTS catagory(id INTEGER,Name TEXT)");
        this.execute("CREATE TABLE IF NOT EXISTS countries(id INTEGER PRIMARY KEY AUTOINCREMENT,Name TEXT)");
        this.execute("INSERT INTO `countries` (`id`, `name`) VALUES\n" +
                "(1, 'Afghanistan'),\n" +
                "(2, 'Albania'),\n" +
                "(3, 'Algeria'),\n" +
                "(4, 'American Samoa'),\n" +
                "(5, 'Andorra'),\n" +
                "(6, 'Angola'),\n" +
                "(7, 'Anguilla'),\n" +
                "(8, 'Antarctica'),\n" +
                "(9, 'Antigua and Barbuda'),\n" +
                "(10, 'Argentina'),\n" +
                "(11, 'Armenia'),\n" +
                "(12, 'Aruba'),\n" +
                "(13, 'Australia'),\n" +
                "(14, 'Austria'),\n" +
                "(15, 'Azerbaijan'),\n" +
                "(16, 'Bahamas'),\n" +
                "(17, 'Bahrain'),\n" +
                "(18, 'Bangladesh'),\n" +
                "(19, 'Barbados'),\n" +
                "(20, 'Belarus'),\n" +
                "(21, 'Belgium'),\n" +
                "(22, 'Belize'),\n" +
                "(23, 'Benin'),\n" +
                "(24, 'Bermuda'),\n" +
                "(25, 'Bhutan'),\n" +
                "(26, 'Bolivia'),\n" +
                "(27, 'Bosnia and Herzegovina'),\n" +
                "(28, 'Botswana'),\n" +
                "(29, 'Bouvet Island'),\n" +
                "(30, 'Brazil'),\n" +
                "(31, 'British Indian Ocean Territory'),\n" +
                "(32, 'British Virgin Islands'),\n" +
                "(33, 'Brunei'),\n" +
                "(34, 'Bulgaria'),\n" +
                "(35, 'Burkina Faso'),\n" +
                "(36, 'Burundi'),\n" +
                "(37, 'Cambodia'),\n" +
                "(38, 'Cameroon'),\n" +
                "(39, 'Canada'),\n" +
                "(40, 'Cape Verde'),\n" +
                "(41, 'Cayman Islands'),\n" +
                "(42, 'Central African Republic'),\n" +
                "(43, 'Chad'),\n" +
                "(44, 'Chile'),\n" +
                "(45, 'China'),\n" +
                "(46, 'Christmas Island'),\n" +
                "(47, 'Cocos Islands'),\n" +
                "(48, 'Colombia'),\n" +
                "(49, 'Comoros'),\n" +
                "(50, 'Cook Islands');\n");

        this.execute("INSERT INTO `countries` (`id`, `name`) VALUES\n" +
                "(51, 'Costa Rica'),\n" +
                "(52, 'Croatia'),\n" +
                "(53, 'Cuba'),\n" +
                "(54, 'Cyprus'),\n" +
                "(55, 'Czech Republic'),\n" +
                "(56, 'Democratic Republic of the Congo'),\n" +
                "(57, 'Denmark'),\n" +
                "(58, 'Djibouti'),\n" +
                "(59, 'Dominica'),\n" +
                "(60, 'Dominican Republic'),\n" +
                "(61, 'East Timor'),\n" +
                "(62, 'Ecuador'),\n" +
                "(63, 'Egypt'),\n" +
                "(64, 'El Salvador'),\n" +
                "(65, 'Equatorial Guinea'),\n" +
                "(66, 'Eritrea'),\n" +
                "(67, 'Estonia'),\n" +
                "(68, 'Ethiopia'),\n" +
                "(69, 'Falkland Islands'),\n" +
                "(70, 'Faroe Islands'),\n" +
                "(71, 'Fiji'),\n" +
                "(72, 'Finland'),\n" +
                "(73, 'France'),\n" +
                "(74, 'French Guiana'),\n" +
                "(75, 'French Polynesia'),\n" +
                "(76, 'French Southern Territories'),\n" +
                "(77, 'Gabon'),\n" +
                "(78, 'Gambia'),\n" +
                "(79, 'Georgia'),\n" +
                "(80, 'Germany'),\n" +
                "(81, 'Ghana'),\n" +
                "(82, 'Gibraltar'),\n" +
                "(83, 'Greece'),\n" +
                "(84, 'Greenland'),\n" +
                "(85, 'Grenada'),\n" +
                "(86, 'Guadeloupe'),\n" +
                "(87, 'Guam'),\n" +
                "(88, 'Guatemala'),\n" +
                "(89, 'Guinea'),\n" +
                "(90, 'Guinea-Bissau'),\n" +
                "(91, 'Guyana'),\n" +
                "(92, 'Haiti'),\n" +
                "(93, 'Heard Island and McDonald Islands'),\n" +
                "(94, 'Honduras'),\n" +
                "(95, 'Hong Kong'),\n" +
                "(96, 'Hungary'),\n" +
                "(97, 'Iceland'),\n" +
                "(98, 'India'),\n" +
                "(99, 'Indonesia'),\n" +
                "(100, 'Iran');\n");

        this.execute("INSERT INTO `countries` (`id`, `name`) VALUES\n" +
                "(101, 'Iraq'),\n" +
                "(102, 'Ireland'),\n" +
                "(103, 'Israel'),\n" +
                "(104, 'Italy'),\n" +
                "(105, 'Ivory Coast'),\n" +
                "(106, 'Jamaica'),\n" +
                "(107, 'Japan'),\n" +
                "(108, 'Jordan'),\n" +
                "(109, 'Kazakhstan'),\n" +
                "(110, 'Kenya'),\n" +
                "(111, 'Kiribati'),\n" +
                "(112, 'Kuwait'),\n" +
                "(113, 'Kyrgyzstan'),\n" +
                "(114, 'Laos'),\n" +
                "(115, 'Latvia'),\n" +
                "(116, 'Lebanon'),\n" +
                "(117, 'Lesotho'),\n" +
                "(118, 'Liberia'),\n" +
                "(119, 'Libya'),\n" +
                "(120, 'Liechtenstein'),\n" +
                "(121, 'Lithuania'),\n" +
                "(122, 'Luxembourg'),\n" +
                "(123, 'Macao'),\n" +
                "(124, 'Macedonia'),\n" +
                "(125, 'Madagascar'),\n" +
                "(126, 'Malawi'),\n" +
                "(127, 'Malaysia'),\n" +
                "(128, 'Maldives'),\n" +
                "(129, 'Mali'),\n" +
                "(130, 'Malta'),\n" +
                "(131, 'Marshall Islands'),\n" +
                "(132, 'Martinique'),\n" +
                "(133, 'Mauritania'),\n" +
                "(134, 'Mauritius'),\n" +
                "(135, 'Mayotte'),\n" +
                "(136, 'Mexico'),\n" +
                "(137, 'Micronesia'),\n" +
                "(138, 'Moldova'),\n" +
                "(139, 'Monaco'),\n" +
                "(140, 'Mongolia'),\n" +
                "(141, 'Montserrat'),\n" +
                "(142, 'Morocco'),\n" +
                "(143, 'Mozambique'),\n" +
                "(144, 'Myanmar'),\n" +
                "(145, 'Namibia'),\n" +
                "(146, 'Nauru'),\n" +
                "(147, 'Nepal'),\n" +
                "(148, 'Netherlands'),\n" +
                "(149, 'Netherlands Antilles'),\n" +
                "(150, 'New Caledonia');\n");

        this.execute("INSERT INTO `countries` (`id`, `name`) VALUES\n" +
                "(151, 'New Zealand'),\n" +
                "(152, 'Nicaragua'),\n" +
                "(153, 'Niger'),\n" +
                "(154, 'Nigeria'),\n" +
                "(155, 'Niue'),\n" +
                "(156, 'Norfolk Island'),\n" +
                "(157, 'North Korea'),\n" +
                "(158, 'Northern Mariana Islands'),\n" +
                "(159, 'Norway'),\n" +
                "(160, 'Oman'),\n" +
                "(161, 'Pakistan'),\n" +
                "(162, 'Palau'),\n" +
                "(163, 'Palestinian Territory'),\n" +
                "(164, 'Panama'),\n" +
                "(165, 'Papua New Guinea'),\n" +
                "(166, 'Paraguay'),\n" +
                "(167, 'Peru'),\n" +
                "(168, 'Philippines'),\n" +
                "(169, 'Pitcairn'),\n" +
                "(170, 'Poland'),\n" +
                "(171, 'Portugal'),\n" +
                "(172, 'Puerto Rico'),\n" +
                "(173, 'Qatar'),\n" +
                "(174, 'Republic of the Congo'),\n" +
                "(175, 'Reunion'),\n" +
                "(176, 'Romania'),\n" +
                "(177, 'Russia'),\n" +
                "(178, 'Rwanda'),\n" +
                "(179, 'Saint Helena'),\n" +
                "(180, 'Saint Kitts and Nevis'),\n" +
                "(181, 'Saint Lucia'),\n" +
                "(182, 'Saint Pierre and Miquelon'),\n" +
                "(183, 'Saint Vincent and the Grenadines'),\n" +
                "(184, 'Samoa'),\n" +
                "(185, 'San Marino'),\n" +
                "(186, 'Sao Tome and Principe'),\n" +
                "(187, 'Saudi Arabia'),\n" +
                "(188, 'Senegal'),\n" +
                "(189, 'Serbia and Montenegro'),\n" +
                "(190, 'Seychelles'),\n" +
                "(191, 'Sierra Leone'),\n" +
                "(192, 'Singapore'),\n" +
                "(193, 'Slovakia'),\n" +
                "(194, 'Slovenia'),\n" +
                "(195, 'Solomon Islands'),\n" +
                "(196, 'Somalia'),\n" +
                "(197, 'South Africa'),\n" +
                "(198, 'South Georgia and the South Sandwich Islands'),\n" +
                "(199, 'South Korea'),\n" +
                "(200, 'Spain');");

        this.execute("INSERT INTO `countries` (`id`, `name`) VALUES\n" +
                "(201, 'Sri Lanka'),\n" +
                "(202, 'Sudan'),\n" +
                "(203, 'Suriname'),\n" +
                "(204, 'Svalbard and Jan Mayen'),\n" +
                "(205, 'Swaziland'),\n" +
                "(206, 'Sweden'),\n" +
                "(207, 'Switzerland'),\n" +
                "(208, 'Syria'),\n" +
                "(209, 'Taiwan'),\n" +
                "(210, 'Tajikistan'),\n" +
                "(211, 'Tanzania'),\n" +
                "(212, 'Thailand'),\n" +
                "(213, 'Togo'),\n" +
                "(214, 'Tokelau'),\n" +
                "(215, 'Tonga'),\n" +
                "(216, 'Trinidad and Tobago'),\n" +
                "(217, 'Tunisia'),\n" +
                "(218, 'Turkey'),\n" +
                "(219, 'Turkmenistan'),\n" +
                "(220, 'Turks and Caicos Islands'),\n" +
                "(221, 'Tuvalu'),\n" +
                "(222, 'U.S. Virgin Islands'),\n" +
                "(223, 'Uganda'),\n" +
                "(224, 'Ukraine'),\n" +
                "(225, 'United Arab Emirates'),\n" +
                "(226, 'United Kingdom'),\n" +
                "(227, 'United States'),\n" +
                "(228, 'United States Minor Outlying Islands'),\n" +
                "(229, 'Uruguay'),\n" +
                "(230, 'Uzbekistan'),\n" +
                "(231, 'Vanuatu'),\n" +
                "(232, 'Vatican'),\n" +
                "(233, 'Venezuela'),\n" +
                "(234, 'Vietnam'),\n" +
                "(235, 'Wallis and Futuna'),\n" +
                "(236, 'Western Sahara'),\n" +
                "(237, 'Yemen'),\n" +
                "(238, 'Zambia'),\n" +
                "(239, 'Zimbabwe'),\n" +
                "(240, 'Aland Islands'),\n" +
                "(241, 'Bonaire Sint Eustatius and Saba'),\n" +
                "(242, 'Curacao'),\n" +
                "(243, 'Guernsey'),\n" +
                "(244, 'Isle of Man'),\n" +
                "(245, 'Jersey'),\n" +
                "(246, 'Montenegro'),\n" +
                "(247, 'Saint Barthelemy'),\n" +
                "(248, 'Saint Martin [French part]'),\n" +
                "(249, 'Serbia'),\n" +
                "(250, 'Sint Maarten [Dutch part]');\n");

        this.execute("INSERT INTO `countries` (`id`, `name`) VALUES (251, 'South Sudan');");



    }

    private void doUpdate2() {
        System.out.println("=========doUpdate2=========");

    }


    public Cursor execute(String statment) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL(statment);
            System.out.println("===========successful execution============");
        } catch (Exception e) {
//			Log.error(this.getClass() + " :: execute() ::", e);
            System.out.println("Error in execution" + e.toString());

        } finally {
            db.close();
            db = null;
        }
        return null;
    }

    public Cursor query(String statment) {
        Cursor cur = null;


        SQLiteDatabase db = this.getReadableDatabase();
        try {
            cur = db.rawQuery(statment, null);
            cur.moveToPosition(-1);
        } catch (Exception e) {
            System.out.println("=========exception====" + e.toString());

        } finally {

            db.close();
            db = null;
        }
        return cur;
    }

    public static String getDBStr(String str) {

        str = str != null ? str.replaceAll("'", "''") : null;
        str = str != null ? str.replaceAll("'", "''") : null;
        str = str != null ? str.replaceAll("&amp;", "&") : null;

        return str;

    }

}