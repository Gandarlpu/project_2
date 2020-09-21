package com.example.prototype.Write

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.prototype.DB_Calendar
import com.example.prototype.MainActivity
import com.example.prototype.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_my_info.*
import kotlinx.android.synthetic.main.activity_show.*

class ShowActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth //계정받아오기
    private val db = FirebaseFirestore.getInstance() //DB받아오기

    private lateinit var DB_year : String
    private lateinit var DB_month : String
    private lateinit var DB_day : String
    private lateinit var formatted : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)

        auth = FirebaseAuth.getInstance()

        if(intent.hasExtra("Cal_List")){

            //Cal_List변수를 초기화할 때 자료형이 정해지지 않아서
            //getParcelableExtraq뒤에 <>가 생기고 이곳에서 자료형을 입력한다.
            var Cal_List = intent.getParcelableExtra<DB_Calendar>("Cal_List")

            DB_year = Cal_List.year.toString()
            DB_month = Cal_List.month.toString()
            DB_day = Cal_List.day.toString()
            formatted = Cal_List.formatted.toString()
        }

        //계정 get
        auth = FirebaseAuth.getInstance()
        //DB의 users경로 받아오기
        val docRef =
            db.collection("Year")
                .document(DB_year)
                .collection("Month")
                .document(DB_month)
                .collection("Day")
                .document(DB_day)
                .collection("write")
                .document(formatted)

        //DB뿌려주기
        docRef.get().addOnSuccessListener{ documentSnapshot->

            show_title.setText(documentSnapshot.get("title").toString())
            show_content.setText(documentSnapshot.get("content").toString())

        }

        //찜구현
        show_zzim.setOnClickListener {

            //이미 찜 되어있을 떄
            if (show_zzim.text.equals("즐겨찾기 완료")) {
                show_zzim.text = "즐겨찾기"
                show_zzim.setTextColor(Color.RED)

                db.collection("users")
                    .document(auth.currentUser?.uid.toString())
                    .update("zzim", "")
                    .addOnSuccessListener { Toast.makeText(this, "성공", Toast.LENGTH_SHORT).show() }
                    .addOnFailureListener { Toast.makeText(this, "실패", Toast.LENGTH_SHORT).show() }


            } else {
                //찜 안되잇을 때
                val show_title = show_title.text

                show_zzim.text = "즐겨찾기 완료"
                show_zzim.setTextColor(Color.BLUE)

                db.collection("users")
                    .document(auth.currentUser?.uid.toString())
                    .update("zzim", show_title.toString())
                    .addOnSuccessListener { Toast.makeText(this, "성공", Toast.LENGTH_SHORT).show() }
                    .addOnFailureListener { Toast.makeText(this, "실패", Toast.LENGTH_SHORT).show() }

            }
        }

        //이미지 다운로드
        val ref = FirebaseStorage.getInstance().getReference("image_1.jpg")

        ref.downloadUrl
            .addOnCompleteListener{ task ->

                //이미지 다운 성공
                if(task.isSuccessful){
                    Toast.makeText(this , "다운로드 성공" , Toast.LENGTH_LONG).show()
                    //이미지 다운받아서 붙여넣기
                    Glide.with(this)
                        .load(task.result)
                        .into(show_picture)//레이아웃 이미지 id

                    //이미지 다운 실패
                }else{
                    Toast.makeText(this , "다운로드 실패" , Toast.LENGTH_LONG).show()
                }

            }

        complete.setOnClickListener {

            val intent = Intent(this , MainActivity::class.java)
            startActivity(intent)

        }


    }
}
