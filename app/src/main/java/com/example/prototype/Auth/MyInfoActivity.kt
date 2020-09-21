package com.example.prototype.Auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.prototype.MainActivity
import com.example.prototype.R
import com.example.prototype.Zzim.ZzimActivity
import com.example.prototype.Zzim.ZzimAdapter
import com.example.prototype.change.IdActivity
import com.example.prototype.change.NickActivity
import com.example.prototype.change.PassActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_my_info.*

class MyInfoActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_info)

        auth = FirebaseAuth.getInstance()

        val doRef = db.collection("users")
            .document(/*"join"*/auth.currentUser?.uid.toString())

        doRef.get().addOnSuccessListener{ documentSnapshot->

            nickname_area.setText(documentSnapshot.get("nickname").toString())

        }

        //로그아웃
        //로그인하고 다시 알람들어가면 자동로그아웃됨.
        logout_bt.setOnClickListener {

            val builder = AlertDialog.Builder(
                ContextThemeWrapper
                    (this, R.style.Theme_AppCompat_Light_Dialog)
            )

            builder.setTitle("로그아웃")
            builder.setMessage("로그아웃 하시겠습니까?")
            builder.setPositiveButton("로그아웃") { dialog, which ->
                Toast.makeText(this, "완료", Toast.LENGTH_LONG).show()

                val intent = Intent(this , MainActivity::class.java)
                startActivity(intent)
            }
            builder.setNegativeButton("취소") { dialog, which ->
                Toast.makeText(this, "취소", Toast.LENGTH_LONG).show()
            }

            builder.show()

            FirebaseAuth.getInstance().signOut()


        }

        change.setOnClickListener {it ->

            val popupMenu = PopupMenu(this , it)
            popupMenu.setOnMenuItemClickListener { item ->
                when(item.itemId){
                    R.id.id_change -> {
                        val intent = Intent(this , IdActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.pass_change -> {
                        val intent = Intent(this , PassActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.nick_change -> {
                        val intent = Intent(this , NickActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }

            popupMenu.inflate(R.menu.popupmenu)

            popupMenu.show()

        }


        //찜 목록
        zzim.setOnClickListener {

            val intent = Intent(this , ZzimActivity::class.java)
            startActivity(intent)

        }
    }
}
