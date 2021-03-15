package com.example.interviewclient.util

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.provider.Settings


object PermissionUtil {

    /**
     * 请求 PACKAGE_USAGE_STATS 权限，使用自带的权限查询，有很大可能是显示无权限
     */
    fun requestPermission(context: Context) {
        val ad: AlertDialog = AlertDialog.Builder(context)
            .setTitle("警告")
            .setMessage("缺少权限：permission.PACKAGE_USAGE_STATS\n需要在\"设置>安全\"中给应用提供权限")
            .setPositiveButton("设置") { dialog, _ ->
                val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                context.startActivity(intent)
                dialog.dismiss()
            }
            .setNegativeButton("拒绝") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
        ad.show()
    }
}