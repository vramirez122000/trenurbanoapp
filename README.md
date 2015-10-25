# Tren Urbano App
La legendaria aplicación de transporte público del Área Metro de San Juan

Escrita en Java con Spring, sobre la base de datos PostgreSQL con la extensión PostGIS

## Instalar herramientas para compilar javascript

    $ sudo npm -g install bower grunt-cli

## Compilar codigo java y javascript

    $ ./gradlew install
    $ cd web
    $ bower install
    $ grunt

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
