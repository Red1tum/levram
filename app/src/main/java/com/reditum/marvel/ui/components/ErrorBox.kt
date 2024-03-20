package com.reditum.marvel.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.reditum.marvel.R
import com.reditum.marvel.ui.theme.MarvelTheme
import com.reditum.marvel.ui.theme.Sizes

@Composable
fun ErrorBox(
    tryAgain: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(stringResource(R.string.couldnt_get_information), color = MaterialTheme.colorScheme.surface)
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            ),
            shape = RoundedCornerShape(Sizes.roundedShapeClipping),
            onClick = tryAgain
        ) {
            Text(stringResource(R.string.try_again))
        }
    }
}

@Preview
@Composable
fun ErrorBoxPreview() {
    MarvelTheme {
        ErrorBox(tryAgain = {})
    }
}