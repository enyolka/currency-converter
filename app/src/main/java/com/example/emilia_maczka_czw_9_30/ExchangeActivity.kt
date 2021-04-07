package com.example.emilia_maczka_czw_9_30

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import kotlin.math.round
import kotlin.math.roundToLong

class ExchangeActivity : AppCompatActivity() {
    internal lateinit var value: EditText
    internal lateinit var spinnerIn: Spinner
    internal lateinit var spinnerOut: Spinner
    internal lateinit var buttonExchange: Button
    internal lateinit var result: TextView

    internal var data:  Array<Pair<String, Double>> = emptyArray()
    private lateinit var queue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exchange)

        value = findViewById(R.id.valueText)

        spinnerIn = findViewById(R.id.spinnerIn)
        spinnerOut = findViewById(R.id.spinnerOut)
        buttonExchange = findViewById(R.id.buttonExchangeRates)
        result = findViewById(R.id.exchangeResultText)

        makeRequest("A")
        makeRequest("B")
        setSpinner()

        this.buttonExchange.setOnClickListener {
            if ((value.text.toString()).matches("[0|\\d*]+\\.?\\d*".toRegex())) {
                result.text = calculate().toString()
            } else {
                result.text = "0.0"
            }
        }
    }

    private fun setSpinner() {
        var arr = arrayOf("USD", "PLN", "SEK")
        var arr2 = data.map { it.first }.toTypedArray()
//
//        spinnerIn.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arr)//data?.map { it.first }.toTypedArray())
        var adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arr)//data?.map { it.first }.toTypedArray())
        spinnerIn.adapter = adapter
        spinnerOut.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arr)//data?.map { it.first }.toTypedArray())
    }

    private fun calculate() : Double {
        var curr_from = 1.0
        var curr_to = 1.0
        data.forEach { item ->
            if (item.first == spinnerIn.selectedItem)
                curr_from = item.second
            if (item.first == spinnerOut.selectedItem)
                curr_to = item.second
        }
        return round(((value.text.toString()).toDouble()) * curr_to / curr_from * 100) / 100
    }

    private fun makeRequest(table: String) {
        queue = Volley.newRequestQueue(applicationContext)

        val url = "http://api.nbp.pl/api/exchangerates/tables/$table?format=json"

        val goldRequest = JsonArrayRequest ( Request.Method.GET, url, null,
            Response.Listener { response ->
                loadData(response)
            },
            Response.ErrorListener { println("Error!!!!!!!") }
        )
        queue.add(goldRequest)
    }


        fun loadData(response: JSONArray?) {
        response?.let {
            val rates = response.getJSONObject(0).getJSONArray("rates")
            val ratesCount = rates.length()
            val tmpData = arrayOfNulls<Pair<String,Double>>(ratesCount)

            for (i in 0 until ratesCount) {
                val currencyCode = rates.getJSONObject(i).getString("code")
                val currencyRate = rates.getJSONObject(i).getDouble("mid")

                tmpData[i] = Pair(currencyCode, currencyRate)
            }

            this.data += tmpData as Array<Pair<String,Double>>
        }
    }
}