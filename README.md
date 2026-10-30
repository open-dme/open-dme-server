# open-dme-server
  
  **Open-source backend for alerting volunteer emergency responders, e.g. volunteer fire departments.**

  Many departments pay for commercial alerting apps. open-dme set out to show that a free, self-hosted alternative is possible. The
  server was built in **two weeks** and, together with the [open-dme-app](https://github.com/open-dme/open-dme-app), acts as a
  digital pager (DME, *Digitaler Meldeempfänger*).

  ## Features
  
  - **Trigger calls:** create alerts for a department
  - **Responses:** members respond via the app and report their status (`available`, `with delay`, `away`, `dispatched`)
  - **Absences:** members set how long they will be unavailable
  - **Manage departments and members:** including skills and vehicles
  - **Onboarding:** new members automatically get a welcome email with a QR code to set up the app
  - **Login and permissions:** via Keycloak (OAuth2 / OpenID Connect, JWT, roles)
  - **API docs:** OpenAPI / Swagger UI

  ## Tech stack

  | Area | Technology |
  |---|---|
  | Language | Java 22, Kotlin |
  | Framework | Spring Boot (Web, Data JPA, Security, Validation, Mail, Thymeleaf) |
  | Auth | Keycloak, OAuth2 Resource Server |
  | Database | PostgreSQL |
  | Testing | JUnit 5, Spring Security Test, Testcontainers |
  | Build & deployment | Gradle (Kotlin DSL), Docker, Docker Compose, GitHub Actions → GHCR |

  ## Quick start

  ```bash
  cd docker
  docker compose --profile dev up
  ```
  The API runs at http://localhost:8888, Keycloak at http://localhost:9080.

  Status

  This project is no longer actively maintained. The code shows what a lean, self-hosted alerting service can look like. Feel free
  to use it as a starting point for your own ideas or to build on it.

  ---

 🇩🇪 Deutsch

  Open-Source-Backend zum Alarmieren von ehrenamtlichen Einsatzkräften, z. B. bei der Freiwilligen Feuerwehr.

  Viele Wehren zahlen für kommerzielle Alarmierungs-Apps. open-dme will zeigen, dass es auch kostenlos und selbst gehostet geht.
  Der Server ist in zwei Wochen entstanden und bildet zusammen mit der open-dme-app (https://github.com/open-dme/open-dme-app)
  einen digitalen Meldeempfänger (DME).

  Funktionen

  - Einsätze auslösen: Alarme für eine Abteilung anlegen
  - Rückmeldungen: Mitglieder sagen per App zu und melden ihren Status (verfügbar, mit Verzögerung, abwesend, im Einsatz)
  - Abwesenheiten: Mitglieder tragen ein, bis wann sie nicht erreichbar sind
  - Abteilungen und Mitglieder verwalten: inklusive Qualifikationen und Fahrzeugen
  - Onboarding: Neue Mitglieder bekommen automatisch eine Willkommens-Mail mit QR-Code für die App-Einrichtung
  - Login und Rechte: über Keycloak (OAuth2 / OpenID Connect, JWT, Rollen)
  - API-Doku: OpenAPI / Swagger UI

  Tech-Stack und Schnellstart: siehe oben.

  Status

  Das Projekt wird nicht mehr aktiv weiterentwickelt. Der Code zeigt, wie ein schlanker, selbst gehosteter Alarmierungsdienst
  aussehen kann. Wer ihn als Grundlage für eigene Ideen nutzen oder weiterentwickeln möchte, kann das gerne tun.

