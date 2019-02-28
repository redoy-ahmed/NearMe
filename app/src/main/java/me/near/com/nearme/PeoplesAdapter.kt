package me.near.com.nearme

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.user_list_item.view.*

class PeoplesAdapter(private val items: ArrayList<User>, private val context: Context) :
    RecyclerView.Adapter<ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.user_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.userNameTextView.text = items[position].firstName + " " + items[position].lastName
        holder.userCountryTextView.text = "Home Country: " + items[position].currentCountry
        holder.userResidenceCountryTextView.text = "Residence Country: " + items[position].countryOfResidence
        holder.jobTitleTextView.text = "Job: " + items[position].jobTitle
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val userNameTextView = view.userNameTextView!!
    val userCountryTextView = view.userCountryTextView!!
    val userResidenceCountryTextView = view.userResidenceCountryTextView!!
    val jobTitleTextView = view.jobTitleTextView!!
}