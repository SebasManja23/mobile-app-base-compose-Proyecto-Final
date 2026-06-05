# MaizApp - Gestión de Inventario Agrícola

**MaizApp** es una aplicación móvil profesional desarrollada en Kotlin y Jetpack Compose diseñada para optimizar el control de inventario de granos (maíz, tamo, mochote) en bodegas y parcelas.

---

##  Características Principales

### Integración con Mercados Reales (API)
* Consulta en tiempo real del precio internacional del maíz mediante la API de **Alpha Vantage**.
* Interfaz compacta (Banner) en el menú principal con indicadores de carga y última actualización.
* Mapeo dinámico de productos para mantener la consistencia entre datos externos y el idioma de la app.

###  Soporte Multilenguaje Robusto (ES/EN)
* Aplicación 100% localizable en Español e Inglés.
* **Arquitectura de Datos Segura:** Se utiliza un sistema de "Claves Técnicas" (Keys) para la base de datos, garantizando que los cálculos de inventario no se rompan al cambiar el idioma del dispositivo.

###  Geolocalización GPS
* Captura automática de coordenadas (Latitud/Longitud) al registrar cada entrada o salida de grano.
* Integración con **Google Maps** desde el historial para visualizar el punto exacto de la operación mediante Intents implícitos.
* Gestión dinámica de permisos de ubicación en tiempo real.

###  Persistencia de Datos Offline-First
* Base de datos local con **Room** para garantizar el funcionamiento sin conexión a internet.
* Control estricto de existencias (Stock) que evita registros de salida si no hay inventario suficiente.

---

## Arquitectura y Tecnologías

La aplicación sigue las mejores prácticas de desarrollo Android moderno:

*   **Arquitectura:** MVVM (Model-View-ViewModel) con separación clara de responsabilidades.
*   **UI:** Jetpack Compose (UI Declarativa y moderna).
*   **Red:** Retrofit 2 + GSON para consumo de servicios REST.
*   **Base de Datos:** Room (SQLite con abstracción de objetos).
*   **Ubicación:** Fused Location Provider de Google Play Services.
*   **Navegación:** Navigation Compose con rutas selladas.

### Estructura de Capas
```text
com.fic.mobile_app_base_compose/
├── data/
│   ├── local/      # Room (Database, DAOs)
│   ├── model/      # Entidades y Constantes técnicas
│   └── remote/     # Retrofit (API Services, Responses)
├── ui/
│   ├── navigation/ # NavHost y Rutas
│   ├── screens/    # Pantallas de la aplicación
│   └── theme/      # Estilos, Colores y Tipografía
├── viewmodel/      # Lógica de negocio y estados (StateFlow)
└── util/           # Clases auxiliares (SelectionOption)
```

---

##  Instalación y Requisitos

1.  **Android Studio:** Ladybug o superior.
2.  **SDK:** Compile SDK 36.
3.  **Permisos:** La aplicación requiere acceso a Internet y Ubicación (GPS).

---

##  Evaluación Técnica
Este proyecto cumple con los siguientes criterios:
*    **MVVM:** Separación total de lógica y vista.
*    **UI/UX:** Diseño responsivo, manejo de estados y multilenguaje.
*    **API:** Integración real con servicios web.
*    **Persistencia:** Funcionamiento offline garantizado con Room.
*    **Funcionalidad Extra:** Geolocalización y mapas integrada.

---

## Autor
Proyecto final desarrollado para la materia de Cómputo Móvil.
