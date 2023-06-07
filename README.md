# ESCaleDemo_Droid11Kt
麦赛科技电子秤Android11主板称重SDK使用DEMO.此DEMO主要针对新主板的新安卓版本设计.
可以兼容老主板.老主板使用注意引用额外的so库和jar包.(老主板使用DEMO可见:https://github.com/YuAJio/EScaleDemo)

使用须知:
如果调用sdk中的方法无响应或回调,需要在本地项目中添加迭代器注入;
注入方法:
如同demo中:在/app/src/main/resources目录下添加META-INF.services/com.mysafe.escale_calisdk.process.IProcess 文件

使用sdk需要使用第三方json解析框架 Gson 的引用
 implementation 'com.google.code.gson:gson:2.8.6'
如果项目结构和此引用冲突,可以联系开发人员
