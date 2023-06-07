package com.mysafe.escalektdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mysafe.escale_calisdk.EScaleController
import com.mysafe.escale_calisdk.enums.InitStateCode
import com.mysafe.escale_calisdk.interfaces.IOnHttpResult
import com.mysafe.escalektdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding?.acty = this
    }

    /**
     * 激活SDK
     */
    fun clickEventActive() {
        EScaleController.getInstance()
            .active(this, "1e05764f-ce7b-4a62-9d4b-a6133f335b80", object : IOnHttpResult {
                override fun onSuccess(message: String?) {
                    binding?.canInit = true
                }

                override fun onError(errorCode: Int, errorMsg: String?) {
                    binding?.canInit = false
                    binding?.weightStr = errorMsg
                }
            })
    }

    /**
     * 初始化SDK
     */
    fun clickEventInit() {
        val code = EScaleController.getInstance().init(this, "TestUseTag")
        if (code == InitStateCode.Succeed) {
            EScaleController.getInstance().openSensor(null)
            EScaleController.getInstance().setWeightingReceiver {
                binding?.weightStr = it.Weight.toString()
            }
        } else
            binding?.weightStr = "初始化失败,失败代号:${code}"
    }

    /**
     * 开关闪光灯
     */
    fun changeFlashLight() {
        if (binding?.isFlashOff == false)
            EScaleController.getInstance().flashLightOn()
        else
            EScaleController.getInstance().flashLightOff()
    }

    /**
     * 以当前秤盘的重量设置为零点值,俗称_置零
     */
    fun setZero() {
        EScaleController.getInstance().setZeroWeight()
    }

    /**
     * 获取当前的零点值
     */
    fun getZero() {
        val zeroPoint = EScaleController.getInstance().zeroWeight
    }
}