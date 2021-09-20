package com.example.ngiu.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.example.ngiu.data.entities.AccountType


private const val DATABASENAME = "Ngiu"
private const val TABLENAME = "Users"
private const val COL_NAME = "name"
private const val COL_AGE = "age"
private const val COL_ID = "id"

private const val SQL_CREATE_ENTRIES =
    """
    CREATE TABLE IF NOT EXISTS AccountType (
    ID   INTEGER PRIMARY KEY AUTOINCREMENT
    UNIQUE ON CONFLICT ROLLBACK,
    Name VARCHAR
    );
    CREATE TABLE IF NOT EXISTS Currency (
    Code         CHAR (3) UNIQUE ON CONFLICT ROLLBACK
    PRIMARY KEY ASC ON CONFLICT ROLLBACK,
    Name         VARCHAR,
    ExchangeRate REAL     DEFAULT (1)
    );
    CREATE TABLE IF NOT EXISTS Person (
    ID   INTEGER PRIMARY KEY AUTOINCREMENT
    UNIQUE ON CONFLICT ROLLBACK,
    Name VARCHAR
    );
    CREATE TABLE IF NOT EXISTS Merchant (
    ID   INTEGER PRIMARY KEY AUTOINCREMENT
    UNIQUE ON CONFLICT ROLLBACK,
    Name VARCHAR
    );
    CREATE TABLE IF NOT EXISTS Project (
    ID   INTEGER PRIMARY KEY AUTOINCREMENT
    UNIQUE ON CONFLICT ROLLBACK,
    Name VARCHAR
    );
    CREATE TABLE IF NOT EXISTS TransactionType (
    ID   INTEGER PRIMARY KEY AUTOINCREMENT
    UNIQUE ON CONFLICT ROLLBACK,
    Name VARCHAR
    );
    CREATE TABLE IF NOT EXISTS MainCategories (
    ID     INTEGER PRIMARY KEY AUTOINCREMENT
    UNIQUE ON CONFLICT ROLLBACK,
    TypeID BIGINT  REFERENCES TransactionType (ID) ON DELETE SET NULL
    ON UPDATE CASCADE
    MATCH SIMPLE,
    Name   VARCHAR
    );
    CREATE TABLE IF NOT EXISTS SubCategories (
    ID             INTEGER PRIMARY KEY AUTOINCREMENT
    UNIQUE ON CONFLICT ROLLBACK,
    MainCategoryID BIGINT  REFERENCES MainCategories (ID) ON DELETE SET NULL
    ON UPDATE CASCADE
    MATCH SIMPLE
    COLLATE BINARY,
    Name           VARCHAR
    );
    CREATE TABLE IF NOT EXISTS Account (
    ID           INTEGER  PRIMARY KEY AUTOINCREMENT
    UNIQUE ON CONFLICT ROLLBACK,
    TypeID       BIGINT   REFERENCES AccountType (ID) ON DELETE SET NULL
    ON UPDATE CASCADE
    MATCH SIMPLE,
    Name         VARCHAR,
    BaseCurrency CHAR (3) REFERENCES Currency (Code) ON DELETE SET DEFAULT
    ON UPDATE CASCADE
    MATCH SIMPLE
    DEFAULT USD
    );
    CREATE TABLE IF NOT EXISTS Period (
    ID             INTEGER  PRIMARY KEY AUTOINCREMENT
    UNIQUE ON CONFLICT ROLLBACK,
    RepeatInterval INTEGER  DEFAULT (0),
    Status         BOOLEAN  DEFAULT (0),
    StartDate      DATETIME,
    EndDate        DATETIME DEFAULT NULL,
    TypeID         BIGINT   REFERENCES TransactionType (ID) ON DELETE SET NULL
    ON UPDATE CASCADE
    MATCH SIMPLE,
    CategoryID     BIGINT   REFERENCES SubCategories (ID) ON DELETE SET NULL
    ON UPDATE CASCADE
    MATCH SIMPLE,
    PayerID        BIGINT   REFERENCES Account (ID) ON DELETE SET NULL
    ON UPDATE CASCADE
    MATCH SIMPLE,
    RecipientID    BIGINT   REFERENCES Account (ID) ON DELETE SET NULL
    ON UPDATE CASCADE
    MATCH SIMPLE,
    Amount         DECIMAL,
    PersonID       INTEGER  REFERENCES Person (ID) ON DELETE SET NULL
    ON UPDATE CASCADE
    MATCH SIMPLE,
    MerchantID     BIGINT   REFERENCES Merchant (ID) ON DELETE SET NULL
    ON UPDATE CASCADE
    MATCH SIMPLE,
    ProjectID      BIGINT   REFERENCES Project (ID) ON DELETE SET NULL
    ON UPDATE CASCADE
    MATCH SIMPLE,
    Reimburse      INT      DEFAULT (0),
    Memo           VARCHAR
    );
    CREATE TABLE IF NOT EXISTS [Transaction] (
        ID          INTEGER  PRIMARY KEY AUTOINCREMENT
                             UNIQUE ON CONFLICT ROLLBACK,
        TypeID      BIGINT   REFERENCES TransactionType (ID) ON DELETE SET NULL
                                                             ON UPDATE CASCADE
                                                             MATCH SIMPLE,
        CategoryID  BIGINT   REFERENCES MainCategories (ID) ON DELETE SET NULL
                                                            ON UPDATE CASCADE
                                                            MATCH SIMPLE,
        PayerID     BIGINT   REFERENCES Account ON DELETE SET NULL
                                                ON UPDATE CASCADE
                                                MATCH SIMPLE,
        RecipientID BIGINT   REFERENCES Account ON DELETE SET NULL
                                                ON UPDATE CASCADE
                                                MATCH SIMPLE,
        Amount      DECIMAL,
        Date        DATETIME COLLATE BINARY,
        PersonID    INTEGER  REFERENCES Individual (ID) ON DELETE SET NULL
                                                        ON UPDATE CASCADE
                                                        MATCH SIMPLE,
        MerchantID  BIGINT   REFERENCES Merchant (ID) ON DELETE SET NULL
                                                      ON UPDATE CASCADE
                                                      MATCH SIMPLE,
        ProjectID   BIGINT   REFERENCES Project (ID) ON DELETE SET NULL
                                                     ON UPDATE CASCADE
                                                     MATCH SIMPLE,
        Reimburse   INT      DEFAULT (0),
        PeriodID    BIGINT   REFERENCES Period (ID) ON DELETE SET NULL
                                                    ON UPDATE CASCADE
                                                    MATCH SIMPLE,
        Memo        VARCHAR
    );
    """

private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "


class DBManager2(context: Context){
    private val db: SQLiteDatabase = context.openOrCreateDatabase("Ngiu",Context.MODE_PRIVATE,null)

    init{
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    fun add(at: AccountType){
        val query = "INSERT INTO AccountType (Name) VALUES ('${at.Name}')"
        db.execSQL(query)
    }

    fun allAccountType(): List<AccountType>{
        val ats = mutableListOf<AccountType>()
        val cursor = db.rawQuery("SELECT * FROM AccountType",null)

        if (cursor.moveToFirst()){
            do{
                val id = cursor.getString(cursor.getColumnIndex("ID"))
                val name = cursor.getString(cursor.getColumnIndex("Name"))
                val at = AccountType(id=0, Name="")
                ats.add(at)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return ats
    }
}







class DBManager(var context: Context) : SQLiteOpenHelper(context, DATABASENAME, null,
    1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = SQL_CREATE_ENTRIES
        db?.execSQL(createTable)
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //onCreate(db);
    }

    fun insertData(at: AccountType) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("Name", at.Name)
        val result = database.insert("AccountType", null, contentValues)
        if (result == (0).toLong()) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        }
    }

    fun readData(): MutableList<AccountType> {
        val list: MutableList<AccountType> = ArrayList()
        val db = this.readableDatabase
        val query = "Select * from AccountType"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                var at = AccountType(id=0,Name="")
                at.id  = result.getString(result.getColumnIndex("ID")).toInt()
                at.Name = result.getString(result.getColumnIndex("Name"))
                list.add(at)
            }
            while (result.moveToNext())
        }
        return list
    }
}