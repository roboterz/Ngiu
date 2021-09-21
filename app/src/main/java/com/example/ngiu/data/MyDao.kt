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
    fun addAccount(account: Account)

    @Delete
    fun deleteAccount(account: Account)

    @Transaction
    @Query("SELECT * FROM Account")
    fun getAcctTransRecip(): Flow<List<AcctTransRecipient>>

    @Transaction
    @Query("SELECT * FROM Account")
    fun getAccountPayer(): Flow<List<AcctTransPayer>>

    @Transaction
    @Query("SELECT * FROM AccountType")
    fun getAccountType(): Flow<List<AcctTypeAcct>>

    @Transaction
    @Query("SELECT * FROM Account")
    fun getAcctPeriodRecip(): Flow<List<AcctPeriodRecipient>>

    @Transaction
    @Query("SELECT * FROM Account")
    fun getAcctPeriodPayer(): Flow<List<AcctPeriodPayer>>


}

// Account Type
@Dao
interface AccountTypeDao{
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addAccountType(accountType: AccountType)

    @Transaction
    @Query("SELECT * FROM AccountType")
    fun getAccountType(): Flow<List<AcctTypeAcct>>
}

// Currency
@Dao
interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addCurrency(currency: Currency)

    @Transaction
    @Query("SELECT * FROM Currency")
    fun getCurrency(): Flow<List<CurrencyAcct>>
}

// Individual/Person
@Dao
interface PersonDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addPerson(person: Person):Long

    @Delete
    fun deletePerson(person: Person)

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
    fun addMC(mainCategories: MainCategories)

    @Delete
    fun deleteMC(mainCategories: MainCategories)

    @Transaction
    @Query("SELECT * FROM MainCategories")
    fun getMC(): Flow<List<MainCatSub>>
}

// Merchant
@Dao
interface MerchantDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addMerchant(merchant: Merchant)

    @Delete
    fun deleteMerchant(merchant: Merchant)

    @Transaction
    @Query("SELECT * FROM Merchant")
    fun getMerchantPeriod(): Flow<List<MerchantPeriod>>
}

// Period
@Dao
interface PeriodDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addPeriodTrans(period: Period)

    @Delete
    fun deletePeriod(period: Period)

    @Transaction
    @Query("SELECT * FROM Period")
    fun getPeriodTrans(): Flow<List<PeriodTrans>>
}

// Project
@Dao
interface ProjectDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addProject(project: Project)

    @Delete
    fun deleteProject(project: Project)

    @Transaction
    @Query("SELECT * FROM Project")
    fun getProjectPeriod(): Flow<List<ProjectPeriod>>
}

// Sub_Categories
@Dao
interface SubCategoriesDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addSubCat(subcategories: SubCategories)

    @Delete
    fun deleteSubCat(subcategories: SubCategories)

    @Transaction
    @Query("SELECT * FROM SubCategories")
    fun getSubCat(): Flow<List<SubCategories>>
}

// Transaction
@Dao
interface TransDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addTransaction(trans: Trans)

    @Delete
    fun deleteTransaction(trans: Trans)

    @Transaction
    @Query("SELECT * FROM Trans")
    fun getTrans(): Flow<List<Trans>>
}

// Transaction Type
@Dao
interface TransTypeDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addTransType(transactionType: TransactionType)

    @Delete
    fun deleteTransType(transactionType: TransactionType)

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



