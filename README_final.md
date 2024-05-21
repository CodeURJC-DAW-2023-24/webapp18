# webapp18
# Nombre: H2Os
## Integrantes: 
  - Pedro Cristino Moreno - PedroCristino2020 - p.cristino.2020@alumnos.urjc.es
  - Jorge Sánchez Díaz - RisiGT - j.sanchezdi.2020@alumnos.ujrc.es 
  - Marcos del Valle Hontanar - MarcoDelValleH - m.delvalle.2020@alumnos.urjc.es

# Fase 0

## Entidades
  - Usuarios (empleadores y socorristas)
  - Piscinas
  - Comentarios
  - Ofertas
## Relaciones
  - Los empleadores publican ofertas para los socorristas.
  - Las ofertas tienen una piscina asociada.
  - Las piscinas tienen comentarios de otros socorristas.

## Permisos usuarios
  - Socorrista: Puede ver y aceptar ofertas. Puede comentar en el foro de cada piscina.
  - Empleadores: Pueden ver y publicar ofertas. Pueden elegir un socorrista de los propuestos para la oferta.
  - Anónimo: Puede ver ofertas y los comentarios de las piscinas.
  - Admin: Además de poder hacer todo lo de los anteriores, también puede dar de alta empleadores.

## Imágenes
  - Tienen imágenes asociadas los socorristas (su titulación, foto personal) y las piscinas (foto de la piscina) y los emleadores (logo de la empresa)

## Gráficos
  - Mostrará el número de socorristas que reúnen cada aptitud.

## Tecnología complementaria
  - Google Maps

## Algoritmo de consulta avanzada
  - Mostrara a cada socorrista sus ofertas más cercanas


# Fase 1

## Diagrama E-R

![imagen](https://github.com/CodeURJC-DAW-2023-24/webapp18/assets/102817772/53cc30c7-72a6-4c19-95b8-3823bf6a607c)


# Fase 2



## Navegación 
![image](https://github.com/CodeURJC-DAW-2023-24/webapp18/assets/102817772/d62c1427-e17f-47ca-8b95-b1a422a1ef59)


## Instrucciones de ejecución
Para iniciar la aplicación web se deben seguir los siguientes pasos:
  - Instalar las extensiones y librerias relativas a Spring y Java
  - Iniciar la base de datos mySQL
  - Descargar la aplicación
  - Configurar en aplication.properties el puerto en el que se inicial el programa, el usuario de la base de datos local, su contraseña y su dirección.
  - Iniciar Application.java
  - Acceder desde el navegador (https://localhost:x, donde x es el puerto configurado en el paso anterior)




## Diagrama de los clases y templates

![image](https://github.com/CodeURJC-DAW-2023-24/webapp18/assets/102817772/d5bc4008-3850-4338-ae25-5a2651e55881)


## Diagrama de entidades

![image](https://github.com/CodeURJC-DAW-2023-24/webapp18/assets/102817772/e28fbed3-cf45-4144-907c-b3614b940f9c)

## Usuarios de ejemplo (nombre de usuario y contraseña)
### administrador: admin, admin
### empleador: e1,e1
### socorrista: s1, s1
## Participación de miembros

El reparto de tareas tenía sentido en una fase inicial. Luego hicimos una lista de cosas pendientes y cada uno ibamos realizando tareas sin ningun criterio de reparto en especial. 

### Jorge:
#### Tareas:
Implementación de las ofertas, paginación, edición de usuario, google maps. Diseño de la página web. Refactorización de código.
#### Commits: 
  - [Applied builder pattern + Pool inside Offer](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/9e65014dbef7c81b47d5604084cf508e1ef9f8c9)
  - [Refactoring services and their connections with the database](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/4fcaa0ce4590894f8f28b4de7aec9c0926233144)
  - [Implemented Google Maps](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/c47b5ad86f7a2199919d446627668c1ed87fd197)
  - [Implemented edit user](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/02ee6b095e5c890bade18d524ab7ccfeb01b42de)
  - [Implemented offers pagination](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/dc48cb26ff26bc7234f1c0b46192f90c3c3949c2)
#### Ficheros: 
  - [maps.js](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase2/backend/helloword-vscode/src/main/resources/static/js/maps.js)
  - [offers.js](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase2/backend/helloword-vscode/src/main/resources/static/js/offers.js)
  - [user_form.html](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase2/backend/helloword-vscode/src/main/resources/templates/user_form.html)
  - [UserService.java](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase2/backend/helloword-vscode/src/main/java/es/codeurjc/service/UserService.java)
  - [UserController.java](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase2/backend/helloword-vscode/src/main/java/es/codeurjc/controller/UserController.java)
     
### Marcos:
#### Tareas:
Gráfica, implementar lo relacionado a pools y messages, las offer applyances, privilegios de admin. 
#### Commits: 
  - [Offer applying and checking added](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/ab07b9be3f03737aaa02bd75446cedaf6d69f917)
  - [Graphic added](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/7e562666737ab5471b687abf588b777729bcf857)
  - [Merge branch 'pruebaSecurity' into fase2](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/b642aca33493b114cef790bbf5caed46e3a392c0)
  - [Added form to create pools](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/8f4728d92ef972f61f96e56bd8a4d403f8dd4131)
  - [AddMessage and delete message now working through DB](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/7f3eab1b42a0488f37e94b2c2dab5419128d1df1)
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
  - [Added employers and lifeguard databases](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/0c7d1b8d262dfd0de85d1ef4ad26eea1bb3d0e8b)
  - [works login and view your profile](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/4fe9d576bcc53fe45a4fe1f282baa650bb4db8d5)
  - [initialize database and management of added roles](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/f4695c0aa2c1e72b6ad8b975dd4b6131b07ca4c8)
  - [logout implementation and header improvement](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/e0935a9b17667bd4b2abb093c9b93a5cb196c504)
  - [Added list of lifeguards and employers can remove them from accepted offers](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/29af60cf44d80bd9e34c355702edd4ad32dd44df)
#### Ficheros: 
  - [UserController](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase2/backend/helloword-vscode/src/main/java/es/codeurjc/controller/UserController.java)
  - [Person](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase2/backend/helloword-vscode/src/main/java/es/codeurjc/model/Person.java)
  - [Lifeguard](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase2/backend/helloword-vscode/src/main/java/es/codeurjc/model/Lifeguard.java)
  - [Employer](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase2/backend/helloword-vscode/src/main/java/es/codeurjc/model/Employer.java)
  - [profile](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase2/backend/helloword-vscode/src/main/resources/templates/profile.html)

### Adrián:
#### Tareas:
API REST de ofertas, edición y actualización.
#### Commits: 
  - offer updates
  - offer edit get and post rest methods
#### Ficheros: 
  - offer.html
  - offer_form.html
  - OfferControles.java

# Fase 3
## Diagrama de los clases y templates

![image](https://github.com/CodeURJC-DAW-2023-24/webapp18/assets/102817772/e7f730fc-fe3f-4e41-bacd-3d00ab52ede9)



## Diagrama de entidades

![image](https://github.com/CodeURJC-DAW-2023-24/webapp18/assets/102817772/e28fbed3-cf45-4144-907c-b3614b940f9c)


## Participación de miembros

El reparto de tareas tenía sentido en una fase inicial. Luego hicimos una lista de cosas pendientes y cada uno ibamos realizando tareas sin ningun criterio de reparto en especial. 

### Jorge:
#### Tareas:
Implementación de los métodos GET y PUT de ofertas, piscinas y mensajes. Refactorización de código y pulir funcionalidades.
#### Commits: 
  - [Implemented getOffers for API REST](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/232f44a05b7f2d562e561ccca046674d72c1c057)
  - [Implemented editOffer for API REST](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/461595f9d17ac7f16a94b2a0d350c63c9ea3f8a6)
  - [Implemented editPool for API REST](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/7b96ba1687726a44ecb2ca9ae0332701e683be2b)
  - [Created isAuthorized](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/854945987ebd7a024aa29e4e4b7b37db3906e6bb)
  - [Checked authorization in delete Pool and Message](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/58df58af13eb1ea261e15e5ec748a285dfedd0b4)
#### Ficheros: 
  - [OfferRestController.java](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase3/backend/helloword-vscode/src/main/java/es/codeurjc/restcontroller/OfferRestController.java)
  - [PoolRestController.java](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase3/backend/helloword-vscode/src/main/java/es/codeurjc/restcontroller/PoolRestController.java)
  - [Message.java](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase3/backend/helloword-vscode/src/main/java/es/codeurjc/model/Message.java)
  - [MessageRepository.java](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase3/backend/helloword-vscode/src/main/java/es/codeurjc/repository/MessageRepository.java)
  - [UserService.java](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase3/backend/helloword-vscode/src/main/java/es/codeurjc/service/UserService.java)
     
### Marcos:
#### Tareas:
RestController de la Grafica y de Offer 
#### Commits: 
  - [Cambios en la seguridad necesarios para el soporte de las peticiones rest](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/393193c2d27e96c866d223055feb222ddd8fa195)
  - [Implementado el rest controller de la gráfica](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/fede9006a2786aac60c26ac1527cfd10ed0bdc72)
  - [Añadido el directorio DOT y offer DTO + implementación parcial del rest controller de offer](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/4266d87739f28db155053c1351d5dd24f5990680)
  - [Cambios en la inicialización de la base de datos](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/c0db575823d546f954b0c4b446d2ede93c6e1904)
  - [Documentation added, error messages added and fixed creating offer without a valid pool ID](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/960fc3a1930560410e5c48c6ae551db6f1983b6c)
#### Ficheros: 
  - [OfferDTO](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase3/backend/helloword-vscode/src/main/java/es/codeurjc/DTO/OfferDTO.java)
  - [PieChartRestController](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase3/backend/helloword-vscode/src/main/java/es/codeurjc/restcontroller/ChartRestController.java)
  - [OfferRestController](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase3/backend/helloword-vscode/src/main/java/es/codeurjc/restcontroller/OfferRestController.java)
  - [LoginRestController](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase3/backend/helloword-vscode/src/main/java/es/codeurjc/restcontroller/LoginRestController.java)

     
### Pedro:
#### Tareas:
Implementar lo relacionado a usuarios (employers y lifeguards) en la API REST
#### Commits: 
  - [Added UserRestController (to check if it works)](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/489c8d52a8e5a4e52be2a6fe04bdb741c536129f)
  - [Setting passwords in the API works and improved put and post methods. Need to check if it works with images](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/36d2a3b67a6147f978c0ae8a873e6db294176c19)
  - [Added Get Offers and OffersAccepted and also Delete one of your offers or offersAccepted for lifeguards. For employers get offers and delete offer permanently](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/4efcd6f9dc805fd1708282147e342d8dd025589f)
  - [Post, delete, put and get user images works. Added permissions to api user](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/cb3ad53084086d6639f0ce157cb9e528920328ba)
  - [Added documentation of UserRestController with Open API](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/ee30fa4730bade866f0794d72a4f3224d2b4f5ae)
#### Ficheros: 
  - [LifeguardRestController](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase3/backend/helloword-vscode/src/main/java/es/codeurjc/restcontroller/LifeguardRestController.java)
  - [EmployerRestController](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase3/backend/helloword-vscode/src/main/java/es/codeurjc/restcontroller/EmployerRestController.java)
  - [LifeguardDTO](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase3/backend/helloword-vscode/src/main/java/es/codeurjc/DTO/LifeguardDTO.java)
  - [EmployerDTO](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase3/backend/helloword-vscode/src/main/java/es/codeurjc/DTO/EmployerDTO.java)
  - [LoginRestController](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase3/backend/helloword-vscode/src/main/java/es/codeurjc/restcontroller/LoginRestController.java)

### Nico:
#### Tareas:
Implementar el RestController de Pool y sus dependencias
#### Commits:
  - Creation PoolDTO
  - Creation PoolRestController
  - Changes in PoolRestController to resemble code
  - Added service on PoolRestController
  - Added UriLocation in PoolRestController
#### Ficheros:
  - PoolRestController
  - MessageDTO
  - PoolDTO
  - Pool
  - Message

### Adrián:
#### Tareas:
Dockerizar la aplicación y desplegarla en el servidor de la Universidad. Post methods de las piscinas.
#### Commits:
  - App image
  - Compose the db and the web app
  - Shell file to push the image
  - Pool post method for posting an image
  - Fix problems with the jar files
#### Ficheros:
  - Dockerfile
  - docker-compose.yml
  - PoolControler.java
  - create-image.sh
     
## Usuarios de ejemplo (nombre de usuario y contraseña)
### administrador: admin, admin
### empleador: e1,e1
### socorrista: s1, s1


## Documentación de la API REST: 

Se puede consultar pulsando [aquí](https://raw.githack.com/CodeURJC-DAW-2023-24/webapp18/fase4/backend/h2os/api-docs/index.html)

## Instrucciones para desplegar la aplicación con docker
1. Instalar docker.
2. Ejecutarlo (mantener la aplicacion de escritorio abierta en windows, en linux se inicia automáticamente)
3. Clonamos el repositorio: ```git clone https://github.com/CodeURJC-DAW-2023-24/webapp18```
4. Vamos a la fase 4: ```git checkout fase4```
5. Vamos al directorio del docker: ```cd webapp18/docker```
6. Desplegamos nuestra aplicación: ```docker-compose up```
7.  Accedemos a ella en https://localhost:8443/

### Publicación de la imagen
1. Repetir los pasos del apartado anterior hasta el 4 (incluido).
2. Ejecutar el create_image.sh que hay en la carpeta docker (es posible que previamente se necesite darle al script los permisos necesarios).

Posteriormente a su publicación se podrá acceder a la imagen desde: https://hub.docker.com/r/mdelvalle2020/webapp18

## Despliegue en la máquina virtual
A continuación se muestra como levantar la aplicacón web desde la máquina virtual proporcionada:

1. Conectarnos a la vpn de la universidad. Nosotros hemos seguido los pasos indicados en el siguiente manual: https://www.urjc.es/principal-intranet/documentos/general/82-configuracion-vpn-urjc/file
2. Obtener la clave, el usuairo y la IP. En nuestro caso eran:     - Usuario: vmuser  - IP: 10.100.139.246   - Clave Privada: prAppWeb18.key
3. Tras descargar la clave navegamos al directorio de descarga y abrimos una cmd para ejecutar: ```ssh -i prAppWeb18.key vmuser@10.100.139.246```
4. Es posible que al ejecutar el paso 3 obtengamos un error relacionado con los permisos de la clave. En ese caso deberemos ejecutar: ```icacls prAppWeb18.key /inheritance:r``` y ```icacls prAppWeb18.key /grant:r "%USERNAME%":F``` Volvemos a realizar el paso 3.
5. Si el paso 3 ha ido bien esteremos dentro de la terminal de la máquina virtual. Clonamos la aplicación y vamos a la fase 3. ```git clone https://github.com/CodeURJC-DAW-2023-24/webapp18.git``` y despues de movernos a la carpeta del .git (```cd webapp18```) vamos a la fase correspondiente: ```git checkout fase4```
6. Vamos al directorio del docker ```cd webapp18/docker```
7. Desplegamos nuestra aplicación: ```docker-compose up``` Es posible que sea necesario iniciar con sudo.
8. Podremos acceder a ella desde: https://10.100.139.246:8443 


# Fase 4

## Preparación del entorno de desarrollo

Tras ejecutar el backend igual que en las fases anteriores se debe proceder a iniciar el frontend.

Para ello se necesita tener instalado Angular CLI. Lo podemos instalar con el siguiente comando: ``` npm install -g @angular/cli```

Si hay algún error es posible que no esté instalado Node.js. Se debe instalar desde la página oficial: https://nodejs.org/en

A continuación se debe ir al directorio del frontend:   ``` cd frontend/h2os```

Instalamos las dependencias necesarias: ``` npm install --force```

Ejecutamos el frontend ``` npm start ```

Ahora podemos acceder a la página desde: http://localhost:4200/

## Diagrama de clases y templates de la SPA

![Diagram_Classes_and_Templates_SPA](https://github.com/CodeURJC-DAW-2023-24/webapp18/assets/102817772/ae5ae1fc-c9ee-446c-8765-94738129f264)

## Usuarios de ejemplo (nombre de usuario y contraseña)
### administrador: admin, admin
### empleador: e1,e1
### socorrista: s1, s1
## Participación de miembros

### Jorge:
#### Tareas:
Implementar Google Maps, manejar las secciones con paginación, mejorar el diseño de la IU e implementación de nuevas funcionalidades.
#### Commits: 
  - [Implemented Google Maps markers in the frontend](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/88cc4e3a383b512ddcdb18e72c47b2906e5b83c5)
  - [Implemented user section](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/1f951da7973fa224351ebff219d681a442370bfe)
  - [Redesigned the display of buttons](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/9f6436424c155f5539e2735b03a17fc977aa7e8c)
  - [Implemented withdraw application](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/fe108ab22f95ea0bc9c9779854f936751b114452)
  - [Created offers.component](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/a5bcb86cb6fc893b95b9524c6cc1520bdc23a9ba)
#### Ficheros: 
  - [maps.component.ts](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase4/frontend/h2os/src/app/components/maps/maps.component.ts)
  - [offers.component.ts](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase4/frontend/h2os/src/app/components/cards/offers.component.ts)
  - [users.component.ts](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase4/frontend/h2os/src/app/components/cards/users.component.ts)
  - [user-detail.component.html](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase4/frontend/h2os/src/app/components/user/user-detail.component.html)
     
### Marcos:
#### Tareas:
Página oferta, lo relacionado a los mensajes de las piscinas, actualizar el docker para que funcione el frontend y SPA controller.
#### Commits: 
  - [Cambios en el docker añadidos a la rama de trabajo](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/39ef5e229ac8967919431a2e563677fb1517298b)
  - [Mensajes de la piscina implementados](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/efa1d48ea7e3f145ae6e5f2377f2fc435cef6551)
  - [Implementada la creación de oferta](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/c2af922a64e15c36657fab59bc1683fca6600a61)
  - [Implementado el editar oferta](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/1c442d6a444535a756be52aad17a00c80ac10ec0)
  - [Desarrollo de la oferta](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/a7f88e10d3e85b289195380abf0996605a0532cd).
#### Ficheros: 
  - [offer.component.ts](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase4/frontend/h2os/src/app/components/offer/offer.component.ts)
  - [offer.create.component.ts](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase4/frontend/h2os/src/app/components/offer/offer.create.component.ts)
  - [Dockerfile](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase4/docker/Dockerfile)
  - [offer.service.ts](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase4/frontend/h2os/src/app/services/offer.service.ts).

     
### Pedro:
#### Tareas:
Implementar lo relacionado a usuarios (employers y lifeguards) en el frontend
#### Commits: 
  - [Initialized Angular web, doesnt work](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/9faa13b93e5f391c129c37e8c1b4086ccbd2dd10)
  - [implemented user-detail and user-form. I haven't tried if it works](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/b930ae386113e419f474c9f46ab9266ede285a7d)
  - [profile and user_form works correctly (photo not implemented yet). You need to reload to update header](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/f30ec6584c99f1fd0fac56f1d4a1656b8ac16a4f)
  - [upload photo completed and form improved](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/10b838f17f88c1e1d4c607748330438c2617aa9d)
  - [implemented pool form](https://github.com/CodeURJC-DAW-2023-24/webapp18/commit/00aeb8a1524f774f5d92e80a637bf2013c85d1c5)
#### Ficheros: 
  - [user.service.ts](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase4/frontend/h2os/src/app/services/user.service.ts)
  - [user-detail.component.ts](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase4/frontend/h2os/src/app/components/user/user-detail.component.ts)
  - [user-form.component.ts](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase4/frontend/h2os/src/app/components/user/user-form.component.ts)
  - [user-login.component.ts](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase4/frontend/h2os/src/app/components/user/user-login.component.ts)
  - [pool-form.component.ts](https://github.com/CodeURJC-DAW-2023-24/webapp18/blob/fase4/frontend/h2os/src/app/components/pools/pool-form.component.ts)

## Link al video de youtube
