# Instazoo KMP App - An Instagram Clone Built with Compose Multiplatform

[![Instazoo KMP App Preview](Instazoo/screenshots/Instazoo-banner-image.png)](https://youtu.be/D9Qnz9lglu0)
Click the image above to watch a YouTube demo of the Instazoo KMP App.

## Overview
Instazoo KMP is a clone of Instagram built with Compose Multiplatform. This project supports Android, iOS, and desktop platforms and fetches data from a static API hosted on GitHub Pages. The primary purpose of this project is to provide a hands-on experience with KMP Compose Multiplatform.

> **Note:**
> This app is still under development.

## Features

- The feed displays posts with like, comment, and share options.
- A horizontal list of stories appears at the top of the home screen.
- The User Stories view enables smooth transitions between different stories.
- Comments on posts can be accessed through a sliding bottom sheet.
- A bottom navigation bar allows users to switch between various app sections.
- The search section features a staggered grid layout showcasing random items by default.
- The app supports responsive loading for requests and images, enhancing performance.
- User profiles display details such as name, bio, profile picture, followers, followings, and posts.
- The profile screen presents user posts in a grid layout.
- Custom fonts are used throughout the app for a unique visual style.
- Both dark and light themes are available to enhance user experience.
- The app supports adaptive design for both mobile and desktop interfaces.

# Technologies:

- Kotlin
- Clean Architecture
- Compose UI
- Network Requests
- Local Database
- Dependency Injection
- Compose Navigation

## Libraries 🛠️

- [Koin](https://insert-koin.io/) - Kotlin dependency injection library with multiplatform support.
- [Ktor](https://ktor.io/docs/http-client-multiplatform.html) - Provides multiplatform libraries required to make
  network calls to the REST API.
- [SQLDelight](https://cashapp.github.io/sqldelight/multiplatform_sqlite/) - Cross-Platform database library
- [kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines) - Library support for Kotlin coroutines with
  multiplatform support.
- [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) - Provides sets of libraries for various
  serialization formats eg. JSON, protocol buffers, CBOR, etc.
- [Voyager](https://voyager.adriel.cafe/) - A Multiplatform navigation library
- [Kamel](https://github.com/Kamel-Media/Kamel) - Asynchronous media loading library for Compose Multiplatform
