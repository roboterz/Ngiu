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
        Transaction::class, ], version = 1, exportSchema = false)
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
    abstract fun Transaction(): TransactionDao
    abstract fun TransTypeTrans(): TransTypeTransDao

    


    companion object {

        // For Singleton instantiation
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance
                    ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, db)
                .build()
        }


    }
}
