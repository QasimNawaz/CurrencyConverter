package com.demo.currencyconvertertest.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun CurrencyOutlinedTextField(
    modifier: Modifier,
    value: String,
    onValueChange: ((String) -> Unit),
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    val pattern = remember { Regex("^\\d+\$") }
    Column {
        OutlinedTextField(
            modifier = modifier,
            value = value,
            onValueChange = {
                if (it.isEmpty() || it.matches(pattern)) {
                    onValueChange(it)
                }
            },
            label = { Text(text = label) },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                textColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                cursorColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                trailingIconColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
            ),
        )
        Spacer(modifier = Modifier.size(10.dp))
    }
}

@Composable
fun ErrorText(modifier: Modifier, errorMessage: String?) {
    Text(
        modifier = modifier,
        text = errorMessage ?: "",
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.error)
    )
}