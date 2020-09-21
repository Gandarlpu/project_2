package com.example.prototype.Zzim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.prototype.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_zzim.*

private val db = FirebaseFirestore.getInstance() //DB받아오기
private lateinit var auth : FirebaseAuth //계정받아오기

class ZzimActivity : AppCompatActivity() {

    val array_list = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zzim)

        auth = FirebaseAuth.getInstance()

        db.collection("users")
            .document(auth.currentUser?.uid.toString())
            .get()
            .addOnSuccessListener { documentSnapshot ->

                if(documentSnapshot.get("zzim") != ""){
                    array_list.add(documentSnapshot.get("zzim").toString())
                }

                val zzimAdapter = ZzimAdapter(this , array_list)
                zzim_listview.adapter = zzimAdapter

            }
            .addOnFailureListener {  }
    }
}
