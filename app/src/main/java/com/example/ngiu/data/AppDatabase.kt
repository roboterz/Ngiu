package com.example.ngiu.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.ngiu.data.entities.*

@Database(
    entities = [
        Account::class, AccountType::class, Currency::class, Individual::class,
        MainCategories::class, Merchant::class, Period::class, Project::class,
        Reimburse::class, SubCategories::class, TransactionType::class,
        Transaction::class, ], version = 1, exportSchema = false,)
@TypeConverters(DateTypeConverter::class)
abstract class AppDatabase : RoomDatabase(){
    //TODO 
}