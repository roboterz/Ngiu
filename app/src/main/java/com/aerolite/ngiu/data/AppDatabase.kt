package com.aerolite.ngiu.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aerolite.ngiu.data.entities.Account
import com.aerolite.ngiu.data.entities.AccountType
import com.aerolite.ngiu.data.entities.Budget
import com.aerolite.ngiu.data.entities.Category
import com.aerolite.ngiu.data.entities.Currency
import com.aerolite.ngiu.data.entities.DateTypeConverter
import com.aerolite.ngiu.data.entities.Event
import com.aerolite.ngiu.data.entities.Icon
import com.aerolite.ngiu.data.entities.ImageConverter
import com.aerolite.ngiu.data.entities.Merchant
import com.aerolite.ngiu.data.entities.Period
import com.aerolite.ngiu.data.entities.Person
import com.aerolite.ngiu.data.entities.Project
import com.aerolite.ngiu.data.entities.Reward
import com.aerolite.ngiu.data.entities.Template
import com.aerolite.ngiu.data.entities.Trans
import com.aerolite.ngiu.data.entities.TransactionType

private const val DB_NAME = "ngiux.db"
private const val DB_PATH = "databases/ngiux.db"

@Database(
    entities = [
        Account::class, AccountType::class, Category::class, Currency::class,
        Person::class, Merchant::class, Period::class, Icon::class, Template::class,
        Project::class, TransactionType::class, Budget::class, Reward::class,
        Trans::class, Event::class, ], version = 9, exportSchema = false)
@TypeConverters(DateTypeConverter::class, ImageConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun account(): AccountDao
    abstract fun accountType(): AccountTypeDao
    abstract fun budget(): BudgetDao
    abstract fun category(): CategoryDao
    abstract fun currency(): CurrencyDao
    abstract fun event(): EventDao
    abstract fun iconDao(): IconDao
    abstract fun person(): PersonDao
    //abstract fun mainCategory(): MainCategoryDao
    abstract fun merchant(): MerchantDao
    abstract fun period(): PeriodDao
    abstract fun project(): ProjectDao
    //abstract fun subCat(): SubCategoryDao
    abstract fun reward(): RewardDao
    abstract fun trans(): TransDao
    abstract fun transType(): TransTypeDao
    abstract fun template(): TemplateDao


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
                    //.createFromAsset(DB_PATH)

                    INSTANCE = instance
                    return instance
                }
            }
        }
    }

