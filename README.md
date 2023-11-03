# AndroidCrashProtect
实现 looper 兜底机制的 demo，支持远程下载 crash 兜底的配置文件，出现 crash 时，通过对异常各种信息、app 和系统版本信息等来实现崩溃画像匹配，保护住之后还可以进行一些自定义操作，如 toast、清除缓存等
                                                     
外部需要：                          
1. 实现 IApp 接口，提供相应操作的实现             
2. 在 Application#onCreate() 中调用 CrashPortrayHelper#init
3. 自定义配置文件的下载、缓存、读取逻辑

实现思路参考：[Android稳定性：可远程配置化的Looper兜底框架](https://juejin.cn/post/7198466997288566842)



个人掘金主页：[邹阿涛涛涛涛涛涛的个人主页](https://juejin.cn/user/3808364009106839/posts)
