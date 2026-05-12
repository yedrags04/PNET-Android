const express = require("express");
const router = express.Router();
const salasService = require("../services/salas-service");

router.get("/", async (_, res) => {
    try {
        const salas = await salasService.getAll();
        res.status(200).json(salas);
    } catch (e) {
        res.status(404).json({ msg: "No se pudieron obtener las salas" });
    }
});

router.get("/:_id", async (req, res) => {
    const { _id } = req.params;
    try {
        const sala = await salasService.get(_id);
        if (!sala) {
            return res.status(404).json({ msg: "Sala no encontrada" });
        }
        res.status(200).json(sala);
    } catch (e) {
        res.status(404).json({ msg: `No se pudo obtener la sala ${_id}` });
    }
});

router.post("/", async (req, res) => {
    try {
        await salasService.create(req.body);
        res.status(200).json({ msg: "Sala creada correctamente" });
    } catch (e) {
        res.status(400).json({ msg: "No se pudo crear la sala" });
    }
});

router.put("/:_id", async (req, res) => {
    const { _id } = req.params;
    try {
        const result = await salasService.update(_id, req.body);
        if (result.modifiedCount) {
            res.status(200).json({ msg: `Sala ${_id} actualizada correctamente` });
        } else {
            res.status(200).json({ msg: `No se pudo actualizar la sala ${_id}` });
        }
    } catch (e) {
        res.status(400).json({ msg: `No se pudo actualizar la sala ${_id}` });
    }
});

router.delete("/:_id", async (req, res) => {
    const { _id } = req.params;
    try {
        await salasService.delete(_id);
        res.status(200).json({ msg: `Sala ${_id} eliminada` });
    } catch (e) {
        res.status(400).json({ msg: `No se pudo eliminar la sala ${_id}` });
    }
});

module.exports = router;
