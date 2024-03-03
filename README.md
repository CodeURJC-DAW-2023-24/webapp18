# webapp18
# Nombre: H2OS
## Integrantes: 
  - Pedro Cristino Moreno - PedroCristino2020 - p.cristino.2020@alumnos.urjc.es
  - Jorge Sánchez Díaz - RisiGT - j.sanchezdi.2020@alumnos.ujrc.es 
  - Marcos del Valle Hontanar - MarcoDelValleH - m.delvalle.2020@alumnos.urjc.es
  - Cristian Kim - cristianvictorkim - cv.kim.2023@alumnos.urjc.es


## Navegación 
![image](https://github.com/CodeURJC-DAW-2023-24/webapp18/assets/102817772/d62c1427-e17f-47ca-8b95-b1a422a1ef59)


## Instrucciones de ejecución
Para iniciar la aplicación web se deben seguir los siguientes pasos:
  - Instalar las extensiones y librerias relativas a Spring y Java
  - Iniciar la base de datos mySQL
  - Descargar la aplicación
  - Configurar en aplication.properties el puerto en el que se inicial el programa, el usuario de la base de datos local, su contraseña y su dirección.
  - Iniciar Application.java
  - Acceder desde el navegador (https://localhost:"puerto configurado en el paso anterior")




## Diagrama de los clases y templates

![image](https://github.com/CodeURJC-DAW-2023-24/webapp18/assets/102817772/d5bc4008-3850-4338-ae25-5a2651e55881)


## Diagrama de entidades

![image](https://github.com/CodeURJC-DAW-2023-24/webapp18/assets/102817772/e28fbed3-cf45-4144-907c-b3614b940f9c)


## Participación de miembros

El reparto de tareas tenía sentido en una fase inicial. Luego hicimos una lista de cosas pendientes y cada uno ibamos realizando tareas sin ningun criterio de reparto en especial. 

### Jorge:
#### Tareas:
Implementación de las ofertas, paginación, edición de usuario, google maps. Diseño de la página web. Refactorización de código.
#### Commits: 
  - Applied builder pattern + Pool inside Offer
  - Refactoring services and their connections with the database
  - Implemented Google Maps
  - Implemented edit user
  - Implemented offers pagination
#### Ficheros: 
  - maps.js
  - offers.js
  - user_form.html
  - UserService.java
  - UserController.java
     
### Marcos:
#### Tareas:
Gráfica, implementar lo relacionado a pools y messages, las offer applyances, privilegios de admin. 
#### Commits: 
  - Offer applying and checking added
  - Graphic added
  - Merge branch 'pruebaSecurity' into fase2
  - Added form to create pools
  - AddMessage and delete message now working through DB
#### Ficheros: 
  - PoolController
  - PieController
  - OfferController
  - Pool
  - Offer
     
### Pedro:
#### Tareas:
Implementar lo relacionado a usuarios (employers y lifeguards), inicio de sesión, registrarse, perfil y seguridad.
#### Commits: 
  - Added employers and lifeguard databases
  - works login and view your profile
  - initialize database and management of added roles
  - logout implementation and header improvement
  - Added list of lifeguards and employers can remove them from accepted offers
#### Ficheros: 
  - UserController
  - Person
  - Lifeguard
  - Employer
  - profile

### Cristian:
#### Tareas:
#### Commits:
#### Ficheros:
     


