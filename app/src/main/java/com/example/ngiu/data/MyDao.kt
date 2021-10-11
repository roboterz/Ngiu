package com.example.ngiu.data


import androidx.room.*
import com.example.ngiu.data.entities.*
import androidx.room.Transaction
import com.example.ngiu.data.entities.list.TransactionDetail
import io.reactivex.Maybe

// Account
@Dao
interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addAccount(account: Account)

    @Delete
    fun deleteAccount(account: Account)

    @Transaction
    @Query("SELECT * FROM Account")
    fun getAllAccount(): List<Account>

    //@Transaction
    //@Query("SELECT * FROM Account")
    //fun getAcctTransRecip(): List<AcctTransRecipient>

    //@Transaction
    //@Query("SELECT * FROM Account")
    //fun getAccountPayer(): List<AcctTransAcct>

    //@Transaction
    //@Query("SELECT * FROM Account")
    //fun getAcctPeriodRecip(): List<AcctPeriodRecipient>

    //@Transaction
    //@Query("SELECT * FROM Account")
    //fun getAcctPeriodPayer(): List<AcctPeriodAcct>


}

// Account Type
@Dao
interface AccountTypeDao{
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addAccountType(accountType: AccountType)

    @Delete
    fun deleteAccountType(accountType: AccountType)

    @Transaction
    @Query("SELECT * FROM AccountType")
    fun getAllAccountType(): Maybe<List<AccountType>>
}

// Currency
@Dao
interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addCurrency(currency: Currency)

    //@Transaction
    //@Query("SELECT * FROM Currency")
    //fun getCurrency(): List<CurrencyAcct>

    @Transaction
    @Query("SELECT * FROM Currency")
    fun getAllCurrency(): List<Currency>
}

// Person
@Dao
interface PersonDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addPerson(person: Person):Long

    @Delete
    fun deletePerson(person: Person)

    //@Transaction
    //@Query("SELECT * FROM Person")
    //un getPersonTrans(): List<PersonTrans>

    @Transaction
    @Query("SELECT * FROM Person")
    fun getAllPerson(): List<Person>

}

//Main Categories
@Dao
interface MainCategoryDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addMC(mainCategory: MainCategory)

    @Delete
    fun deleteMC(mainCategory: MainCategory)

    //@Transaction
    //@Query("SELECT * FROM MainCategories")
    //fun getMC(): Flow<List<MainCatSub>>

    @Transaction
    @Query("SELECT * FROM MainCategory")
    fun getAllMainCategory(): List<MainCategory>
}

// Merchant
@Dao
interface MerchantDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addMerchant(merchant: Merchant)

    @Delete
    fun deleteMerchant(merchant: Merchant)

    //@Transaction
    //@Query("SELECT * FROM Merchant")
    //fun getMerchantPeriod(): Flow<List<MerchantPeriod>>

    @Transaction
    @Query("SELECT * FROM Merchant")
    fun getAllMerchant(): List<Merchant>
}

// Period
@Dao
interface PeriodDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addPeriodTrans(period: Period)

    @Delete
    fun deletePeriod(period: Period)

    //@Transaction
    //@Query("SELECT * FROM Period")
    //fun getPeriodTrans(): Flow<List<PeriodTrans>>

    @Transaction
    @Query("SELECT * FROM Period")
    fun getAllPeriodTrans(): List<Period>
}

// Project
@Dao
interface ProjectDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addProject(project: Project)

    @Delete
    fun deleteProject(project: Project)

    //@Transaction
    //@Query("SELECT * FROM Project")
    //fun getProjectPeriod(): Flow<List<ProjectPeriod>>

    @Transaction
    @Query("SELECT * FROM Project")
    fun getAllProject(): List<Project>
}

// Sub_Categories
@Dao
interface SubCategoryDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addSubCat(subcategory: SubCategory)

    @Delete
    fun deleteSubCat(subcategory: SubCategory)

    //@Transaction
    //@Query("SELECT * FROM SubCategories")
    //fun getSubCat(): List<SubCategories>

    @Transaction
    @Query("SELECT * FROM SubCategory")
    fun getIncomeCommonCategory(): List<SubCategory>

    @Transaction
    @Query("""
        SELECT *
        FROM SubCategory
        WHERE MainCategory_ID = 1
            AND SubCategory_Common = 0
    """)
    fun getExpenseCommonCategory(): List<SubCategory>

    @Transaction
    @Query("SELECT * FROM SubCategory")
    fun getTransferCommonCategory(): List<SubCategory>

    @Transaction
    @Query("SELECT * FROM SubCategory")
    fun getDebitCreditCommonCategory(): List<SubCategory>

    @Transaction
    @Query("SELECT * FROM SubCategory")
    fun getAllSubCategory(): List<SubCategory>
}

// Transaction
@Dao
interface TransDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addTransaction(trans: Trans)

    @Delete
    fun deleteTransaction(trans: Trans)

    @Transaction
    @Query("""
        SELECT Transaction_ID, TransactionType_Name, SubCategory_Name, Account.Account_Name, AccountRecipient.Account_Name as AccountRecipient_Name, 
                Transaction_Amount, Transaction_Date, Person_Name, Merchant_Name, Transaction_Memo, Project_Name, 
                Transaction_ReimburseStatus, Period_ID  
        FROM Trans, TransactionType, SubCategory, Account, Account as AccountRecipient, Person, Merchant, Project
        WHERE Trans.TransactionType_ID = TransactionType.TransactionType_ID 
                AND Trans.SubCategory_ID = SubCategory.SubCategory_ID
                And Trans.Account_ID = Account.Account_ID
                AND Trans.AccountRecipient_ID = AccountRecipient.Account_ID
                AND Trans.Person_ID = Person.Person_ID
                AND Trans.Merchant_ID = Merchant.Merchant_ID
                AND Trans.Project_ID = Project.Project_ID
        """)
    fun getAllTrans(): List<TransactionDetail>

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
    fun getAllTransactionType(): List<TransactionType>

    //@Transaction
    //@Query("SELECT * FROM TransactionType")
    //fun getTransTypeTrans(): Flow<List<TransTypeTrans>>

    //@Transaction
    //@Query("SELECT * FROM TransactionType")
    //fun getTransTypeMC(): Flow<List<TransTypeMC>>

    //@Transaction
    //@Query("SELECT * FROM TransactionType")
    //fun getTransTypePeriod(): Flow<List<TransTypePeriod>>
}



