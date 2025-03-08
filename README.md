# Proyecto 2: Persecucción de aviones
Proyecto realizado para la clase de Sistemas Distribuidos del profesor Ukranio Coronilla.
**Sin terminar**.

En este proyecto se debe simular en modo gráfico y en dos dimensiones la persecución de un avión por otro avión tal y como se muestra en el siguiente video:
[![Demostracion avion colisiones](https://www.youtube.com/watch?v=AdOwzheo2ww)]
Por sencillez sólo se debe dibujar en un fondo blanco o negro la trayectoria que van siguiendo los aviones. Cada avión se considera del tamaño de un pixel por lo que no es necesario dibujarlos como en el video.

## Requerimientos
El proyecto debe cumplir con los siguientes requerimientos:
- Debe recibir como parámetro un numero flotante el cual indica cuantas veces va más rápido el avión que persigue, respecto al perseguido.
- El área de la ventana debe ser de 1280x720 pixeles.
- Todo avión viaja a la misma rapidez constante y deja la marca de su trayecto.
- Como en el video, un avión no puede hacer cambios bruscos de dirección, por lo que la curva que describe debe ser suave y continúa.
- Ningún avión debe salir del área de la ventana durante la persecución.
- Al ser alcanzado el avión perseguido deberá terminar el programa e imprimir la coordenada donde se dio la colisión.
- Debe ser posible que existan hasta siete aviones perseguidores, por lo que el numero de aviones que persiguen se debe pasar también como parámetro en la línea de comandos.
- Si después de 2 minutos no se ha alcanzado al avión perseguido, el programa termina y en modo gráfico deberá imprimir la distancia recorrida en pixeles por cada avión

### To Do:
- [ ] Diseño elaborado de aviones.
- [X] Avion perseguido evade perseguidores. 