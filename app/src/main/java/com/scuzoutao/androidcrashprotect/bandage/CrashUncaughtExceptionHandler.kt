package com.scuzoutao.androidcrashprotect.bandage

import android.os.Looper
import java.lang.Thread.UncaughtExceptionHandler

/**
 * @description
 * @author zoutao
 * @since 2023/11/3 星期五 14:57
 */
object CrashUncaughtExceptionHandler : UncaughtExceptionHandler {
    private var oldHandler: UncaughtExceptionHandler? = null

    fun init() {
        oldHandler = Thread.getDefaultUncaughtExceptionHandler()
        oldHandler?.let {
            Thread.setDefaultUncaughtExceptionHandler(this)
        }
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        if (CrashPortrayHelper.needBandage(e)) {
            bandage()
            return
        }

        //崩吧
        oldHandler?.uncaughtException(t, e)
    }

    /**
     * 让当前线程恢复运行
     */
    private fun bandage() {
        while (true) {
            try {
                if (Looper.myLooper() == null) {
                    Looper.prepare()
                }
                Looper.loop()
            } catch (e: Exception) {
                uncaughtException(Thread.currentThread(), e)
                break
            }
        }
    }
}