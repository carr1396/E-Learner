package edu.craw.e_learner.data;

import android.provider.BaseColumns;

/**
 * Created by farid on 3/22/2016.
 */
public final class LeanerDBContracts {
    public LeanerDBContracts() {
    }// To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.


    /* Inner class that defines the table contents */
    public static abstract class HistoryEntry implements BaseColumns {
        public static final String TABLE_NAME = "history";
        public static final String COLUMN_NAME_LOG = "log";
        public static final String COLUMN_NAME_CREATEDAT = "created_at";
        public static final String COLUMN_NAME_UPDATEDAT = "updated_at";

    }

    public static abstract class CourseEntry implements BaseColumns {
        public static final String TABLE_NAME = "courses";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_CODE = "code";
        public static final String COLUMN_NAME_NETWORK_ID = "network_id";
        public static final String COLUMN_NAME_SCHOOLID = "school_id";
        public static final String COLUMN_NAME_SCHOOLNAME = "school_name";
        public static final String COLUMN_NAME_ADDEDID = "added_id";
        public static final String COLUMN_NAME_ADDEDUSERNAME = "added_username";
        public static final String COLUMN_NAME_ABBREVIATION = "abbrev";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_CREATEDAT = "created_at";
        public static final String COLUMN_NAME_UPDATEDAT = "updated_at";
        public static final String COLUMN_NAME_ACTIVE="active";
    }
}
