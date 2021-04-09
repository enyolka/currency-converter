package com.example.emilia_maczka_czw_9_30

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley.newRequestQueue
import com.blongho.country_data.Country
import com.blongho.country_data.World
import org.json.JSONArray

object TemporaryData {
    private lateinit var queue: RequestQueue
    private lateinit var countries: List<Country>
    internal lateinit var dataSet: Array<CurrencyDetails>

    fun getQueue(): RequestQueue {
        return queue
    }

    fun prepare(context: Context) {
        queue = newRequestQueue(context)
        World.init(context)
        countries = World.getAllCountries().distinctBy { it.currency.code }
    }

    fun getFlagForCurrency(currencyCode: String): Int {
        if (currencyCode == "USD")
            return R.drawable.us
        else if (currencyCode == "GBP")
            return R.drawable.gb
        else if (currencyCode == "CHF")
            return R.drawable.ch
        else if (currencyCode == "EUR")
            return R.drawable.eu
        else if (currencyCode == "HKD")
            return R.drawable.hk
        else if (currencyCode == "PLN")
            return R.drawable.pl
        else
            return countries.find { it.currency.code == currencyCode } ?. flagResource ?: World.getWorldFlag()
    }



}