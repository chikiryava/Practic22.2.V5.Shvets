package com.example.practic221

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.practic221.adapters.ConversionsAdapter
import com.example.practic221.adapters.UserdataAdapter
import com.example.practic221.databinding.ActivityMainBinding
import com.example.practic221.entities.ConversionsEntity
import com.example.practic221.entities.UserdataEntity
import com.example.practic221.room_database.AppDatabase
import com.example.practic221.room_database.DatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.util.Date

class MainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding
    private lateinit var repository: DatabaseRepository
    private val key: String = "e8tMZlKPTYXXHOIlj3lQxreacFnTte6O"
    private lateinit var fromCurrency: String
    private lateinit var toCurrency: String
    private var amount: Double = 0.000
    private var conversionResult: Double = 0.000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent

        repository = DatabaseRepository(AppDatabase.getDatabase(this), this)

        fromCurrency = intent.getStringExtra("from").toString()
        toCurrency = intent.getStringExtra("to").toString()
        amount = intent.getDoubleExtra("count", 0.000)

        getCurrency()

        binding.showDatabaseDataButton.setOnClickListener {
            startActivity(Intent(this, DatabaseDataActivity::class.java))
        }
    }

    private fun getCurrency() {
        val url = "https://api.apilayer.com/fixer/convert?to=${toCurrency}&from=${fromCurrency}&amount=${amount}"
        val queue = Volley.newRequestQueue(this)

        val stringRequest = object : StringRequest(Request.Method.GET, url,
            Response.Listener { response ->
                GlobalScope.launch {
                    conversionResult = JSONObject(response).getDouble("result")
                    conversionResult = String.format("%.2f", conversionResult).replace(',','.').toDouble()
                    amount = String.format("%.2f", amount).replace(',','.').toDouble()
                    repository.insertConversion(
                        ConversionsEntity(
                            0,
                            fromCurrency,
                            toCurrency,
                            amount,
                            conversionResult,
                            Date()
                        )

                    )
                    withContext(Dispatchers.Main) {
                        binding.resultTextView.text =
                            "Результат перевода из $fromCurrency в $toCurrency количества${amount} = ${conversionResult} $toCurrency"
                        Log.d(
                            "responseResult",
                            "Результат перевода ${amount} ${fromCurrency} в ${toCurrency} = ${
                                JSONObject(response).getString("result")
                            }"
                        )
                        binding.showDatabaseDataButton.isEnabled = true
                    }
                }
            },
            Response.ErrorListener {
                Log.d(
                    "responseResult",
                    "error:  ${fromCurrency} в ${toCurrency} количества$amount "
                )
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["apikey"] = key
                return headers
            }
        }
        queue.add(stringRequest)
    }
}