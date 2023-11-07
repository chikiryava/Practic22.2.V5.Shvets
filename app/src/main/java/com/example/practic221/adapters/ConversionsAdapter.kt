package com.example.practic221.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.practic221.R
import com.example.practic221.entities.ConversionsEntity

class ConversionsAdapter(private val listener: OnItemClickListener) :RecyclerView.Adapter<ConversionsAdapter.ConversionsViewHolder>() {
    private var conversionsList:List<ConversionsEntity> = emptyList()

    inner class ConversionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val idTextView: TextView = itemView.findViewById(R.id.conversion_id)
        private val originalCurrencyTextView: TextView = itemView.findViewById(R.id.conversion_original_currency)
        private val finalCurrencyTextView: TextView = itemView.findViewById(R.id.conversion_final_currency)
        private val amountCurrencyTextView: TextView = itemView.findViewById(R.id.conversion_amount)
        private val conversionResultTextView:TextView = itemView.findViewById(R.id.conversion_result)
        private val conversionTimeTextView: TextView = itemView.findViewById(R.id.conversion_time)
        private val showDialogButton: Button = itemView.findViewById(R.id.show_dialog_button1)
        init {
            // Добавьте обработчик нажатия кнопки
            showDialogButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val conversions = conversionsList[position]
                    listener.onItemClicked(conversions)
                }
            }
        }

        fun bind(conversion: ConversionsEntity) {
            idTextView.text = "ID: ${conversion.id}"
            originalCurrencyTextView.text = "Перевод из: ${conversion.originalCurrency}"
            finalCurrencyTextView.text = "Первод в: ${conversion.finalCurrency}"
            amountCurrencyTextView.text = "Количество валюты для перевода: ${conversion.amountCurrency.toString()}"
            conversionResultTextView.text = "Результат конвертации: ${conversion.conversionResult}"
            conversionTimeTextView.text = "Время перевода: ${conversion.conversionTime}"
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversionsAdapter.ConversionsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.conversions_item, parent, false)
        return ConversionsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ConversionsAdapter.ConversionsViewHolder, position: Int) {
        holder.bind(conversionsList[position])
    }

    override fun getItemCount(): Int {
        return conversionsList.size
    }

    fun getConversions():List<ConversionsEntity>{
        return conversionsList
    }
    fun setConversions(conversion: List<ConversionsEntity>) {
        conversionsList = conversion
        notifyDataSetChanged()
    }
    interface OnItemClickListener {
        fun onItemClicked(conversion: ConversionsEntity)
    }


}