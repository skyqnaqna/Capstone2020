package com.cs2020.capstone;

public class DBActivity {

    public static final String COL_NAME = "name"; //제품명
    public static final String COL_CATE = "category"; //카테고리
    public static final String COL_LYEAR = "life_year"; //유통기한 년
    public static final String COL_LMONTH = "life_month" ;//유통기한 월
    public static final String COL_LDAY = "life_day" ;//유통기한 일
    public static final String COL_AYEAR = "alarm_year "; //알람 년
    public static final String COL_AMONTH = "alarm_month"; //알람 월
    public static final String COL_ADAY = "alarm_day"; //알람 일
    public static final String COL_COM = "company"; //제조사
    public static final String COL_MEMO = "memo"; //메모
    public static final String COL_IMAGE = "image"; //이미지 경로
    public static final String _TABLENAME = "user_product"; //테이블 명

    public static final String _CREATE0 = "create table if not exists "+_TABLENAME+"("
            +"_ID integer primary key autoincrement, "
            +COL_NAME+" text , "
            +COL_CATE+" text , "
            +COL_LYEAR+" integer , "
            +COL_LMONTH+" integer , "
            +COL_LDAY+" integer , "
            +COL_AYEAR+" integer , "
            +COL_AMONTH+" integer , "
            +COL_ADAY+" integer , "
            +COL_COM+" text , "
            +COL_MEMO+" text , "
            +COL_IMAGE+" text );";

}
