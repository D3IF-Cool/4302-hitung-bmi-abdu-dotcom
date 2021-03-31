package org.d3if3067.hitungbmi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import android.widget.Toast
import org.d3if3067.hitungbmi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonHitung.setOnClickListener{
            hitungBmi()
        }
        binding.buttonReset.setOnClickListener{
            resetBmi()
        }

    }
    private fun resetBmi(){
        binding.beratEditText.text.clear()
        binding.tinggiEditText.text.clear()
    }
    private fun hitungBmi(){
        val berat = binding.beratEditText.text.toString()
        if (TextUtils.isEmpty(berat)){
            Toast.makeText(this, R.string.berat_invalid, Toast.LENGTH_LONG).show()
            return
        }
        val tinggi = binding.tinggiEditText.toString()
        if (TextUtils.isEmpty(tinggi)){
            Toast.makeText(this,R.string.tinggi_invalid, Toast.LENGTH_LONG).show()
            return
        }
        val tinggiCm = tinggi.toFloat() / 100

        val selectedId = binding.radioGroup.checkedRadioButtonId
        if (selectedId == -1){
            Toast.makeText(this, R.string.gender_invalid, Toast.LENGTH_LONG).show()
            return
        }
        val isMale = selectedId == R.id.priaRadioButton
        val bmi = berat.toFloat() / (tinggiCm * tinggiCm)
        val kategori = getKategori(bmi,isMale)

        binding.bmiTextView.text = getString(R.string.bmi_x, bmi)
        binding.kategoriTextView.text = getString(R.string.kategori_x,kategori)
    }
    private  fun getKategori(bmi: Float, isMale: Boolean): String {
        val stringRes = if(isMale){
            when {
                bmi < 20.5 -> R.string.kurus
                bmi >= 27.0 -> R.string.gemuk
                else -> R.string.ideal
            }
        }else{
            when{
                bmi <18.5 -> R.string.kurus
                bmi >= 25.0 -> R.string.gemuk
                else -> R.string.ideal
            }
        }
        return getString(stringRes)
    }
}