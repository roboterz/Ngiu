package com.example.ngiu.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.ngiu.data.entities.*

private const val db = "ngiu"

@Database(
    entities = [
        Account::class, AccountType::class, Currency::class, Person::class,
        MainCategories::class, Merchant::class, Period::class, Project::class,
        SubCategories::class, TransactionType::class,
        Trans::class, ], version = 2, exportSchema = false)
@TypeConverters(DateTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun Account(): AccountDao
    abstract fun Currency(): CurrencyDao
    abstract fun Person(): PersonDao
    abstract fun MainCategories(): MainCategoriesDao
    abstract fun MerchantPeriod(): MerchantPeriodDao
    abstract fun PeriodTrans(): PeriodTransDao
    abstract fun ProjectPeriod(): ProjectPeriodDao
    abstract fun SubCatPeriod(): SubCatPeriodDao
    abstract fun Trans(): TransDao
    abstract fun TransTypeTrans(): TransTypeTransDao


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
                        "ngiu"
                    ).build()
                    INSTANCE = instance
                    return instance
                }
            }
        }
    }

