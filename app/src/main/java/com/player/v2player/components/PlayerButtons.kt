package com.player.v2player.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.player.v2player.R

@Composable
fun PlayerButton(
    onClick: () -> Unit,
    icon: Int? = 0,
    text: String? = "",
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(50.dp)
            ),
        colors = ButtonDefaults.buttonColors(Color.Transparent),
    ) {
        icon?.let {
            Image(
                painter = painterResource(id = it),
                contentDescription = "",
                modifier = Modifier.size(45.dp)
            )
        }
        text?.let {
            Text(
                text = it,
                fontFamily = FontFamily(Font(R.font.raleway_bold)),
                fontSize = 18.sp
            )
        }
    }
}