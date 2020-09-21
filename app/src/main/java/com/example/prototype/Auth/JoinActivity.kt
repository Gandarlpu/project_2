package com.example.prototype.Auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.prototype.MainActivity
import com.example.prototype.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_join.*

class JoinActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        auth = FirebaseAuth.getInstance()

        join_login_button.setOnClickListener{
            //회원가입
            auth.createUserWithEmailAndPassword(join_email_area.text.toString(), join_password_area.text.toString())
                .addOnCompleteListener(this){ task->

                    if(task.isSuccessful){
                        //가입성공
                        val user = hashMapOf(

                            "id" to join_email_area.text.toString(),
                            "password" to join_password_area.text.toString(),
                            "name" to join_name_area.text.toString(),
                            "phone" to join_phone_area.text.toString(),
                            "nickname" to join_nickname_area.text.toString()
                        )
                        //데이터베이스 경로설정
                        db.collection("users")
                            .document(auth.currentUser?.uid.toString())
                                /*join*/
                            .set(user)
                            .addOnSuccessListener {

                                Toast.makeText(this, "가입성공" , Toast.LENGTH_LONG).show()

                                val intent = Intent(this , MainActivity::class.java)
                                startActivity(intent)

                            }
                            .addOnFailureListener {

                                Toast.makeText(this , "DB실패" , Toast.LENGTH_LONG).show()

                            }

                    }else{
                        //가입실패
                        Toast.makeText(this, "Fail" , Toast.LENGTH_LONG).show()

                    }
                }
        }

    }
}