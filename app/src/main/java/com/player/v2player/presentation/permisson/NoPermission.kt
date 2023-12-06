package com.player.v2player.presentation.permisson

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.player.v2player.R
import com.player.v2player.ui.theme.LocalColors

@Composable
fun NoPermission(text: String, image: Int) {
    val painter = painterResource(id = image)
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("permission.json"))
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.9f),
        verticalArrangement = Arrangement.Center
    ) {
//        Text(
//            text = "PERMISSION DENIED!!",
//            modifier = Modifier.align(Alignment.CenterHorizontally),
//            fontFamily = FontFamily(
//                Font(R.font.raleway_bold)
//            ),
//            color = LocalColors.Black,
//            textAlign = TextAlign.Center,
//            fontSize = 18.sp
//        )
        Spacer(modifier = Modifier.height(45.dp))
        LottieAnimation(
            composition = composition,
            iterations = Int.MAX_VALUE,
            modifier = Modifier
                .padding(top = 0.dp)
                .requiredHeight(250.dp)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = text,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontFamily = FontFamily(
                Font(R.font.raleway_medium)
            ),
            color = LocalColors.Purple.copy(alpha = 0.9f),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
        )
    }


}