# About the project

The project is under development and still very experimental.

## What is it?

The project aims to provide a system to display any X application running on a remote server into a web browser, using modern web technologies (websockets and Canvas).

## How is it supposed to work?

The project is divided into three major parts.

The backend part communicates directly with the X clients, serialize/deserialize clients requests, handle these locally when possible or transfer them to the frontend when not.

The web application backend handles the authentication process, and is responsible for launching the backend application is a new process. It is also used as a bridge to communicate from the backend application to the browser.

The frontend (web browser) application, handles the requests received via web sockets, and is responsible for the rendering. It is also responsible for listening events and transfering them to the server (not yet implemented).

## Does it work?

Not yet.
The main framework to communicate between the browser and the X clients is more or less done, but only a very few X request are implemented (quite partially).
There are still some important things to fix in the backend, some parts which are not threadsafe yet.

# Running the application

The application at least needs a JDK and maven installed. To run the application in production mode (which is not really worth right now though), [nix-password-checker](https://github.com/tuvistavie/nix-password-checker) will be required. After setting the paths in `frontend/conf/application.conf`

```
mvn clean compile install
```

should be enough to setup everything.
The application can then be ran using

```
mvn play2:run
```

you can then login with your usual Linux/UNIX username/password combination. The password is only checked when ran in production mode, but this requires (for now) to run the application as a root user.

# Contributing

The project is still only starting, but any help or suggestions would be greatly appreciated.
