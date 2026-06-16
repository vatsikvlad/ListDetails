# Szlaki Przygód (Adventure Trails)
​
 A modern Android application for hiking and cycling enthusiasts to discover, track, and record their outdoor adventures. Built with Jetpack Compose and Material Design 3.
​
 ## Main Features
​
 - **Trail Discovery**: Browse a curated list of hiking and cycling trails with detailed descriptions and distance information.
 - **Smart Search**: Quickly find specific trails using the integrated search bar with real-time filtering.
 - **Activity Tracker**: Integrated stopwatch feature allowing users to measure the duration of their trips.
 - **Personal Records**: Save your trail completion history. Each record includes the date, time, and duration, stored locally using a database.
 - **Interactive UI**: Dynamic UI elements that respond to device tilt using the accelerometer sensor, providing a unique and engaging experience.
 - **Adaptive Interface**: Specialized layouts for both smartphones (**PhoneLayout**) and tablets (**TabletLayout**) that optimize the user experience based on screen real estate.
 - **Customization**: Full support for Dark/Light mode and dynamic Material You color schemes (Android 12+).
 - **Professional Splash Screen**: Implementation of the modern Android Splash Screen API combined with custom Compose animations.
​
 ## Tech Stack
​
 - **Language**: [Kotlin](https://kotlinlang.org/)
 - **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Declarative UI)
 - **Architecture**: MVVM (Model-View-ViewModel)
 - **Database**: [Room Persistence Library](https://developer.android.com/training/data-storage/room) (SQLite)
 - **Design System**: [Material Design 3](https://m3.material.io/) (Material You)
 - **Navigation**: [Jetpack Navigation Compose](https://developer.android.com/jetpack/compose/navigation)
 - **Sensors**: Android Sensor API (Accelerometer integration)
 - **Networking**: [Retrofit](https://square.github.io/retrofit/) & Gson (infrastructure for remote data)
​
 ## Project Architecture
​
 The project follows clean architecture principles and the **MVVM** pattern to ensure maintainability:
​
 - `data/`: Contains Room database definitions, entities (`Trasa`), and DAOs for local data persistence.
 - `viewmodel/`: Business logic, state handling, and coordination between data and UI (`ElementsViewModel`).
 - `ui/phone/` & `ui/tablet/`: Specific UI implementations tailored for different form factors.
 - `ui/components/`: Reusable Compose components such as `StopwatchDisplay`, `SearchTopBar`, and the custom `SplashScreen`.
 - `ui/theme/`: Centralized definitions for colors, typography, and Material 3 theme configurations.
​
