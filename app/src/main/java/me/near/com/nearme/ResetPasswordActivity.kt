package me.near.com.nearme

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPasswordActivity : AppCompatActivity() {

    private var auth: FirebaseAuth? = null
    var pDialog: SweetAlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        initializeData()
        setOnClickListeners()
    }

    private fun initializeData() {
        auth = FirebaseAuth.getInstance()

        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }

        pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        pDialog!!.progressHelper.barColor = Color.parseColor("#00bcd4")
        pDialog!!.titleText = "Loading"
        pDialog!!.setCancelable(false)
    }

    private fun setOnClickListeners() {
        resetPasswordButton!!.setOnClickListener(View.OnClickListener {
            val email = resetPasswordButton!!.text.toString().trim { it <= ' ' }

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(application, "Enter your registered email id", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            pDialog!!.show()
            auth!!.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this@ResetPasswordActivity,
                            "We have sent you instructions to reset your password!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@ResetPasswordActivity,
                            "Failed to send reset email!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    pDialog!!.cancel()
                }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}

