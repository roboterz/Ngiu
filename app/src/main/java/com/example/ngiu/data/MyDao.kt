package com.example.ngiu.data


import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.ngiu.data.entities.*
import androidx.room.Transaction
import com.example.ngiu.data.entities.Currency
import com.example.ngiu.data.entities.returntype.AccountBalance
import com.example.ngiu.data.entities.returntype.AccountCount
import com.example.ngiu.data.entities.returntype.RecordDetail
import com.example.ngiu.data.entities.returntype.TransactionDetail
import com.example.ngiu.functions.CATEGORY_LIMIT
import com.example.ngiu.functions.chart.CategoryAmount
import java.util.*

/** Account **/
@Dao
interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAccount(account: Account)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAccounts(account: List<Account>)

    @Delete
    fun deleteAccount(account: Account)

/*    @Query("DELETE FROM Account WHERE Account_ID = :rID")
    fun deleteAccountById(rID:Long)

    @Query("DELETE FROM Trans WHERE Account_ID = :rID")
    fun deleteRecordByID(rID:Long)*/

    @Transaction
    @Query("SELECT COUNT(*) FROM Account WHERE AccountType_ID <> :typeID")
    fun getAccountCountExcept(typeID: Long): Int

    @Transaction
    @Query("SELECT COUNT(*) FROM Account WHERE AccountType_ID = :typeID")
    fun getAccountCountType(typeID: Long): Int

    @Transaction
    @Query("SELECT * FROM Account WHERE AccountType_ID <> :typeID")
    fun getAccountExceptType(typeID: Long): List<Account>

    @Transaction
    @Query("SELECT * FROM Account WHERE AccountType_ID = :typeID")
    fun getAccountByType(typeID: Long): List<Account>

    @Transaction
    @Query("SELECT * FROM Account ORDER BY AccountType_ID, Account_Name")
    fun getAllAccount(): List<Account>

    @Transaction
    @Query("SELECT * FROM Account ORDER BY AccountType_ID ASC, Account_Name ")
    fun getAllAccountASC(): List<Account>

    // get a record BY ID
    @Transaction
    @Query("SELECT * FROM Account WHERE Account_ID = :acctID")
    fun getRecordByAccountID(acctID:Long): Account

    @Update
    fun updateAccount(account: Account)

    @Transaction
    @Query("SELECT Account_Balance FROM Account WHERE Account_ID = :acctID")
    fun getAccountInitialBalance(acctID: Long): Double

    @Transaction
    @Query("SELECT Account_CreditLimit FROM Account WHERE Account_ID = :acctID")
    fun getAccountCreditLimit(acctID: Long): Double

    @Transaction
    @Query("""
      SELECT SUM(Transaction_Amount) FROM Trans trans
    INNER JOIN Account acct ON trans.Account_ID = acct.Account_ID
    WHERE trans.TransactionType_ID = 2 AND trans.Account_ID = :acctID 
    """)
   fun getInflowA(acctID:Long): Double


    @Transaction
    @Query("""
        SELECT SUM(Transaction_Amount) FROM Trans trans
        INNER JOIN Account acct ON trans.AccountRecipient_ID = acct.Account_ID
        WHERE trans.TransactionType_ID in (3,4) AND trans.AccountRecipient_ID = :acctID
      """)
    fun getInflowB(acctID:Long): Double

    @Transaction
    @Query("""
         SELECT SUM(Transaction_Amount) FROM Trans trans
        INNER JOIN Account acct ON trans.Account_ID = acct.Account_ID
        WHERE trans.TransactionType_ID = 1 AND trans.Account_ID = :acctID

      """)
    fun getOutflowA(acctID:Long): Double

    @Transaction
    @Query("""
      SELECT SUM(Transaction_Amount) FROM Trans trans
        INNER JOIN Account acct on trans.Account_ID = acct.Account_ID
        WHERE trans.TransactionType_ID IN (3,4) AND trans.Account_ID = :acctID
      """)
    fun getOutflowB(acctID:Long): Double

    @Transaction
    @Query("SELECT * FROM Account WHERE AccountType_ID = :acctTypeID Order By Account_PaymentDay ASC")
    fun getRecordByType(acctTypeID:Long): MutableList<Account>








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

/** Account Type **/
@Dao
interface AccountTypeDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAccountType(accountType: AccountType)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAccountTypes(accountType: List<AccountType>)

    @Delete
    fun deleteAccountType(accountType: AccountType)

    @Update
    fun updateAccountType(accountType: AccountType)

    // get a record BY ID
    @Transaction
    @Query("SELECT * FROM AccountType WHERE AccountType_ID = :acctTypeID")
    fun getRecordByID(acctTypeID:Long): AccountType

    @Transaction
    @Query("SELECT * FROM AccountType")
    fun getAllAccountType(): List<AccountType>

    @Transaction
    @Query("Update AccountType SET AccountType_Expanded = :value WHERE AccountType_ID = :acctTypeID")
    fun updateExpandedValueByID(acctTypeID:Long, value: Boolean)
}

/** Budget **/
@Dao
interface BudgetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addBudget(budget: Budget)

    // get a record BY ID
    @Transaction
    @Query("SELECT * FROM Budget WHERE Budget_ID = :budgetID")
    fun getRecordByID(budgetID:Long): Budget

    @Transaction
    @Query("SELECT * FROM Budget")
    fun getAllBudget(): List<Budget>
}

/** Category **/
@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCategory(category: Category)

    // get a record BY ID
    @Transaction
    @Query("SELECT * FROM Category WHERE Category_ID = :cateID")
    fun getRecordByID(cateID:Long): Category

    @Transaction
    @Query("SELECT * FROM Category")
    fun getAllCategory(): List<Category>


    @Update
    fun updateCategory(vararg category: Category)

    @Delete
    fun deleteCategory(category: Category)

    @Transaction
    @Query("DELETE FROM Category WHERE Category_ParentID = :cateParentID ")
    fun deleteCategoryByParentID(cateParentID: Long)

    // get a record BY ID
    @Transaction
    @Query("SELECT * FROM Category WHERE Category_ID = :cateID")
    fun getCategoryByID(cateID:Long): Category



    @Transaction
    @Query("""
        SELECT * FROM Category
        WHERE Category.TransactionType_ID = :transTypeID
            AND Category_Common= 1
    """)
    fun getCommonCategoryByTransactionType(transTypeID: Long): MutableList<Category>

    @Transaction
    @Query("""
        SELECT * FROM Category
        WHERE Category.TransactionType_ID = :transTypeID
        ORDER BY Category_OrderNo
    """)
    fun getCategoryByTransactionType(transTypeID: Long): MutableList<Category>

    @Transaction
    @Query("""
        SELECT * FROM Category
        WHERE Category.TransactionType_ID = :transTypeID
            AND Category_ParentID = :cateParentID
        ORDER BY Category_OrderNo
    """)
    fun getCategoryByTransactionTypeAndParentID(transTypeID: Long, cateParentID: Long): MutableList<Category>

    @Transaction
    @Query("""
        SELECT * FROM Category
        WHERE Category.TransactionType_ID = :transTypeID
            AND Category_ParentID = :cateParentID
            AND Category_ID >= :limit
        ORDER BY Category_OrderNo
    """)
    fun getCategoryByTransactionTypeAndParentIDWithLimit(transTypeID: Long, cateParentID: Long, limit: Long): MutableList<Category>

    @Transaction
    @Query("""
        SELECT * FROM Category
        WHERE Category_ParentID = :cateParentID
        ORDER BY Category_Name
    """)
    fun getCategoryByParentID(cateParentID: Long): MutableList<Category>

    @Transaction
    @Query("""
        SELECT * FROM Category
        WHERE Category_ParentID = :cateParentID
            AND Category_ID >= :limit
        ORDER BY Category_Name
    """)
    fun getCategoryByParentIDWithLimit(cateParentID: Long, limit: Long): MutableList<Category>


}



/** Currency **/
@Dao
interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCurrency(currency: Currency)

    // get a record BY ID
    @Transaction
    @Query("SELECT * FROM Currency WHERE Currency_ID = :currencyID")
    fun getRecordByID(currencyID:Long): Currency

    @Transaction
    @Query("SELECT * FROM Currency")
    fun getAllCurrency(): List<Currency>
}

/** Person **/
@Dao
interface PersonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPerson(person: Person):Long

    @Update
    fun updatePerson(vararg person: Person)

    @Delete
    fun deletePerson(person: Person)

    // get a record BY ID
    @Transaction
    @Query("SELECT * FROM Person WHERE Person_ID = :personID")
    fun getRecordByID(personID:Long): Person

    @Transaction
    @Query("SELECT * FROM Person")
    fun getAllPerson(): List<Person>

}


/** Event **/
@Dao
interface EventDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addEvent(event: Event)

    @Update
    fun updateEvent(vararg event: Event)

    @Delete
    fun deleteEvent(event: Event)

    @Transaction
    @Query("Select * From Event")
    fun getAllEvent(): List<Event>

    @Transaction
    @Query("Select * From Event WHERE Event_ID = :eventID")
    fun getRecordByID(eventID:Long): Event
}


/** ICON **/
@Dao
interface IconDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addIcon(icon: Icon)

    @Update
    fun updateIcon(vararg icon:Icon)

    @Delete
    fun deleteIcon(icon: Icon)

}


/** Merchant **/
@Dao
interface MerchantDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMerchant(merchant: Merchant)

    @Update
    fun updateMerchant(vararg merchant: Merchant)

    @Delete
    fun deleteMerchant(merchant: Merchant)

    // get a record BY ID
    @Transaction
    @Query("SELECT * FROM Merchant WHERE Merchant_ID = :merchantID")
    fun getRecordByID(merchantID:Long): Merchant

    @Transaction
    @Query("SELECT * FROM Merchant")
    fun getAllMerchant(): List<Merchant>
}

/** Period **/
@Dao
interface PeriodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
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

/** Project **/
@Dao
interface ProjectDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addProject(project: Project)

    @Update
    fun updateProject(vararg project: Project)

    @Delete
    fun deleteProject(project: Project)

    // get a record BY ID
    @Transaction
    @Query("SELECT * FROM Project WHERE Project_ID = :projectID")
    fun getRecordByID(projectID:Long): Project

    @Transaction
    @Query("SELECT * FROM Project")
    fun getAllProject(): List<Project>
}

////Main Categories
//@Dao
//interface MainCategoryDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun addMainCategory(mainCategory: MainCategory)
//
//    @Update
//    fun updateMainCategory(vararg mainCategory: MainCategory)
//
//    @Delete
//    fun deleteMainCategory(mainCategory: MainCategory)
//
//    // get a record BY ID
//    @Transaction
//    @Query("SELECT * FROM MainCategory WHERE MainCategory_ID = :rID")
//    fun getMainCategoryByID(rID:Long): MainCategory
//
//    @Transaction
//    @Query("SELECT * FROM MainCategory WHERE TransactionType_ID = :tID" )
//    fun getMainCategoryByTransactionType(tID: Long): MutableList<MainCategory>
//
//    @Transaction
//    @Query("SELECT * FROM MainCategory ORDER BY MainCategory_Name")
//    fun getAllMainCategory(): List<MainCategory>
//}
//
//// Sub_Categories
//@Dao
//interface SubCategoryDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun addSubCategory(subcategory: SubCategory)
//
//    @Update
//    fun updateSubCategory(vararg subCategory: SubCategory)
//
//    @Delete
//    fun deleteSubCategory(subcategory: SubCategory)
//
//
//    // get a record BY ID
//    @Transaction
//    @Query("SELECT * FROM SubCategory WHERE SubCategory_ID = :rID")
//    fun getSubCategoryByID(rID:Long): SubCategory
//
//
//
//    @Transaction
//    @Query("""
//        SELECT SubCategory_ID, SubCategory.MainCategory_ID, SubCategory_Name, SubCategory_Common
//        FROM SubCategory, MainCategory, TransactionType
//        WHERE SubCategory.MainCategory_ID = MainCategory.MainCategory_ID
//            AND MainCategory.TransactionType_ID = TransactionType.TransactionType_ID
//            AND MainCategory.TransactionType_ID = :tID
//            AND SubCategory_Common= 1
//    """)
//    fun getCommonCategoryByTransactionType(tID: Long): MutableList<SubCategory>
//
//    @Transaction
//    @Query("""
//        SELECT SubCategory_ID, SubCategory.MainCategory_ID, SubCategory_Name, SubCategory_Common
//        FROM SubCategory, MainCategory, TransactionType
//        WHERE SubCategory.MainCategory_ID = MainCategory.MainCategory_ID
//            AND MainCategory.TransactionType_ID = TransactionType.TransactionType_ID
//            AND MainCategory.TransactionType_ID = :tID
//        LIMIT 2
//    """)
//    fun getSubCategoryByTransactionType(tID: Long): List<SubCategory>
//
//    @Transaction
//    @Query("""
//        SELECT SubCategory_ID, MainCategory_ID, SubCategory_Name, SubCategory_Common
//        FROM SubCategory
//        WHERE MainCategory_ID = :rID
//        ORDER BY SubCategory_Name
//    """)
//    fun getSubCategoryByMainCategoryID(rID: Long): MutableList<SubCategory>
//
//    /*
//    @Transaction
//    @Query("""
//        SELECT SubCategory.SubCategory_ID, SubCategory.MainCategory_ID, TransactionType.TransactionType_ID, SubCategory.SubCategory_Name, SubCategory.SubCategory_Common
//        FROM SubCategory, MainCategory, TransactionType
//        WHERE SubCategory.MainCategory_ID = MainCategory.MainCategory_ID
//            AND MainCategory.TransactionType_ID = TransactionType.TransactionType_ID
//    """)
//    fun getAllSubCategory(): List<RecordSubCategory>
//
//     */
//
//    @Transaction
//    @Query("SELECT * FROM SUBCATEGORY ORDER BY SubCategory_Name")
//    fun getAllSubCategory(): List<SubCategory>
//
//}

/** Reward **/
@Dao
interface RewardDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addReward(reward: Reward)

    @Update
    fun updateReward(vararg reward: Reward)

    @Delete
    fun deleteReward(reward: Reward)

    @Transaction
    @Query("SELECT * FROM Reward WHERE Reward_ID = :rewardID")
    fun getRecordByID(rewardID:Long): Reward

    @Transaction
    @Query("SELECT * FROM Reward")
    fun getAllReward(): List<Reward>


}

/** Transaction **/
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
    @Query("SELECT * FROM Trans WHERE Transaction_ID = :transID")
    fun getRecordByID(transID:Long):Trans

    @Transaction
    @Query("SELECT * FROM Trans WHERE Account_ID = :acctID")
    fun getRecordsByAcctID(acctID:Long): List<Trans>

    @Transaction
    @Query("SELECT * FROM Trans WHERE Account_ID = :acctID OR AccountRecipient_ID = :acctID")
    fun getRecordsByAccountAndAccountRecipientID(acctID:Long): List<Trans>


    @Transaction
    @Query("SELECT * FROM Trans")
    fun getAllTransactions(): List<Trans>

    @Transaction
    @Query("""
        SELECT Transaction_ID, Trans.TransactionType_ID, Trans.Category_ID, Category_Name, Account.Account_ID, Account.Account_Name, 
                AccountRecipient.Account_ID as AccountRecipient_ID, AccountRecipient.Account_Name as AccountRecipient_Name, 
                Transaction_Amount, Transaction_Amount2, Transaction_Date, Trans.Person_ID, Person_Name, Trans.Merchant_ID, Merchant_Name, Transaction_Memo, Trans.Project_ID, Project_Name, 
                Transaction_ReimburseStatus, Period_ID  
        FROM Trans, Category, Account, Account as AccountRecipient, Person, Merchant, Project
        WHERE Trans.Category_ID = Category.Category_ID
                And Trans.Account_ID = Account.Account_ID
                AND Trans.AccountRecipient_ID = AccountRecipient.Account_ID
                AND Trans.Person_ID = Person.Person_ID
                AND Trans.Merchant_ID = Merchant.Merchant_ID
                AND Trans.Project_ID = Project.Project_ID
                AND Transaction_ID = :transID
        """)
    fun getOneTransactionDetail(transID: Long): TransactionDetail

    @Transaction
    @Query("""
        SELECT Transaction_ID, Trans.TransactionType_ID, Trans.Category_ID, Category_Name, Account.Account_ID, Account.Account_Name, 
                AccountRecipient.Account_ID as AccountRecipient_ID, AccountRecipient.Account_Name as AccountRecipient_Name, 
                Transaction_Amount, Transaction_Amount2, Transaction_Date, Trans.Person_ID, Person_Name, Trans.Merchant_ID, Merchant_Name, Transaction_Memo, Trans.Project_ID, Project_Name,
                Transaction_ReimburseStatus, Period_ID  
        FROM Trans, Category, Account, Account as AccountRecipient, Person, Merchant, Project
        WHERE Trans.Category_ID = Category.Category_ID
                And Trans.Account_ID = Account.Account_ID
                AND Trans.AccountRecipient_ID = AccountRecipient.Account_ID
                AND Trans.Person_ID = Person.Person_ID
                AND Trans.Merchant_ID = Merchant.Merchant_ID
                AND Trans.Project_ID = Project.Project_ID
        ORDER BY Transaction_Date DESC
        """)
    fun getAllTransDetail(): List<TransactionDetail>

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
        FROM Trans, Account
        WHERE Trans.Account_ID = Account.Account_ID
            AND TransactionType_ID = :transTypeID
            AND Transaction_Date Between :fromDate AND :toDate
            AND Transaction_ReimburseStatus = :reimburseStatus
            AND Account.Account_CountInNetAssets = :countInNetAssets
            AND Category_ID >= :cateIDLimit
        """)
    fun getSumOfDaysByTransactionType(transTypeID: Long, fromDate: String, toDate: String, reimburseStatus: Int = 0, countInNetAssets: Boolean = true, cateIDLimit: Long = CATEGORY_LIMIT): Double


    @Transaction
    @Query("""
        SELECT SUM(Transaction_Amount)
        FROM Trans
        WHERE TransactionType_ID = 2
            AND Transaction_Date Between :fromDate AND :toDate
        """)
    fun getMonthIncome(fromDate: String, toDate: String): Double



//    @Transaction
//    @Query("SELECT SUM(Transaction_Amount) FROM Trans WHERE TransactionType_ID = :transTypeID")
//    fun getSumOfAmountByTransactionType(transTypeID: Long): Double

//    @Transaction
//    @Query("""
//            SELECT SUM(Transaction_Amount) FROM Trans trans
//            INNER JOIN ACCOUNT acct ON trans.AccountRecipient_ID = acct.Account_ID
//            WHERE trans.TransactionType_ID = 4 AND acct.AccountType_ID != 9 -
//            (SELECT SUM(trans.Transaction_Amount) FROM Trans trans
//            INNER JOIN Account acct  ON trans.Account_ID = acct.Account_ID
//            WHERE trans.TransactionType_ID = 4 AND acct.AccountType_ID != 9)
//            """)
//    fun getSumOfAmountForPayableReceivable(): Double

    /*
    @Transaction
    @Query("""
            SELECT (SELECT sum(Account_Balance) FROM Account WHERE Account_CountInNetAssets = 1) 
                + (SELECT sum(Transaction_Amount) FROM Trans WHERE TransactionType_ID = 2) 
                - (SELECT sum(Transaction_Amount) FROM Trans WHERE TransactionType_ID = 1)
                + (SELECT sum(Transaction_Amount) 
                    FROM Trans
                    INNER JOIN ACCOUNT ON Trans.AccountRecipient_ID = Account.Account_ID 
                    WHERE Trans.TransactionType_ID = 4 
                        AND Account.AccountType_ID <> 9 )
            """)
    fun getAccountDetailByAccountID(acctID: Long): List<CreditCardDetailList>

     */


//    @Transaction
//    @Query("""
//        SELECT * FROM Trans trans
//        WHERE trans.Account_ID = :rID OR trans.AccountRecipient_ID = :rID
//        """)
//    fun getTransRecordAccount(rID:Long): List<Trans>

    @Transaction
    @Query("""
        SELECT Trans.Transaction_ID, Trans.TransactionType_ID, Trans.Category_ID, Category.Category_ID, Category.Category_Name, 
                Account.Account_ID, Account.Account_Name, AccountRecipient.Account_ID as AccountRecipient_ID, AccountRecipient.Account_Name as AccountRecipient_Name,
                Transaction_Amount, Transaction_Amount2, Transaction_Date, Transaction_Memo
        FROM Trans, Category, Account, Account as AccountRecipient
        WHERE (Trans.Account_ID = :acctID OR Trans.AccountRecipient_ID = :acctID)
            AND Trans.Category_ID = Category.Category_ID
            AND Trans.Account_ID = Account.Account_ID
            AND Trans.AccountRecipient_ID = AccountRecipient.Account_ID
        ORDER BY Transaction_Date DESC
        """)
    fun getTransRecordDetailByAccount(acctID:Long): List<RecordDetail>

    @Transaction
    @Query("""
        SELECT Transaction_ID, Trans.TransactionType_ID, Trans.Category_ID, Category_Name, Account.Account_ID, Account.Account_Name, 
                AccountRecipient.Account_ID as AccountRecipient_ID, AccountRecipient.Account_Name as AccountRecipient_Name, 
                Transaction_Amount, Transaction_Amount2, Transaction_Date, Trans.Person_ID, Person_Name, Trans.Merchant_ID, Merchant_Name, Transaction_Memo, Trans.Project_ID, Project_Name, 
                Transaction_ReimburseStatus, Period_ID  
        FROM Trans, Category, Account, Account as AccountRecipient, Person, Merchant, Project
        WHERE Trans.Category_ID = :cateID
            AND Trans.Transaction_Date >= :startDate
            AND Trans.Transaction_Date < :endDate
            AND Trans.Category_ID = Category.Category_ID
            And Trans.Account_ID = Account.Account_ID
            AND Trans.AccountRecipient_ID = AccountRecipient.Account_ID
            AND Trans.Person_ID = Person.Person_ID
            AND Trans.Merchant_ID = Merchant.Merchant_ID
            AND Trans.Project_ID = Project.Project_ID
        ORDER BY Transaction_Date DESC
        """)
    fun getTransRecordDetailByCategory(cateID: Long, startDate: String, endDate: String): List<TransactionDetail>

    @Transaction
    @Query("""
        SELECT Transaction_ID, Trans.TransactionType_ID, Trans.Category_ID, Category_Name, Account.Account_ID, Account.Account_Name, 
                AccountRecipient.Account_ID as AccountRecipient_ID, AccountRecipient.Account_Name as AccountRecipient_Name, 
                Transaction_Amount, Transaction_Amount2, Transaction_Date, Trans.Person_ID, Person_Name, Trans.Merchant_ID, Merchant_Name, Transaction_Memo, Trans.Project_ID, Project_Name,
                Transaction_ReimburseStatus, Period_ID  
        FROM Trans, Category, Account, Account as AccountRecipient, Person, Merchant, Project
        WHERE Trans.Transaction_ReimburseStatus = :reimburseStatus
            AND Trans.Category_ID = Category.Category_ID
            And Trans.Account_ID = Account.Account_ID
            AND Trans.AccountRecipient_ID = AccountRecipient.Account_ID
            AND Trans.Person_ID = Person.Person_ID
            AND Trans.Merchant_ID = Merchant.Merchant_ID
            AND Trans.Project_ID = Project.Project_ID
        ORDER BY Transaction_Date DESC
        """)
    fun getTransRecordDetailByReimburseStatus(reimburseStatus: Int): List<TransactionDetail>

    @Transaction
    @Query("""
        SELECT SUM(Transaction_Amount) 
        FROM Trans 
        WHERE Account_ID = :acctID 
            AND TransactionType_ID = 2
        """)
    fun getTotalAmountOfIncomeByAccount(acctID:Long): Double


    @Transaction
    @Query("""
        SELECT SUM(Transaction_Amount) 
        FROM Trans 
        WHERE Account_ID = :acctID 
            AND TransactionType_ID = 1
        """)
    fun getTotalAmountOfExpenseByAccount(acctID:Long):Double


    @Transaction
    @Query("""
        SELECT SUM(Transaction_Amount) 
        FROM Trans 
        WHERE Account_ID = :acctID 
            AND TransactionType_ID > 2
        """)
    fun getTotalAmountOfTransferOutByAccount(acctID:Long): Double


    @Transaction
    @Query("""
        SELECT SUM(Transaction_Amount) 
        FROM Trans 
        WHERE AccountRecipient_ID = :acctID 
            AND TransactionType_ID > 2
        """)
    fun getTotalAmountOfTransferInByAccount(acctID:Long): Double


    @Transaction
    @Query("""
        SELECT SUM(Transaction_Amount) 
        FROM Trans 
        WHERE TransactionType_ID = 4 
            AND Category_ID = :cateID
            AND (Account_ID = :acctID OR AccountRecipient_ID = :acctID)
        """)
    fun getTotalAmountFromPRAccountBYCategoryID(acctID:Long, cateID: Long): Double

    @Transaction
    @Query("SELECT COUNT(*) FROM Trans")
    fun getTransCount(): Long

    @Transaction
    @Query("""
        SELECT Account.Account_Name, COUNT(Trans.Account_ID) as Account_Count, Category_ID
        FROM Trans, Account
        WHERE Trans.TransactionType_ID = :transTypeID AND Category_ID = :cateID
            AND Trans.Account_ID = Account.Account_ID
        GROUP BY Trans.Account_ID
        ORDER BY Account_Count DESC
        """)
    fun getCountOfAccountsByTransactionTypeAndCategory(transTypeID: Long, cateID: Long): List<AccountCount>

    @Transaction
    @Query("""
        SELECT Account.Account_Name, COUNT(Trans.Account_ID) as Account_Count, Category_ID
        FROM Trans, Account
        WHERE Trans.TransactionType_ID = :transTypeID
            AND Trans.Account_ID = Account.Account_ID
        GROUP BY Trans.Account_ID
        ORDER BY Account_Count DESC
        """)
    fun getCountOfAccountsByTransactionType(transTypeID: Long): List<AccountCount>

    @Transaction
    @Query("""
        SELECT Account.Account_Name, COUNT(Trans.Account_ID) as Account_Count, Category_ID
        FROM Trans, Account
        WHERE TransactionType_ID = :transTypeID AND Category_ID = :cateID
            AND Trans.AccountRecipient_ID = Account.Account_ID
        GROUP BY Trans.AccountRecipient_ID
        ORDER BY Account_Count DESC
        """)
    fun getCountOfRecipientAccountsByTransactionTypeAndCategory(transTypeID: Long, cateID: Long): List<AccountCount>

    @Transaction
    @Query("""
        SELECT Account.Account_Name, COUNT(Trans.Account_ID) as Account_Count, Category_ID
        FROM Trans, Account
        WHERE TransactionType_ID = :transTypeID 
            AND Trans.AccountRecipient_ID = Account.Account_ID
        GROUP BY Trans.AccountRecipient_ID
        ORDER BY Account_Count DESC
        """)
    fun getCountOfRecipientAccountsByTransactionType(transTypeID: Long): List<AccountCount>


    @Transaction
    @Query("""
        SELECT Trans.Category_ID, 
                Category.Category_Name, 
                SUM(Trans.Transaction_Amount) as Amount, 
                Category.Category_ParentID, 
                Trans.TransactionType_ID
        FROM Trans, Category, Account
        WHERE Trans.Category_ID = Category.Category_ID
            AND Trans.TransactionType_ID = :transTypeID
            AND Trans.Transaction_Date >= :startDate
            AND Trans.Transaction_Date < :endDate
            AND Trans.Category_ID >= :cateIDLimit
            AND Trans.Transaction_ReimburseStatus = 0
            AND Trans.Account_ID = Account.Account_ID
            AND Account.Account_CountInNetAssets = 1
        GROUP BY Trans.Category_ID
        ORDER BY Amount DESC
        """)
    fun getCategoryAmountByTransactionType(transTypeID: Long, startDate: String, endDate: String, cateIDLimit: Long = CATEGORY_LIMIT): MutableList<CategoryAmount>
}


/** Transaction Type **/
@Dao
interface TransTypeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTransType(transactionType: TransactionType)

    @Delete
    fun deleteTransType(transactionType: TransactionType)

    //get a record BY ID
    @Transaction
    @Query("SELECT * FROM TransactionType WHERE TransactionType_ID = :transTypeID")
    fun getRecordByID(transTypeID:Long):TransactionType

    @Transaction
    @Query("SELECT * FROM TransactionType")
    fun getAllTransactionType(): List<TransactionType>


}

/** Template **/
@Dao
interface TemplateDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTemplate(template: Template)

    @Delete
    fun deleteTemplate(template: Template)

    @Update
    fun updateTemplate(vararg template: Template)

    //get a record BY ID
    @Transaction
    @Query("SELECT * FROM Template WHERE Template_ID = :templateID")
    fun getRecordByID(templateID:Long):Template

    @Transaction
    @Query("SELECT * FROM Template")
    fun getAllTemplate(): List<Template>

}


