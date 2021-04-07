package com.example.emilia_maczka_czw_9_30

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.typeOf

class HistoricRatesActivity : AppCompatActivity() {
    internal lateinit var todayRate: TextView
    internal lateinit var yesterdayRate: TextView
    internal lateinit var lineChart_week: LineChart
    internal lateinit var lineChart_month: LineChart

//    internal lateinit var currency: CurrencyDetails
    internal lateinit var currencyCode: String
    internal lateinit var tableType: String
    internal var historicRates: Array<Pair<String, Double>> = emptyArray()
    @SuppressLint("SimpleDateFormat")
    internal var sdf = SimpleDateFormat("yyyy-MM-dd")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historic_rates)

        todayRate = findViewById(R.id.todayRate)
        yesterdayRate = findViewById(R.id.yesterdayRate)
        lineChart_week = findViewById(R.id.historicRatesChartWeek)
        lineChart_month = findViewById(R.id.historicRatesChartMonth)

        currencyCode = intent.getStringExtra("currencyCode") ?: "USD"
        tableType = intent.getStringExtra("tableType") ?: "A"
//        val pos = intent.getIntExtra("positionInArray", 0)
//        currency = TemporaryData.getDataSet()[pos]
        getData()
    }

    private fun getData() {
        val queue = TemporaryData.getQueue()

        val cal = Calendar.getInstance();
        cal.setTime(Date());
        cal.add(Calendar.DAY_OF_MONTH, -30);
        val prev_month = sdf.format(cal.getTime());

        val url = "http://api.nbp.pl/api/exchangerates/rates/$tableType/$currencyCode/$prev_month/${ sdf.format(Date())}/"

        val historicRatesRequest = JsonObjectRequest ( Request.Method.GET, url, null,
            Response.Listener { response ->
                loadDetails(response)
                setDetails()
            },
            Response.ErrorListener { println("Error!!!!!!!") }
        )
        queue.add(historicRatesRequest)
    }

    fun loadDetails(response: JSONObject) {
        response?.let {
            val rates = response.getJSONArray("rates")
            val ratesCount = rates.length()
            val tmpData = arrayOfNulls<Pair<String, Double>>(ratesCount)

            for (i in 0 until ratesCount) {
                val date = rates.getJSONObject(i).getString("effectiveDate")
                val currencyRate = rates.getJSONObject(i).getDouble("mid")
                tmpData[i] = Pair(date, currencyRate)
            }
             this.historicRates += tmpData as Array<Pair<String, Double>>
        }
    }


    private fun setDetails() {
        todayRate.text = getString(R.string.todayRateText, historicRates.last().second)
        yesterdayRate.text =  getString(R.string.yesterdayRateText, historicRates[historicRates.size-2].second)

        val cal = Calendar.getInstance();
        cal.setTime(Date());
        cal.add(Calendar.DAY_OF_MONTH, -7);
        val prev_week = cal.getTime()

        val entries_month = ArrayList<Entry>()
        var entries_week = ArrayList<Entry>()
        for (( idx, item ) in historicRates.withIndex()) {
            entries_month.add(Entry(idx.toFloat(), item.second.toFloat()))
            if (item.first >= sdf.format(prev_week) ) {
                entries_week.add(Entry(idx.toFloat(), item.second.toFloat()))
            }
        }

        if (entries_week.size > 1) {
            lineChart_week.data = LineData(LineDataSet(entries_week, "Kurs (wartość w PLN"))
            lineChart_week.getDescription().setText("Kurs %s z ostatnich 7 dni".format(/*currency.*/currencyCode))
        } else {
            lineChart_week.data = LineData(LineDataSet(entries_month.takeLast(2), "Kurs (wartość w PLN"))
            lineChart_week.getDescription().setText("Kurs %s z ostatnich 2 zapisów (brak danych dla ostatniego tygodnia)".format(/*currency.*/currencyCode))
        }
        lineChart_week.xAxis.valueFormatter = IndexAxisValueFormatter(historicRates.map { it.first.substring(5) }.toTypedArray())
        lineChart_week.invalidate()

        lineChart_month.data = LineData(LineDataSet(entries_month, "Kurs (wartość w PLN"))
        lineChart_month.getDescription().setText("Kurs %s z ostatnich 30 dni".format(/*currency.*/currencyCode))
        lineChart_month.xAxis.valueFormatter = IndexAxisValueFormatter(historicRates.map { it.first.substring(5) }.toTypedArray())
        lineChart_month.invalidate()
    }
}