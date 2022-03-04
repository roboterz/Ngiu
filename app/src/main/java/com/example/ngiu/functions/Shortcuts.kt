package com.example.ngiu.functions

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import androidx.core.content.ContextCompat.getSystemService
import com.example.ngiu.MainActivity
import com.example.ngiu.R
import com.example.ngiu.ui.record.RecordFragment
const val shortcut_new_id = "id_new"

object Shortcuts {

    fun setUp(context: Context){
        val shortcutManager: ShortcutManager? =
            getSystemService<ShortcutManager>(context, ShortcutManager::class.java)

        val intents:Array<Intent> = arrayOf(Intent(Intent.ACTION_VIEW, null, context, MainActivity::class.java),
            Intent(Intent.ACTION_VIEW, null, context, RecordFragment::class.java ))

        val shortcutNew = ShortcutInfo.Builder(context, shortcut_new_id)
            .setShortLabel("New")
            .setLongLabel("Create a new record.")
            .setIcon(Icon.createWithResource(context, R.drawable.ic_baseline_add_24))
            .setIntents(intents)
            .build()

        shortcutManager!!.dynamicShortcuts = listOf(shortcutNew)
    }
}