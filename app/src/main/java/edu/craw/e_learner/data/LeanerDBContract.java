package edu.craw.e_learner.data;

import android.provider.BaseColumns;

/**
 * Created by farid on 3/22/2016.
 */
public final class LeanerDBContract {
    public LeanerDBContract() {
    }// To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.


    /* Inner class that defines the table contents */
    public static abstract class HistoryEntry implements BaseColumns {
        public static final String TABLE_NAME = "history";
        public static final String COLUMN_NAME_LOG = "log";
        public static final String COLUMN_NAME_CREATEDAT = "created_at";
        public static final String COLUMN_NAME_UPDATEDAT = "updated_at";

    }
}
