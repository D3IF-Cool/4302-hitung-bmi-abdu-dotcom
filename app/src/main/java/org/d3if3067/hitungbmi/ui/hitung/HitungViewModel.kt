package org.d3if3067.hitungbmi.ui.hitung

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3if3067.hitungbmi.data.HasilBmi
import org.d3if3067.hitungbmi.data.HitungBmi
import org.d3if3067.hitungbmi.data.KategoriBmi
import org.d3if3067.hitungbmi.db.BmiDao
import org.d3if3067.hitungbmi.db.BmiEntity

class HitungViewModel(private val db: BmiDao) : ViewModel() {

    // Hasil BMI bisa null jika pengguna belum menghitung BMI
    private val hasilBmi = MutableLiveData<HasilBmi?>()
    // navigasi akan bernilai null ketika tidak bernavigasi
    private val navigasi = MutableLiveData<KategoriBmi?>()

    // Variabel ini sudah berupa LiveData (tidak mutable),
    // sehingga tidak perlu dijadikan private

    fun hitungBmi(berat: String, tinggi: String, isMale: Boolean) {

        val dataBmi = BmiEntity(
            berat = berat.toFloat(),
            tinggi = tinggi.toFloat(),
            isMale = isMale
        )
        hasilBmi.value = HitungBmi.hitung(dataBmi)

        viewModelScope.launch {
            withContext(Dispatchers.IO){

                db.insert(dataBmi)
            }
        }
    }

    fun mulaiNavigasi(){
        navigasi.value = hasilBmi.value?.kategori
    }
    fun selesaiNavigasi(){
        navigasi.value = null
    }
    fun getHasilBmi() : LiveData<HasilBmi?> = hasilBmi
    fun getNavigasi() : LiveData<KategoriBmi?> = navigasi


}