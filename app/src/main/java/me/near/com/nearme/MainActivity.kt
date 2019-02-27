package me.near.com.nearme

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.content.Intent
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import java.nio.file.Files.delete
import android.R
import android.widget.ProgressBar
import android.widget.EditText
import android.R.attr.password
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Button
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private var btnChangeEmail: Button? = null
    private var btnChangePassword: Button? = null
    private var btnSendResetEmail: Button? = null
    private var btnRemoveUser: Button? = null
    private var changeEmail: Button? = null
    private var changePassword: Button? = null
    private var sendEmail: Button? = null
    private var remove: Button? = null
    private var signOut: Button? = null

    private var oldEmail: EditText? = null
    private var newEmail: EditText? = null
    private var password: EditText? = null
    private var newPassword: EditText? = null
    private var progressBar: ProgressBar? = null
    private var authListener: FirebaseAuth.AuthStateListener? = null
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById(R.id.toolbar)
        toolbar.setTitle(getString(R.string.app_name))
        setSupportActionBar(toolbar)

        //get firebase auth instance
        auth = FirebaseAuth.getInstance()

        //get current user
        val user = FirebaseAuth.getInstance().currentUser

        authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {
                // user auth state is changed - user is null
                // launch login activity
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
        }

        btnChangeEmail = findViewById(R.id.change_email_button)
        btnChangePassword = findViewById(R.id.change_password_button)
        btnSendResetEmail = findViewById(R.id.sending_pass_reset_button)
        btnRemoveUser = findViewById(R.id.remove_user_button)
        changeEmail = findViewById(R.id.changeEmail)
        changePassword = findViewById(R.id.changePass)
        sendEmail = findViewById(R.id.send)
        remove = findViewById(R.id.remove)
        signOut = findViewById(R.id.sign_out)

        oldEmail = findViewById(R.id.old_email)
        newEmail = findViewById(R.id.new_email)
        password = findViewById(R.id.password)
        newPassword = findViewById(R.id.newPassword)

        oldEmail!!.visibility = View.GONE
        newEmail!!.visibility = View.GONE
        password!!.visibility = View.GONE
        newPassword!!.visibility = View.GONE
        changeEmail!!.visibility = View.GONE
        changePassword!!.visibility = View.GONE
        sendEmail!!.visibility = View.GONE
        remove!!.visibility = View.GONE

        progressBar = findViewById(R.id.progressBar)

        if (progressBar != null) {
            progressBar!!.visibility = View.GONE
        }

        btnChangeEmail!!.setOnClickListener(View.OnClickListener {
            oldEmail!!.visibility = View.GONE
            newEmail!!.visibility = View.VISIBLE
            password!!.visibility = View.GONE
            newPassword!!.visibility = View.GONE
            changeEmail!!.setVisibility(View.VISIBLE)
            changePassword!!.setVisibility(View.GONE)
            sendEmail!!.setVisibility(View.GONE)
            remove!!.setVisibility(View.GONE)
        })

        changeEmail!!.setOnClickListener(View.OnClickListener {
            progressBar!!.visibility = View.VISIBLE
            if (user != null && newEmail!!.text.toString().trim { it <= ' ' } != "") {
                user.updateEmail(newEmail!!.text.toString().trim { it <= ' ' })
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this@MainActivity,
                                "Email address is updated. Please sign in with new email id!",
                                Toast.LENGTH_LONG
                            ).show()
                            signOut()
                            progressBar!!.visibility = View.GONE
                        } else {
                            Toast.makeText(this@MainActivity, "Failed to update email!", Toast.LENGTH_LONG).show()
                            progressBar!!.visibility = View.GONE
                        }
                    }
            } else if (newEmail!!.text.toString().trim { it <= ' ' } == "") {
                newEmail!!.error = "Enter email"
                progressBar!!.visibility = View.GONE
            }
        })

        btnChangePassword!!.setOnClickListener(View.OnClickListener {
            oldEmail!!.visibility = View.GONE
            newEmail!!.visibility = View.GONE
            password!!.visibility = View.GONE
            newPassword!!.visibility = View.VISIBLE
            changeEmail!!.setVisibility(View.GONE)
            changePassword!!.setVisibility(View.VISIBLE)
            sendEmail!!.setVisibility(View.GONE)
            remove!!.setVisibility(View.GONE)
        })

        changePassword!!.setOnClickListener(object : View.OnClickListener() {
            fun onClick(v: View) {
                progressBar!!.visibility = View.VISIBLE
                if (user != null && newPassword!!.text.toString().trim { it <= ' ' } != "") {
                    if (newPassword!!.text.toString().trim { it <= ' ' }.length < 6) {
                        newPassword!!.error = "Password too short, enter minimum 6 characters"
                        progressBar!!.visibility = View.GONE
                    } else {
                        user.updatePassword(newPassword!!.text.toString().trim { it <= ' ' })
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Password is updated, sign in with new password!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    signOut()
                                    progressBar!!.visibility = View.GONE
                                } else {
                                    Toast.makeText(this@MainActivity, "Failed to update password!", Toast.LENGTH_SHORT)
                                        .show()
                                    progressBar!!.visibility = View.GONE
                                }
                            }
                    }
                } else if (newPassword!!.text.toString().trim { it <= ' ' } == "") {
                    newPassword!!.error = "Enter password"
                    progressBar!!.visibility = View.GONE
                }
            }
        })

        btnSendResetEmail!!.setOnClickListener(object : View.OnClickListener() {
            fun onClick(v: View) {
                oldEmail!!.visibility = View.VISIBLE
                newEmail!!.visibility = View.GONE
                password!!.visibility = View.GONE
                newPassword!!.visibility = View.GONE
                changeEmail!!.setVisibility(View.GONE)
                changePassword!!.setVisibility(View.GONE)
                sendEmail!!.setVisibility(View.VISIBLE)
                remove!!.setVisibility(View.GONE)
            }
        })

        sendEmail!!.setOnClickListener(object : View.OnClickListener() {
            fun onClick(v: View) {
                progressBar!!.visibility = View.VISIBLE
                if (oldEmail!!.text.toString().trim { it <= ' ' } != "") {
                    auth!!.sendPasswordResetEmail(oldEmail!!.text.toString().trim { it <= ' ' })
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this@MainActivity, "Reset password email is sent!", Toast.LENGTH_SHORT)
                                    .show()
                                progressBar!!.visibility = View.GONE
                            } else {
                                Toast.makeText(this@MainActivity, "Failed to send reset email!", Toast.LENGTH_SHORT)
                                    .show()
                                progressBar!!.visibility = View.GONE
                            }
                        }
                } else {
                    oldEmail!!.error = "Enter email"
                    progressBar!!.visibility = View.GONE
                }
            }
        })

        btnRemoveUser!!.setOnClickListener(View.OnClickListener {
            progressBar!!.visibility = View.VISIBLE
            user?.delete()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this@MainActivity,
                        "Your profile is deleted:( Create a account now!",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this@MainActivity, SignupActivity::class.java))
                    finish()
                    progressBar!!.visibility = View.GONE
                } else {
                    Toast.makeText(this@MainActivity, "Failed to delete your account!", Toast.LENGTH_SHORT).show()
                    progressBar!!.visibility = View.GONE
                }
            }
        })

        signOut!!.setOnClickListener(View.OnClickListener { signOut() })

    }

    //sign out method
    fun signOut() {
        auth!!.signOut()
    }

    override fun onResume() {
        super.onResume()
        progressBar!!.visibility = View.GONE
    }

    public override fun onStart() {
        super.onStart()
        auth!!.addAuthStateListener(authListener!!)
    }

    public override fun onStop() {
        super.onStop()
        if (authListener != null) {
            auth!!.removeAuthStateListener(authListener!!)
        }
    }
}
