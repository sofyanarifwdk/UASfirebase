package com.example.firebase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TransactionAdapter(
    private val transactionList: MutableList<Transaction>,
    private val onEditClick: (Transaction) -> Unit,
    private val onDeleteClick: (String) -> Unit
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTanggal: TextView = itemView.findViewById(R.id.tvTanggal)
        val tvPemasukan: TextView = itemView.findViewById(R.id.tvPemasukan)
        val tvPengeluaran: TextView = itemView.findViewById(R.id.tvPengeluaran)
        val tvKeterangan: TextView = itemView.findViewById(R.id.tvKeterangan)
        val tvSaldoAkhir: TextView = itemView.findViewById(R.id.tvSaldoAkhir)
//        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val currentItem = transactionList[position]
        holder.tvTanggal.text = currentItem.tanggal
        holder.tvPemasukan.text = currentItem.pemasukan.toString()
        holder.tvPengeluaran.text = currentItem.pengeluaran.toString()
        holder.tvKeterangan.text = currentItem.keterangan
        holder.tvSaldoAkhir.text = currentItem.saldoAkhir.toString()

//        holder.btnEdit.setOnClickListener {
//            onEditClick(currentItem)
//        }

        holder.btnDelete.setOnClickListener {
            onDeleteClick(currentItem.id)
        }
    }

    override fun getItemCount() = transactionList.size
}
