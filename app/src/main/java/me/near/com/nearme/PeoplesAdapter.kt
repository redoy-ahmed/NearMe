package me.near.com.nearme

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.user_list_item.view.*

class PeoplesAdapter(private val items: ArrayList<User>, private val clickListener: (User) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.user_list_item, parent, false)
        return PartViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PartViewHolder).bind(items[position], clickListener)
    }

    override fun getItemCount() = items.size

    class PartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User, clickListener: (User) -> Unit) {
            itemView.userNameTextView.text = user.firstName + " " + user.lastName
            itemView.userCountryTextView.text = "Home Country: " + user.currentCountry
            itemView.userResidenceCountryTextView.text = "Residence Country: " + user.countryOfResidence
            itemView.jobTitleTextView.text = "Job: " + user.jobTitle
            itemView.setOnClickListener { clickListener(user) }
        }
    }
}