package com.example.ngiu.data


import androidx.room.*
import com.example.ngiu.data.entities.*
import androidx.room.Transaction
import com.example.ngiu.data.entities.Currency
import com.example.ngiu.data.entities.returntype.TransactionDetail
import io.reactivex.Maybe
import java.time.LocalDate
import java.time.LocalDateTime

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

    // get a record BY ID
    @Transaction
    @Query("SELECT * FROM Account WHERE Account_ID = :rID")
    fun getRecordByID(rID:Long): Account



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

    // get a record BY ID
    @Transaction
    @Query("SELECT * FROM AccountType WHERE AccountType_ID = :rID")
    fun getRecordByID(rID:Long): AccountType

    @Transaction
    @Query("SELECT * FROM AccountType")
    fun getAllAccountType(): Maybe<List<AccountType>>

    @Transaction
    @Query("SELECT * FROM AccountType")
    fun getAllAccountTypes(): List<AccountType>
}

// Currency
@Dao
interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addCurrency(currency: Currency)

    // get a record BY ID
    @Transaction
    @Query("SELECT * FROM Currency WHERE Currency_ID = :rID")
    fun getRecordByID(rID:Long): Currency

    @Transaction
    @Query("SELECT * FROM Currency")
    fun getAllCurrency(): List<Currency>
}

// Person
@Dao
interface PersonDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addPerson(person: Person):Long

    @Update
    fun updatePerson(vararg person: Person)

    @Delete
    fun deletePerson(person: Person)

    // get a record BY ID
    @Transaction
    @Query("SELECT * FROM Person WHERE Person_ID = :rID")
    fun getRecordByID(rID:Long): Person

    @Transaction
    @Query("SELECT * FROM Person")
    fun getAllPerson(): List<Person>

}



// Merchant
@Dao
interface MerchantDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addMerchant(merchant: Merchant)

    @Update
    fun updateMerchant(vararg merchant: Merchant)

    @Delete
    fun deleteMerchant(merchant: Merchant)

    // get a record BY ID
    @Transaction
    @Query("SELECT * FROM Merchant WHERE Merchant_ID = :rID")
    fun getRecordByID(rID:Long): Merchant

    @Transaction
    @Query("SELECT * FROM Merchant")
    fun getAllMerchant(): List<Merchant>
}

// Period
@Dao
interface PeriodDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addPeriodTrans(period: Period)

    @Update
    fun updatePeriod(vararg period: Period)

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

    @Update
    fun updateProject(vararg project: Project)

    @Delete
    fun deleteProject(project: Project)

    // get a record BY ID
    @Transaction
    @Query("SELECT * FROM Project WHERE Project_ID = :rID")
    fun getRecordByID(rID:Long): Project

    @Transaction
    @Query("SELECT * FROM Project")
    fun getAllProject(): List<Project>
}

//Main Categories
@Dao
interface MainCategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMainCategory(mainCategory: MainCategory)

    @Update
    fun updateMainCategory(vararg mainCategory: MainCategory)

    @Delete
    fun deleteMainCategory(mainCategory: MainCategory)

    // get a record BY ID
    @Transaction
    @Query("SELECT * FROM MainCategory WHERE MainCategory_ID = :rID")
    fun getMainCategoryByID(rID:Long): MainCategory

    @Transaction
    @Query("SELECT * FROM MainCategory WHERE TransactionType_ID = :tID")
    fun getMainCategoryByTransactionType(tID: Long): MutableList<MainCategory>

    @Transaction
    @Query("SELECT * FROM MainCategory")
    fun getAllMainCategory(): List<MainCategory>
}

// Sub_Categories
@Dao
interface SubCategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addSubCategory(subcategory: SubCategory)

    @Update
    fun updateSubCategory(vararg subCategory: SubCategory)

    @Delete
    fun deleteSubCategory(subcategory: SubCategory)


    // get a record BY ID
    @Transaction
    @Query("SELECT * FROM SubCategory WHERE SubCategory_ID = :rID")
    fun getSubCategoryByID(rID:Long): SubCategory



    @Transaction
    @Query("""
        SELECT SubCategory_ID, SubCategory.MainCategory_ID, SubCategory_Name, SubCategory_Common
        FROM SubCategory, MainCategory, TransactionType
        WHERE SubCategory.MainCategory_ID = MainCategory.MainCategory_ID
            AND MainCategory.TransactionType_ID = TransactionType.TransactionType_ID
            AND MainCategory.TransactionType_ID = :tID
            AND SubCategory_Common= 1
    """)
    fun getCommonCategoryByTransactionType(tID: Long): MutableList<SubCategory>

    @Transaction
    @Query("""
        SELECT SubCategory_ID, SubCategory.MainCategory_ID, SubCategory_Name, SubCategory_Common
        FROM SubCategory, MainCategory, TransactionType
        WHERE SubCategory.MainCategory_ID = MainCategory.MainCategory_ID
            AND MainCategory.TransactionType_ID = TransactionType.TransactionType_ID
            AND MainCategory.TransactionType_ID = :tID
        LIMIT 2
    """)
    fun getSubCategoryByTransactionType(tID: Long): List<SubCategory>

    @Transaction
    @Query("""
        SELECT SubCategory_ID, MainCategory_ID, SubCategory_Name, SubCategory_Common
        FROM SubCategory
        WHERE MainCategory_ID = :rID
    """)
    fun getSubCategoryByMainCategoryID(rID: Long): MutableList<SubCategory>

    /*
    @Transaction
    @Query("""
        SELECT SubCategory.SubCategory_ID, SubCategory.MainCategory_ID, TransactionType.TransactionType_ID, SubCategory.SubCategory_Name, SubCategory.SubCategory_Common
        FROM SubCategory, MainCategory, TransactionType
        WHERE SubCategory.MainCategory_ID = MainCategory.MainCategory_ID
            AND MainCategory.TransactionType_ID = TransactionType.TransactionType_ID
    """)
    fun getAllSubCategory(): List<RecordSubCategory>

     */

    @Transaction
    @Query("SELECT * FROM SUBCATEGORY")
    fun getAllSubCategory(): List<SubCategory>

}



// Transaction
@Dao
interface TransDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTransaction(trans: Trans)

    @Update
    fun updateTransaction(vararg trans: Trans)

    @Delete
    fun deleteTransaction(trans: Trans)

    // get a record BY ID
    @Transaction
    @Query("SELECT * FROM Trans WHERE Transaction_ID = :rID")
    fun getRecordByID(rID:Long):Trans

    @Transaction
    @Query("SELECT * FROM Trans WHERE Account_ID = :rID")
    fun getRecordsByAcctID(rID:Long): List<Trans>

    @Transaction
    @Query("""
        SELECT Transaction_ID, Trans.TransactionType_ID, SubCategory_Name, Account.Account_Name, AccountRecipient.Account_Name as AccountRecipient_Name, 
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
                AND Transaction_ID = :rID
        """)
    fun getOneTransaction(rID: Long): TransactionDetail

    @Transaction
    @Query("""
        SELECT Transaction_ID, Trans.TransactionType_ID, SubCategory_Name, Account.Account_Name, AccountRecipient.Account_Name as AccountRecipient_Name, 
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
        ORDER BY Transaction_Date DESC
        """)
    fun getAllTrans(): List<TransactionDetail>

    @Transaction
    @Query("""
        SELECT SUM(Transaction_Amount)
        FROM Trans
        WHERE TransactionType_ID = 1
            AND Transaction_Date Between :fromDate AND :toDate
        """)
    fun getMonthExpense(fromDate: String, toDate: String): Double

    @Transaction
    @Query("""
        SELECT SUM(Transaction_Amount)
        FROM Trans
        WHERE TransactionType_ID = 2
            AND Transaction_Date Between :fromDate AND :toDate
        """)
    fun getMonthIncome(fromDate: String, toDate: String): Double

    //@Query("SELECT *, :date AS passed_date, coalesce(date(date),'ouch') AS cnv_date, coalesce(date(:date),'ouch') AS cnv_passed_date FROM user WHERE date(date / 1000,'unixepoch') = date(:date / 1000,'unixepoch');")

    @Transaction
    @Query("SELECT SUM(Transaction_Amount) as Transaction_Amount FROM Trans WHERE Account_ID = :rID")
    fun getTotalSumA(rID: Long): Double

    @Transaction
    @Query("SELECT SUM(Transaction_Amount) as Transaction_Amount FROM Trans WHERE AccountRecipient_ID = :rID AND TransactionType_ID IN (3,4)")
    fun getTotalSumB(rID: Long): Double

    // 0 for false, 1 for true: so countnetassets if true
    @Transaction
    @Query("SELECT SUM(Account_Balance)  FROM Account WHERE Account_CountInNetAssets = 1 ")
    fun getSumAccount(): Double


    @Transaction
    @Query("SELECT SUM(Transaction_Amount) FROM Trans WHERE TransactionType_ID = :rID")
    fun getTransactionSums(rID: Long): Double

    @Transaction
    @Query("""
            SELECT SUM(Transaction_Amount) FROM Trans trans 
            INNER JOIN ACCOUNT acct ON trans.AccountRecipient_ID = acct.Account_ID 
            WHERE trans.TransactionType_ID = 4 AND acct.AccountType_ID != 9 -  
            (SELECT SUM(trans.Transaction_Amount) FROM Trans trans  
            INNER JOIN Account acct  ON trans.Account_ID = acct.Account_ID 
            WHERE trans.TransactionType_ID = 4 AND acct.AccountType_ID != 9)
            """)
    fun getSumTotalAsset(): Double


}

// Transaction Type
@Dao
interface TransTypeDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addTransType(transactionType: TransactionType)

    @Delete
    fun deleteTransType(transactionType: TransactionType)

    // get a record BY ID
    @Transaction
    @Query("SELECT * FROM TransactionType WHERE TransactionType_ID = :rID")
    fun getRecordByID(rID:Long):TransactionType

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



