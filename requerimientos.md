# Historias de Usuario del Sistema de Peajes

## Aplicación para Propietarios

---

### Historia de Usuario 1: Inicio de sesión del propietario

> **COMO** un propietario de vehículo,
> **QUIERO** ingresar a la aplicación con mi cédula de identidad y contraseña,
> **PARA** acceder a mi tablero de control.

#### Criterios de Aceptación:

* **Escenario:** Ingreso exitoso
    * [cite_start]**DADO** que soy un propietario registrado con estado "Habilitado", "Suspendido" o "Penalizado" [cite: 43, 46, 47]
    * [cite_start]**CUANDO** ingreso mi cédula de identidad y mi contraseña correctas [cite: 74]
    * [cite_start]**ENTONCES** el sistema me muestra el "Tablero de control del propietario". [cite: 75]

* **Escenario:** Credenciales incorrectas
    * **DADO** que soy un usuario intentando ingresar a la aplicación
    * **CUANDO** ingreso una cédula de identidad y/o contraseña incorrecta
    * [cite_start]**ENTONCES** el sistema muestra el mensaje "Acceso denegado". [cite: 77]

* **Escenario:** Propietario deshabilitado
    * [cite_start]**DADO** que soy un propietario registrado con estado "Deshabilitado" [cite: 44]
    * **CUANDO** ingreso mi cédula de identidad y mi contraseña correctas
    * [cite_start]**ENTONCES** el sistema muestra el mensaje "Usuario deshabilitado, no puede ingresar al sistema". [cite: 78]

---

### Historia de Usuario 2: Visualización del tablero de control

> **COMO** un propietario de vehículo,
> **QUIERO** ver un tablero con el resumen detallado de mi información,
> **PARA** tener una visión completa de mi estado, saldo, vehículos, bonificaciones, tránsitos y notificaciones.

#### Criterios de Aceptación:

* **Escenario:** Visualización de información completa en el tablero
    * **DADO** que he ingresado exitosamente a la aplicación
    * **CUANDO** se carga mi "Tablero de control"
    * **ENTONCES** el sistema me muestra:
        * [cite_start]Mi nombre completo, estado y saldo actual. [cite: 145, 146, 147]
        * [cite_start]Una tabla de bonificaciones asignadas (nombre, puesto, fecha de asignación). [cite: 148]
        * [cite_start]Una tabla de vehículos registrados (matrícula, modelo, color, cantidad de tránsitos, monto total gastado). [cite: 149, 150]
        * [cite_start]Una tabla de tránsitos realizados ordenada por fecha/hora descendente (puesto, matrícula, categoría, monto tarifa, bonificación, monto bonificación, monto pagado, fecha, hora). [cite: 151]
        * [cite_start]Una tabla de notificaciones del sistema ordenada por fecha/hora descendente (fecha, hora, mensaje). [cite: 152]

---

### Historia de Usuario 3: Borrar notificaciones

> **COMO** un propietario de vehículo,
> **QUIERO** poder borrar todas mis notificaciones,
> **PARA** limpiar mi panel de notificaciones.

#### Criterios de Aceptación:

* **Escenario:** Borrado de notificaciones exitoso
    * **DADO** que estoy en mi "Tablero de control" y tengo notificaciones visibles
    * [cite_start]**CUANDO** hago clic en la opción "Borrar notificaciones" [cite: 153]
    * [cite_start]**ENTONCES** el sistema elimina todas mis notificaciones. [cite: 153]

* **Escenario:** Intento de borrado sin notificaciones
    * **DADO** que estoy en mi "Tablero de control" y no tengo notificaciones
    * **CUANDO** intento borrar las notificaciones
    * [cite_start]**ENTONCES** el sistema me muestra el mensaje "No hay notificaciones para borrar". [cite: 155]

---

## Aplicación para Administradores

---

### Historia de Usuario 4: Inicio de sesión del administrador

> **COMO** un administrador,
> **QUIERO** ingresar a la aplicación con mi cédula y contraseña,
> **PARA** acceder a las funcionalidades de gestión del sistema.

#### Criterios de Aceptación:

* **Escenario:** Ingreso exitoso
    * **DADO** que soy un administrador registrado y no tengo una sesión activa
    * [cite_start]**CUANDO** ingreso mi cédula de identidad y contraseña correctas [cite: 169]
    * [cite_start]**ENTONCES** el sistema me muestra el menú principal de administrador. [cite: 170]

* **Escenario:** Credenciales incorrectas
    * **DADO** que soy un usuario intentando ingresar a la aplicación de administradores
    * **CUANDO** ingreso una cédula de identidad y/o contraseña incorrecta
    * [cite_start]**ENTONCES** el sistema muestra el mensaje "Acceso denegado". [cite: 172]

* **Escenario:** Administrador ya autenticado
    * **DADO** que soy un administrador que ya ha iniciado sesión en el sistema
    * **CUANDO** intento ingresar nuevamente
    * [cite_start]**ENTONCES** el sistema muestra el mensaje "Ud. Ya está logueado". [cite: 173]

---

### Historia de Usuario 5: Emular un tránsito

> **COMO** un administrador,
> **QUIERO** emular el tránsito de un vehículo por un puesto de peaje,
> **PARA** registrar la pasada, cobrar la tarifa y verificar el funcionamiento del sistema.

#### Criterios de Aceptación:

* **Escenario:** Tránsito emulado exitosamente
    * **DADO** que selecciono un puesto y existe un vehículo registrado con la matrícula ingresada, cuyo propietario tiene saldo suficiente y está "Habilitado"
    * [cite_start]**CUANDO** ingreso la matrícula, fecha/hora y hago clic en "Emular tránsito" [cite: 205]
    * [cite_start]**ENTONCES** el sistema registra el tránsito, descuenta el monto del saldo del propietario, le genera una notificación y muestra en pantalla el resultado con el nuevo saldo. [cite: 206, 207, 212, 213]

* **Escenario:** Matrícula no encontrada
    * **DADO** que he seleccionado un puesto
    * **CUANDO** ingreso una matrícula que no corresponde a ningún vehículo registrado y hago clic en "Emular tránsito"
    * [cite_start]**ENTONCES** el sistema muestra el mensaje "No existe el vehículo". [cite: 215]

* **Escenario:** Saldo insuficiente
    * **DADO** que el propietario del vehículo no tiene saldo suficiente para cubrir el costo del tránsito
    * **CUANDO** intento emular el tránsito
    * [cite_start]**ENTONCES** el sistema no registra el tránsito y muestra el mensaje "Saldo insuficiente:" seguido del saldo actual. [cite: 216]

* **Escenario:** Propietario deshabilitado o suspendido
    * [cite_start]**DADO** que el propietario del vehículo está en estado "Deshabilitado" o "Suspendido" [cite: 217, 218]
    * **CUANDO** intento emular el tránsito
    * [cite_start]**ENTONCES** el sistema no registra el tránsito y muestra el mensaje correspondiente. [cite: 217, 218]

---

### Historia de Usuario 6: Asignar bonificaciones

> **COMO** un administrador,
> **QUIERO** asignar bonificaciones a propietarios en puestos de peaje específicos,
> **PARA** gestionar los beneficios de los usuarios del sistema.

#### Criterios de Aceptación:

* **Escenario:** Asignación de bonificación exitosa
    * [cite_start]**DADO** que he buscado un propietario por su cédula y este no tiene una bonificación asignada para el puesto seleccionado [cite: 244]
    * [cite_start]**CUANDO** selecciono una bonificación, un puesto y hago clic en "Asignar bonificación" [cite: 247]
    * [cite_start]**ENTONCES** el sistema asigna la bonificación al propietario para ese puesto y la muestra en la lista de "Bonificaciones asignadas". [cite: 248]

* **Escenario:** Propietario no encontrado
    * **DADO** que estoy en la pantalla de "Asignar bonificaciones"
    * [cite_start]**CUANDO** ingreso una cédula de identidad que no existe y hago clic en "Buscar propietario" [cite: 244]
    * [cite_start]**ENTONCES** el sistema muestra el mensaje "no existe el propietario". [cite: 250]

* **Escenario:** Propietario ya tiene bonificación en el puesto
    * **DADO** que un propietario ya tiene una bonificación asignada para un puesto específico
    * **CUANDO** intento asignar otra bonificación para el mismo puesto
    * [cite_start]**ENTONCES** el sistema muestra el mensaje "Ya tiene una bonificación asignada para ese puesto". [cite: 253]

* **Escenario:** Propietario deshabilitado
    * **DADO** que busco un propietario cuyo estado es "Deshabilitado"
    * **CUANDO** intento asignarle una bonificación
    * [cite_start]**ENTONCES** el sistema muestra el mensaje "El propietario esta deshabilitado. No se pueden asignar bonificaciones". [cite: 255]

---

### Historia de Usuario 7: Cambiar estado de propietario

> **COMO** un administrador,
> **QUIERO** cambiar el estado de un propietario,
> **PARA** gestionar sus permisos y habilitaciones dentro del sistema.

#### Criterios de Aceptación:

* **Escenario:** Cambio de estado exitoso
    * [cite_start]**DADO** que he buscado un propietario existente por su cédula [cite: 272]
    * [cite_start]**CUANDO** selecciono un nuevo estado (diferente al actual) de la lista y hago clic en "Cambiar estado" [cite: 276]
    * [cite_start]**ENTONCES** el sistema actualiza el estado del propietario, le envía una notificación, y el nuevo estado se refleja en pantalla. [cite: 277]

* **Escenario:** Propietario no encontrado
    * **DADO** que estoy en la pantalla de "Cambiar estado de propietario"
    * **CUANDO** ingreso una cédula de identidad que no existe y hago clic en "Buscar"
    * [cite_start]**ENTONCES** el sistema muestra el mensaje "no existe el propietario". [cite: 281, 282]

* **Escenario:** El estado seleccionado es el mismo que el actual
    * **DADO** que he buscado un propietario existente
    * [cite_start]**CUANDO** selecciono el mismo estado que ya tiene y hago clic en "Cambiar estado" [cite: 276]
    * [cite_start]**ENTONCES** el sistema muestra el mensaje "El propietario ya esta en estado " seguido del nombre del estado. [cite: 283]