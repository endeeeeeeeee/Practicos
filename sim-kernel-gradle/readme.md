# 🧠 Sim-Kernel: Simulador de Sistema Operativo

## 👥 Integrantes del Proyecto

- [Tu Nombre Aquí]
- Carrera: Ingeniería de Sistemas
- Materia: Programación III
- Universidad: NUR
- Gestión: 2025

---

## 🧾 Nombre del Proyecto

**Sim-Kernel: Sistema Operativo Simulado con Visualización de Procesos, Asignación de Memoria, Seguridad y Asistente Inteligente**

---

## 💡 ¿Por qué se eligió este proyecto?

Sim-Kernel surge como una solución educativa para **visualizar el funcionamiento interno de un sistema operativo real**, pero de forma simplificada, didáctica y atractiva. A través de este simulador, los usuarios (estudiantes) pueden entender:

- Cómo se gestionan los procesos
- Cómo se asigna la memoria
- Qué pasa con los procesos bloqueados o en espera
- Cómo opera un planificador FIFO o Round Robin
- Cómo actúa un semáforo para acceso concurrente
- Cómo se bloquea el acceso a procesos no autorizados

Además, se incorpora un **asistente de inteligencia artificial** (usando Groq/GPT) que permite hacer preguntas sobre el sistema operativo simulado, aumentando el valor educativo.

---

## 🛠️ ¿Cómo se implementará?

### 🧰 Tecnologías

- **Java 21**
- **JavaFX 21** (interfaz gráfica)
- **Gradle** (construcción del proyecto)
- **FXML + CSS moderno** (interfaz tipo WidgetLabApp)
- **Groq/GPT API** (asistente de IA)
- **Diseño modular y escalable**

---

## 🔧 Módulos Clave

| Módulo                    | Descripción técnica |
|--------------------------|---------------------|
| `Proceso`                | Modelo con PID, nombre, estado, tiempo, tamaño y prioridad |
| `SimViewController`      | Controlador principal de la vista JavaFX |
| `Planificador`           | Controla el orden en que los procesos son atendidos |
| `Semaforo`               | Controla acceso concurrente simulado (recurso crítico) |
| `EstrategiaMemoria`      | Enum que define First Fit y Best Fit |
| `GroqHelper`             | Clase para invocar el asistente IA con preguntas personalizadas |
| `MainApp`                | Entrada principal de la aplicación |

---

## 🎯 ¿Qué funcionalidades tendrá?

### ✅ Simulación de procesos
- Carga inicial con múltiples procesos (navegador, editor, backup, etc.).
- Se visualiza su estado (activo, bloqueado, en espera).
- Algunos procesos son bloqueados por seguridad.

### ✅ Asignación de memoria: First Fit / Best Fit
- Al presionar "Iniciar Simulación", se toma el valor del combo `comboEstrategia`.
- **First Fit**: se asigna la primera partición libre que tenga suficiente espacio.
- **Best Fit**: se busca la partición más ajustada al tamaño del proceso.
- La barra de progreso (`barraMemoria`) se actualiza simulando uso de RAM.

### ✅ Planificación de procesos
- Se simula una cola de procesos lista usando el algoritmo **FIFO** (por defecto).
- Se puede extender para simular **Round Robin** usando ciclos de CPU y prioridades.
- Se imprimen los resultados y se agregan al área de logs.

### ✅ Seguridad (accesos denegados)
- Los procesos que intentan acceder a recursos de administrador (como `Updater` o `Backup`) sin ser `root` son **registrados como accesos denegados**.
- Se muestra un mensaje en `listaSeguridad`.

### ✅ Procesos bloqueados / en espera
- Algunos procesos se marcan manualmente con estado `BLOQUEADO` para testear visualmente.
- Aparecen en `tablaBloqueados` y `listaProcesosEnEspera`.

### ✅ Logs del sistema
- Cada evento (creación de proceso, acceso denegado, cambio de estado) se agrega a `areaLogs`.

### ✅ Asistente Inteligente
- Al escribir una pregunta en el campo de texto, el sistema la envía a Groq o GPT.
- El asistente responde en lenguaje natural simulando una explicación del estado actual o del concepto consultado.

---

## 🧪 ¿Cómo se testea?

- Se hace clic en **"Iniciar Simulación"**
- Se cargan procesos con diferentes características
- Se observa:
    - Tabla de procesos activos
    - Tabla de procesos bloqueados
    - Logs generados
    - Accesos denegados
    - Procesos en espera
- Se cambia entre **First Fit** y **Best Fit** para probar efectos en memoria
- Se puede interactuar con el **Asistente IA** en la pestaña respectiva

---

## 🔐 Seguridad

> Se simula seguridad básica al nivel de procesos:
- Si un proceso que no es `root` accede a un recurso restringido, se registra como "acceso denegado".
- Este comportamiento se observa en la interfaz en tiempo real.

---

## 📚 Conclusión

**Sim-Kernel** permite entender cómo un sistema operativo gestiona procesos, memoria, seguridad y planificación. Es ideal para prácticas de programación, demostraciones educativas, y como base para futuros sistemas más complejos.

---

## 📸 Capturas (opcional)

> Puedes insertar capturas de pantalla de cada tab en ejecución.

---

## 📂 Estructura del proyecto

