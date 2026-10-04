package com.example.burgershop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Estos 7 imports son solo para PantallaPrincipal(), que está comentada
// más abajo. Si no la activas, Android Studio los marcará en gris como
// "no usados" — no pasa nada, no rompen la compilación.
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import kotlinx.coroutines.delay


// ===========================================================
// ACTIVITY PRINCIPAL
// La "puerta de entrada" de la app. Aquí casi nunca se toca
// nada más que la línea de dentro de Surface { ... }.
// ===========================================================
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            // MaterialTheme: aplica los colores y tipografías por defecto
            // a todo lo que haya dentro.
            MaterialTheme {

                // Surface: el "lienzo" de fondo que ocupa toda la pantalla.
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {

                    // Esta es la pantalla que se ve ahora mismo: el catálogo
                    // directamente, sin portada.
                    //CatalogoHamburguesas(catalogoHamburguesas)

                    // Si quisiéramos mostrar antes una portada de 1.5 segundos,
                    // cambiaríamos la línea de arriba por PantallaPrincipal()
                    // (función comentada al final de este archivo).
                    PantallaPrincipal()

                }
            }
        }
    }
}


// ===========================================================
// MODELO DE DATOS
// El "molde" que define qué información tiene cada producto.
// ===========================================================
data class Producto(
    val nombre: String,
    val precio: String,
    val imagenResId: Int   // el identificador de la imagen en res/drawable
)


// ===========================================================
// DATOS DE PRUEBA (hardcodeados)
// De momento viven aquí mismo, en el código. No vienen de ningún
// servidor ni base de datos — eso llegará más adelante, con APIs
// reales, en el Reto 5.
// ===========================================================
val catalogoHamburguesas = listOf(

    Producto(
        "Clásica con Queso",
        "6,50 €",
        R.drawable.burger_clasica
    ),

    Producto(
        "BBQ Bacon",
        "7,90 €",
        R.drawable.burger_bbq
    ),

    Producto(
        "Doble Carne",
        "8,50 €",
        R.drawable.burger_doble
    ),

    Producto(
        "Vegetariana",
        "7,20 €",
        R.drawable.burger_vegetariana
    ),

    Producto(
        "Picante Jalapeño",
        "7,80 €",
        R.drawable.burger_picante
    ),

    Producto(
        "Pollo Crispy",
        "6,90 €",
        R.drawable.burger_pollo
    )
)


// ===========================================================
// CATÁLOGO
// LazyColumn: pinta una lista que se puede recorrer con scroll
// vertical. Solo dibuja en memoria lo que se ve en pantalla
// (por eso se llama "lazy", perezoso): es eficiente aunque la
// lista tenga cientos de elementos.
// ===========================================================
@Composable
fun CatalogoHamburguesas(productos: List<Producto>) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),            // margen alrededor de toda la lista
        verticalArrangement = Arrangement.spacedBy(16.dp)  // espacio entre una tarjeta y la siguiente
    ) {

        // items() recorre la lista de productos y, por cada uno,
        // dibuja una TarjetaProducto. Es como un "for" pero pensado
        // para listas que se muestran en pantalla.
        items(productos) { producto ->

            TarjetaProducto(producto)

        }
    }
}


// ===========================================================
// TARJETA DE PRODUCTO
// Una "caja" (Card) con imagen arriba y datos + botón debajo.
// ===========================================================
@Composable
fun TarjetaProducto(producto: Producto) {

    // Card: una superficie elevada, con sombra y bordes redondeados
    // por defecto — ideal para agrupar visualmente la info de un producto.
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        // Column: apila sus elementos de arriba a abajo (como un
        // flexbox vertical). Aquí: imagen, y debajo, el bloque de texto.
        Column {

            Image(
                painter = painterResource(
                    id = producto.imagenResId
                ),
                contentDescription = producto.nombre,   // para accesibilidad (lectores de pantalla)
                modifier = Modifier
                    .fillMaxWidth()     // ocupa todo el ancho de la tarjeta
                    .height(180.dp),    // alto fijo — esto es justo lo que "se rompe" al rotar la pantalla
                contentScale = ContentScale.Crop   // recorta la imagen sin deformarla
            )

            // Segunda Column, con margen interior, para el texto y el botón.
            Column(
                modifier = Modifier.padding(12.dp)
            ) {

                Text(
                    text = producto.nombre,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))   // hueco pequeño

                Text(
                    text = producto.precio,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary   // color de acento del tema
                )

                Spacer(modifier = Modifier.height(8.dp))   // hueco un poco mayor, antes del botón

                Button(
                    onClick = {
                        // De momento no hace nada: la interactividad
                        // real (ej. un carrito de verdad) no toca en
                        // este reto.
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Añadir al carrito")
                }
            }
        }
    }
}


// ===========================================================
// PANTALLA DE PORTADA (OPCIONAL — EXTRA SI SOBRA TIEMPO)
// Está comentada a propósito: no aporta nada a lo que evalúa
// el Reto 2 (limitaciones de dispositivo, perfiles, emuladores).
// Actívala solo si algún alumno termina el catálogo antes de
// tiempo y quiere un reto extra.
// ===========================================================
@Composable
fun PantallaPrincipal() {

    // remember + mutableStateOf: crea una "variable que Compose vigila".
    // Cuando su valor cambia, Compose vuelve a dibujar la pantalla solo,
    // sin que tengas que hacer nada más.
    var mostrarPortada by remember { mutableStateOf(true) }

    // LaunchedEffect: lanza una tarea que se ejecuta una sola vez
    // cuando la pantalla aparece. Aquí: esperar 1.5 segundos y
    // luego cambiar el estado para ocultar la portada.
    LaunchedEffect(Unit) {
        delay(1500)
        mostrarPortada = false
    }

    // Dependiendo del valor de mostrarPortada, se dibuja una cosa
    // u otra. Esto simula un cambio de "pantalla" sin usar
    // navegación real (eso llegará con Flutter, en el Reto 3).
    if (mostrarPortada) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("🍔 BurgerShop", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        }
    } else {
        CatalogoHamburguesas(catalogoHamburguesas)
    }
}