package com.example.practic221

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.practic221.databinding.ActivityLoginBinding
import com.example.practic221.entities.UserdataEntity
import com.example.practic221.room_database.AppDatabase
import com.example.practic221.room_database.DatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var repository: DatabaseRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        repository = DatabaseRepository(AppDatabase.getDatabase(this), this)

        // Событие при нажатии на кнопку Войти
        binding.loginButton.setOnClickListener {

            // Проверка на заполненность полей
            if (binding.loginEdittext.text.isNullOrEmpty() || binding.passwordEdittext.text.isNullOrEmpty()) {
                runOnUiThread {
                    Toast.makeText(
                        this@LoginActivity,
                        "Вы не заполнили все поля",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return@setOnClickListener
            }
            // Корутина которая из БД берет пользователя
            GlobalScope.launch(Dispatchers.IO) {
                val user = repository.findUser(binding.loginEdittext.text.toString())
                if (user == null) {
                    runOnUiThread {
                        Toast.makeText(
                            this@LoginActivity,
                            "Такого пользователя нет",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    return@launch
                }
                withContext(Dispatchers.Main) {
                    if (user.password == binding.passwordEdittext.text.toString()) {
                        runOnUiThread {
                            startActivity(
                                Intent(
                                    this@LoginActivity,
                                    SelectCurrencyActivity::class.java
                                )
                            )
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(
                                this@LoginActivity,
                                "Неверный логин или пароль",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        // Собитие при нажатии на кнопку Зарегестрироваться
        binding.registrButton.setOnClickListener {
            if (binding.loginEdittext.text.isNullOrEmpty() || binding.passwordEdittext.text.isNullOrEmpty()) {
                runOnUiThread {
                    Toast.makeText(
                        this@LoginActivity,
                        "Вы не заполнили все поля",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return@setOnClickListener
            }

            // Корутина для добавление пользователя в БД
            lifecycleScope.launch(Dispatchers.IO) {
                repository.insertUser(
                    UserdataEntity(
                        binding.loginEdittext.text.toString(),
                        binding.passwordEdittext.text.toString()
                    )
                )
            }
        }
    }
}