package com.example.ngiu.functions

import android.content.Context
import com.example.ngiu.data.AppDatabase
import com.example.ngiu.data.entities.AccountType
import java.io.File
import java.io.FileWriter
import java.io.InputStreamReader
import kotlin.reflect.full.memberProperties
import com.opencsv.CSVReaderBuilder

class CSVFile {

    fun exportToCSV(context: Context){
        // account
        val accounts = AppDatabase.getDatabase(context).account().getAllAccount()
        var csvFile = File(context.getExternalFilesDir(null), "account.csv")
        writeCsvFile(csvFile,accounts)
        // accountType
        val accountType = AppDatabase.getDatabase(context).accountType().getAllAccountType()
        csvFile = File(context.getExternalFilesDir(null), "account_type.csv")
        writeCsvFile(csvFile,accountType)
        // Budget
        val budget = AppDatabase.getDatabase(context).budget().getAllBudget()
        csvFile = File(context.getExternalFilesDir(null), "budget.csv")
        writeCsvFile(csvFile,budget)
        // Category
        val category = AppDatabase.getDatabase(context).category().getAllCategory()
        csvFile = File(context.getExternalFilesDir(null), "category.csv")
        writeCsvFile(csvFile,category)
        // Currency
        val currency = AppDatabase.getDatabase(context).currency().getAllCurrency()
        csvFile = File(context.getExternalFilesDir(null), "currency.csv")
        writeCsvFile(csvFile,currency)
        // Event
        val event = AppDatabase.getDatabase(context).event().getAllEvent()
        csvFile = File(context.getExternalFilesDir(null), "event.csv")
        writeCsvFile(csvFile,event)
        // Icon
//        val icon = AppDatabase.getDatabase(context)
//        csvFile = File(context.getExternalFilesDir(null), "icon.csv")
//        writeCsvFile(csvFile,icon)
        // Merchant
        val merchant = AppDatabase.getDatabase(context).merchant().getAllMerchant()
        csvFile = File(context.getExternalFilesDir(null), "merchant.csv")
        writeCsvFile(csvFile,merchant)
        // Period
        val period = AppDatabase.getDatabase(context).period().getAllPeriodTrans()
        csvFile = File(context.getExternalFilesDir(null), "period.csv")
        writeCsvFile(csvFile,period)
        // person
        val person = AppDatabase.getDatabase(context).person().getAllPerson()
        csvFile = File(context.getExternalFilesDir(null), "person.csv")
        writeCsvFile(csvFile,person)
        // project
        val project = AppDatabase.getDatabase(context).project().getAllProject()
        csvFile = File(context.getExternalFilesDir(null), "project.csv")
        writeCsvFile(csvFile,project)
        // Reward
        val reward = AppDatabase.getDatabase(context).reward().getAllReward()
        csvFile = File(context.getExternalFilesDir(null), "reward.csv")
        writeCsvFile(csvFile,reward)
        // Template
        val template = AppDatabase.getDatabase(context).template().getAllTemplate()
        csvFile = File(context.getExternalFilesDir(null), "template.csv")
        writeCsvFile(csvFile,template)
        // Trans
        val trans = AppDatabase.getDatabase(context).trans().getAllTrans()
        csvFile = File(context.getExternalFilesDir(null), "transaction.csv")
        writeCsvFile(csvFile,trans)
        // Transaction Type
        val transType = AppDatabase.getDatabase(context).transType().getAllTransactionType()
        csvFile = File(context.getExternalFilesDir(null), "transaction_type.csv")
        writeCsvFile(csvFile,transType)

    }


    // doesn't word
    fun importFromCSV(context: Context){
        val accountsCsvFile = File(context.getExternalFilesDir(null), "account_type.csv")
        val accounts = importFromCSV<AccountType>(accountsCsvFile)

        //AppDatabase.getDatabase(context).accountType().addAccountTypes(accounts)
    }




    private inline fun <reified T : Any> readCsvFile(csvFile: File): List<T> {
        val fieldNames = T::class.java.declaredFields.map { it.name }

        return csvFile.bufferedReader().useLines { lines ->
            lines.drop(1).map { line ->
                val values = line.split(",").map { it.trim() }
                val constructor = T::class.java.getDeclaredConstructor(*Array<Class<*>>(fieldNames.size) { String::class.java })
                constructor.newInstance(*values.toTypedArray())
            }.toList()
        }
    }

    private inline fun <reified T> CSVFile.importFromCSV(file: File) {
        val reader = CSVReaderBuilder(file.reader()).build()
        val headerRow = reader.readNext() ?: return
        val headerMap = headerRow.mapIndexed { index, column -> index to column }.toMap()
        val dataList = mutableListOf<T>()
        reader.forEach { row ->
            val data = Array<Any?>(headerMap.size) { null }
            row.forEachIndexed { index, value ->
                headerMap[index]?.let {
                    data[it.toInt()] = value
                }
            }
            val dataInstance = T::class.java.declaredConstructors[0].newInstance(*data)
            dataList.add(dataInstance as T)
        }
        // insert to database
        //database.insertAll(dataList)

    }

    private fun <T : Any> writeCsvFile(csvFile: File, data: List<T>) {
        if (data.isEmpty()) {
            return
        }

        val fieldNames = data[0].javaClass.declaredFields.map { it.name }
        val csvHeader = fieldNames.joinToString(separator = ",") { escapeCsvField(it) }

        csvFile.bufferedWriter().use { writer ->
            writer.write(csvHeader)
            writer.newLine()

            data.forEach { obj ->
                val csvLine = fieldNames.joinToString(separator = ",") { field ->
                    val value = obj.javaClass.getDeclaredField(field).apply { isAccessible = true }.get(obj)
                    escapeCsvField(value?.toString() ?: "")
                }
                writer.write(csvLine)
                writer.newLine()
            }
        }
    }

    private fun escapeCsvField(value: String): String {
        return if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            "\"${value.replace("\"", "\"\"")}\""
        } else {
            value
        }
    }

}