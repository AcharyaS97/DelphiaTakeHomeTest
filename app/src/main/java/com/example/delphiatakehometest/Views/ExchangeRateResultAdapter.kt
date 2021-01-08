package com.example.delphiatakehometest.Views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.delphiatakehometest.Models.Currency
import com.example.delphiatakehometest.databinding.ExchangeListItemBinding

/*
    Adapter class for the RecyclerView that results will be displayed to
 */
class ExchangeRateResultAdapter(private val currencyList : ArrayList<Currency>) :
    RecyclerView.Adapter<ExchangeRateResultAdapter.CurrencyExchangeViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CurrencyExchangeViewHolder {
        val binding = ExchangeListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CurrencyExchangeViewHolder(binding)
    }

    override fun getItemCount() = currencyList.size

    fun addItems(items : List<Currency>){
        currencyList.clear()
        currencyList.addAll(items)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CurrencyExchangeViewHolder, position: Int) {
        holder.binding.exchangeRate.text = currencyList[position].rate;
        holder.binding.countryName.text = currencyList[position].symbol;

        with(holder){
            with(currencyList[position]) {
                binding.countryName.text = symbol
                binding.exchangeRate.text = rate
            }
        }
    }


    //This inner class is the binding for the layout in exchange_list_item
    inner class CurrencyExchangeViewHolder(val binding : ExchangeListItemBinding) : RecyclerView.ViewHolder(binding.root)


}