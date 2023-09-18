package com.example.notesapp.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.notesapp.R

@Composable
fun GoogleSignInButton(
    signInClicked: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .padding(start = 30.dp, end = 30.dp)
                .height(55.dp)
                .fillMaxWidth()
                .clickable {
                    signInClicked()
                },
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(width = 1.5.dp, color = Color.Black),
            elevation = 5.dp,
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .size(20.dp)
                        .padding(0.dp)
                        .align(Alignment.CenterVertically),
                    painter = painterResource(id = R.drawable.ic_google_logo),
                    contentDescription = "Logo Google"

                )
                Text(
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .align(Alignment.CenterVertically),
                    text = "Accede con Google",
                    fontSize = MaterialTheme.typography.h6.fontSize
                )
            }

        }
    }

}

@Preview
@Composable
fun PreviewGoogleSignInButton() {
    GoogleSignInButton(signInClicked = {})
}