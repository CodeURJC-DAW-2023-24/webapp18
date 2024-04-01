# webapp18
# Nombre: H2OS
## Integrantes: 
  - Pedro Cristino Moreno - PedroCristino2020 - p.cristino.2020@alumnos.urjc.es
  - Jorge Sánchez Díaz - RisiGT - j.sanchezdi.2020@alumnos.ujrc.es 
  - Marcos del Valle Hontanar - MarcoDelValleH - m.delvalle.2020@alumnos.urjc.es




## Diagrama de los clases y templates

![image](https://github.com/CodeURJC-DAW-2023-24/webapp18/assets/102817772/e7f730fc-fe3f-4e41-bacd-3d00ab52ede9)



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
RestController de la Grafica y de Offer 
#### Commits: 
  - security changes needed to do rest petitions added
  - chart rest controller added
  - added DOT dir, offer rest controller almost implemented
  - error messages to GET api/offers and offer check function
  - Documentation added, error messages added and fixed creating offer without a valid pool ID
#### Ficheros: 
  - OfferDTO
  - PieChartRestController
  - OfferRestController
  - LoginRestController

     
### Pedro:
#### Tareas:
Implementar lo relacionado a usuarios (employers y lifeguards) en la API REST
#### Commits: 
  - Added UserRestController (to check if it works)
  - Setting passwords in the API works and improved put and post methods. Need to check if it works with images
  - Added Get Offers and OffersAccepted and also Delete one of your offers or offersAccepted for lifeguards. For employers get offers and delete offer permanently
  - Post, delete, put and get user images works. Added permissions to api user
  - Added documentation of UserRestController with Open API
#### Ficheros: 
  - LifeguardRestController
  - EmployerRestController
  - LifeguardDTO
  - EmployerDTO
  - LoginRestController

### Nico:
#### Tareas:
#### Commits:
#### Ficheros:

### Adrián:
#### Tareas:
#### Commits:
#### Ficheros:
     


