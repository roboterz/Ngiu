package com.example.ngiu.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import android.database.sqlite.*
import java.time.LocalDateTime

private const val SQL_CREATE_ENTRIES =
    """
    CREATE TABLE AccountType (
    ID   INTEGER PRIMARY KEY AUTOINCREMENT
    UNIQUE ON CONFLICT ROLLBACK,
    Name VARCHAR
    );
    CREATE TABLE Currency (
    Code         CHAR (3) UNIQUE ON CONFLICT ROLLBACK
    PRIMARY KEY ASC ON CONFLICT ROLLBACK,
    Name         VARCHAR,
    ExchangeRate REAL     DEFAULT (1)
    );
    CREATE TABLE Person (
    ID   INTEGER PRIMARY KEY AUTOINCREMENT
    UNIQUE ON CONFLICT ROLLBACK,
    Name VARCHAR
    );
    CREATE TABLE Merchant (
    ID   INTEGER PRIMARY KEY AUTOINCREMENT
    UNIQUE ON CONFLICT ROLLBACK,
    Name VARCHAR
    );
    CREATE TABLE Project (
    ID   INTEGER PRIMARY KEY AUTOINCREMENT
    UNIQUE ON CONFLICT ROLLBACK,
    Name VARCHAR
    );
    CREATE TABLE TransactionType (
    ID   INTEGER PRIMARY KEY AUTOINCREMENT
    UNIQUE ON CONFLICT ROLLBACK,
    Name VARCHAR
    );
    CREATE TABLE MainCategories (
    ID     INTEGER PRIMARY KEY AUTOINCREMENT
    UNIQUE ON CONFLICT ROLLBACK,
    TypeID BIGINT  REFERENCES TransactionType (ID) ON DELETE SET NULL
    ON UPDATE CASCADE
    MATCH SIMPLE,
    Name   VARCHAR
    );
    CREATE TABLE SubCategories (
    ID             INTEGER PRIMARY KEY AUTOINCREMENT
    UNIQUE ON CONFLICT ROLLBACK,
    MainCategoryID BIGINT  REFERENCES MainCategories (ID) ON DELETE SET NULL
    ON UPDATE CASCADE
    MATCH SIMPLE
    COLLATE BINARY,
    Name           VARCHAR
    );
    CREATE TABLE Account (
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
    CREATE TABLE Period (
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
    CREATE TABLE [Transaction] (
    ID          INTEGER  PRIMARY KEY AUTOINCREMENT
    UNIQUE ON CONFLICT ROLLBACK,
    TypeD       BIGINT   REFERENCES TransactionType (ID) ON DELETE SET NULL
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

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
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
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Ngiu.db"
    }
}

data class Record(
    val id: Long,
    val name: String,
    val type: String,
    val category: String,
    val payer: String,
    val recipient: String,
    val amount: Double,
    val date: LocalDateTime,
    val person: String,
    val merchant: String,
    val memo: String,
    val project: String

    // join all tables and load the
)