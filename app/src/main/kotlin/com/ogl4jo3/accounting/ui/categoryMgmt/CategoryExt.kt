package com.ogl4jo3.accounting.ui.categoryMgmt

import android.app.Activity
import android.content.res.Resources
import com.ogl4jo3.accounting.utils.safeLet

fun String.drawableId(activity: Activity?, resources: Resources?): Int =
    safeLet(activity, resources) { act, res ->
        res.getIdentifier(this, "drawable", act.packageName)
    } ?: -1

fun Int.drawableName(resources: Resources?): String =
    resources?.getResourceEntryName(this) ?: ""