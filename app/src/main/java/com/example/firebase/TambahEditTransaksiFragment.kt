package com.example.firebase

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TambahEditTransaksiFragment : Fragment() {

    private lateinit var etTanggal: EditText
    private lateinit var etPemasukan: EditText
    private lateinit var etPengeluaran: EditText
    private lateinit var etKeterangan: EditText
    private lateinit var btnSave: Button
    private var transactionId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tambah_edit_transaksi, container, false)
        etTanggal = view.findViewById(R.id.etTanggal)
        etPemasukan = view.findViewById(R.id.etPemasukan)
        etPengeluaran = view.findViewById(R.id.etPengeluaran)
        etKeterangan = view.findViewById(R.id.etKeterangan)
        btnSave = view.findViewById(R.id.btnSave)

        btnSave.setOnClickListener {
            saveTransaction()
        }

        transactionId = arguments?.getString("id")
        if (transactionId != null) {
            loadTransactionData(transactionId!!)
        }

        return view
    }

    private fun loadTransactionData(id: String) {
        val database = FirebaseDatabase.getInstance().getReference("datakas").child(id)
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val id = snapshot.child("id").getValue(String::class.java)
                    val keterangan = snapshot.child("keterangan").getValue(String::class.java)
                    val pemasukan = snapshot.child("pemasukan").getValue(Int::class.java)
                    val pengeluaran = snapshot.child("pengeluaran").getValue(Int::class.java)
                    val saldoAkhir = snapshot.child("saldoAkhir").getValue(Int::class.java)
                    val tanggal = snapshot.child("tanggal").getValue(String::class.java)

                    etTanggal.setText(tanggal)
                    etPemasukan.setText(pemasukan?.toString())
                    etPengeluaran.setText(pengeluaran?.toString())
                    etKeterangan.setText(keterangan)

                } else {
                    // Handle case where the snapshot does not exist
                    Toast.makeText(context, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors.
                Toast.makeText(context, "Gagal memuat data", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun saveTransaction() {
        val tanggal = etTanggal.text.toString()
        val pemasukan = etPemasukan.text.toString().toIntOrNull() ?: 0
        val pengeluaran = etPengeluaran.text.toString().toIntOrNull() ?: 0
        val keterangan = etKeterangan.text.toString()
        val saldoAkhir = pemasukan - pengeluaran

        val transaction = Transaction(
            id = transactionId ?: "",
            tanggal = tanggal,
            pemasukan = pemasukan,
            pengeluaran = pengeluaran,
            keterangan = keterangan,
            saldoAkhir = saldoAkhir
        )

        val database = FirebaseDatabase.getInstance().getReference("datakas")
        val key = transactionId ?: database.push().key

        if (key != null) {
            transaction.id = key
            database.child(key).setValue(transaction)
            Toast.makeText(context, "Data Berhasil Disimpan.", Toast.LENGTH_SHORT).show()
        }

        fragmentManager?.popBackStack()
    }

    companion object {
        fun newInstance(transactionId: String?): TambahEditTransaksiFragment {
            val fragment = TambahEditTransaksiFragment()
            val args = Bundle()
            args.putString("id", transactionId)
            fragment.arguments = args
            return fragment
        }
    }
}
