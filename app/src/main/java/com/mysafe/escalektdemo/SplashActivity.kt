package com.mysafe.escalektdemo

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermissions()
    }

    private fun toMainActy() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    //region 请求权限相关
    private fun checkPermissions() {
        if (!isStoragePermissionGranted()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                toOpenMediaStoragePermission()
            else
                requestPermissions()
        } else
            requestPermissions()
    }

    private fun isStoragePermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            return Environment.isExternalStorageManager()
        else {
            val readPermission =
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            val writePermission =
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            return readPermission == PackageManager.PERMISSION_GRANTED && writePermission == PackageManager.PERMISSION_GRANTED;
        }
    }


    @RequiresApi(Build.VERSION_CODES.R)
    private fun toOpenMediaStoragePermission() {
        // 定义一个ActivityResultContract对象，用于执行intent
        val requestPermission = object : ActivityResultContract<Void, Boolean>() {
            @RequiresApi(Build.VERSION_CODES.R)
            override fun createIntent(context: Context, input: Void?): Intent {
                return Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                    data = Uri.parse("package:${context.packageName}")
                }
            }

            @RequiresApi(Build.VERSION_CODES.R)
            override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
                // 返回是否获得了权限
                return Environment.isExternalStorageManager()
            }
        }

        // 定义一个ActivityResultCallback对象，用于接收结果
        val permissionResult = ActivityResultCallback<Boolean> { result ->
            // 根据结果做相应的处理
            checkPermissions()
        }
        // 注册一个ActivityResultLauncher对象，传入上面定义的对象
        val launcher = registerForActivityResult(requestPermission, permissionResult)
        // 在需要请求权限的时候，调用launch()方法，传入null作为参数
        launcher.launch(null)
    }

    private fun requestPermissions() {
        println("Yurishi_进入请求权限方法")
        val permissions = arrayListOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R)
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val deniedPermissions = arrayListOf<String>()
        permissions.forEach {
            if (ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_DENIED)
                deniedPermissions.add(it)
        }
        if (deniedPermissions.any())
        {
            println("Yurishi_存在未授权的权限${deniedPermissions.joinToString(",")}")
            requestPermissions(deniedPermissions.toTypedArray(), 0x114)
        }
         else
        {
            println("Yurishi_前往下个页面")
            toMainActy()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        checkPermissions()
    }
    //endregion

}