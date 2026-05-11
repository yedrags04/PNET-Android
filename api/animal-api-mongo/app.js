// 1. Cargar variables de entorno (.env)
//require("dotenv").config();  // importa las rutas definidas en routes/animales.js.

// 2. Importar dependencias
const express = require("express");
//const mongoose = require("mongoose");

// 3. Crear instancia de Express
const app = express();

// 4. Importar las rutas
const animalesRouter = require("./routes/animales"); // <-- aquí importas tus rutas

// 5. Middleware para analizar JSON
app.use(express.json());  //Asume que esas rutas estarán disponibles bajo el prefijo /api.
app.use("/api", animalesRouter);

module.exports = app;  // 👈 Exportamos app para usarlo en index.js

// Por ejemplo. En animales.js defines una ruta router.get("/animales")
//              En el navegador o Android accedes a http://localhost:3000/api/animales




