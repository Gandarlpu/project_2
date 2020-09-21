package com.example.prototype.Write

import android.content.Intent
import android.net.Uri
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
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_write.*
import java.io.File

class WriteActivity : AppCompatActivity() {

    //lateinit늦은 초기화 = 선언과 동시에 초기화 , 즉 데이터를 입력하지 않고 밑에서 써줘도 됨
    private lateinit var user_id : String //DB에 id 받아오기
    private lateinit var auth : FirebaseAuth //계정받아오기
    private val db = FirebaseFirestore.getInstance() //DB받아오기
    private lateinit var DB_year : String
    private lateinit var DB_month : String
    private lateinit var DB_day : String
    private lateinit var formatted : String

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write)

        //계정 get
        auth = FirebaseAuth.getInstance()

        //사진업로드 구현
        write_picture.setOnClickListener{

            //storage받아오기
            val ref = FirebaseStorage.getInstance().getReference("image_1.jpg")

            ref.downloadUrl
                .addOnCompleteListener{ task ->

                    //이미지 다운 성공
                    if(task.isSuccessful){
                        Toast.makeText(this , "글쓰기 성공" , Toast.LENGTH_LONG).show()
                        //이미지 다운받아서 붙여넣기
                        /*Glide.with(this)
                            .load(task.result)
                            .into()//레이아웃 이미지 id*/

                        //이미지 다운 실패
                    }else{

                        Toast.makeText(this , "글쓰기 실패" , Toast.LENGTH_LONG).show()

                    }

                }
        }

        //켈린더 데이터값 받아오기
        //아직 메인에 켈린더가 없어서 임시로 년월일을 받아오는거 구현한것
        if(intent.hasExtra("Cal_List")){
            //Cal_List변수를 초기화할 때 자료형이 정해지지 않아서
            //getParcelableExtraq뒤에 <>가 생기고 이곳에서 자료형을 입력한다.
            var Cal_List = intent.getParcelableExtra<DB_Calendar>("Cal_List")

            DB_year = Cal_List.year.toString()
            DB_month = Cal_List.month.toString()
            DB_day = Cal_List.day.toString()
            formatted = Cal_List.formatted.toString()

        }

        //DB에 아이디값 받아오기위해 DB에서 불러온 후 user_id에 넣기
        val docRef =
            db.collection("users")
                .document(auth.currentUser?.uid.toString())

        docRef.get().addOnSuccessListener { documentSnapshot->

            user_id = documentSnapshot.get("id").toString()

        }

        //글쓰기 구현
        write_save.setOnClickListener {
            val form = hashMapOf(

                "id" to user_id,
                "title" to write_title.text.toString(),
                "content" to write_content.text.toString()

            )
            db.collection("Year")
                .document(DB_year)
                .collection("Month")
                .document(DB_month)
                .collection("Day")
                .document(DB_day)
                .collection("write")
                .document(formatted)
                .set(form)
                .addOnSuccessListener {

                    Toast.makeText(this, "글쓰기 성공", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, ShowActivity::class.java)
                    var Cal_List = DB_Calendar(DB_year, DB_month, DB_day , formatted)

                    intent.putExtra("Cal_List", Cal_List)
                    startActivity(intent)

                }
                .addOnFailureListener {

                    Toast.makeText(this, "글쓰기 실패", Toast.LENGTH_LONG).show()

                }
        }

        //취소
        write_cancel.setOnClickListener{

            finish()

        }

    }
}
