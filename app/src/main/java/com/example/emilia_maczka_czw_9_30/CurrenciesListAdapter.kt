package com.example.emilia_maczka_czw_9_30

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.toColor
import androidx.recyclerview.widget.RecyclerView

class CurrenciesListAdapter(var dataSet: Array<CurrencyDetails>, val context: Context) : RecyclerView.Adapter<CurrenciesListAdapter.ViewHolder>() {
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val currencyCodeTextView: TextView
        val currencyRateTextView: TextView
        val flagView: ImageView
        val arrowView: ImageView

        init {
            // Define click listener for the ViewHolder's View.
            currencyCodeTextView = view.findViewById(R.id.currencyTextView)
            currencyRateTextView = view.findViewById(R.id.rateTextView)
            flagView = view.findViewById(R.id.flagView)
            arrowView = view.findViewById(R.id.arrowView)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.currency_rate, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from yo ur dataset at this position and replace the
        // contents of the view with that element
        val currency = dataSet[position]

        viewHolder.currencyCodeTextView.text = currency.currencyCode
        viewHolder.currencyRateTextView.text = currency.rate.toString()
        viewHolder.flagView.setImageResource(currency.flag)
        if (currency.rise > 0) {
            viewHolder.arrowView.setImageResource(android.R.drawable.arrow_up_float)
            viewHolder.arrowView.setColorFilter(Color.GREEN)
        }
        else if (currency.rise < 0) {
            viewHolder.arrowView.setImageResource(android.R.drawable.arrow_down_float)
            viewHolder.arrowView.setColorFilter(Color.RED)
        }
        else viewHolder.arrowView.setImageResource(android.R.drawable.bottom_bar)

        viewHolder.itemView.setOnClickListener { goToDetails(currency.currencyCode, currency.type) } // { goToDetails(position) }
    }

    private fun goToDetails(currencyCode: String, table: String) {
        val intent = Intent(context, HistoricRatesActivity::class.java).apply {
            putExtra("currencyCode", currencyCode)
            putExtra("tableType", table)
        }
        context.startActivity(intent)
    }


//    private fun goToDetails(position: Int) {
//        val intent = Intent(context, HistoricRatesActivity::class.java).apply {
//            putExtra("positionInArray", position)
//        }
//        context.startActivity(intent)
//    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size


}