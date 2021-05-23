package com.example.quitter;

import android.provider.BaseColumns;

public class TablesInfo {

    public static final class User implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_AMOUNT = "amount";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_PRICE = "price";
    }
}
