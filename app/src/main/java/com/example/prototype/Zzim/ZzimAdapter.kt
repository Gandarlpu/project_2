package com.example.prototype.Zzim

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.prototype.R
import kotlinx.android.synthetic.main.zzim_item.view.*

class ZzimAdapter (val context : Context, val list_array : ArrayList<String>) : BaseAdapter(){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view : View = LayoutInflater.from(context).inflate(R.layout.zzim_item , null)

        view.zzim_text.text = list_array.get(position)

        return view

    }

    override fun getItem(position: Int): Any {
        return 0
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return list_array.size
    }


}