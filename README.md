# Prompt Studio

An Android app for trying OpenAI chat prompts from a phone: set the model, system prompt, temperature, top-p and max tokens, send the request, and read the raw response. I built it during my 2024 internship at CoolBoots Media as an internal tool for testing AI-driven features. It's in closed testing on Google Play.

## What it does

- Choose a model from a dropdown (defaults to `gpt-3.5-turbo`)
- Set the system and user prompts, temperature, top-p and max tokens
- Send the request to the OpenAI Chat Completions API and show the JSON response
- You enter your own API key in the app; none is stored in this repo

## How it's built

| | |
|---|---|
| Language | Java |
| UI | Android Views, Material Components |
| Networking | OkHttp, with Gson for request and response models |
| Min/target | see `app/build.gradle.kts` |

All of the logic lives in `MainActivity.java`, which builds the request and sends it with OkHttp.

## Run it

```bash
git clone https://github.com/vishalbharti08/PromptStudio.git
```

Open the project in Android Studio, let Gradle sync, and run the `app` configuration on a device or emulator. Paste your OpenAI API key into the app to send requests.
