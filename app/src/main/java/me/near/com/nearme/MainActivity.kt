package me.near.com.nearme

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var authListener: FirebaseAuth.AuthStateListener? = null
    private var auth: FirebaseAuth? = null

    private val peoples: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        addPeoples()
        peoplesListRecyclerView.layoutManager = LinearLayoutManager(this)
        peoplesListRecyclerView.adapter = PeoplesAdapter(peoples, this)
    }

    private fun addPeoples() {
        peoples.add("dog")
        peoples.add("cat")
        peoples.add("owl")
        peoples.add("cheetah")
        peoples.add("raccoon")
        peoples.add("bird")
        peoples.add("snake")
        peoples.add("lizard")
        peoples.add("hamster")
        peoples.add("bear")
        peoples.add("lion")
        peoples.add("tiger")
        peoples.add("horse")
        peoples.add("frog")
        peoples.add("fish")
        peoples.add("shark")
        peoples.add("turtle")
        peoples.add("elephant")
        peoples.add("cow")
        peoples.add("beaver")
        peoples.add("bison")
        peoples.add("porcupine")
        peoples.add("rat")
        peoples.add("mouse")
        peoples.add("goose")
        peoples.add("deer")
        peoples.add("fox")
        peoples.add("moose")
        peoples.add("buffalo")
        peoples.add("monkey")
        peoples.add("penguin")
        peoples.add("parrot")
    }

    //sign out method
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

                return true
            }
            R.id.home_action_sign_out -> {
                signOut()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
