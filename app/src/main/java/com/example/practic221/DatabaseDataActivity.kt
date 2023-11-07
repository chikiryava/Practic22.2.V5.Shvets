package com.example.practic221

import com.example.practic221.adapters.UserdataAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practic221.adapters.ConversionsAdapter
import com.example.practic221.databinding.ActivityDatabaseDataBinding
import com.example.practic221.entities.ConversionsEntity
import com.example.practic221.entities.UserdataEntity
import com.example.practic221.room_database.AppDatabase
import com.example.practic221.room_database.DatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DatabaseDataActivity : AppCompatActivity(), UserdataAdapter.OnItemClickListener,
    ConversionsAdapter.OnItemClickListener {
    private lateinit var binding: ActivityDatabaseDataBinding
    private lateinit var userDataAdapter: UserdataAdapter
    private lateinit var conversionsAdapter: ConversionsAdapter
    private lateinit var repository: DatabaseRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDatabaseDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = DatabaseRepository(AppDatabase.getDatabase(this), this)

        binding.userdataRecyclerview.layoutManager = LinearLayoutManager(this)
        binding.conversionsRecyclerview.layoutManager = LinearLayoutManager(this)

        conversionsAdapter = ConversionsAdapter(this)
        userDataAdapter = UserdataAdapter(this)

        binding.userdataRecyclerview.adapter = userDataAdapter
        binding.conversionsRecyclerview.adapter = conversionsAdapter

        setListsInAdapters()
    }

    private fun setListsInAdapters() {
        GlobalScope.launch(Dispatchers.IO) {
            val userList = repository.getAllUsers()
            val conversionsList = repository.getAllConversions()
            withContext(Dispatchers.Main) {
                userDataAdapter.setUserData(userList)
                conversionsAdapter.setConversions(conversionsList)

            }
        }
    }

    override fun onItemClicked(userData: UserdataEntity) {
        // Показать список действий, например, в виде диалогового окна или PopupMenu.
        showActionsDialog(userData)
    }

    override fun onItemClicked(conversion: ConversionsEntity) {
        showActionsForConversions(conversion)
    }

    private fun showActionsForConversions(conversion: ConversionsEntity) {
        // Создайте диалоговое окно или PopupMenu для показа списка действий.
        // Пример:
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Выберите действие")
        builder.setItems(arrayOf("Удалить элемент")) { _, which ->
            // Обработка выбора действия.
            when (which) {
                0 -> {
                    // Действие "Удалить элемент"
                    GlobalScope.launch(Dispatchers.IO) {
                        repository.deleteConversion(conversion)
                    }
                    runOnUiThread {
                        val updatedList = conversionsAdapter.getConversions().toMutableList()
                        updatedList.remove(conversion)
                        conversionsAdapter.setConversions(updatedList)
                    }
                }
            }
        }
        builder.show()
    }

    private fun showActionsDialog(userData: UserdataEntity) {
        // Создайте диалоговое окно или PopupMenu для показа списка действий.
        // Пример:
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Выберите действие")
        builder.setItems(arrayOf("Удалить элемент", "Редактировать элемент")) { _, which ->
            // Обработка выбора действия.
            when (which) {
                0 -> {
                    // Действие "Удалить элемент"
                    GlobalScope.launch(Dispatchers.IO) {
                        repository.deleteUser(userData)
                    }
                    runOnUiThread {
                        val updatedList = userDataAdapter.getUserdata().toMutableList()
                        updatedList.remove(userData)
                        userDataAdapter.setUserData(updatedList)
                    }
                }
                1 -> {
                    showEditDialog(userData)
                }
            }
        }
        builder.show()
    }


    private fun showEditDialog(userData: UserdataEntity) {
        val editDialog = AlertDialog.Builder(this)
        val editView = layoutInflater.inflate(R.layout.edit_user_data_dialog, null)

        val loginEditText = editView.findViewById<EditText>(R.id.edit_login)
        val passwordEditText = editView.findViewById<EditText>(R.id.edit_password)

        loginEditText.setText(userData.login)
        passwordEditText.setText(userData.password)

        editDialog.setView(editView)
        editDialog.setPositiveButton("Сохранить") { _, _ ->
            // Получите новые данные пользователя из полей ввода
            val newLogin = loginEditText.text.toString()
            val newPassword = passwordEditText.text.toString()
            if(newLogin.isEmpty() || newPassword.isEmpty()){
                Toast.makeText(this,"Вы не заполнили не все поля",Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }

            // Обновите данные в базе данных
            GlobalScope.launch(Dispatchers.IO) {
                val updatedUserData = userData.copy(login = newLogin, password = newPassword)
                if(updatedUserData.login==userData.login){
                    repository.updateUser(updatedUserData)

                }
                else {
                    if(repository.findUser(newLogin)==null) {
                        repository.deleteUser(userData)
                        repository.insertUser(updatedUserData)
                    }
                    else{
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(
                                this@DatabaseDataActivity,
                                "Такой логин уже существует",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                val userList = repository.getAllUsers() // Получите обновленный список пользователей
                withContext(Dispatchers.Main) {
                    userDataAdapter.setUserData(userList) // Обновите данные адаптера
                }
            }
        }
        editDialog.setNegativeButton("Отмена", null)
        editDialog.show()
    }
}