package org.d3if3067.hitungbmi.ui.hitung

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import org.d3if3067.hitungbmi.R
import org.d3if3067.hitungbmi.data.KategoriBmi
import org.d3if3067.hitungbmi.databinding.FragmentHitungBinding

class HitungFragment : Fragment() {
    private val viewModel: HitungViewModel by viewModels()

    private lateinit var binding : FragmentHitungBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHitungBinding.inflate(layoutInflater,container,false)
        binding.buttonHitung.setOnClickListener{
            hitungBmi()
        }
        binding.buttonReset.setOnClickListener{
            resetBmi()
        }
        binding.saranButton.setOnClickListener{ viewModel.mulaiNavigasi()}
        binding.shareButton.setOnClickListener { shareData() }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getHasilBmi().observe(viewLifecycleOwner, {
            if (it == null) return@observe
            binding.bmiTextView.text = getString(R.string.bmi_x, it.bmi)
            binding.kategoriTextView.text = getString(R.string.kategori_x,
                getKategori(it.kategori))
            binding.buttonGroup.visibility = View.VISIBLE
        })

        viewModel.getNavigasi().observe(viewLifecycleOwner, {
            if (it == null) return@observe
            findNavController().navigate(HitungFragmentDirections
                .actionHitungFragmentToSaranFragment(it))
            viewModel.selesaiNavigasi()
        })
    }

    private fun resetBmi(){
        binding.beratEditText.text.clear()
        binding.tinggiEditText.text.clear()
    }

    private fun hitungBmi(){
        val berat = binding.beratEditText.text.toString()
        if (TextUtils.isEmpty(berat)){
            Toast.makeText(context, R.string.berat_invalid, Toast.LENGTH_LONG).show()
            return
        }
        val tinggi = binding.tinggiEditText.text.toString()
        if (TextUtils.isEmpty(tinggi)){
            Toast.makeText(context, R.string.tinggi_invalid, Toast.LENGTH_LONG).show()
            return
        }


        val selectedId = binding.radioGroup.checkedRadioButtonId
        if (selectedId == -1){
            Toast.makeText(context, R.string.gender_invalid, Toast.LENGTH_LONG).show()
            return
        }
        val isMale = selectedId == R.id.priaRadioButton


        viewModel.hitungBmi(berat, tinggi, isMale)

    }
    private fun getKategori(kategori: KategoriBmi): String {

            val stringRes = when (kategori) {

            KategoriBmi.KURUS -> R.string.kurus
            KategoriBmi.GEMUK -> R.string.gemuk
            KategoriBmi.IDEAL -> R.string.ideal
        }
        return getString(stringRes)
    }

    private fun shareData() {
        val selectedId = binding.radioGroup.checkedRadioButtonId
        val gender = if (selectedId == R.id.priaRadioButton)
            getString(R.string.pria)
        else
            getString(R.string.wanita)
        val message = getString(R.string.bagikan_template,
            binding.beratEditText.text,
            binding.tinggiEditText.text,
            gender,
            binding.bmiTextView.text,
            binding.kategoriTextView.text
        )
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain").putExtra(Intent.EXTRA_TEXT, message)
        if (shareIntent.resolveActivity(
                requireActivity().packageManager) != null) {
            startActivity(shareIntent)
        }
    }

}