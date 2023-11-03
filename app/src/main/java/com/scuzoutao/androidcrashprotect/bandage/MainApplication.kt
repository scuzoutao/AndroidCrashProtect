package com.scuzoutao.androidcrashprotect.bandage

import android.app.Application
import android.content.Context
import com.taou.maimai.handler.bandage.pojo.CrashPortray
import java.io.File

/**
 * @description
 * @author zoutao
 * @since 2023/11/3 星期五 15:07
 */
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        CrashPortrayHelper.init(this, getCrashPortrayConfig(), getAppImpl())
    }

    fun getCrashPortrayConfig(): List<CrashPortray>? {
        TODO()
    }

    fun getAppImpl(): IApp {
        return object : IApp {
            override fun showToast(context: Context, msg: String) {
                TODO("Not yet implemented")
            }

            override fun cleanCache(context: Context) {
                TODO("Not yet implemented")
            }

            override fun finishCurrentPage() {
                TODO("Not yet implemented")
            }

            override fun getVersionName(context: Context): String {
                TODO("Not yet implemented")
            }

            override fun donwloadFile(url: String): File? {
                TODO("Not yet implemented")
            }

            override fun readStringFromCache(key: String): String {
                TODO("Not yet implemented")
            }

            override fun writeStringToCache(file: File, content: String) {
                TODO("Not yet implemented")
            }
        }
    }
}