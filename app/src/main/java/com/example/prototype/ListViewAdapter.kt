package com.example.prototype


import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class ListViewAdapter(val context : Context , val data : Array<String>) : BaseAdapter(){
    override fun getView(position: Int, convertView : View?, parent: ViewGroup?): View? {
        //항목 1개를 구성하기위해 호출 반환된걸로 항목을 구성
        //스크롤에서 내리면 안보이는뷰가 생기는데 이것을 버리지않고 2번째 매개변수에 저장
        var convertView : View? = convertView

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.row_list , null)
        }

        var text : TextView?= convertView?.findViewById<TextView>(R.id.day_text)

        text?.text = data[position]

        return convertView
    }

    override fun getItem(position: Int): Any { //아이템을 반환
        return data[position]
    }

    override fun getItemId(position: Int): Long {//항목을 대표하는걸 반환
        return 0
    }

    override fun getCount(): Int { //리스트 항목 갯수를 리턴

        return data.size
    }


}