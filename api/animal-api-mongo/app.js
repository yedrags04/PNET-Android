const express = require("express");
const cors = require("cors");

const app = express();

app.use(cors());
app.use(express.json());

app.use("/api/bancos", require("./routes/bancos"));
app.use("/api/reservas", require("./routes/reservas"));
app.use("/api/utensilios", require("./routes/utensilios"));
app.use("/api/salas", require("./routes/salas"));

const animalesRouter = require("./routes/animales");
app.use("/api", animalesRouter);

module.exports = app;
