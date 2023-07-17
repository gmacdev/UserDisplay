package com.gmacv.userdisplay.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gmacv.userdisplay.R
import com.gmacv.userdisplay.data.model.Users
import com.gmacv.userdisplay.databinding.ItemUserListBinding

class UserAdapter(
    private val list: ArrayList<Users>
) : RecyclerView.Adapter<UserAdapter.DataViewHolder>() {

    class DataViewHolder(private val binding: ItemUserListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(users: Users) {
            val name = "${users.first_name} ${users.last_name}"
            binding.name.text = name
            binding.email.text = users.email
            Glide.with(binding.photo.context)
                .load(users.avatar)
                .placeholder(R.drawable.placeholder_image)
                .centerCrop()
                .error(R.drawable.placeholder_image)
                .into(binding.photo)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        return DataViewHolder(
            ItemUserListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addData(newList: List<Users>) {
        list.clear()
        list.addAll(newList)
    }
}