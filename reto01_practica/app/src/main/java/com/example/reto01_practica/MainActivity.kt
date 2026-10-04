package com.example.reto01_practica

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.reto01_practica.ui.theme.Reto01_practicaTheme


// Main activity : "La puerta de entrada de la app
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            //Aplicamos el tema de colores de proyecto a todo lo de dentro
            Reto01_practicaTheme {

                // Surface = "el lienzo de fondo que icuoa la pantalla
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TarjetaPresentacion()
                }

                // Se usan cuando la barra superior con titulo obarra de navegacion finfoerio con un boton flotante

/*
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )*/
                }
            }
        }
    }


// PILDORA TECNICA : La tarjeta de presentacion
// @Composable esta funcion dibuja algo en la pantalla
@Composable
fun TarjetaPresentacion () {
    //LocalContext: asi un Composable"pide prestado el contexto de Android
    // Lo necesitaos para poder abrir el navegador desde el boton
    val context = LocalContext.current


    // 1. COLUMN: aplia los elementos de arriba a aabjo (flexbox)

    Column(
        modifier = Modifier
            .fillMaxSize() // ocupa toda la pantalla
            .padding(16.dp), // margen para que nada toque los bordes
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        //2.IMAGE

        Image(
            painter = painterResource(id = R.drawable.foto_perfil),
            contentDescription = "foto",
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop

        )

        // Hueco vacio entre imagen y texto

        Spacer(Modifier.height(24.dp))

        // TEXT nombre
        Text(
            text = "Desarrollador MERN & Docente DAM",
            fontSize = 32.sp,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(Modifier.height(32.dp))
        // BUTTON
        Button(
            // Inten action vier, le decimos a android quier vereste recurso
            // y el sistema decide que app usar normane el naverdaro
            // Uri.parse converte el texto de la url en el formato que andriud
            //star activit lanza esa accion
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW,Uri.parse("https://github.com/joscanoav"))
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text(text = "Mi perfilde Github")

        }

    }


}

@Preview(showBackground = true)
@Composable
fun TarjetaPreview() {
    Reto01_practicaTheme {
        TarjetaPresentacion()
    }
}
