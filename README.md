# Tren Urbano App

La legendaria aplicación de transporte público del Área Metro de San Juan. Escrita en Java 8, 
sobre la base de datos PostgreSQL versión 9.2+ con la extensión PostGIS

## Build

Luego de instalar Java 8 JDK y NodeJS NPM

### Compilar codigo java y javascript

    $ ./gradlew build install

## Base de datos

Véase el proceso de instalación de PostgreSQL v9.2+ y PostGIS para su sistema operativo.

### Crear

    $ psql -U postgres
    postgres=# create database tuapp; --o el nombre de base de datos que desees
    postgres=# \c tuapp
    tuapp=# create extension postgis;

### Poblar base de datos

    $ cat db/ref_data.sql db/log_schema.sql | psql -U postgres -d tuapp

### Cómo correr el sistema

Puedes correr Tren Urbano App en tu contenedor web favorito (e.g. Tomcat 7+). Los siguientes argumentos del JVM son requeridos:

    # Marronazo que registra el Driver de POSTGIS con el mecanismo de escupir SQL a la consola 
    -Dlog4jdbc.drivers=org.postgis.DriverWrapper 


    # Configuracion de BD. Archivo de ejemplo incluído en el directorio db/
    -Dconfig.property.file=/absolute/path/to/config.properties
