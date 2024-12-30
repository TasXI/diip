package com.example.khai_dipplom

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.ColorSpace.Rgb
import android.provider.CalendarContract.Colors
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class MessageAdapter(val currentUserId: String) : RecyclerView.Adapter<MessageAdapter.MyViewHolder>() {

    var messages: List<Message> = listOf()

    fun setmMessages(mess: List<Message>) {
        messages = mess
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val mesText = itemView.findViewById<TextView>(R.id.messageText)
        val lay = itemView.findViewById<LinearLayout>(R.id.main_con)
        val backmes = itemView.findViewById<CardView>(R.id.card_conteiner)
        val usernameV = itemView.findViewById<TextView>(R.id.userNameText)

        fun setMes(text: String, userName: String = ""){
            mesText.text = text
            usernameV.text = userName
        }

        fun setReceiver(){
            usernameV.visibility = View.GONE
            backmes.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor("#effdde")))
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return  MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false))
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val message = messages[position]
        holder.setMes(message.messageText, message.username)
        if (message.userIdSend == currentUserId) holder.setReceiver()
    }
}