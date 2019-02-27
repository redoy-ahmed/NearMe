package me.near.com.nearme

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_signup.*


class SignupActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private var auth: FirebaseAuth? = null

    var listOfCountry = arrayOf("Country1", "Country2", "Country3")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance()

        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, listOfCountry)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        currentCountrySpinner!!.adapter = aa
        countryOfResidenceSpinner!!.adapter = aa

        /*btnSignIn!!.setOnClickListener { finish() }

        btnSignUp!!.setOnClickListener(View.OnClickListener {
            val email = inputEmail!!.text.toString().trim { it <= ' ' }
            val password = inputPassword!!.text.toString().trim { it <= ' ' }

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(applicationContext, "Enter email address!", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(
                    applicationContext,
                    "Password too short, enter minimum 6 characters!",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }

            //create user
            auth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    this@SignupActivity
                ) { task ->
                    Toast.makeText(
                        this@SignupActivity,
                        "createUserWithEmail:onComplete:" + task.isSuccessful,
                        Toast.LENGTH_SHORT
                    ).show()
                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful) {
                        Toast.makeText(
                            this@SignupActivity, "Authentication failed." + task.exception!!,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        startActivity(Intent(this@SignupActivity, MainActivity::class.java))
                        finish()
                    }
                }
        })*/
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
