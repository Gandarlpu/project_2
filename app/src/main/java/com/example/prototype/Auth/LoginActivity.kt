package com.example.prototype.Auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.prototype.MainActivity
import com.example.prototype.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        login_button.setOnClickListener{

            auth.signInWithEmailAndPassword(email_area.text.toString() , password_area.text.toString())
                .addOnCompleteListener(this) {task->
                    if(task.isSuccessful){
                        //로그인 성공시
                        auth.currentUser
                        val intent = Intent(this , MainActivity::class.java)
                        startActivity(intent)

                    }else{
                        //실패 = 회원이 아닐때
                        Toast.makeText(this , "가입되지 않은 정보입니다." , Toast.LENGTH_LONG).show()

                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this , "실패" , Toast.LENGTH_LONG).show()
                }

        }

        join_button.setOnClickListener{
            //회원가입
            val intent = Intent(this , JoinActivity::class.java)
            startActivity(intent)

        }

    }
}
