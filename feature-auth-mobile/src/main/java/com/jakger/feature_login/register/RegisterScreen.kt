package com.jakger.feature_login.register

// Pole: Hasło
//                OutlinedTextField(
//                    value = "password",
//                    onValueChange = {  },
//                    label = { "Hasło" },
//                    leadingIcon = {
//                        Icon(Icons.Default.Lock, contentDescription = "Hasło")
//                    },
//                    trailingIcon = {
//                        val image = if (passwordVisible)
//                            Icons.Filled.Visibility
//                        else
//                            Icons.Filled.VisibilityOff
//                    },
//                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//                    modifier = Modifier.fillMaxWidth(),
//                    singleLine = true,
//                    shape = MaterialTheme.shapes.medium
//                ){
//                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
//                        Icon(imageVector = image, contentDescription = "Pokaż/ukryj hasło")
//                    }
//                }