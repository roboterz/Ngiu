package com.example.ngiu

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

import java.util.ArrayList
const val DATABASE_VERSION = 1
const val DATABASE_NAME = "Ngiu"
class MyHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_ACCOUNT)
        db.execSQL(CREATE_ACCOUNT_TYPE)
        db.execSQL(CREATE_MAIN_CATEGORIES)
        db.execSQL(CREATE_MERCHANT)
        db.execSQL(CREATE_PERIOD)
        db.execSQL(CREATE_PERSON)

    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
    companion object {
        private val CREATE_ACCOUNT =
            "CREATE TABLE " + AcctContract.UserEntry.TABLE_NAME + " (" +
                    AcctContract.UserEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    AcctContract.UserEntry.COLUMN_TypeID + " INTEGER," +
                    AcctContract.UserEntry.COLUMN_Name + " TEXT)"

        private val CREATE_ACCOUNT_TYPE =
            "CREATE TABLE " + AcctType.UserEntry.TABLE_NAME + " (" +
                    AcctType.UserEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    AcctType.UserEntry.COLUMN_Name + " TEXT)"

        private val CREATE_MAIN_CATEGORIES =
            "CREATE TABLE " + MainCategory.UserEntry.TABLE_NAME + " (" +
                    MainCategory.UserEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    MainCategory.UserEntry.COLUMN_TypeID + " INTEGER," +
                    MainCategory.UserEntry.COLUMN_Name + " TEXT)"

        private val CREATE_MERCHANT =
            "CREATE TABLE " + Merchant.UserEntry.TABLE_NAME + " (" +
                    Merchant.UserEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Merchant.UserEntry.COLUMN_Name + " TEXT)"

        private val CREATE_PERIOD =
            "CREATE TABLE " + Period.UserEntry.TABLE_NAME + " (" +
                    Period.UserEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Period.UserEntry.COLUMN_TypeID  + " INTEGER," +
                    Period.UserEntry.COLUMN_TransactionID + " INTEGER," +
                    Period.UserEntry.COLUMN_RepeatInterval + " INTEGER," +
                    Period.UserEntry.COLUMN_Status + " INTEGER," +
                    Period.UserEntry.COLUMN_EndDate + " INTEGER)"

        private val CREATE_PERSON =
            "CREATE TABLE " + Person.UserEntry.TABLE_NAME + " (" +
                    Person.UserEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Person.UserEntry.COLUMN_Name + " TEXT)"





        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + AcctContract.UserEntry.TABLE_NAME
    }
}

