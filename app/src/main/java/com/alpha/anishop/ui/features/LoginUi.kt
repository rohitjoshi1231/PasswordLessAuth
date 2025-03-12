package com.alpha.anishop.ui.features

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.alpha.anishop.R

class LoginUi {

    private val poppinsFamily = FontFamily(
        Font(R.font.poppins_regular)
    )

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun ConstraintLayoutExample() {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            // Create references for images
            val (backgroundImage, profileImage, customTextField, buttonField, txt, title) = createRefs()

            // Background Image (Full Width)

            Text(text = "Password Less Authentication",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = poppinsFamily,
                modifier = Modifier.constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(profileImage.top)
                })

            Image(
                painter = painterResource(id = R.drawable.img),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(backgroundImage) {
                        top.linkTo(parent.top) // Align to top of parent
                    },
                contentScale = ContentScale.Crop
            )

            // Circular Profile Image (Centered)
            Image(
                painter = painterResource(id = R.drawable.profile_picture),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(153.dp) // Set image size
                    .clip(CircleShape) // Make it circular
                    .border(2.dp, Color.Gray, CircleShape) // Optional border
                    .constrainAs(profileImage) {
                        top.linkTo(backgroundImage.top, margin = 140.dp)
                        bottom.linkTo(backgroundImage.bottom) // Adjust position
                        start.linkTo(parent.start)
                        end.linkTo(parent.end) // Center horizontally
                    },
                contentScale = ContentScale.FillBounds
            )

            Text(text = "Login",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0, 130, 252),
                fontFamily = poppinsFamily,
                modifier = Modifier.constrainAs(txt) {
                    top.linkTo(profileImage.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(customTextField.top)
                })


            // Custom Text Field
            CustomTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
                    .constrainAs(customTextField) {
                        top.linkTo(profileImage.bottom, margin = 40.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                    }, labelText = "Enter contact number"
            )
            FilledTonalButtonExample(modifier = Modifier
                .size(width = 200.dp, height = 55.dp)
                .constrainAs(buttonField) {
                    top.linkTo(customTextField.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                }) {
                // implementation
            }
        }
    }


    @Composable
    fun FilledTonalButtonExample(modifier: Modifier, onClick: () -> Unit) {
        FilledTonalButton(
            modifier = modifier,
            onClick = { onClick() },
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color(0, 130, 252), // Keeps the button background transparent
                contentColor = Color.White // Text and icon color
            ),
            border = ButtonDefaults.outlinedButtonBorder.copy( // Custom border color
                brush = androidx.compose.ui.graphics.SolidColor(Color.Black)
            )
        ) {
            Text("Receive Otp", fontSize = 18.sp, fontFamily = poppinsFamily)

        }
    }

    @Composable
    fun CustomTextField(modifier: Modifier = Modifier, labelText: String = "label") {
        var text by remember { mutableStateOf(TextFieldValue("")) }



        OutlinedTextField(value = text,
            onValueChange = { newText -> text = newText },

            label = {
                Text(
                    labelText, fontSize = 18.sp, fontFamily = poppinsFamily
                )
            },
            modifier = modifier,
            textStyle = androidx.compose.ui.text.TextStyle(
                color = Color.Black, fontSize = 16.sp
            ),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp), // Curved corners
            leadingIcon = {
                Icon(Icons.Filled.Call, contentDescription = "Call", tint = Color.Black)
            })
    }
}
