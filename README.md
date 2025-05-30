
# CryptoWalletApp
A feature-rich Crypto Wallet simulation app built with Kotlin using Firebase (Auth, Firestore, Storage), Retrofit, Coroutines, Navigation Component, and modern Android libraries to offer real-time data, secure authentication, profile customization, and a smooth multi-screen user experience.

# 💰 Crypto Wallet Simulation App

A feature-rich and modern Android application built with Kotlin that simulates a virtual crypto wallet. Users can sign in securely, manage their profile, and perform virtual deposit and withdrawal operations — all supported by Firebase and real-time data.

# 💰 Crypto Wallet Simulation App **APK**

[🎭 APK](https://drive.google.com/file/d/1ZU906jFJwWZymbZ5aHR2KORoXIk7aEmh/view?usp=drive_link)

---

## 🚀 Features

- 🔐 **User Authentication** — Firebase Authentication ensures secure sign-up and login processes.
- ☁️ **Cloud Firestore** — Real-time data handling of user transactions and profiles.
- 🖼️ **Firebase Storage** — Profile pictures are uploaded and retrieved via cloud storage.
- 💱 **Live Crypto Data** — Real-time crypto price information is fetched using **Retrofit**, **Coroutines**, and **Gson** from the CoinMarketCap API.
- 🧭 **Navigation Component + SafeArgs** — Seamless and type-safe fragment transitions using Jetpack’s Navigation architecture and SafeArgs.
- 🎨 **Modern UI** — Designed with Material Components like Bottom Navigation, Toolbar, Progress Bars, and responsive multi-screen layouts.
- 🖼️ **Image Cropping & Display** — Profile pictures are cropped into a circular shape using **uCrop** and displayed with **Glide**.
- 🔊 **Sound Effects** — MediaPlayer is used to enhance user experience with interaction-based audio feedback.
- 📱 **Responsive Design** — The app supports multiple screen sizes and orientations.
  
---

## 🛠️ Tech Stack

| Category                | Tools & Libraries |
|-------------------------|-------------------------------------------|
| Language                | Kotlin                                    |
| Authentication          | Firebase Authentication                   |
| Database                | Firebase Firestore                        |
| Storage                 | Firebase Cloud Storage                    |
| API Integration         | Retrofit + Gson Converter                 |
| Async Operations        | Kotlin Coroutines                         |
| UI Navigation           | Android Navigation Component + SafeArgs   |
| Image Handling          | Glide, uCrop                              |
| Design                  | Material Design Components                |
| Media                   | MediaPlayer                               |

---

## 📦 Dependencies

```kotlin
// Firebase BoM & Analytics
implementation("com.google.firebase:firebase-bom:33.13.0")
implementation("com.google.firebase:firebase-analytics-ktx:22.4.0")

// Firebase Authentication
implementation("com.google.firebase:firebase-auth-ktx:23.2.0")

// Firebase Firestore & Storage
implementation("com.google.firebase:firebase-firestore:25.1.4")
implementation("com.google.firebase:firebase-storage:21.0.1")

// Retrofit & Gson
implementation("com.squareup.retrofit2:retrofit:2.11.0")
implementation("com.squareup.retrofit2:converter-gson:2.11.0")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.10.2")

// Navigation
implementation("androidx.navigation:navigation-fragment:2.7.7")
implementation("androidx.navigation:navigation-ui:2.7.7")

// Material Design
implementation("com.google.android.material:material:1.12.0")

// Image Processing
implementation("com.github.bumptech.glide:glide:4.16.0")
implementation("com.github.yalantis:ucrop:2.2.10")
```


----------------------------------------------------------------------------------

# 💰 Kripto Cüzdan Simülasyon Uygulaması

Kotlin ile geliştirilmiş, kapsamlı ve modern bir Android uygulamasıdır. Kullanıcıların sanal olarak bir kripto cüzdana sahip olmasını sağlar. Kullanıcılar güvenli bir şekilde giriş yapabilir, profillerini yönetebilir, para yatırma ve çekme işlemlerini gerçekleştirebilir. Tüm bu işlemler Firebase ve gerçek zamanlı veri desteğiyle yönetilmektedir.

---

## 🚀 Özellikler

- 🔐 **Kullanıcı Giriş Sistemi** — Firebase Authentication ile güvenli kayıt ve giriş işlemleri.
- ☁️ **Gerçek Zamanlı Veri** — Cloud Firestore ile kullanıcı işlemleri ve profilleri gerçek zamanlı olarak yönetilir.
- 🖼️ **Fotoğraf Depolama** — Kullanıcı profil fotoğrafları Firebase Storage ile bulutta saklanır.
- 💱 **Canlı Kripto Veri Akışı** — CoinMarketCap API’sinden **Retrofit**, **Coroutines** ve **Gson** kullanılarak alınan gerçek zamanlı kripto fiyat bilgileri.
- 🧭 **Navigation Component + SafeArgs** — Jetpack Navigation ve SafeArgs kullanılarak sorunsuz ve tür güvenli fragment geçişleri.
- 🎨 **Modern Arayüz** — Bottom Navigation, Toolbar, Progress Bar gibi Material bileşenleriyle tasarlanmış kullanıcı dostu arayüz.
- 🖼️ **Resim Kırpma ve Gösterimi** — **uCrop** ile yuvarlak biçimde kırpılmış profil fotoğrafları, **Glide** ile gösterilir.
- 🔊 **Ses Efektleri** — MediaPlayer ile kullanıcı etkileşimlerinde sesli geri bildirimler.
- 📱 **Responsive Tasarım** — Farklı ekran boyutları ve yönelimler için optimize edilmiştir.

---

## 🛠️ Kullanılan Teknolojiler

| Kategori         | Kullanılan Araç ve Kütüphaneler          |
|------------------|------------------------------------------|
| Programlama Dili | Kotlin                                   |
| Kimlik Doğrulama | Firebase Authentication                  |
| Veritabanı       | Firebase Firestore                       |
| Dosya Depolama   | Firebase Cloud Storage                   |
| API Entegrasyonu | Retrofit + Gson Converter                |
| Asenkron İşlemler| Kotlin Coroutines                        |
| Navigasyon       | Android Navigation Component + SafeArgs  |
| Görsel İşleme    | Glide, uCrop                             |
| Arayüz Tasarımı  | Material Design Bileşenleri              |
| Medya            | MediaPlayer                              |

---

## 📦 Bağımlılıklar

```kotlin
// Firebase BoM & Analytics
implementation("com.google.firebase:firebase-bom:33.13.0")
implementation("com.google.firebase:firebase-analytics-ktx:22.4.0")

// Firebase Authentication
implementation("com.google.firebase:firebase-auth-ktx:23.2.0")

// Firebase Firestore & Storage
implementation("com.google.firebase:firebase-firestore:25.1.4")
implementation("com.google.firebase:firebase-storage:21.0.1")

// Retrofit & Gson
implementation("com.squareup.retrofit2:retrofit:2.11.0")
implementation("com.squareup.retrofit2:converter-gson:2.11.0")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.10.2")

// Navigation
implementation("androidx.navigation:navigation-fragment:2.7.7")
implementation("androidx.navigation:navigation-ui:2.7.7")

// Material Design
implementation("com.google.android.material:material:1.12.0")

// Görsel İşleme
implementation("com.github.bumptech.glide:glide:4.16.0")
implementation("com.github.yalantis:ucrop:2.2.10")
```


## 📸 Screenshots


<img width="836" alt="1" src="https://github.com/user-attachments/assets/145a1e3c-057e-4af2-b03c-38ad63a7c1ab" />

<img width="882" alt="2" src="https://github.com/user-attachments/assets/3670faa5-cfb5-46cb-b6b6-423102b97e0c" />

<img width="882" alt="3" src="https://github.com/user-attachments/assets/8132e740-ba09-4ec7-a587-2b3f4d098f28" />

<img width="917" alt="4" src="https://github.com/user-attachments/assets/d5fd750e-b56c-475b-ac4a-dcad67400153" />

<img width="930" alt="5" src="https://github.com/user-attachments/assets/0f44d351-be00-46ef-894a-78b3418a2a37" />




## 📬 Contact

If you’d like to connect, feel free to reach out:

**Bora Yıldırım**

[🔗 LinkedIn](https://www.linkedin.com/in/borayldrmm/)    —    [📨 Mail](mailto:borayldrm@hotmail.com)



## 🏁 Final Note

This project was built as part of my journey through Android development using Kotlin. It showcases both my technical capabilities and attention to design and user experience. I’m proud to share this as a polished and practical demonstration of what I’ve learned so far.






<p align="center">
  <img src="https://github.com/user-attachments/assets/82ac4f5a-af6f-439f-b0fc-dc99b5a03e0c" alt="android" height="40" style="margin-right: 10px;"/>
  <img src="https://github.com/user-attachments/assets/c4898ca8-3069-4ba8-881e-a129d4418669" alt="androidstudio" height="40" style="margin-right: 10px;"/>
  <img src="https://github.com/user-attachments/assets/fa894ba7-aed6-44c9-8e4f-acf6521f6514" alt="authentication" height="40" style="margin-right: 10px;"/>
  <img src="https://github.com/user-attachments/assets/d816026b-df18-4e43-8861-aa74695a7f9a" alt="coroutine" height="40" style="margin-right: 10px;"/>
  <img src="https://github.com/user-attachments/assets/5165bf94-57d4-429a-b077-9e3e4d469c26" alt="firebase" height="40" style="margin-right: 10px;"/>
  <img src="https://github.com/user-attachments/assets/f4ad945d-0ae2-4883-9e02-f7a640b5fbff" alt="glide" height="40" style="margin-right: 10px;"/>
  <img src="https://github.com/user-attachments/assets/7bfcb865-a686-453f-813e-0df9240bb8a2" alt="json" height="40" style="margin-right: 10px;"/>
  <img src="https://github.com/user-attachments/assets/516a0514-065c-461f-992e-25ef3c397e05" alt="kotlin" height="40" style="margin-right: 10px;"/>
  <img src="https://github.com/user-attachments/assets/db6941e1-caff-443f-b98b-1b292867730c" alt="retrofit" height="40" style="margin-right: 10px;"/>
  <img src="https://github.com/user-attachments/assets/bb1e4569-9445-4be1-a5a8-e669d77da1c3" alt="ucrop" height="40" style="margin-right: 10px;"/>
  <img src="https://github.com/user-attachments/assets/6c2a22d7-41ba-4cee-b829-ab229640c3db" alt="firebasestorage" height="40"/>
</p>



