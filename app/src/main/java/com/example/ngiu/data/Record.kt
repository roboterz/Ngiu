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



class Record(
    val id: Long?=null,
    var name: String?=null,
    var type: String?=null,
    var category: String?=null,
    var payer: String?=null,
    var recipient: String?=null,
    var amount: Double?=0.00,
    var date: LocalDateTime?= LocalDateTime.now(),
    var person: String?=null,
    var merchant: String?=null,
    var project: String?=null,
    var reimburse: Int?=0,
    var period: String?=null,
    var memo: String?="",
) {


    init {
        if (id==null) {
            //load data
        }else{
            //query with id=xxx
        }
        this.name="xxx7"
    }



}