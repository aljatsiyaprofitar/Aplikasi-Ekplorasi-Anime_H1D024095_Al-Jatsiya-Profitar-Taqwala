# Aplikasi Eksplorasi Anime

Aplikasi mobile Android modern berbasis **Kotlin**, **Jetpack Compose**, **Material Design 3**, **Navigation Component**, dan **Arsitektur MVVM (Model-View-ViewModel-Repository)** untuk menjelajahi katalog anime secara dinamis menggunakan REST API dari [Tenrai API](https://api.tenrai.org/v1).

Proyek ini dibangun untuk memenuhi seluruh kriteria dan spesifikasi teknis penugasan **Aplikasi Eksplorasi Anime**.

---

## 📋 Daftar Isi
1. [Fitur Utama & Kepatuhan Spesifikasi](#-fitur-utama--kepatuhan-spesifikasi)
2. [Tangkapan Layar & Desain UI](#-tangkapan-layar--desain-ui)
3. [Arsitektur Aplikasi (MVVM)](#-arsitektur-aplikasi-mvvm)
4. [Pemanfaatan Fitur Bahasa Kotlin](#-pemanfaatan-fitur-bahasa-kotlin)
5. [Pengelolaan State UI (Loading, Data, Error)](#-pengelolaan-state-ui-loading-data-error)
6. [Integrasi REST API (Tenrai API)](#-integrasi-rest-api-tenrai-api)
7. [Struktur Direktori Proyek](#-struktur-direktori-proyek)
8. [Panduan Menjalankan Aplikasi](#-panduan-menjalankan-aplikasi)

---

## ✅ Fitur Utama & Kepatuhan Spesifikasi

| Kategori | Persyaratan Tugas | Implementasi | Status |
| :--- | :--- | :--- | :---: |
| **Bahasa** | Kotlin (Data class, Null safety, Lambda, Collection) | Digunakan secara ekstensif pada Model, ViewModel, dan UI | ✅ Terpenuhi |
| **UI Framework** | Jetpack Compose (100% Tanpa XML Layout) | Seluruh antarmuka berbasis fungsi `@Composable` deklaratif | ✅ Terpenuhi |
| **Desain Sistem** | Material Design 3 | Menggunakan komponen M3 (Scaffold, TopAppBar, ElevatedCard, AssistChip) | ✅ Terpenuhi |
| **Tema** | Custom Theme & Custom Typography | Skema warna kustom (Light & Dark) dan `AppTypography` khusus | ✅ Terpenuhi |
| **Penyajian Data** | `LazyColumn` / `LazyVerticalGrid` | Menggunakan `LazyColumn` berkinerja tinggi untuk render daftar | ✅ Terpenuhi |
| **Sumber Data** | REST API Tenrai (`https://api.tenrai.org/v1`) | Retrofit + Gson Converter untuk request asynchronous | ✅ Terpenuhi |
| **Data Minimal** | Judul, Rating, Tahun Rilis, Jumlah Episode | Ditampilkan pada kartu anime di Home Screen & Detail Screen | ✅ Terpenuhi |
| **Arsitektur** | MVVM (Model, View, ViewModel, Repository) | Terstruktur rapi dengan pemisahan tanggung jawab yang jelas | ✅ Terpenuhi |
| **Navigasi** | Maksimal 2 Screens (Home & Anime Detail) | Jetpack Compose Navigation (`NavHost`, `composable`, rute berparameter) | ✅ Terpenuhi |
| **State Handling** | Minimal: Data (Success), Loading, Error | Menerapkan `UiState<T>` (sealed interface) dengan mekanisme Retry | ✅ Terpenuhi |
| **Batasan** | Tanpa XML, tanpa database, tanpa auth, tanpa image loading berlebih | Sesuai aturan: tampilan modern tanpa library berlebih | ✅ Terpenuhi |

---

## 📱 Tangkapan Layar & Desain UI

Aplikasi terdiri dari **2 layar utama** yang saling terhubung:

### 1. Home Screen (Daftar Anime)
- **TopAppBar** bertajuk *"Eksplorasi Anime"* dengan tombol aksi *Refresh*.
- **Search Bar Dinamis**: Pencarian judul anime secara real-time langsung ke API Tenrai.
- **LazyColumn**: Menampilkan daftar kartu anime dengan elevasi Material 3.
- **Informasi Kartu**:
  - Judul Anime (Title)
  - Rating Skor (⭐ Rating / Score)
  - Tahun Rilis (📅 Year)
  - Jumlah Episode (🎬 Episodes)
  - Rating Batas Usia (🛡️ Age Rating, misal: PG-13, R-17+)
  - Indikator Navigasi ke Detail

### 2. Anime Detail Screen (Rincian Anime)
- **TopAppBar** dengan tombol kembali (*Back Navigation*).
- **Kartu Identitas**: Judul utama, judul bahasa Jepang, badge skor, tahun rilis, episode, dan usia penonton.
- **Informasi Produksi**: Tipe anime (TV/Movie), status penayangan, durasi per episode, dan studio animasi.
- **Genre Chips**: Tag kategori genre interaktif menggunakan Material 3 `AssistChip`.
- **Sinopsis Cerita**: Uraian sinopsis cerita lengkap dengan tipografi yang nyaman dibaca.

---

## 🏛️ Arsitektur Aplikasi (MVVM)

Aplikasi mengadopsi pola arsitektur **MVVM (Model - View - ViewModel - Repository)** dengan prinsip **Unidirectional Data Flow (UDF)**:

```
┌────────────────────────────────────────────────────────┐
│                   Tenrai REST API                      │
│               https://api.tenrai.org/v1                │
└───────────────────────────┬────────────────────────────┘
                            │ (JSON)
                            ▼
┌────────────────────────────────────────────────────────┐
│                 Remote Data Source                     │
│           ApiService.kt (Retrofit Client)              │
└───────────────────────────┬────────────────────────────┘
                            │
                            ▼
┌────────────────────────────────────────────────────────┐
│                   Repository Layer                     │
│         AnimeRepository.kt / AnimeRepositoryImpl       │
└───────────────────────────┬────────────────────────────┘
                            │ Result<T>
                            ▼
┌────────────────────────────────────────────────────────┐
│                   ViewModel Layer                      │
│                  AnimeViewModel.kt                     │
│           (StateFlow<UiState<T>>, Coroutines)          │
└───────────────────────────┬────────────────────────────┘
                            │ UiState (Loading, Success, Error)
                            ▼
┌────────────────────────────────────────────────────────┐
│                      View Layer                        │
│          HomeScreen.kt & DetailScreen.kt               │
│               (Jetpack Compose M3 UI)                  │
└────────────────────────────────────────────────────────┘
```

1. **Model (`data/model/Anime.kt`)**: Data class pembungkus respons JSON dari API Tenrai, dilengkapi properti terhitung (*computed properties*) untuk penyajian data yang aman terhadap nilai null.
2. **Repository (`data/repository/AnimeRepository.kt`)**: Abstraksi data layer untuk menjembatani komunikasi jaringan dan penanganan error menggunakan `Result<T>`.
3. **ViewModel (`ui/viewmodel/AnimeViewModel.kt`)**: Mengelola state UI menggunakan `MutableStateFlow` dan coroutine `viewModelScope`.
4. **View (`ui/screens/` & `ui/components/`)**: Fungsi-fungsi Compose murni yang mengamati StateFlow dan menyusun antarmuka reaktif.

---

## 💡 Pemanfaatan Fitur Bahasa Kotlin

Sesuai instruksi teknis poin 2.a, aplikasi memaksimalkan fitur-fitur unggulan Kotlin:

### 1. Data Class
Digunakan untuk merepresentasikan entitas data secara ringkas, immutabel, dan otomatis menghasilkan `equals()`, `hashCode()`, `toString()`, serta `copy()`.
```kotlin
data class Anime(
    @SerializedName("mal_id") val malId: Int,
    @SerializedName("title") val title: String? = null,
    @SerializedName("score") val score: Double? = null,
    @SerializedName("year") val year: Int? = null,
    @SerializedName("episodes") val episodes: Int? = null,
    // ...
)
```

### 2. Null Safety
Menghilangkan risiko `NullPointerException` (The Billion Dollar Mistake) dengan safe-call operator (`?.`), elvis operator (`?:`), dan scope function (`let`, `takeIf`).
```kotlin
// Fallback judul yang aman
val displayTitle: String
    get() = title ?: titleEnglish ?: "Judul Tidak Diketahui"

// Format skor jika bernilai null
val displayScore: String
    get() = score?.let { String.format(Locale.US, "%.2f", it) } ?: "N/A"

// Fallback bertingkat untuk tahun rilis
val displayYear: String
    get() = (year ?: aired?.prop?.from?.year)?.toString() ?: "N/A"
```

### 3. Lambda Expression
Digunakan pada callback navigasi, filter koleksi, Coroutine builder, dan definisi slot Jetpack Compose.
```kotlin
// Lambda pada event klik navigasi
AnimeCard(
    anime = anime,
    onClick = { onAnimeClick(anime.malId) }
)

// Lambda pada coroutines dan Result handling
repository.getAnimeList(query)
    .onSuccess { list -> _animeListState.value = UiState.Success(list) }
    .onFailure { error -> _animeListState.value = UiState.Error(error.localizedMessage) }
```

### 4. Collection Functions
Pemanfaatan operasi koleksi fungsional seperti `.map()`, `.filter()`, dan `.joinToString()` untuk transformasi data yang deklaratif dan efisien.
```kotlin
val displayGenres: String
    get() = genres?.map { it.name }
        ?.filter { it.isNotBlank() }
        ?.joinToString(", ")
        ?.ifEmpty { "Tidak ada genre" } ?: "Tidak ada genre"
```

---

## ⚡ Pengelolaan State UI (Loading, Data, Error)

Aplikasi menerapkan `sealed interface UiState<out T>` untuk menjamin *exhaustive pattern matching*:

```kotlin
sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<out T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}
```

Pada UI Compose, state ini ditangani dengan blok `when`:
```kotlin
when (val state = animeListState) {
    is UiState.Loading -> LoadingView(message = "Memuat daftar anime...")
    is UiState.Error   -> ErrorView(message = state.message, onRetry = { viewModel.fetchAnimeList() })
    is UiState.Success -> LazyColumn { items(state.data) { anime -> AnimeCard(anime, ...) } }
}
```

---

## 🌐 Integrasi REST API (Tenrai API)

- **Base URL**: `https://api.tenrai.org/v1/`
- **Endpoints yang digunakan**:
  1. `GET /v1/anime?q={query}&limit=25` : Mengambil daftar anime atau hasil pencarian judul anime secara dinamis.
  2. `GET /v1/anime/{id}` : Mengambil rincian detail anime lengkap berdasarkan ID unik anime (`mal_id`).

---

## 📂 Struktur Direktori Proyek

```
app/src/main/java/com/pemmob/eksplorasianime/
├── MainActivity.kt                  # Entry point activity (Compose setContent)
├── data/
│   ├── model/
│   │   └── Anime.kt                # Data class model respons API & DTO
│   ├── remote/
│   │   ├── ApiService.kt           # Definisi endpoint Retrofit
│   │   └── RetrofitClient.kt       # Singleton Retrofit & Gson converter
│   └── repository/
│       ├── AnimeRepository.kt      # Abstraksi Interface Repository
│       └── AnimeRepositoryImpl.kt  # Implementasi data fetching & error handling
└── ui/
    ├── components/
    │   ├── AnimeBadge.kt           # Komponen badge pill rating, tahun, episode
    │   ├── AnimeCard.kt            # Kartu anime di LazyColumn
    │   ├── ErrorView.kt            # Tampilan error + tombol retry
    │   └── LoadingView.kt          # Indikator loading animasi M3
    ├── navigation/
    │   ├── AppNavigation.kt        # NavHost konfigurasi rute Home <-> Detail
    │   └── Screen.kt               # Sealed class rute navigasi
    ├── screens/
    │   ├── DetailScreen.kt         # Layar rincian detail anime
    │   └── HomeScreen.kt           # Layar daftar anime (LazyColumn + Search)
    ├── state/
    │   └── UiState.kt              # Sealed interface Loading, Success, Error
    ├── theme/
    │   ├── Color.kt                # Palet warna Light & Dark M3
    │   ├── Theme.kt                # Custom Material 3 Theme Composable
    │   └── Type.kt                 # Custom Typography M3
    └── viewmodel/
        └── AnimeViewModel.kt       # ViewModel pengelola StateFlow & logika bisnis
```

---

## 🚀 Panduan Menjalankan Aplikasi

### Persyaratan Lingkungan
- **Android Studio** (Koala / Ladybug / Meerkat atau yang lebih baru)
- **JDK**: Versi 17 atau 21
- **Android SDK Platform**: API Level 36 (Minimum SDK API 24 - Android 7.0+)

### Langkah Menjalankan via Terminal / Gradle Wrapper
1. Masuk ke direktori root proyek:
   ```bash
   cd "d:/Aplikasi Eksplorasi Anime"
   ```
2. Jalankan build debug APK:
   ```bash
   ./gradlew assembleDebug
   ```
   *File APK siap pakai berlokasi di*: `app/build/outputs/apk/debug/app-debug.apk`
3. Menjalankan Unit Test:
   ```bash
   ./gradlew testDebugUnitTest
   ```

---

*Dikembangkan untuk Tugas Praktikum Pemrograman Mobile.*
