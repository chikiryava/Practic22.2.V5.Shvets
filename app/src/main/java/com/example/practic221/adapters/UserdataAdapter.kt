package com.example.practic221.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.practic221.R
import com.example.practic221.entities.UserdataEntity

class UserdataAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<UserdataAdapter.UserDataViewHolder>() {
    private var userDataList: List<UserdataEntity> = emptyList()

    inner class UserDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userNameTextView: TextView = itemView.findViewById(R.id.user_name_textview)
        private val userEmailTextView: TextView = itemView.findViewById(R.id.user_email_textview)
        private val showDialogButton: Button = itemView.findViewById(R.id.show_dialog_button)
        init {
            // Добавьте обработчик нажатия кнопки
            showDialogButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val userData = userDataList[position]
                    listener.onItemClicked(userData)
                }
            }
        }

        fun bind(userData: UserdataEntity) {
            userNameTextView.text = "Логин: ${userData.login}"
            userEmailTextView.text = "Пароль: ${userData.password}"

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserDataViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.userdata_item, parent, false)
        return UserDataViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserDataViewHolder, position: Int) {
        holder.bind(userDataList[position])
    }

    override fun getItemCount(): Int {
        return userDataList.size
    }
    fun getUserdata():List<UserdataEntity>{
        return userDataList
    }

    fun setUserData(userData: List<UserdataEntity>) {
        userDataList = userData
        notifyDataSetChanged()
    }
    interface OnItemClickListener {
        fun onItemClicked(userData: UserdataEntity)
    }
}