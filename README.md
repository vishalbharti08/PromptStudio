# 🤖 Prompt Studio (Pre-Release)

AI-powered Android app for testing and fine-tuning OpenAI prompt parameters directly from mobile.  
Currently in **closed testing** on Google Play Console with a planned production release in 2025.

---

## 🚀 Features
- Real-time prompt experimentation using **OpenAI API**
- Adjustable parameters (temperature, top-p, max tokens)
- JSON viewer for structured output visualization
- Dark-mode UI with persistent session history
- Built-in API key configuration for secure use

---

## 🧠 Tech Stack
| Layer | Technologies |
|-------|---------------|
| **Language** | Kotlin |
| **Architecture** | MVVM |
| **Networking** | Retrofit2 + OkHttp |
| **UI** | Material 3 Components |
| **Backend API** | OpenAI GPT API |
| **Build & Deploy** | Android Studio, Gradle, Google Play Console |

---

## 📱 Play Store Status
- **Stage:** Closed testing (11 testers opted in)  
- **Next milestone:** Reach 12 testers, maintain 14-day closed test  
- **Target:** Public release on Play Store — Q1 2025  

---

## ⚙️ Local Setup
```bash
# Clone the repo
git clone https://github.com/vishalbharti08/prompt-studio-android.git
# Open in Android Studio and sync Gradle
# Insert your OpenAI API key in local.properties or secrets.xml
