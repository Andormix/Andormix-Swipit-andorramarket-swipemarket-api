# Andormix Swipe Market API

[![CI Build, Test & JaCoCo Coverage](https://github.com/Andormix/Andormix-Swipit-andorramarket-swipemarket-api/actions/workflows/main.yml/badge.svg)](https://github.com/Andormix/Andormix-Swipit-andorramarket-swipemarket-api/actions/workflows/main.yml)

[![JaCoCo Coverage Report](https://img.shields.io/badge/JaCoCo-Coverage%20Report-brightgreen?logo=github)](https://andormix.github.io/Andormix-Swipit-andorramarket-swipemarket-api/)

SwipeMarket is a mobile-first second-hand marketplace where users can discover products through a swipe-based interface, search using filters, save favorites, publish listings and contact sellers.

## Project

This repository contains the backend API for the SwipeMarket application.

The API is built with:

- Java 21
- Spring Boot
- Spring Data JPA
- MySQL
- Flyway
- Maven
- JWT authentication

## Continuous Integration

Every push to `main` and every pull request runs:

- Maven compilation
- Automated tests
- JaCoCo code coverage generation
- JaCoCo report upload as a GitHub Actions artifact

The latest JaCoCo HTML report is published automatically to GitHub Pages:

### [View JaCoCo Coverage Report](https://andormix.github.io/Andormix-Swipit-andorramarket-swipemarket-api/)

Reports from individual workflow executions can also be downloaded from the [GitHub Actions workflow](https://github.com/Andormix/Andormix-Swipit-andorramarket-swipemarket-api/actions) as the `jacoco-coverage-report` artifact.
