package me.near.com.nearme

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private var auth: FirebaseAuth? = null
    lateinit var pDialog: SweetAlertDialog
    var user: User? = null
    private val TAG = LoginActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        if (auth!!.currentUser != null) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }

        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        setOnClickListeners()
    }

    private fun setOnClickListeners() {

        signupButton!!.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
        }

        resetPasswordButton!!.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ResetPasswordActivity::class.java))
        }

        loginButton!!.setOnClickListener(View.OnClickListener {
            val email = emailEditText!!.text.toString()
            val password = passwordEditText!!.text.toString()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(applicationContext, "Enter email address!", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
            pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
            pDialog.titleText = "Loading"
            pDialog.setCancelable(false)
            pDialog.show()

            auth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    this@LoginActivity
                ) { task ->
                    pDialog.cancel()
                    if (!task.isSuccessful) {
                        if (password.length < 6) {
                            passwordEditText!!.error = getString(R.string.minimum_password)
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                getString(R.string.auth_failed),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        saveDataIntoPreference(email)
                    }
                }
        })
    }

    private fun saveDataIntoPreference(email: String) {

        pDialog.show()

        val sharedPreference = getSharedPreferences("NEAR_ME", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()

        val database = FirebaseDatabase.getInstance().reference
        val ref = database.child("users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (singleSnapshot in dataSnapshot.children) {
                    user = singleSnapshot.getValue(User::class.java)
                    if (user!!.email == email) {

                        editor.putString("cfirstName", user!!.firstName)
                        editor.putString("clastName", user!!.lastName)
                        editor.putString("cemail", user!!.email)
                        editor.putString("cpassword", user!!.password)
                        editor.putString("ccellPhone", user!!.cellPhone)
                        editor.putString("ccurrentCountry", user!!.currentCountry)
                        editor.putString("ccountryOfResidence", user!!.countryOfResidence)
                        editor.putString("cjobTitle", user!!.jobTitle)
                        editor.commit()

                        pDialog.cancel()

                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException())
            }
        })
    }
}