# Kleos – Content extraction made simple

[![CI Status](https://circleci.com/gh/tuusuario/kleos.svg?style=svg)](https://circleci.com/gh/tuusuario/kleos)
[![License Apache 2.0](https://img.shields.io/badge/License-Apache%202.0-blue.svg?style=flat-square)](LICENSE-APACHE.md)
[![License MIT](https://img.shields.io/badge/License-MIT%202.0-blue.svg?style=flat-square)](LICENSE-MIT.md)

**Kleos** is a lightweight and modular content extraction library written in Kotlin. Inspired by [Postlight's Mercury Parser](https://github.com/postlight/mercury-parser), Kleos extracts clean content from messy HTML, returning only the meaningful parts: title, main article content, author, publish date, etc.

It’s ideal for building read-it-later tools, semantic parsers, or research assistants that need clean and structured text from arbitrary web pages.

---

## ✨ Features

- Extracts **title**, **content**, **author**, **date**, and more.
- Works with **HTML strings** or directly from a **URL**.
- Optional **Markdown** or **plain text** output.
- Extendable with **custom extractors** for specific sites.

---

## 📦 Installation

Coming soon via Maven Central:

```kotlin
dependencies {
    implementation("io.github.tuusuario:kleos:0.1.0")
}