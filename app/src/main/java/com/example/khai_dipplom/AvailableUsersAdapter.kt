package com.example.khai_dipplom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Visibility
import com.example.khai_dipplom.databinding.DialogItemBinding

class AvailableUsersAdapter(
    private val users: List<UserInfo>,
    private val onAddClick: (UserInfo) -> Unit
) : RecyclerView.Adapter<AvailableUsersAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = DialogItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.binding.btnRemove.visibility = View.GONE
        holder.binding.tvUserName.text = user.username
        holder.binding.btnAdd.setOnClickListener { onAddClick(user) }
    }

    override fun getItemCount(): Int = users.size

    inner class UserViewHolder(val binding: DialogItemBinding) : RecyclerView.ViewHolder(binding.root)

}
