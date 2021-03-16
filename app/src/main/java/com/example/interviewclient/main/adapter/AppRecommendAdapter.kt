package com.example.interviewclient.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.interviewclient.R
import com.example.interviewclient.bean.AppInfo
import com.example.interviewclient.bean.RecommendInfo
import com.example.interviewclient.util.extensions.loadImage

/**
 * @author qiuyunfei
 * @date 2021/3/15 0015
 * @description
 */
class AppRecommendAdapter(private val context: Context?, val onInstallClick: (Int) -> Unit) :
    RecyclerView.Adapter<AppRecommendAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_app_info, parent, false)
        return ViewHolder(view)
    }

    var appInfo: List<RecommendInfo>? = null

    override fun getItemCount(): Int = appInfo?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = appInfo?.get(position)
        holder.run {
            icon.loadImage(data?.icon ?: "")
            appName.text = data?.appName
            appVersion.text = data?.version
            appInstall.visibility = VISIBLE
            appInstall.setOnClickListener {
                onInstallClick(position)
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: AppCompatImageView = itemView.findViewById(R.id.app_detail_icon)
        val appName: AppCompatTextView = itemView.findViewById(R.id.app_info_name)
        val appVersion: AppCompatTextView = itemView.findViewById(R.id.app_info_version)
        val appInstall: AppCompatButton = itemView.findViewById(R.id.app_info_install)
    }

}