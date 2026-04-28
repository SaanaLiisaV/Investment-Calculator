package com.example.investmentcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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

    // user input values
    var stringValueInitialAmount by remember { mutableStateOf("0")}
    var stringValueInterestRate by remember { mutableStateOf("0")}
    var stringValueMonthlyContribution by remember { mutableStateOf("0")}

    var sliderPosition by remember { mutableStateOf(0f)}

    var YearsLabel by remember { mutableStateOf("0")}
    var resultText by remember { mutableStateOf("")}


    fun updateValues(){
    }


}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    InvestmentCalculatorTheme {
        ReactiveInvestmentValueUI()
    }
}