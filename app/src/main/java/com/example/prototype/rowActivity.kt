package com.example.prototype

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View.inflate
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.prototype.Write.WriteActivity
import com.example.prototype.change.IdActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_row.*
import kotlinx.android.synthetic.main.row_list.*
import java.lang.NumberFormatException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class rowActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth //계정받아오기
    private val db = FirebaseFirestore.getInstance() //DB받아오기

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_row)

        val cal = Calendar.getInstance()
        val cal_2 = Calendar.getInstance().getTime()

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss")
        val formatted = current.format(formatter)

        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val dayFormat = SimpleDateFormat("d" , Locale.getDefault())
        //현재 일자
        val today = dayFormat.format(cal_2)

        cal.set(year,month+1,1)

        //년,월 출력
        year_text.setText(cal.get(Calendar.YEAR).toString() + "년")
        month_text.setText(cal.get(Calendar.MONTH).toString() + "월")


        //일 출력
        val data = Array(day , {i -> (i+1).toString()}) //day만큼의 배열을 i로 초기화
        //람다식으로 i는 0부터 day까지 순서대로 초기화

        var adapter = ListViewAdapter(this , data)
        listView.adapter = adapter

        //DB뿌려주기
        var str : Array<String> = Array(5 , {i->i.toString()})
        var i = 0
        var items : String = ""
        test_bt.setOnClickListener {
                    db.collection("Year")
                        .document(year.toString())
                        .collection("Month")
                        .document((month + 1).toString())
                        .collection("Day")
                        .document(today)
                        .collection("write")
                        .get()
                        .addOnSuccessListener { result ->
                            Toast.makeText(
                                this,
                                "년" + year + "월" + (month + 1) + "일" + today,
                                Toast.LENGTH_SHORT
                            ).show()
                            for (document in result) { //해당 day만 받아옴
                                println("일${document.id} => ${document.data}")

                                var arr = document.data
                                str[i] = parse(arr.toString()) //parse함수
                                //배열초기화해워쟈아함
                                println("str배열 " + str[i])

                                items = items + str[i] + "\n"
                                println("아이템 : " + items)
                                i++

                            }//for
                            var text: TextView? = findViewById<TextView>(R.id.textView_content)
                            text?.setText(items)
                            println("최종 : " + items)

                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show()
                        }
                    //println("${document.id} => ${document.data}")
                    //2020-05-25-06:09:22 => {id=phs811@naver.com, title=asdfasdf, content=asdfasdf}
                    //textView_content.setText((document.data).toString())

        }


        listView.setOnItemClickListener { adapterView , view , i , l ->
            //i번쨰 데이터를 클릭하면 출력
            //textView.text = data[i]
            Toast.makeText(this , "년"+year+"월"+(month+1)+"일"+data[i] , Toast.LENGTH_SHORT).show()

            //관리자 글쓰기
            var Cal_List = DB_Calendar(year.toString(), (month+1).toString(), data[i].toString() , formatted.toString())

            val popupMenu = PopupMenu(this , view)
            popupMenu.setOnMenuItemClickListener { item ->
                when(item.itemId){
                    R.id.write_menu -> {
                        val intent = Intent(this, WriteActivity::class.java)
                        intent.putExtra("Cal_List", Cal_List)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
            //context메뉴로 바꾸기
            //밑에서 올라오는 애니메이션으로 타이틀을 넣어줄것
            popupMenu.inflate(R.menu.row_popup_menu)

            popupMenu.show()

        }

    }

    //문자열 자르기 함수
    fun parse(arr : String) : String {

        var str = arr.split(",") //[title=sorane]
        var title_str : String = str[1].split("title=").toString() //[ , sojsidn]
        var title_str2 : String = title_str.split(",").toString()
        println("자른 문자 : " + title_str2) //[{id=phs811@naver.com,  title=sovnsjdfk,  content=aejrkev}]

        return title_str
    }

}


