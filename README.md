Iniciando el proyecto del Router
================================

Ciencias de la Computación VIII - Laboratorio 9
-----------------------------------------------

En este laboratorio estaremos comenzando el desarrollo del proyecto del Router. El objetivo es lograr implementar la sección que se encarga de enviar y recibir las notificaciones de Distance Vector. 

Debe implementar una solución modular para que le pueda ser funcional al momento de integrar este trabajo con su proyecto. 

Se requiere que el programa solicite al usuario un listado de posibles destinos y los Distance Vectors a dichos destinos. Esta información será enviada a un módulo que se encargará de dar formato a la información, construyendo el mensaje visto en clase:

~~
Type:DV
Len: <Cantidad de líneas del mensaje>
<Listado con el siguiente formato:
<Destino>:<Costo de la ruta>
<Destino>:<Costo de la ruta>
…
<Destino n>:<Costo de la ruta al destino n>
>
~~

Recuerde que el sistema que intercambia información entre los routers debe tener dos módulos. Un módulo Cliente y un módulo Servidor. 
El componente Cliente se encargará de enviar la data de los distance vector ingresados por el usuario al puerto 9080 de sus routers adyacentes. 
Para efectos del ejercicio puede conectarse arbitrariamente con otros compañeros de clase. 
Y el componente Servidor debe escuchar mensajes en el puerto 9080 que serán enviados desde otros routers. 
De momento, únicamente debe recibir dichos mensajes y parsear los datos, mostrando en pantalla el desglose de la información recibida. 
Recuerde que el componente servidor debe ser Multithreading, pues es probable que más de un router adyacente intente comunicarse con su Router, por lo que debe poder atender las comunicaciones de manera concurrente.
