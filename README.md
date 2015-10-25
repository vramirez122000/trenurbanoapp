# Tren Urbano App
La legendaria aplicación de transporte público del Área Metro de San Juan

Escrita en Java con Spring, sobre la base de datos PostgreSQL con la extensión PostGIS

## Instalar herramientas

    $ sudo npm -g install bower grunt-cli

## Compilar codigo java y javascript

    $ ./gradlew
    $ cd web
    $ bower install
    $ grunt

## Base de datos

Véase el proceso de instalación de PostgreSQL para su sistema operativo.

### Crear

    $ psql -U postgres
    postgres=# create database tuapp; --o el nombre de base de datos que desees
    postgres=# \c tuapp
    tuapp=# create extension postgis;

### Poblar base de datos

    $ cat db/ref_data.sql db/log_schema.sql | psql -U postgres -d tuapp

### Correr

Puedes correr Tren Urbano App en tu contenedor web favorito (e.g. Tomcat 7+). Las siguientes argumentos del JVM son requeridos:

    -Dconfig.property.file=/path/to/config.properties #
    -Dlog4jdbc.drivers=org.postgis.DriverWrapper # Marronazo que registra el Driver de POSTGIS con el mecanismo de escupir SQL a la consola 
