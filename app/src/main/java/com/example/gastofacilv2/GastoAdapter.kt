package com.example.gastofacilv2

// GastoAdapter.kt
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GastoAdapter(private val gastos: List<Gasto>) : RecyclerView.Adapter<GastoAdapter.GastoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GastoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_gasto, parent, false)
        return GastoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GastoViewHolder, position: Int) {
        val currentGasto = gastos[position]
        holder.textViewFecha.text = "Fecha: ${currentGasto.fecha}"
        holder.textViewCantidad.text = "Cantidad: ${currentGasto.cantidad}"
        holder.textViewTipoGasto.text = "Tipo de Gasto: ${currentGasto.tipoGasto}"
        holder.textViewLugar.text = "Lugar: ${currentGasto.lugar}"
    }

    override fun getItemCount() = gastos.size

    class GastoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewFecha: TextView = itemView.findViewById(R.id.textViewFecha)
        val textViewCantidad: TextView = itemView.findViewById(R.id.textViewCantidad)
        val textViewTipoGasto: TextView = itemView.findViewById(R.id.textViewTipoGasto)
        val textViewLugar: TextView = itemView.findViewById(R.id.textViewLugar)
    }
}
