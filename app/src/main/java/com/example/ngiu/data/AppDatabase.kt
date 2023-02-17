package com.example.ngiu.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.ngiu.data.entities.*
import java.io.File

private const val DB_NAME = "ngiu.db"
private const val DB_PATH = "databases/ngiu.db"

@Database(
    entities = [
        Account::class, AccountType::class, Currency::class, Person::class,
        MainCategory::class, Merchant::class, Period::class, Project::class,
        SubCategory::class, TransactionType::class,
        Trans::class, ], version = 5, exportSchema = false)
@TypeConverters(DateTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun account(): AccountDao
    abstract fun accounttype(): AccountTypeDao
    abstract fun currency(): CurrencyDao
    abstract fun person(): PersonDao
    abstract fun mainCategory(): MainCategoryDao
    abstract fun merchant(): MerchantDao
    abstract fun period(): PeriodDao
    abstract fun project(): ProjectDao
    abstract fun subcat(): SubCategoryDao
    abstract fun trans(): TransDao
    abstract fun transtype(): TransTypeDao


    companion object {
            // Singleton prevents multiple instances of database opening at the same time
            @Volatile
            private var INSTANCE: AppDatabase? = null

            fun getDatabase(context: Context): AppDatabase {
                val tempInstance = INSTANCE
                if (tempInstance != null) {
                    return tempInstance
                }
                synchronized(this){
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        DB_NAME
                    ).createFromAsset(DB_PATH).allowMainThreadQueries().build()

                    INSTANCE = instance
                    return instance
                }
            }
        }
    }

