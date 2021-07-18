package com.ogl4jo3.accounting.ui.categoryMgmt

import android.app.Activity
import android.content.res.Resources
import androidx.annotation.ArrayRes
import androidx.annotation.DrawableRes
import com.ogl4jo3.accounting.utils.safeLet

@DrawableRes
fun String.drawableId(activity: Activity?, resources: Resources?): Int =
    safeLet(activity, resources) { act, res ->
        res.getIdentifier(this, "drawable", act.packageName)
    } ?: -1

fun @receiver:DrawableRes Int.drawableName(resources: Resources?): String =
    resources?.getResourceEntryName(this) ?: ""

fun @receiver:ArrayRes Int.toCategoryIconList(resources: Resources?): List<CategoryIcon> =
    mutableListOf<CategoryIcon>().apply {
        resources?.let {
            val icons = it.obtainTypedArray(this@toCategoryIconList)
            for (i in 0 until icons.length()) {
                val resId = icons.getResourceId(i, -1)
                val resEntryName = it.getResourceEntryName(resId)
                this.add(CategoryIcon(resId, resEntryName))
            }
            icons.recycle()
        }
    }
