package com.example.ngiu.data


import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.ngiu.data.entities.*
import com.example.ngiu.data.relationships.*
import kotlinx.coroutines.flow.Flow
import androidx.room.Transaction

// Account
@Dao
interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addAccount(account: Account)

    @Delete
    suspend fun deleteAccount(account: Account)

    @Transaction
    @Query("SELECT * FROM Account")
    fun getAccountRecipient(): Flow<List<AcctTransRecipient>>

    @Transaction
    @Query("SELECT * FROM Account")
    fun getAccountPayer(): Flow<List<AcctTransPayer>>

    @Transaction
    @Query("SELECT * FROM AccountType")
    fun getAccountType(): Flow<List<AcctTypeAcct>>

}

// Currency
@Dao
interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addAccount(currency: Currency)

    @Transaction
    @Query("SELECT * FROM Currency")
    fun readAllData(): Flow<List<CurrencyAcct>>
}

// Individual/Person
@Dao
interface PersonDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addPerson(person: Person)

    @Delete
    suspend fun deletePerson(person: Person)

    @Transaction
    @Query("SELECT * FROM Person")
    fun getPersonTrans(): Flow<List<PersonTrans>>

    @Transaction
    @Query("SELECT * FROM Person")
    fun getPersonPeriod(): Flow<List<PersonPeriod>>
}

//Main Categories
@Dao
interface MainCategoriesDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addMC(mainCategories: MainCategories)

    @Delete
    suspend fun deleteMC(mainCategories: MainCategories)

    @Transaction
    @Query("SELECT * FROM MainCategories")
    fun getMC(): Flow<List<MainCatSub>>
}

// Merchant
@Dao
interface MerchantPeriodDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addMerchant(merchant: Merchant)

    @Delete
    suspend fun deleteMerchant(merchant: Merchant)

    @Transaction
    @Query("SELECT * FROM Merchant")
    fun getMerchantPeriod(): Flow<List<MerchantPeriod>>
}

// Period
@Dao
interface PeriodTransDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addPeriodTrans(period: Period)

    @Delete
    suspend fun deletePeriod(period: Period)

    @Transaction
    @Query("SELECT * FROM Period")
    fun getPeriodTrans(): Flow<List<PeriodTrans>>
}

// Project
@Dao
interface ProjectPeriodDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addProject(project: Project)

    @Delete
    suspend fun deleteProject(project: Project)

    @Transaction
    @Query("SELECT * FROM Project")
    fun getProjectPeriod(): Flow<List<ProjectPeriod>>
}

//Sub Cateogries
@Dao
interface SubCatPeriodDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addSubCat(subcategories: SubCategories)

    @Delete
    suspend fun deleteSubCat(subcategories: SubCategories)

    @Transaction
    @Query("SELECT * FROM SubCategories")
    fun getSubCat(): Flow<List<SubCategories>>
}

// Transaction
@Dao
interface TransDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addTransaction(trans: Trans)

    @Delete
    suspend fun deleteTransaction(trans: Trans)

    @Transaction
    @Query("SELECT * FROM Trans")
    fun readAllData(): Flow<List<Trans>>
}

// Transaction Type
@Dao
interface TransTypeTransDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addAccount(transactionType: TransactionType)

    @Delete
    suspend fun deleteTransType(transactionType: TransactionType)

    @Transaction
    @Query("SELECT * FROM TransactionType")
    fun getTransTypeTrans(): Flow<List<TransTypeTrans>>

    @Transaction
    @Query("SELECT * FROM TransactionType")
    fun getTransTypeMC(): Flow<List<TransTypeMC>>

    @Transaction
    @Query("SELECT * FROM TransactionType")
    fun getTransTypePeriod(): Flow<List<TransTypePeriod>>
}



