package com.example.prototype.change

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.prototype.MainActivity
import com.example.prototype.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_id.*
import kotlinx.android.synthetic.main.activity_id.com_bt
import kotlinx.android.synthetic.main.activity_pass.*

class PassActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    private lateinit var id : String
    private lateinit var password : String
    private lateinit var name : String
    private lateinit var phone : String
    private lateinit var nickname : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pass)

        //stan_id / change_id / com_bt
        auth = FirebaseAuth.getInstance()

        val docRef =
            db.collection("users")
                .document(auth.currentUser?.uid.toString())

        docRef.get().addOnSuccessListener { documentSnapshot->

            id = documentSnapshot.get("id").toString()
            password = documentSnapshot.get("password").toString()
            name = documentSnapshot.get("name").toString()
            phone = documentSnapshot.get("phone").toString()
            nickname = documentSnapshot.get("nickname").toString()

        }

        //덮어쓰기
        com_bt.setOnClickListener {
            val Edit_change_pass = change_pass.text.toString()
            val Edit_change_pass2 = change_pass2.text.toString()

            if(Edit_change_pass == Edit_change_pass2){
                //println("아이디 : " + id + "changeID : " + Edit_change_id)
                val form = hashMapOf(

                    "id" to id,
                    "name" to name,
                    "nickname" to nickname,
                    "password" to Edit_change_pass,
                    "phone" to phone

                )
                db.collection("users")
                    .document(auth.currentUser?.uid.toString())
                    .set(form)
                    .addOnSuccessListener {

                        Toast.makeText(this , "변경되었습니다." , Toast.LENGTH_LONG).show()
                        val intent = Intent(this , MainActivity::class.java)
                        startActivity(intent)

                    }
                    .addOnFailureListener {

                        Toast.makeText(this , "실패하였습니다." , Toast.LENGTH_LONG).show()

                    }

            }else{
                Toast.makeText(this , "아이디를 다시 확인하세요." , Toast.LENGTH_LONG).show()
            }//if
        }

    }
}
