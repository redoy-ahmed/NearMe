package me.near.com.nearme

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_signup.*
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener

class SignupActivity : AppCompatActivity() {

    private var auth: FirebaseAuth? = null
    private var mFirebaseDatabase: DatabaseReference? = null
    private var mFirebaseInstance: FirebaseDatabase? = null
    private val TAG = SignupActivity::class.java.simpleName
    var pDialog: SweetAlertDialog? = null

    var listOfCountry = arrayOf("Burkina Faso", "Ghana", "Togo", "Benin")
    var listOfResidence = arrayOf("Canada", "USA", "Mexico")

    var firstName = ""
    var lastName = ""
    var email = ""
    var password = ""
    var cellPhone = ""
    var currentCountry = ""
    var countryOfResidence = ""
    var jobTitle = ""
    var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        initializeView()
        initializeData()
        setOnclickListeners()
    }

    private fun initializeView() {
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }
    }

    private fun initializeData() {
        auth = FirebaseAuth.getInstance()

        mFirebaseInstance = FirebaseDatabase.getInstance()

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance!!.getReference("users")

        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, listOfCountry)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        currentCountrySpinner!!.adapter = aa

        val bb = ArrayAdapter(this, android.R.layout.simple_spinner_item, listOfResidence)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        countryOfResidenceSpinner!!.adapter = bb
    }

    private fun setOnclickListeners() {

        currentCountrySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                currentCountry = parent.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        countryOfResidenceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                countryOfResidence = parent.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        signupButton.setOnClickListener {
            firstName = firstNameEditText.text.trim().toString()
            lastName = lastNameEditText.text.trim().toString()
            email = emailEditText.text.trim().toString()
            password = passwordEditText.text.trim().toString()
            cellPhone = cellPhoneEditText.text.trim().toString()
            jobTitle = jobTitleEditText.text.trim().toString()

            if (TextUtils.isEmpty(firstName)) {
                Toast.makeText(applicationContext, "Enter first name!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(lastName)) {
                Toast.makeText(applicationContext, "Enter last name!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(applicationContext, "Enter email address!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(applicationContext, "Password too short, enter minimum 6 characters!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(cellPhone)) {
                Toast.makeText(applicationContext, "Enter cell Phone number!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(jobTitle)) {
                Toast.makeText(applicationContext, "Enter job title!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
            pDialog!!.progressHelper.barColor = Color.parseColor("#A5DC86")
            pDialog!!.titleText = "Loading"
            pDialog!!.setCancelable(false)
            pDialog!!.show()

            auth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this@SignupActivity) { task ->

                Toast.makeText(this@SignupActivity, "createUserWithEmail:onComplete:" + task.isSuccessful, Toast.LENGTH_LONG).show()
                if (!task.isSuccessful) {
                    Toast.makeText(this@SignupActivity, "Sign up failed." + task.exception!!, Toast.LENGTH_LONG)
                        .show()
                } else {
                    createUser(firstName, lastName, email, password, cellPhone, currentCountry, countryOfResidence, jobTitle)
                    Toast.makeText(this@SignupActivity, "User sign up Successful", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
                    finish()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createUser(firstName: String, lastName: String, email: String, password: String, cellPhone: String, currentCountry: String, countryOfResidence: String, jobTitle: String) {

        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabase!!.push().key
        }

        val user = User(firstName, lastName, email, password, cellPhone, currentCountry, countryOfResidence, jobTitle)

        mFirebaseDatabase!!.child(userId!!).setValue(user)
        pDialog!!.cancel()

        //addUserChangeListener()
    }

    private fun addUserChangeListener() {

        mFirebaseDatabase!!.child(userId!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)

                if (user == null) {
                    Log.e(TAG, "User data is null!")
                    return
                }
                Log.e(TAG, "User data is changed!" + user.firstName + ", " + user.lastName)

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Failed to read user", error.toException())
            }
        })
    }
}
