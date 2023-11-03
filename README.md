# AndroidCrashProtect
实现 looper 兜底机制的 demo，支持远程下载 crash 兜底的配置文件，出现 crash 时，通过对异常各种信息、app 和系统版本信息等来实现崩溃画像匹配，保护住之后还可以进行一些自定义操作，如 toast、清除缓存等
                                                     
外部需要：                          
1. 实现 IApp 接口，提供相应操作的实现             
2. 在 Application#onCreate() 中调用 CrashPortrayHelper#init
3. 自定义配置文件的下载、缓存、读取逻辑

实现思路参考：[Android稳定性：可远程配置化的Looper兜底框架](https://juejin.cn/post/7198466997288566842)
个人掘金主页：[邹阿涛涛涛涛涛涛的个人主页](https://juejin.cn/user/3808364009106839/posts)

# 核心代码：
## 按配置判断是否保护
```
fun needBandage(throwable: Throwable): Boolean {                                                                            
    if (crashPortrayConfig.isNullOrEmpty()) {                                                                               
        return false                                                                                                        
    }                                                                                                                       
                                                                                                                            
    val config: List<CrashPortray>? = crashPortrayConfig                                                                    
    if (config.isNullOrEmpty()) {                                                                                           
        return false                                                                                                        
    }                                                                                                                       
    for (i in config.indices) {                                                                                             
        val crashPortray = config[i]                                                                                        
        if (!crashPortray.valid()) {                                                                                        
            continue                                                                                                        
        }                                                                                                                   
                                                                                                                            
        //1. app 版本号                                                                                                        
        if (crashPortray.appVersion.isNotEmpty()                                                                            
            && !crashPortray.appVersion.contains(actionImpl.getVersionName(application))                                    
        ) {                                                                                                                 
            continue                                                                                                        
        }                                                                                                                   
                                                                                                                            
        //2. os_version                                                                                                     
        if (crashPortray.osVersion.isNotEmpty()                                                                             
            && !crashPortray.osVersion.contains(Build.VERSION.SDK_INT)                                                      
        ) {                                                                                                                 
            continue                                                                                                        
        }                                                                                                                   
                                                                                                                            
        //3. model                                                                                                          
        if (crashPortray.model.isNotEmpty()                                                                                 
            && crashPortray.model.firstOrNull { Build.MODEL.equals(it, true) } == null                                      
        ) {                                                                                                                 
            continue                                                                                                        
        }                                                                                                                   
                                                                                                                            
        val throwableName = throwable.javaClass.simpleName                                                                  
        val message = throwable.message ?: ""                                                                               
        //4. class_name                                                                                                     
        if (crashPortray.className.isNotEmpty()                                                                             
            && crashPortray.className != throwableName                                                                      
        ) {                                                                                                                 
            continue                                                                                                        
        }                                                                                                                   
                                                                                                                            
        //5. message                                                                                                        
        if (crashPortray.message.isNotEmpty() && !message.contains(crashPortray.message)                                    
        ) {                                                                                                                 
            continue                                                                                                        
        }                                                                                                                   
                                                                                                                            
        //6. stack                                                                                                          
        if (crashPortray.stack.isNotEmpty()) {                                                                              
            var match = false                                                                                               
            throwable.stackTrace.forEach { element ->                                                                       
                val str = element.toString()                                                                                
                if (crashPortray.stack.find { str.contains(it) } != null) {                                                 
                    match = true                                                                                            
                    return@forEach                                                                                          
                }                                                                                                           
            }                                                                                                               
            if (!match) {                                                                                                   
                continue                                                                                                    
            }                                                                                                               
        }                                                                                                                   
                                                                                                                            
        //7. 相应操作                                                                                                           
        if (crashPortray.clearCache == 1) {                                                                                 
            actionImpl.cleanCache(application)                                                                              
        }                                                                                                                   
        if (crashPortray.finishPage == 1) {                                                                                 
            actionImpl.finishCurrentPage()                                                                                  
        }                                                                                                                   
        if (crashPortray.toast.isNotEmpty()) {                                                                              
            actionImpl.showToast(application, crashPortray.toast)                                                           
        }                                                                                                                   
        return true                                                                                                         
    }                                                                                                                       
    return false                                                                                                            
}                                                                                                                           
```

# 实现保护，looper 兜底
```
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
```
