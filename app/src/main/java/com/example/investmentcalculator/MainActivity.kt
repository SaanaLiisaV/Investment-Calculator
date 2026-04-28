package com.example.investmentcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.investmentcalculator.ui.theme.InvestmentCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InvestmentCalculatorTheme {
                enableEdgeToEdge()
                Column(modifier = Modifier.padding(16.dp))
                {ReactiveInvestmentValueUI()}
            }
        }
    }
}

@Composable
fun ReactiveInvestmentValueUI() {

    val investmentInfoText = stringResource(R.string.investment_info)
    val resultsText = stringResource(R.string.total_results)

    val initialAmountText = stringResource(R.string.initial_amount)
    val monthlyContributionText = stringResource(R.string.monthly_contribution)
    val annualInterestRateText = stringResource(R.string.annual_interestrate)
    val investmentYearsText = stringResource(R.string.investment_years)

    val totalInvestedText = stringResource(R.string.total_invested)
    val totalReturnText = stringResource(R.string.total_return)
    val finalValueText = stringResource(R.string.final_value)


    //käyttäjän syötteet
    var stringValueInitialAmount by remember { mutableStateOf("0")}
    var stringValueInterestRate by remember { mutableStateOf("0")}
    var stringValueMonthlyContribution by remember { mutableStateOf("0")}
    var sliderPosition by remember { mutableStateOf(0f)} // Slider - years

    var resultText by remember { mutableStateOf("")}

    fun updateValues(){

        //Tänne päädytään, kun käyttäjä muuttaa arvoja

        var investmentYears = sliderPosition.toInt()
        var initialAmount = stringValueInitialAmount.toDoubleOrNull() ?: 0.0
        var monthlyContribution = stringValueMonthlyContribution.toDoubleOrNull() ?: 0.0
        val annualInterestRate = (stringValueInterestRate.toDoubleOrNull() ?: 0.0) / 100.0

        //TÄMÄ TOIMII KUN ON PELKKÄ PÄÄOMA:
        //val finalValue = initialAmount * Math.pow((1 + annualInterestRate).toDouble(), investmentYears.toDouble())
        //val totalReturn = finalValue - initialAmount


        //TÄÄLLÄ LISÄTÄÄN KUUKAUSITTAINEN SIJOITUS + LASKUT:

        val monthlyInterestRate = annualInterestRate / 12

        var finalValue = initialAmount

        val totalMonths = investmentYears * 12

        //iteroidaan jokainen kuukausi yksi kerrallaan
        for (i in 1..totalMonths) {

            //kuukausittainen saasto
            finalValue = finalValue + monthlyContribution
            //lisataan korko
            finalValue = finalValue * (1 + monthlyInterestRate)

        }
        //oma alkupääoma + kuukausisäästö × 12 × sijoitusvuodet
        val totalInvested = initialAmount + (monthlyContribution * totalMonths)

        //tuotto/korko = loppusumma - sijoitettu yhteensä
        val totalReturn = finalValue - totalInvested

        //haetaan lokalisoidut merkkijonot, muotoillaan luettaviksi
        resultText = "$investmentInfoText\n"
        resultText += "$initialAmountText $initialAmount\n"
        resultText += "$monthlyContributionText $monthlyContribution\n"
        resultText += "$annualInterestRateText $stringValueInterestRate%\n"
        resultText += "$investmentYearsText $investmentYears \n\n"

        resultText += "$resultsText\n"
        resultText += "$totalInvestedText ${"%.2f".format(totalInvested)}\n"
        resultText += "$finalValueText ${"%.2f".format(finalValue)}\n"
        resultText += "$totalReturnText ${"%.2f".format(totalReturn)} \n"

    }

    Spacer(modifier=Modifier.fillMaxSize(0.2f))

    Text(text=resultText)
    Spacer(modifier=Modifier.fillMaxSize(0.2f))

    //ALKUPÄÄOMA
    TextField(
        value = stringValueInitialAmount,
        onValueChange = {
            stringValueInitialAmount = it
            updateValues() //tämä kutsuu funtiota, joka laskee uudelleen kun arvot muuttuu
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = {
            Text("${stringResource(R.string.initial_amount)}:")
        },
        modifier=Modifier.fillMaxWidth()
    )

    Spacer(modifier=Modifier.fillMaxSize(0.2f))

    //KUUKAUSITTAINEN SIJOITUS
    TextField(
     value = stringValueMonthlyContribution,
     onValueChange = {
         stringValueMonthlyContribution = it
        updateValues() //tämä kutsuu funtiota, joka laskee uudelleen kun arvot muuttuu
     },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = {
            Text("${stringResource(R.string.monthly_contribution)}:")
        },
     modifier=Modifier.fillMaxWidth()
    )

    Spacer(modifier=Modifier.fillMaxSize(0.2f))

    //KORKOPROSENTTI
    TextField(
        value = stringValueInterestRate,
        onValueChange = {
            stringValueInterestRate = it
            updateValues() //tämä kutsuu funtiota, joka laskee uudelleen kun arvot muuttuu
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = {
            Text("${stringResource(R.string.annual_interestrate)}:")
        },
        modifier=Modifier.fillMaxWidth()
    )

    Spacer(modifier=Modifier.fillMaxSize(0.2f))

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    )

    //currently selected investment years
    { Text("$investmentYearsText ${sliderPosition.toInt()}") }

    //SLIDER MÄÄRITTÄMÄÄN AIKA VUOSINA
    Slider(
        value=sliderPosition,
        onValueChange = {
            sliderPosition = it.toInt().toFloat()
            updateValues() //tämä kutsuu funtiota, joka laskee uudelleen kun arvot muuttuu
        },
        valueRange= 0f..50.0f,
        steps = 49
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    InvestmentCalculatorTheme {
        ReactiveInvestmentValueUI()
    }
}