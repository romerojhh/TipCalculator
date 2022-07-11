package com.example.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tipcalculator.ui.theme.TipCalculatorTheme
import java.text.NumberFormat
import kotlin.math.ceil

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipCalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TipCalculatorScreen()
                }
            }
        }
    }
}

@Composable
fun EditNumberField(
    // @StringRes annotated variable store the reference to a text in string.xml
    @StringRes strRes: Int,
    value: String,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(text = stringResource(id = strRes))
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )
}

@Composable
fun RoundTipRow(
    modifier: Modifier = Modifier,
    isRounded: Boolean,
    onIsRoundedChanged: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(id = R.string.round_up_tip))
        Switch(
            checked = isRounded,
            onCheckedChange = onIsRoundedChanged,
            modifier = modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End),

            // HERE: To change switch color
            colors = SwitchDefaults.colors(
                uncheckedThumbColor = Color.DarkGray
            )
        )
    }
}

@Composable
fun TipCalculatorScreen() {
    var amountInput by remember { mutableStateOf("") }
    var tipInput by remember { mutableStateOf("") }
    var isRoundedInput by remember { mutableStateOf(false) }

    val tipAmount: Double = tipInput.toDoubleOrNull() ?: 0.0
    val amount: Double = amountInput.toDoubleOrNull() ?: 0.0

    val tip: String = calculateTip(
        percentage = tipAmount,
        total = amount,
        isRounded = isRoundedInput
    )

    // The LocalFocusManager interface is used to control focus in Compose.
    // You use this variable to move the focus to, and clear the focus from, the text boxes.
    val focusManager: FocusManager = LocalFocusManager.current

    // UI Below
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentWidth()
            .padding(32.dp),

        // This adds a fixed 8dp space between child elements.
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.calculate_tip),
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        EditNumberField(
            strRes = R.string.bill_amount,
            value = amountInput,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                // IME stands for Input Method Editor
                imeAction = ImeAction.Next
            ),

            // HERE: Watch how keyboardActions used to switch focus between TextFields
            // move the focus downwards to the next composable.
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        ) { amountInput = it }
        EditNumberField(
            strRes = R.string.how_was_the_service,
            value = tipInput,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                // IME stands for Input Method Editor
                imeAction = ImeAction.Done
            ),

            // HERE: focusManager.clearedFocus will minimize the onscreen keyboard when
            // the user click the done button
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus(true) }
            )
        ) { tipInput = it }
        RoundTipRow(isRounded = isRoundedInput) { isRoundedInput = it }
        Spacer(Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.tip_amount, tip),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// annotation indicates that this method public,
// but indicates to others that it's only public for testing purposes.
@VisibleForTesting
internal fun calculateTip(
    percentage: Double,
    total: Double,
    isRounded: Boolean
): String {
    var final: Double = total * percentage / 100
    if (isRounded) {
        final = ceil(final)
    }

    // HERE: This is how we format Double for USD
    return NumberFormat.getCurrencyInstance().format(final)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TipCalculatorTheme {
        TipCalculatorScreen()
    }
}