package com.mbhre.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mbhre.R
import com.mbhre.databinding.ListUsersCellBinding
import com.mbhre.model.DataItem


class UsersAdapter(var applicationContext: Context) : RecyclerView.Adapter<UsersAdapter.UsersViewHolder>() {



    inner class UsersViewHolder(var itemViews: ListUsersCellBinding,var context: Context): RecyclerView.ViewHolder(itemViews.root){
        fun setdata(picItem: DataItem,) {
             itemViews.textUsersId.text=picItem.id
             itemViews.txtUsersName.text=picItem.name.capitalize()
             when( picItem.gender ) {
                "male" -> itemViews.img1.setImageResource(R.drawable.man)
                "female" -> itemViews.img1.setImageResource(R.drawable.woman)

                else ->"female"
            }
            when( picItem.status ) {
                "active" -> itemViews.txtUsersStatus.setTextColor(context.getColor(R.color.md_green_900))
                "inactive" -> itemViews.txtUsersStatus.setTextColor(context.getColor(R.color.red))

                else ->"inactive"
            }
             itemViews.textUsersEmail.text=picItem.email
             itemViews.txtUsersStatus.text=picItem.status.capitalize()
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): UsersViewHolder {

        val userBinding = ListUsersCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UsersViewHolder(userBinding,applicationContext)
    }


    override fun getItemCount() =  differ.currentList.size

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val picItem = differ.currentList[position]
        holder.setdata(picItem)


    }
}