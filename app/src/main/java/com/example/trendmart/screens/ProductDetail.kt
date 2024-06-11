package com.example.trendmart.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.trendmart.restapi.MainViewModel
import org.koin.compose.koinInject

@Composable
fun ProductDetail(
    navController: NavController,
    tittle: String?,
    pic: String?,
    price: String?,
    des: String?,
    category: String?,
    rating: String?,

    ) {

    val scroll = rememberScrollState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(top = 20.dp, bottom = 90.dp)
            .background(color = Color.White),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier
                .padding(start = 5.dp)
                .clip(CircleShape)
                .clickable { navController.popBackStack() }
                .size(40.dp)
                .background(Color.LightGray.copy(alpha = 0.50f)),
                contentAlignment = Alignment.Center) {
                Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = "")
            }
        }
        AsyncImage(
            model = pic, contentDescription = "", modifier = Modifier.size(350.dp)
        )

        Box(
            modifier = Modifier
                .padding(top = 20.dp)
                .clip(CircleShape)
                .size(40.dp)
                .background(Color.LightGray.copy(alpha = 0.50f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "1/1")
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .background(Color.LightGray.copy(alpha = 0.50f))
                .padding(top = 40.dp, start = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "$tittle",
                fontWeight = FontWeight.Medium,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "",
                    tint = Color(0XFFFCBF0C)
                )

                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "",
                    tint = Color(0XFFFCBF0C)
                )

                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "",
                    tint = Color(0XFFFCBF0C)
                )

                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "",
                    tint = Color(0XFFFCBF0C)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "$category",
                    modifier = Modifier.padding(end = 10.dp),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Text(text = "$rating", modifier = Modifier.padding(start = 4.dp, top = 6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp, start = 2.dp, end = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "$$price", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)

                Text(
                    text = "Available in stock", fontSize = 14.sp, fontWeight = FontWeight.SemiBold
                )
            }



            Text(
                text = "About",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 30.dp)
            )

            Text(
                text = "$des", modifier = Modifier.padding(top = 12.dp, start = 10.dp, end = 10.dp)
            )


            Row(
                modifier = Modifier
                    .clip(
                        shape = RoundedCornerShape(
                            topStart = 5.dp,
                            topEnd = 5.dp,
                            bottomStart = 5.dp,
                            bottomEnd = 5.dp
                        )
                    )
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 10.dp, end = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .width(44.dp)
                        .height(43.dp)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "35")
                }

                Box(
                    modifier = Modifier
                        .width(44.dp)
                        .height(43.dp)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "36")
                }

                Box(
                    modifier = Modifier
                        .width(44.dp)
                        .height(43.dp)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "37")
                }

                Box(
                    modifier = Modifier
                        .width(44.dp)
                        .height(43.dp)
                        .background(Color.Yellow),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "38")
                }
                Box(
                    modifier = Modifier
                        .width(44.dp)
                        .height(43.dp)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "39")
                }
                Box(
                    modifier = Modifier
                        .width(44.dp)
                        .height(43.dp)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "40")
                }
            }

            Button(
                onClick = {
                    Toast.makeText(context, "Add To Card SuccessFully", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0XFFF16A26)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(20.dp),
                shape = RoundedCornerShape(15.dp),
            ) {
                Text(text = "Add To Card")
            }

        }
    }
}