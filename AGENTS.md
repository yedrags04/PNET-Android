# AGENTS.md - HeistCraft Android

## Proyecto
HeistCraft es una aplicación Android paródica de "reserva de bancos y utensilios para robarlos" desarrollada con JetPack Compose.

## Build y Ejecución
- `./gradlew assembleDebug` - Compilar APK de debug
- `./gradlew assembleRelease` - Compilar APK de release
- Emulador Android → API pointing to `http://10.0.2.2:8080/` (configurado en `app/build.gradle.kts`)

## UI/UX
### Colores
Fuente de verdad en `app/src/main/res/values/colors.xml`. En Compose, usar `HeistPalette` (`ui/theme/Color.kt`) o `colorResource` / `MaterialTheme.colorScheme` según el caso (`Theme.kt`).

Nunca hardcodear colores en código.

### Tipografías
- **DmSerifDisplay**: Títulos, headings, elementos que deban destacar (`displayLarge`, `headline*`, `title*`, `labelLarge`)
- **Dmsans**: Texto normal, elementos interactuables, labels (`body*`, `labelMedium`, `labelSmall`)

Ver `app/src/main/kotlin/com/heistcorp/heistcraft/ui/theme/Type.kt`.

## Convenciones
- Material 3 con tema oscuro (`darkColorScheme`)
- Navigation Compose para rutas
- `@OptIn(ExperimentalMaterial3Api::class)` donde se use Material3 experimental
- Usar `MaterialTheme.colorScheme` para acceder a colores del tema

## Workflow
- **Antes de cambios sustanciales**: Advertir al usuario y pedir confirmación explícita
- **Por defecto**: Hacer cambios muy poco a poco; no hacer más de lo solicitado
- Antes de commits: ejecutar `./gradlew check` si existe, o al menos verificar build

## Estructura clave
- `app/src/main/kotlin/com/heistcorp/heistcraft/` - Paquete principal
  - `app/` - Estado global y App composable
  - `navigation/` - NavHost y Destination
  - `screens/` - Pantallas (BancosScreen, UtensiliosScreen, etc.)
  - `network/` - ApiClient, HeistApi (Retrofit)
  - `data/` - DTOs y parseo JSON
  - `ui/theme/` - HeistPalette, Theme, Typography
- `app/src/main/res/values/` - `colors.xml`, `themes.xml`, `strings.xml`, `dimens.xml`