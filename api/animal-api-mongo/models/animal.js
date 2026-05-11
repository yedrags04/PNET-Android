const mongoose = require("mongoose");

const AnimalSchema = new mongoose.Schema({
  id: Number,
  nombre: String,
  especie: String,
  edad: Number,
  habitat: String,
  enPeligro: Boolean,
});

module.exports = mongoose.model("Animal", AnimalSchema);