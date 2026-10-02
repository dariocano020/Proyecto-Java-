# Sistema de Inventario (Proyecto Java)

Bienvenido al **Sistema de Inventario**, una aplicación de consola desarrollada en Java puro, diseñada bajo el patrón de arquitectura **MVC (Modelo-Vista-Controlador)**.

## Características Principales

*   **Arquitectura MVC**: Código limpio y separado en paquetes (`modelo`, `vista`, `controlador` y `utilidades`).
*   **Roles de Usuario**: Soporte para perfiles de **Administrador** y **Cliente**.
*   **Autenticación Segura (OTP)**: Verificación de inicio de sesión en 2 pasos mediante envío de códigos al correo electrónico (JavaMail).
*   **Catálogo Dinámico**: Gestión de diferentes tipos de productos utilizando Herencia (`ProductoFisico` y `ProductoDigital`).
*   **Carrito de Compras**: Funcionalidad para que los clientes agreguen, quiten y compren productos, calculando totales automáticamente.
*   **Persistencia de Datos**: Guardado y carga automática del estado del inventario y los usuarios utilizando JSON (mediante la librería Gson de Google).
*   **Exportación de Informes**: Generación automática de reportes estadísticos e historial de pedidos en archivos CSV.
*   **Generación de Facturas**: Envío automático de facturas detalladas en formato HTML al correo electrónico del cliente tras cada compra.

## Estructura del Proyecto

El código fuente está organizado en la siguiente jerarquía:

```text
src/inventario/
├── Main.java                  # Punto de entrada de la aplicación
│
├── modelo/                    # Entidades y lógica de datos
│   └── Producto.java, Pedido.java, Usuario.java...
│
├── vista/                     # Interfaz de usuario (Consola)
│   └── ConsolaVista.java
│
├── controlador/               # Lógica de negocio y coordinación
│   └── TiendaControlador.java, GestionDatos.java
│
└── utilidades/                # Servicios externos y herramientas
    └── ServicioOTP.java, PersistenciaDatos.java, ExportadorCSV.java...
```

## Tecnologías y Librerías Utilizadas

*   **Java**: Lenguaje de programación principal (Compatible con Java 8+).
*   **Gson (com.google.code.gson)**: Para la serialización y deserialización de objetos a JSON.
*   **JavaMail (javax.mail)**: Para el envío de correos electrónicos (OTP y Facturas).

## Cómo ejecutar el proyecto

1.  Asegúrate de tener el JDK instalado.
2.  Importa el proyecto en tu IDE favorito (como IntelliJ IDEA o Eclipse).
3.  Asegúrate de agregar los archivos `.jar` de Gson, JavaMail y Activation (que se encuentran en la carpeta `lib/`) al **Classpath** de tu proyecto.
4.  Ejecuta la clase `Main.java` ubicada en el paquete `inventario`.

---
*Desarrollado para el módulo DAW.*
