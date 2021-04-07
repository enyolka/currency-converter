package com.example.emilia_maczka_czw_9_30

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import org.json.JSONArray

class CurrenciesListActivity : AppCompatActivity() {
    internal lateinit var recycler: RecyclerView
    internal lateinit var adapter: CurrenciesListAdapter
    internal var dataSet: Array<CurrencyDetails> = emptyArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currencies_list)

        recycler = findViewById(R.id.recycler)
        adapter = CurrenciesListAdapter(emptyArray(), this)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        TemporaryData.prepare(applicationContext)
        makeRequest( "A")
        makeRequest( "B")
    }

    fun makeRequest(table: String) {
        val queue = TemporaryData.getQueue()

        val url = "http://api.nbp.pl/api/exchangerates/tables/$table/last/2?format=json"

        val currenciesListRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener {
                    response ->
                loadData(response, table)
                adapter.dataSet = dataSet
                adapter.notifyDataSetChanged()
            },
            Response.ErrorListener {
                println("Error")
            }
        )
        queue.add(currenciesListRequest)
    }

    fun loadData(response: JSONArray?, table: String) {
        response?.let {
            val rates = response.getJSONObject(0).getJSONArray("rates")
            val ratesCount = rates.length()
            val tmpData = arrayOfNulls<CurrencyDetails>(ratesCount)
            var arrow: String

            for (i in 0 until ratesCount) {
                val currencyCode = rates.getJSONObject(i).getString("code")
                val currencyRate = rates.getJSONObject(i).getDouble("mid")
                val flag = TemporaryData.getFlagForCurrency(currencyCode)
                val rise = currencyRate - response.getJSONObject(1).getJSONArray("rates").getJSONObject(i).getDouble("mid")
                val currencyObject = CurrencyDetails(currencyCode, currencyRate, flag, table, rise)

                tmpData[i] = currencyObject
            }

            this.dataSet += tmpData as Array<CurrencyDetails>
        }
    }



}