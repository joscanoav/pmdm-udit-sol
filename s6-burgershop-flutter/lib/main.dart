import 'package:flutter/material.dart';

// main(): el punto de arranque de CUALQUIER app Flutter. Siempre
// existe, siempre llama a runApp(). Es el equivalente a onCreate()
// en vuestra MainActivity de Kotlin.
void main() {
  runApp(const BurgerShopApp());
}

// ===========================================================
// APP RAÍZ
// Todo widget en Flutter es una clase. StatelessWidget significa
// "no tiene datos que cambien con el tiempo" — es fijo una vez
// dibujado. Es el equivalente de un @Composable normal en Kotlin
// (sin remember/mutableStateOf).
// ===========================================================
class BurgerShopApp extends StatelessWidget {
  // "const" + "super.key": boilerplate que Flutter pide siempre
  // en el constructor. No hace falta explicarlo a fondo, solo que
  // "siempre va así".
  const BurgerShopApp({super.key});

  // build(): el método que TODO widget debe tener. Aquí es donde
  // "dibujas" lo que se ve en pantalla. Se llama automáticamente
  // cada vez que Flutter necesita redibujar.
  @override
  Widget build(BuildContext context) {
    // MaterialApp: el "contenedor raíz" de toda la app — aplica
    // el tema de colores y le dice a Flutter cuál es la primera
    // pantalla (home). Equivalente a vuestro
    // Reto1TarjetaPresentacionTheme { ... } envolviendo todo.
    return MaterialApp(
      debugShowCheckedModeBanner: false, // quita la cinta roja "DEBUG" de la esquina
      title: 'BurgerShop',

      theme: ThemeData(
        // COLOR DE LA APP: esta es la ÚNICA línea que hay que tocar para
        // cambiar el aspecto. Flutter genera toda la paleta (botones, barra
        // superior, precios...) a partir de este color "semilla".
        //
        // Opción 1: colores predefinidos de Flutter
        //   Colors.deepOrange, Colors.teal, Colors.indigo, Colors.green...
        // Opción 2: tu propio color con código hexadecimal
        //   const Color(0xFF6650A4)  -> "0xFF" + los 6 dígitos del color
        colorSchemeSeed: Colors.deepOrange,
        useMaterial3: true,
      ),
      home: const PantallaCatalogo(), // la primera pantalla que se ve al abrir la app
    );
  }
}

// ===========================================================
// MODELO DE DATOS
// En Dart no existe "data class" como en Kotlin: una clase normal
// con un constructor que recibe los campos hace el mismo trabajo.
// ===========================================================
class Producto {
  // "final": el valor no cambia una vez creado el objeto (como "val" en Kotlin).
  final String nombre;
  final String precio;
  final String imagenAsset; // la RUTA del archivo (texto), no un ID numérico como R.drawable

  // Constructor: "required" obliga a pasar siempre estos tres datos
  // al crear un Producto, para que no se nos olvide ninguno.
  Producto({
    required this.nombre,
    required this.precio,
    required this.imagenAsset,
  });
}

// ===========================================================
// DATOS DE PRUEBA
// Los mismos productos que en Kotlin, apuntando a los MISMOS
// archivos que copiasteis a assets/images. "final List<Producto>"
// = una lista que no se puede reasignar, pero cuyo contenido sí
// podríamos modificar si hiciera falta.
// ===========================================================
final List<Producto> catalogoHamburguesas = [
  Producto(
    nombre: 'Clásica con Queso',
    precio: '6,50 €',
    imagenAsset: 'assets/images/burger_clasica.jpg',
  ),
  Producto(
    nombre: 'BBQ Bacon',
    precio: '7,90 €',
    imagenAsset: 'assets/images/burger_bbq.jpg',
  ),
  Producto(
    nombre: 'Doble Carne',
    precio: '8,50 €',
    imagenAsset: 'assets/images/burger_doble.jpg',
  ),
  Producto(
    nombre: 'Vegetariana',
    precio: '7,20 €',
    imagenAsset: 'assets/images/burger_vegetariana.jpg',
  ),
  Producto(
    nombre: 'Picante Jalapeño',
    precio: '7,80 €',
    imagenAsset: 'assets/images/burger_picante.jpg',
  ),
  Producto(
    nombre: 'Pollo Crispy',
    precio: '6,90 €',
    imagenAsset: 'assets/images/burger_pollo.jpg',
  ),
];

// ===========================================================
// PANTALLA 1: CATÁLOGO RESPONSIVE (RETO 2)
// Equivalente a vuestro @Composable fun CatalogoHamburguesas()
// con LazyVerticalGrid.
// ===========================================================
class PantallaCatalogo extends StatelessWidget {
  const PantallaCatalogo({super.key});

  @override
  Widget build(BuildContext context) {
    // Scaffold: la "estructura" estándar de una pantalla Android/iOS
    // (barra superior + cuerpo). Vosotros en Kotlin usabais Surface
    // "pelado"; Scaffold os da la barra de título de regalo.
    return Scaffold(
      appBar: AppBar(title: const Text('🍔 BurgerShop')), // barra superior con el título

      // LayoutBuilder: nos da el ANCHO disponible de la pantalla. Cambia
      // al girar el móvil, y así recalculamos las columnas.
      body: LayoutBuilder(
        builder: (context, constraints) {
          // Mismo cálculo que GridCells.Adaptive(minSize = 250.dp) de Compose:
          // "cuántas tarjetas de al menos 250 caben en el ancho disponible".
          const double anchoMinimo = 250; // = minSize de Kotlin
          const double espacio = 16;      // hueco entre tarjetas y margen exterior
          final double anchoUtil = constraints.maxWidth - (espacio * 2);
          int columnas = ((anchoUtil + espacio) / (anchoMinimo + espacio)).floor();
          if (columnas < 1) columnas = 1; // como mínimo 1 columna

          // GridView.builder: el equivalente de LazyVerticalGrid en Compose.
          // Solo dibuja en memoria lo que se ve en pantalla (como "lazy").
          return GridView.builder(
            padding: const EdgeInsets.all(espacio), // margen alrededor de la rejilla
            gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
              crossAxisCount: columnas, // móvil vertical: 1; horizontal: 3 (como en Kotlin)
              mainAxisSpacing: espacio, // hueco vertical entre tarjetas
              crossAxisSpacing: espacio, // hueco horizontal entre columnas
              // ALTO fijo de cada tarjeta. La imagen (Expanded) se ajusta
              // al espacio que quede, y el texto y el botón siempre caben.
              //   - Si se corta el contenido: SÚBELO (prueba 360)
              //   - Si sobra mucho hueco:     BÁJALO (prueba 320)
              mainAxisExtent: 340,
            ),
            itemCount: catalogoHamburguesas.length, // cuántos elementos tiene la lista
            itemBuilder: (context, index) {
              // Esta función se llama una vez POR CADA elemento visible.
              final producto = catalogoHamburguesas[index];
              return TarjetaProducto(producto: producto);
            },
          );
        },
      ),
    );
  }
}

// ===========================================================
// TARJETA DE PRODUCTO
// Calcada de vuestra TarjetaProducto() de Kotlin: imagen arriba,
// datos y botón debajo.
// ===========================================================
class TarjetaProducto extends StatelessWidget {
  final Producto producto; // el dato que esta tarjeta necesita para dibujarse
  const TarjetaProducto({super.key, required this.producto});

  @override
  Widget build(BuildContext context) {
    // Card: una "caja" con sombra y bordes redondeados por defecto,
    // igual que vuestro Card de Compose.
    return Card(
      margin: EdgeInsets.zero,       // el hueco entre tarjetas ya lo da la rejilla
      clipBehavior: Clip.antiAlias,  // recorta la imagen a las esquinas redondeadas automáticamente

      // Column: apila elementos de arriba a abajo, igual que en Compose.
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start, // alinea el texto a la izquierda
        children: [

          // Expanded: la imagen ocupa TODO el alto que sobre después de
          // colocar el texto y el botón. Es como Modifier.weight(1f) en
          // Compose. Por eso NO lleva "height" fijo: así el texto y el
          // botón siempre se ven completos, en vertical y en horizontal.
          Expanded(
            // Image.asset: el equivalente de painterResource(R.drawable...),
            // pero apuntando a la RUTA del archivo en vez de a un ID numérico.
            child: Image.asset(
              producto.imagenAsset,
              width: double.infinity, // ocupa todo el ancho disponible
              fit: BoxFit.cover,      // = ContentScale.Crop en Kotlin: recorta sin deformar
            ),
          ),

          // Segunda Column, con margen interior, para el texto y el botón.
          Padding(
            padding: const EdgeInsets.all(12),
            child: Column(
              mainAxisSize: MainAxisSize.min, // ocupa solo lo que necesita su contenido
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  producto.nombre,
                  style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                ),
                const SizedBox(height: 4), // = Spacer(height) en Kotlin: hueco pequeño

                Text(
                  producto.precio,
                  style: TextStyle(
                    fontSize: 16,
                    color: Theme.of(context).colorScheme.primary, // color de acento del tema actual
                  ),
                ),
                const SizedBox(height: 8), // hueco algo mayor, antes del botón

                SizedBox(
                  width: double.infinity, // el botón ocupa todo el ancho
                  child: ElevatedButton(
                    onPressed: () {
                      // De momento no hace nada "de verdad": solo lo
                      // demostramos en la consola. La semana que viene
                      // (Reto 3) esto sumará al carrito real.
                      print('¡Añadido: ${producto.nombre}!');
                    },
                    child: const Text('Añadir al carrito'),
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}