package me.near.com.nearme

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var authListener: FirebaseAuth.AuthStateListener? = null
    private var auth: FirebaseAuth? = null
    private val TAG = MainActivity::class.java.simpleName
    var pDialog: SweetAlertDialog? = null

    private val peoples: ArrayList<User> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeView()
        initializeData()
    }

    private fun initializeView() {

    }

    private fun initializeData() {

        auth = FirebaseAuth.getInstance()

        val user = FirebaseAuth.getInstance().currentUser

        authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
        }

        showPeoples()
        peoplesListRecyclerView.layoutManager = LinearLayoutManager(this)
        peoplesListRecyclerView.adapter = PeoplesAdapter(peoples, this)
    }

    private fun showPeoples() {

        val sharedPref = getSharedPreferences("NEAR_ME", Context.MODE_PRIVATE)

        pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        pDialog!!.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialog!!.titleText = "Loading"
        pDialog!!.setCancelable(false)
        pDialog!!.show()

        val database = FirebaseDatabase.getInstance().reference
        val ref = database.child("users")

        val countryQuery = ref.orderByChild("currentCountry").equalTo(sharedPref.getString("ccurrentCountry", ""))
        countryQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (singleSnapshot in dataSnapshot.children) {
                    peoples.add(singleSnapshot.getValue(User::class.java)!!)
                }
                peoplesListRecyclerView.adapter!!.notifyDataSetChanged()
                pDialog!!.cancel()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException())
            }
        })
    }

    private fun signOut() {
        auth!!.signOut()
        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        finish()
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home_action_profile -> {
                val intent = Intent(this@MainActivity, ProfileActivity::class.java)
                intent.putExtra("from", "current")
                startActivity(intent)
                return true
            }
            R.id.home_action_sign_out -> {
                SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Are you sure to Sign out?")
                    .setConfirmText("Yes")
                    .setCancelText("No")
                    .setConfirmClickListener { sDialog ->
                        sDialog.dismissWithAnimation()
                        signOut()
                    }
                    .show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Are you sure to exit?")
            .setConfirmText("Yes")
            .setCancelText("No")
            .setConfirmClickListener { sDialog ->
                sDialog.dismissWithAnimation()
                System.exit(0)
            }
            .show()
    }
}
