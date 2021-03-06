package com.example.ttymyday.view.adapter

import android.content.Context
import android.provider.ContactsContract
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.ttymyday.R
import com.example.ttymyday.data.DataSource
import com.example.ttymyday.listener.OnRItemClickListener
import com.example.ttymyday.model.ScheduleTag
import com.example.ttymyday.view.converter.IconConverter
import kotlinx.android.synthetic.main.sample_schedule_tag_view.view.*

class ScheduleTagAdapter(var tags:ArrayList<ScheduleTag>,var converter:IconConverter):RecyclerView.Adapter<ScheduleTagAdapter.ViewHolder>(){
    //lateinit var tags:ArrayList<ScheduleTag>
    var mListener: OnRItemClickListener? = null

    lateinit var context: Context

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        context = p0.context
        return ViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.sample_schedule_tag_view,p0,false),mListener)
    }

    override fun getItemCount(): Int {
        return tags.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val tag = tags[p1]
        //WARING 为了兼容智能清单而加入的判断
        p0.imgIcon.setImageResource(converter.getIconRes(tag.icon!!))
        p0.tbxTitle.text = tag.title
        if(tag.extra_tag == null){
            if (tag.alarm_count > 0){
                //p0.imgAlarm.visibility = View.VISIBLE
                p0.tbxAlarmCount.text = if(tag.alarm_count > 99) "99+" else tag.alarm_count.toString()
                p0.tbxAlarmCount.visibility = View.VISIBLE
            } else{
                //p0.imgAlarm.visibility = View.INVISIBLE
                p0.tbxAlarmCount.visibility = View.INVISIBLE
            }
        }else{
            when(tag.extra_tag){
                "clockIn"->{
                    if (DataSource.clockInCompleted == 0 && DataSource.clockInAll == 0){
                        p0.tbxAlarmCount.visibility = View.INVISIBLE
                    } else {
                        p0.tbxAlarmCount.text = "${DataSource.clockInCompleted}/${DataSource.clockInAll}"
                        p0.tbxAlarmCount.visibility = View.VISIBLE
                    }
                }
                "memory"->{
                    p0.tbxAlarmCount.text = DataSource.displayMemory
                }
                "important"->{
                    if (DataSource.importantCount == 0){
                        p0.tbxAlarmCount.visibility = View.INVISIBLE
                    } else{
                        p0.tbxAlarmCount.text = DataSource.importantCount.toString()
                        p0.tbxAlarmCount.visibility = View.VISIBLE
                    }
                }
                "table"->{
                    p0.tbxAlarmCount.text = DataSource.displayTable
                }
            }
        }

    }

    fun setOnRItemClickListener(listener: OnRItemClickListener){
        mListener = listener;
    }

    class ViewHolder(v: View,var listener: OnRItemClickListener?):RecyclerView.ViewHolder(v),View.OnClickListener{
        override fun onClick(v: View?) {
            listener?.onItemClick(v,layoutPosition)
        }

        //SOLVED BUG 解决了点击无事件的问题
        init {
            v.setOnClickListener(this)
        }

        var root:LinearLayout = v.rootView as LinearLayout
        var imgIcon:ImageView = v.img_icon_schedule_tag
        var tbxTitle:TextView = v.tbx_title_schedule_tag
        //var imgAlarm:ImageView = v.img_alarm_schedule_tag
        var tbxAlarmCount:TextView = v.tbx_alarm_count_schedule_tag
    }
}