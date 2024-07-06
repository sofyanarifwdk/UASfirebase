package com.example.firebase

data class Transaction(
    var id: String = "",
    var keterangan: String = "",
    var pemasukan: Int = 0,
    var pengeluaran: Int = 0,
    var saldoAkhir: Int = 0,
    var tanggal: String = ""

)
