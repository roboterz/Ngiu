package com.example.ngiu

import android.provider.BaseColumns

object AcctContract {

    /* Inner class that defines the table contents */
    class UserEntry : BaseColumns {
        companion object {
            const val TABLE_NAME = "Account"
            const val COLUMN_ID = "id"
            const val COLUMN_TypeID = "type_id"
            const val COLUMN_Name = "name"
        }
    }
}

object AcctType {
    class UserEntry : BaseColumns {
        companion object {
            const val TABLE_NAME = "AccountType"
            const val COLUMN_ID = "id"
            const val COLUMN_Name = "name"
        }
    }
}

object MainCategory {
    class UserEntry : BaseColumns {
        companion object {
            const val TABLE_NAME = "MainCateogries"
            const val COLUMN_ID = "id"
            const val COLUMN_TypeID = "type_id"
            const val COLUMN_Name = "name"
        }
    }
}

object Merchant {
    class UserEntry : BaseColumns {
        companion object {
            const val TABLE_NAME = "Merchant"
            const val COLUMN_ID = "id"
            const val COLUMN_Name = "name"
        }
    }
}

object Period {
    class UserEntry : BaseColumns {
        companion object {
            const val TABLE_NAME = "Period"
            const val COLUMN_ID = "id"
            const val COLUMN_TypeID = "type_id"
            const val COLUMN_TransactionID = "trans_id"
            const val COLUMN_RepeatInterval = "repeat"
            const val COLUMN_Status = "status"
            const val COLUMN_EndDate = "endDate"
        }
    }
}

object Person {
    class UserEntry : BaseColumns {
        companion object {
            const val TABLE_NAME = "Person"
            const val COLUMN_ID = "id"
            const val COLUMN_Name = "name"
        }
    }
}