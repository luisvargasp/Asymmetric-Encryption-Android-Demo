package com.lfvp.publickeyappdemo.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lfvp.publickeyappdemo.R

@Composable
fun EmptyContent(message: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(120.dp),
            painter = painterResource(id = R.drawable.ic_sad_face),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        )
        Text(
            text = message,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.titleSmall.fontSize
        )
    }
}

@Composable
@Preview
private fun EmptyContentPreview() {
    EmptyContent("No users found")
}