package com.scuzoutao.androidcrashprotect.bandage

import android.content.Context
import java.io.File

/**
 * @description
 * @author zoutao
 * @since 2023/11/3 星期五 14:53
 */
interface IApp {
    fun showToast(context: Context, msg: String)
    fun cleanCache(context: Context)
    fun finishCurrentPage()
    fun getVersionName(context: Context): String

    fun donwloadFile(url: String): File?
    fun readStringFromCache(key : String): String
    fun writeStringToCache(file: File, content: String)
}