# TastyTrade OAuth2 Project

This project is written in Kotlin and provides an integration with the TastyTrade API using OAuth2 authentication. The code is intended for use with the TastyTrade sandbox environment and includes hardcoded credentials and endpoints for demonstration and development purposes.

## Project Structure

- **Language:** Kotlin
- **Main Purpose:** OAuth2 authentication and interaction with the TastyTrade API
- **License:** GNU GPL v3.0

## Requirements

- Java 17 or higher
- Gradle (recommended for building and running)
- Android environment (the project uses Android libraries and SharedPreferences for token storage)

## Configuration

Authentication and OAuth2 configuration is provided via Kotlin code, not via environment variables or external config files. The credentials are defined in `AuthModule` and `ConfigurationModule` as follows:

- **Client ID:** `2912f9e1-91a7-4665-ab77-05ddf5d00414`
- **Client Secret:** `cb63c6b8ccef7a004318a851ca6cf07aea5b4235`
- **Redirect URI:** `com.antyzero.tastytrade://oauth2redirect`
- **Authorization Endpoint:** `https://cert-my.staging-tasty.works/auth.html`

> **Warning:** These credentials are for sandbox/testing only. Do not use in production. To use custom credentials, modify `AuthConfiguration` and `AuthUriConfiguration` in the code.

## How to Build

1. **Clone the repository**
   ```bash
   git clone https://github.com/outlying/tasty-trade.git
   cd tasty-trade
   ```

2. **Build the project with Gradle**
   ```bash
   ./gradlew build
   ```

## How to Run

This is an Android/Kotlin application, to easily run it Android Studio IDE is recommended.

## OAuth2 Flow Outline

1. Direct the user to the authorization URL (`loginUri()`).
2. After the user authenticates, handle the redirect with the provided code.
3. Exchange the code for tokens using `Authentication.exchangeCodeForAccessToken`.
4. Tokens are stored using a SharedPreferences-based `TokenStorage`.

## Token Storage

- Tokens are stored unencrypted in Android `SharedPreferences` (not recommended for production).
- For production, implement an encrypted storage mechanism. (TODO)

## Contributing

See LICENSE for terms. Contributions welcome via pull requests.

## Author

- [outlying](https://github.com/outlying)