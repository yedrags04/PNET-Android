const express = require("express");
const router = express.Router();
const Animal = require("../models/animal"); // Ajusta la ruta según tu estructura
const getNextSequence = require("../helpers/getNextSequence");

// GET todos
router.get("/animales", async (req, res) => {
  try {
    const animales = await Animal.find();  // Sin filtros, devuelve todos
    res.json(animales);
  } catch (err) {
    res.status(500).json({ error: "Error al obtener los animales" });
  }
});

// GET por ID
router.get("/animales/:id", async (req, res) => {
  const id = parseInt(req.params.id);

  try {
    const animal = await Animal.findOne({ id: id });

    if (!animal) {
      return res.status(404).json({ error: "Animal no encontrado" });
    }

    res.json(animal);
  } catch (err) {
    res.status(500).json({ error: "Error al buscar el animal" });
  }
});

// POST crear
router.post("/animales", async (req, res) => {
try{
  const nextId = await getNextSequence("animalid");  // 'animalid' será la clave del contador
  const nuevo = new Animal({ ...req.body, id: nextId });
  const guardado = await nuevo.save();
  res.json(guardado);
 } catch (err) {
    res.status(500).json({ error: "Error al guardar el animal" });
  }
});


// PUT reemplazo completo
router.put("/animales/:id", async (req, res) => {
  const id = parseInt(req.params.id); // Asegúrate de que es un número
  try {
    const actualizado = await Animal.findOneAndUpdate(
      { id: id },             // buscar por el campo "id"
      req.body,               // nuevos datos a reemplazar
      { new: true }           // devolver el documento actualizado
    );

    if (!actualizado) {
      return res.status(404).json({ error: "Animal no encontrado" });
    }

    res.json(actualizado);
  } catch (err) {
    res.status(500).json({ error: "Error al actualizar el animal" });
  }
});




// PATCH actualización parcial
router.patch("/animales/:id", async (req, res) => {
  const id = parseInt(req.params.id);

  try {
    const actualizado = await Animal.findOneAndUpdate(
      { id: id },           // Buscar por el campo `id`
      { $set: req.body },   // Solo actualizar los campos presentes
      { new: true }         // Devolver el documento actualizado
    );

    if (!actualizado) {
      return res.status(404).json({ error: "Animal no encontrado" });
    }

    res.json(actualizado);
  } catch (err) {
    res.status(500).json({ error: "Error al actualizar parcialmente el animal" });
  }
});

// DELETE
router.delete("/animales/:id", async (req, res) => {
  const id = parseInt(req.params.id);

  try {
    const eliminado = await Animal.findOneAndDelete({ id: id });

    if (!eliminado) {
      return res.status(404).json({ error: "Animal no encontrado" });
    }

    res.json({ mensaje: "Animal eliminado correctamente", animal: eliminado });
  } catch (err) {
    res.status(500).json({ error: "Error al eliminar el animal" });
  }
});






module.exports = router;