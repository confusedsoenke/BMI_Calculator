package com.hchelpers.bmicalculator

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.CompoundButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv_float_groesse.requestFocus();

        iBtn_info.setOnClickListener{
            val informationAct = Intent(this, Information::class.java)
            startActivity(informationAct)
        }

        switch_darkmode.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        })

        // on changed groesse
        tv_float_groesse.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {

                val maxLength:Int = 3;
                val fArray = arrayOfNulls<InputFilter>(1)
                fArray[0] = InputFilter.LengthFilter(maxLength)
                tv_float_groesse.setFilters(fArray)

                try{
                    getGroesse(s);
                    calcBMI();
                }
                catch(e:Exception){
                    tv_bmi_res.text = "";
                    Log.d("onCreate", "Fehler: $e")
                }
            }
        })

        // on change gewicht
        tv_float_gewicht.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                val maxLength:Int = 43;
                val fArray = arrayOfNulls<InputFilter>(1)
                fArray[0] = InputFilter.LengthFilter(maxLength)
                tv_float_gewicht.setFilters(fArray)

                try{
                    getGewicht(s);
                    calcBMI();
                }
                catch(e:Exception){
                    tv_bmi_res.text = "";
                    Log.d("onCreate", "Fehler: $e")
                }
            }
        })
    }

    var groesse:Double = 0.0;
    var gewicht:Double = 0.0;

    fun getGewicht(sGewicht:CharSequence) {
        var strGewicht = "";
        for (value in sGewicht )
            strGewicht += value;
        gewicht = strGewicht.toDouble();
    }
    fun getGroesse(sGroesse:CharSequence) {
        var strGroesse = "";
        for (value in sGroesse )
            strGroesse += value;
        groesse = strGroesse.toDouble();
    }

    fun calcBMI() {
        try{
            val bmi = (gewicht / (groesse * groesse)) * 10000;
            if (gewicht == 0.0 || groesse == 0.0){
                tv_bmi_res.text = "";
                tv_BMI.visibility = View.INVISIBLE;
                tv_text_res.text = "";
                tv_text_res.visibility = View.INVISIBLE
            }else{
                tv_bmi_res.text = "%.1f".format(bmi);
                tv_BMI.visibility = View.VISIBLE;
                tv_text_res.visibility = View.VISIBLE
                ShowBMIResultAsText(bmi);
            }
        }
        catch(e:Exception){
            tv_bmi_res.text = "";
            tv_text_res.text = "";
            Log.d("calcBMI()", "Exception: $e");
        }
    }

    fun ShowBMIResultAsText(bmi:Double){

        var bmiRounded = Math.round(bmi * 10.0) / 10.0
        when (bmiRounded) {
            in 0.1..15.9 -> tv_text_res.text = getText(R.string.BMI_Untergewicht_stark)
            in 16.0..16.9 -> tv_text_res.text = getText(R.string.BMI_Untergewicht_maessig)
            in 17.0..18.4 -> tv_text_res.text = getText(R.string.BMI_Untergewicht_leicht)
            in 18.5..24.9 -> tv_text_res.text = getText(R.string.BMI_Normal)
            in 25.0..29.9 -> tv_text_res.text = getText(R.string.BMI_praeadipositas)
            in 30.0..34.9 -> tv_text_res.text = getText(R.string.BMI_Adi1)
            in 35.0..39.9 -> tv_text_res.text = getText(R.string.BMI_Adi2)
            in 40.0..100.0 -> tv_text_res.text = getText(R.string.BMI_Adi3)
            else -> tv_text_res.text = "";
        }
    }
}