package com.example.emilia_maczka_czw_9_30

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import org.json.JSONArray

class MainActivity : AppCompatActivity() {
    internal lateinit var currenciesListButton: Button
    internal lateinit var goldButton: Button
    internal lateinit var exchangeButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        currenciesListButton = findViewById(R.id.buttonCurrencies)
        goldButton = findViewById(R.id.buttonGold)
        exchangeButton = findViewById(R.id.buttonExchange)

        this.currenciesListButton.setOnClickListener {
            val intent = Intent(this, CurrenciesListActivity::class.java)
            this.startActivity(intent)
        }

        this.goldButton.setOnClickListener {
            val intent = Intent(this, GoldActivity::class.java)
            this.startActivity(intent)
        }

        this.exchangeButton.setOnClickListener {
            val intent = Intent(this, ExchangeActivity::class.java)
            this.startActivity(intent)
        }

    }
}