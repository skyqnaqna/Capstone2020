package com.cs2020.capstone;

public class DBActivity {

    public static final String COL_NAME = "name"; //제품명
    public static final String COL_CATE = "category"; //카테고리
    public static final String COL_LIFE = "shelf life"; //유통기한
    public static final String COL_ALARM = "alarm"; //알람
    public static final String COL_COM = "company"; //제조사
    public static final String COL_MEMO = "memo"; //메모
    public static final String COL_IMAGE = "image"; //이미지 경로
    public static final String _TABLENAME = "usertable";

    public static final String _CREATE0 = "create table if not exists "+_TABLENAME+"("
            +"_ID integer primary key autoincrement, "
            +COL_NAME+" text , "
            +COL_CATE+" text , "
            +COL_LIFE+" text , "
            +COL_ALARM+" text , "
            +COL_COM+" text , "
            +COL_MEMO+" text , "
            +COL_IMAGE+" text );";

}
