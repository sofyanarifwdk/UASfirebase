package com.example.firebase

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class InfoFragment : Fragment() {

    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var transactionList: MutableList<Transaction>
    private lateinit var recyclerView: RecyclerView
    private lateinit var buttonTambah: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_info, container, false)
        recyclerView = view.findViewById(R.id.rvTransactions)
//        buttonTambah = view.findViewById(R.id.btntambah)


//        buttonTambah.setOnClickListener {
////            val transaction = parentFragmentManager.beginTransaction()
////            transaction.replace(R.id.nav_host_fragment, TambahEditTransaksiFragment())
////            transaction.addToBackStack(null) // Allows the user to navigate back
////            transaction.commit()
//            val intent = Intent(requireContext(),TambahEditTransaksiFragment::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//            startActivity(intent)
//        }

        transactionList = mutableListOf()
        transactionAdapter = TransactionAdapter(transactionList, { transaction ->
            navigateToAddEditTransaction(transaction.id)
        }, { id ->
            deleteTransaction(id)
        })
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = transactionAdapter
        loadTransactions()
        return view
    }

    private fun loadTransactions() {
        val database = FirebaseDatabase.getInstance().getReference("datakas")
        Log.d("Database", database.toString())
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                transactionList.clear()
                for (data in snapshot.children) {
                    val transaction = data.getValue(Transaction::class.java)
                    if (transaction != null) {
                        transactionList.add(transaction)
                    }
                }
                transactionAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors.
            }
        })
    }

    private fun deleteTransaction(id: String) {
        val database = FirebaseDatabase.getInstance().getReference("datakas")
//        database.child(id).removeValue()

        // Create an AlertDialog
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Hapus Data ini ?")
        builder.setMessage("Apa anda yakin ingin menghapus data?")

        // Set the positive button to delete the transaction
        builder.setPositiveButton("ya") { dialog, which ->
            database.child(id).removeValue()
        }

        // Set the negative button to cancel the deletion
        builder.setNegativeButton("Tidak") { dialog, which ->
            // Do nothing if the user cancels
            dialog.dismiss()
        }

        // Show the AlertDialog
        builder.show()
    }

    private fun navigateToAddEditTransaction(transactionId: String?) {
        val fragment = TambahEditTransaksiFragment.newInstance(transactionId)
        fragmentManager?.beginTransaction()?.apply {
            replace(R.id.rvTransactions, fragment)
            addToBackStack(null)
            commit()
        }
    }
}
