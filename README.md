# Spotify Collage Generator

## General info
An app for generating collage from Spotify playlists and top tracks written in Java + Spring (backend) and Typescript + Angular (frontend).

![gif](readme_files/screen.gif)

## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
* [Features](#features)

## Technologies
Project is created with:
* Java
* Spring Boot
* Typescript
* Angular

## Setup
In order to run project locally you need to clone this repository and build project with Docker Compose:

```
$ git clone https://github.com/xpakx/spotify-collage.git
$ cd spotify-collage
$ docker-compose up --build -d
```

To stop:
```
$ docker-compose stop
```

## Features
- [x] Connecting to Spotify with OAuth
- [x] Viewing playlists and top tracks
- [x] Generating image with album covers

