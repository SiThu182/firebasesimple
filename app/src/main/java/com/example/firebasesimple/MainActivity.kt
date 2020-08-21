package com.example.firebasesimple

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.example.firebasesimple.model.Person
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var dbReference : DatabaseReference
    private lateinit var firebaseDatabase : FirebaseDatabase
    private var userId :String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseDatabase = FirebaseDatabase.getInstance()
        dbReference = firebaseDatabase.getReference("users")

        userId = dbReference.push().key.toString()


        btnadd.setOnClickListener {
            var name = edt_name.text.toString()

            var phone = edt_phone.text.toString().toInt()

            if(TextUtils.isEmpty(userId)){
                createUser(name,phone)
            }else{
                updateUser(name,phone)
            }

        }

        btnUpdate.setOnClickListener {
            addUserChange()
        }


    }

    private fun updateUser(name: String, phone: Int) {
        if (!TextUtils.isEmpty(name)){
            dbReference.child(userId).child("name").setValue(name)
        }
        if (!TextUtils.isEmpty(phone.toString())){
            dbReference.child(userId).child("phone").setValue(phone)
        }
        showPersron()
        edt_name.setText("")
        edt_phone.setText("")

        changeButtonText()

      

    }

    private fun createUser(s: String, s1: Int) {
        val person = Person(s,s1)
        dbReference.child(userId).setValue(person)

        showPersron()




    }

    fun addUserChange(){
        dbReference.child(userId).addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                var users = snapshot.getValue(Person::class.java)

                edt_name.setText(users?.name)
                edt_phone.setText(users?.phone.toString())



            }

        })


    }

    private fun changeButtonText(){
        if (TextUtils.isEmpty(userId)) {
            btnadd.text = "Save";
        } else {
            btnadd.text = "Update";
        }
    }

    fun showPersron(){
        dbReference.child(userId).addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                var users = snapshot.getValue(Person::class.java)
                txtName.setText(users?.name)
                txtphone.setText(users?.phone.toString())
            }

        })
    }
}