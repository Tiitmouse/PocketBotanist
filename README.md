# PocketBotanist - Your Digital Plant Companion 🪴

PocketBotanist is a comprehensive Android application designed to help users manage and care for their plants. It features a multi-language interface, persistent local storage, background notifications for watering reminders, and dynamic data fetching from a remote API. The app is built with modern Android technologies, following best practices for architecture, security, and user experience.

## Program Showcase

*(Application screenshots to be added here later)*

## Core Features

This project implements a wide range of modern Android features to provide a robust and user-friendly experience.

* **Multi-Language Support**: The app is fully localized in English, Croatian, and Spanish, adapting automatically based on user settings.

* **Modern UI/UX**: Built with Material Design components, including `RecyclerView`, `CardView`, `ViewPager2`, and a `NavigationDrawer` for a clean and intuitive user interface.

* **Persistent Storage**: All plant and notification data is stored locally in a SQLite database, ensuring data is available offline. CRUD operations are managed through a `ContentProvider` and a Repository pattern.

* **Background Notifications**: Using `WorkManager`, the app schedules reliable background tasks to remind users to water their plants, even if the app is closed or the device is restarted.

* **API Integration**: The app can fetch data from a remote web service (mockapi.io) using Retrofit and Gson, allowing for features like resetting the plant list from a server source.

* **Security**: Implements best practices for security, including code obfuscation with R8, runtime permissions for notifications, and secure handling of application signing keys.

* **Adaptive UI**: The user interface is designed to work across various screen sizes and orientations, with custom dimensions and layouts.

* **Custom Animations**: Features a custom splash screen, animated onboarding transitions, and other subtle animations to enhance the user experience.

## Technical Stack & Architecture

The application is built using a modern, scalable architecture based on Google's recommendations.

* **Language**: **Kotlin**

* **Architecture**: MVVM (Model-View-ViewModel) with a Repository pattern

* **UI**: XML Layouts with ViewBinding

* **Navigation**: Jetpack Navigation Component (Single-Activity architecture)

* **Asynchronous Operations**: Kotlin Coroutines

* **Networking**: Retrofit & Gson

* **Database**: SQLite with `SQLiteOpenHelper`

* **Background Tasks**: Jetpack WorkManager

* **Dependency Management**: Gradle with `libs.versions.toml`

### Project Structure

The project is organized into packages based on functionality:

* **`api/`**: Contains Retrofit service interfaces and data models for network communication.

* **`adapter/`**: Holds `RecyclerView` and `ViewPager2` adapters.

* **`dal/`**: Data Access Layer, including `SQLiteOpenHelper` and `ContentProvider`.

* **`model/`**: Contains the Kotlin `data class` objects (`Plant`, `Notification`).

* **`receiver/`**: `BroadcastReceiver` implementations (e.g., for device boot).

* **`repository/`**: The `PlantRepository` class, which serves as a single source of truth for data.

* **`ui/`**: Contains all UI-related components (Fragments and ViewModels), organized by feature.

* **`utils/`**: Helper classes and extension functions.

* **`worker/`**: `WorkManager` worker classes for background tasks.

## Quick Start

To run this project, you'll need Android Studio.

1. **Clone the repository:**
