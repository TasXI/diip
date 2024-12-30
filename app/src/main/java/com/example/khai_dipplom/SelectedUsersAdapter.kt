package com.example.khai_dipplom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.khai_dipplom.databinding.ChatItemBinding
import com.example.khai_dipplom.databinding.DialogItemBinding

class SelectedUsersAdapter(
    private val users: List<UserInfo>,
    private val onRemoveClick: (UserInfo) -> Unit
) : RecyclerView.Adapter<SelectedUsersAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = DialogItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.binding.btnAdd.visibility = View.GONE
        holder.binding.tvUserName.text = user.username
        holder.binding.btnRemove.setOnClickListener { onRemoveClick(user) }
    }

    override fun getItemCount(): Int = users.size

    inner class UserViewHolder(val binding: DialogItemBinding) : RecyclerView.ViewHolder(binding.root)
}