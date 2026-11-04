package com.example.lab12

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import android.Manifest
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.Marker

@Composable
fun MapScreen() {
    val context = LocalContext.current
    val hasLocationPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val ArequipaLocation = LatLng(-16.4040102, -71.559611) // Arequipa, Perú
    val cameraPositionState = rememberCameraPositionState {
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(ArequipaLocation, 12f)
    }

    var mapProperties by remember { 
        mutableStateOf(
            MapProperties(
                mapType = MapType.NORMAL,
                isMyLocationEnabled = hasLocationPermission
            )
        ) 
    }

    LaunchedEffect(Unit) {
        cameraPositionState.animate(
            update = CameraUpdateFactory.newLatLngZoom(LatLng(-16.2520984, -71.6836503), 12f), // Mover a Yura
            durationMs = 3000
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Añadir GoogleMap al layout
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties
        ) {
            // Añadir marcador en Arequipa, Perú
            Marker(
                state = rememberMarkerState(position = ArequipaLocation),
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE), // Icono azul
                title = "Arequipa, Perú"
            )

            val locations = listOf(
                LatLng(-16.433415, -71.5442652), // JLByR
                LatLng(-16.4205151, -71.4945209), // Paucarpata
                LatLng(-16.3524187, -71.5675994) // Zamacola
            )

            locations.forEach { location ->
                Marker(
                    state = rememberMarkerState(position = location),
                    title = "Ubicación",
                    snippet = "Punto de interés"
                )
            }

            val mallAventuraPolygon = listOf(
                LatLng(-16.432292, -71.509145),
                LatLng(-16.432757, -71.509626),
                LatLng(-16.433013, -71.509310),
                LatLng(-16.432566, -71.508853)
            )

            val parqueLambramaniPolygon = listOf(
                LatLng(-16.422704, -71.530830),
                LatLng(-16.422920, -71.531340),
                LatLng(-16.423264, -71.531110),
                LatLng(-16.423050, -71.530600)
            )

            val plazaDeArmasPolygon = listOf(
                LatLng(-16.398866, -71.536961),
                LatLng(-16.398744, -71.536529),
                LatLng(-16.399178, -71.536289),
                LatLng(-16.399299, -71.536721)
            )

            Polygon(
                points = plazaDeArmasPolygon,
                strokeColor = Color.Red,
                fillColor = Color.Blue,
                strokeWidth = 5f
            )

            Polygon(
                points = parqueLambramaniPolygon,
                strokeColor = Color.Red,
                fillColor = Color.Blue,
                strokeWidth = 5f
            )

            Polygon(
                points = mallAventuraPolygon,
                strokeColor = Color.Red,
                fillColor = Color.Blue,
                strokeWidth = 5f
            )

            // Ejemplos de Polilíneas (rutas/líneas)
            val rutaArequipa = listOf(
                LatLng(-16.398866, -71.536961), // Plaza de Armas
                LatLng(-16.4040102, -71.559611), // Arequipa centro
                LatLng(-16.422704, -71.530830)  // Parque Lambramani
            )

            Polyline(
                points = rutaArequipa,
                color = Color.Green,
                width = 8f
            )

            // Bonus: Dibujar un Círculo (por ejemplo, alrededor del Mirador de Yanahuara)
            Circle(
                center = LatLng(-16.3883, -71.5414), // Mirador de Yanahuara
                radius = 500.0, // Radio en metros
                strokeColor = Color.Magenta,
                strokeWidth = 6f,
                fillColor = Color.Magenta.copy(alpha = 0.3f)
            )
        }

        // Fila de botones superpuesta para cambiar el tipo de mapa
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Button(onClick = {
                mapProperties = mapProperties.copy(mapType = MapType.NORMAL)
            }) {
                Text("Normal")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                mapProperties = mapProperties.copy(mapType = MapType.HYBRID)
            }) {
                Text("Híbrido")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                mapProperties = mapProperties.copy(mapType = MapType.TERRAIN)
            }) {
                Text("Terreno")
            }
        }
    }
}
