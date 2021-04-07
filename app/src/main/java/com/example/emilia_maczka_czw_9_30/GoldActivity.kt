package com.example.emilia_maczka_czw_9_30

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import org.json.JSONArray
import org.json.JSONObject
import kotlin.collections.ArrayList

class GoldActivity : AppCompatActivity() {
    internal lateinit var dataSet: Array<Pair<String, Double>>
    private lateinit var queue: RequestQueue

    internal lateinit var todayRate: TextView
    internal lateinit var lineChart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gold)

        todayRate = findViewById(R.id.goldRate)
        lineChart = findViewById(R.id.goldChart)

        makeRequest()
    }

    private fun makeRequest() {
        queue = Volley.newRequestQueue(applicationContext)

        val url = "http://api.nbp.pl/api/cenyzlota/last/30?format=json"

        val goldRequest = JsonArrayRequest ( Request.Method.GET, url, null,
            Response.Listener { response ->
                loadDetails(response)
                setDetails()
            },
            Response.ErrorListener { println("Error!!!!!!!") }
        )
        queue.add(goldRequest)
    }

    fun loadDetails(response: JSONArray) {
        response?.let {
            val size = response.length()
            val tmpData = arrayOfNulls<Pair<String, Double>>(size)

            for (i in 0 until size) {
                val date = response.getJSONObject(i).getString("data")
                val currencyRate = response.getJSONObject(i).getDouble("cena")
                tmpData[i] = Pair(date, currencyRate)
                print(tmpData[i])
            }
            this.dataSet = tmpData as Array<Pair<String, Double>>
        }
    }


    private fun setDetails() {
        todayRate.text = getString(R.string.goldRateText, dataSet.last().second)

        val entries = ArrayList<Entry>()
        for (( idx, item ) in dataSet.withIndex()) {
            entries.add(Entry(idx.toFloat(), item.second.toFloat()))
        }

        lineChart.data = LineData(LineDataSet(entries, "Kurs (wartość w PLN"))
        lineChart.getDescription().setText("Kurs złota z ostatnich 30 dni")
        lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(dataSet.map { it.first.substring(5) }.toTypedArray())
        lineChart.invalidate()
    }
}