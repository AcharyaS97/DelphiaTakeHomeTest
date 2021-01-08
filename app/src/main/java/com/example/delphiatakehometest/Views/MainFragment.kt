package com.example.delphiatakehometest.Views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.delphiatakehometest.Models.ExchangeRateResponse
import com.example.delphiatakehometest.R
import com.example.delphiatakehometest.databinding.MainFragmentBinding
import com.example.delphiatakehometest.ViewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(), AdapterView.OnItemSelectedListener  {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModels()

    private var _binding : MainFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ExchangeRateResultAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = MainFragmentBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Click event listener for "Get Rate" button, invokes a ViewModel function to make the API call
        binding.getRateButton.setOnClickListener {
            binding.progressCircular.visibility = View.VISIBLE
            binding.recyclerView.visibility=View.GONE
            viewModel.getExchangeRatesWithBase()
        }

        //Listener for the exchangeRateResult LiveData observable in the MainViewModel class that will update when ViewModel gets an update from
        //ExchangeRateRepository
        viewModel.exchangeRateRateResult.observe(viewLifecycleOwner){

            binding.progressCircular.visibility = View.GONE
            binding.recyclerView.visibility=View.VISIBLE

            binding.header.text="1 ${viewModel.getBaseCurrency()} = "

            if (it.status == ExchangeRateResponse.Status.SUCCESS){
                adapter.addItems(it.data?.rates!!)
            }
            else if (it.status == ExchangeRateResponse.Status.ERROR){
                Toast.makeText(context,getString(R.string.app_toast_error_label),Toast.LENGTH_LONG).show()
            }
        }
        setupRecyclerView()

        //Create an Array Adapter for the Spinner
        val arrayAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.currency_options_array,
            android.R.layout.simple_spinner_item,
        )

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = arrayAdapter
        binding.spinner.onItemSelectedListener = this
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView(){
        adapter = ExchangeRateResultAdapter(ArrayList())
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
    }

    //Listener for Spinner Item being selected, sets the state of the current baseCurrency in MainViewModel
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selected = parent?.getItemAtPosition(position) as String
        viewModel.setBaseCurrency(selected)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

}