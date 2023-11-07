package com.example.practic221

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.practic221.databinding.ActivitySelectCurrencyBinding
import org.json.JSONException
import org.json.JSONObject
import java.util.TreeMap

class SelectCurrencyActivity : AppCompatActivity() {
    lateinit var binding: ActivitySelectCurrencyBinding
    private val key = "e8tMZlKPTYXXHOIlj3lQxreacFnTte6O"
    lateinit var listOfCurrency: MutableMap<String, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectCurrencyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fetchCurrencyData()
        binding.convertButton.setOnClickListener{
            if(binding.countEditText.text.toString().isEmpty()){
                Toast.makeText(this,"Вы не указали количество валюты",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent =Intent(this,MainActivity::class.java)
            intent.putExtra("to", listOfCurrency[binding.toSpinner.selectedItem.toString()])
            intent.putExtra("from",listOfCurrency[binding.fromSpinner.selectedItem.toString()])
            intent.putExtra("count",binding.countEditText.text.toString().toDouble())
            startActivity(intent)
        }
    }
    private fun fetchCurrencyData(){
        val currencyList = HashMap<String, String>()
        val url = "https://api.apilayer.com/fixer/symbols" // Replace with the actual URL
        val queue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(
            Method.GET, url,
            Response.Listener { response ->
                try {
                    val jsonResponse = JSONObject(response)
                    val success = jsonResponse.getBoolean("success")
                    if (success) {
                        val symbols = jsonResponse.getJSONObject("symbols")
                        val keys = symbols.keys()
                        for (key in keys) {
                            val currencyName = symbols.getString(key)
                            currencyList[currencyName] = key
                        }
                        val sortedList = TreeMap(currencyList)
                        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sortedList.keys.toList())
                        binding.toSpinner.adapter = adapter
                        binding.fromSpinner.adapter = adapter
                        listOfCurrency = sortedList
                        binding.convertButton.isEnabled=true
                    } else {
                        Log.d("responseResult", "API request failed.")
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Log.d("responseResult", "Error parsing JSON.")
                }
            },
            Response.ErrorListener {
                val networkResponse = it.networkResponse
                if (networkResponse != null) {
                    val statusCode = networkResponse.statusCode
                    Log.e("Volley Error", "HTTP status code: $statusCode")
                    val data = String(networkResponse.data)
                    Log.e("Volley Error", "Network response data: $data")
                } else {
                    Log.e("Volley Error", "Network response is null.")
                }
            })
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["apikey"] = key // Replace with your API key
                return headers
            }
        }
        queue.add(stringRequest)
    }
}