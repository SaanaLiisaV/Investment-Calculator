package com.example.investmentcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
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

    //localized texts from strings.xml
    val investmentInfoText = stringResource(R.string.investment_info)
    val resultsText = stringResource(R.string.total_results)
    val initialAmountText = stringResource(R.string.initial_amount)
    val monthlyContributionText = stringResource(R.string.monthly_contribution)
    val annualInterestRateText = stringResource(R.string.annual_interestrate)
    val investmentYearsText = stringResource(R.string.investment_years)

    val totalInvestedText = stringResource(R.string.total_invested)
    val totalReturnText = stringResource(R.string.total_return)
    val finalValueText = stringResource(R.string.final_value)


    //user input values
    var stringValueInitialAmount by remember { mutableStateOf("0")}
    var stringValueInterestRate by remember { mutableStateOf("0")}
    var stringValueMonthlyContribution by remember { mutableStateOf("0")}
    var sliderPosition by remember { mutableStateOf(0f)} // Slider - years

    var resultText by remember { mutableStateOf("")}

    fun updateValues(){
        //this function is called every time user changes a value

        var investmentYears = sliderPosition.toInt()
        var initialAmount = stringValueInitialAmount.toDoubleOrNull() ?: 0.0
        var monthlyContribution = stringValueMonthlyContribution.toDoubleOrNull() ?: 0.0
        val annualInterestRate = (stringValueInterestRate.toDoubleOrNull() ?: 0.0) / 100.0

        val monthlyInterestRate = annualInterestRate / 12

        var finalValue = initialAmount

        val totalMonths = investmentYears * 12

        //loop goes through every month one by one
        for (i in 1..totalMonths) {

            //add monthly contribution
            finalValue = finalValue + monthlyContribution
            //add interest growth for this month
            finalValue = finalValue * (1 + monthlyInterestRate)

        }
        //how much money user invested in total
        val totalInvested = initialAmount + (monthlyContribution * totalMonths)

        val totalReturn = finalValue - totalInvested

        //Creating the result text using localized strings
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
        //layout
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                start = 20.dp,
                end = 20.dp,
                top = 80.dp,
                bottom = 10.dp
        ),

            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            //Investment calculator -title
            Text(
                text = stringResource(R.string.investment_calculator),
                style = MaterialTheme.typography.headlineMedium
            )
            //result box
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = resultText,
                    modifier = Modifier.padding(12.dp)
                )
            }

    //Initial amount
    TextField(
        value = stringValueInitialAmount,
        onValueChange = {
            stringValueInitialAmount = it //Save new value
            updateValues() //Recalculate when value changes
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = {
            Text("${stringResource(R.string.initial_amount)}:")
        },
        modifier=Modifier.fillMaxWidth()
    )

    //Monthly contribution
    TextField(
     value = stringValueMonthlyContribution,
     onValueChange = {
         stringValueMonthlyContribution = it
        updateValues()
     },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = {
            Text("${stringResource(R.string.monthly_contribution)}:")
        },
     modifier=Modifier.fillMaxWidth()
    )

    //Annual interest rate
    TextField(
        value = stringValueInterestRate,
        onValueChange = {
            stringValueInterestRate = it
            updateValues() //Recalculate when value changes
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = {
            Text("${stringResource(R.string.annual_interestrate)}:")
        },
        modifier=Modifier.fillMaxWidth()
    )
    //Box is used to center the slider label
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    )

    //currently selected investment years
    { Text("$investmentYearsText ${sliderPosition.toInt()}") }

    //Slider for selecting investment duration (in years)
    Slider(
        value=sliderPosition,
        onValueChange = {
            sliderPosition = it.toInt().toFloat()
            updateValues() //Recalculate when value changes
        },
        valueRange= 0f..50.0f,
        steps = 49 //years between 0 and 50
    )
}}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    InvestmentCalculatorTheme {
        ReactiveInvestmentValueUI()
    }
}