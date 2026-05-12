const express = require("express");
const router = express.Router();
const reservasService = require("../services/reservas-service");

router.get("/", async (_, res) => {
    try {
        const reservas = await reservasService.getAll();
        res.status(200).json(reservas);
    } catch (e) {
        res.status(404).json({ msg: "No se pudieron obtener las reservas" });
    }
});

router.get("/:_id", async (req, res) => {
    const { _id } = req.params;
    try {
        const reserva = await reservasService.get(_id);
        res.status(200).json(reserva);
    } catch (e) {
        res.status(404).json({ msg: `No se pudo obtener la reserva con _id: ${_id}` });
    }
});

router.post("/", async (req, res) => {
    try {
        const result = await reservasService.create(req.body);
        if (result.insertedCount != null && result.insertedCount > 0) {
            res.status(200).json({
                msg: "Reservas creadas correctamente",
            });
        } else if (result.insertedId) {
            res.status(200).json({
                msg: `Reserva creada correctamente con id: ${result.insertedId}`,
            });
        } else {
            res.status(200).json({ msg: "Reserva creada correctamente" });
        }
    } catch (e) {
        res.status(400).json({ msg: "No se pudo crear la reserva" });
    }
});

router.put("/:_id", async (req, res) => {
    const { _id } = req.params;
    try {
        const result = await reservasService.update(_id, req.body);
        if (result.modifiedCount) {
            res.status(200).json({
                msg: `Reserva con _id: ${_id} actualizada correctamente`,
            });
        } else {
            res.status(200).json({
                msg: `No se pudo actualizar la reserva con _id: ${_id}`,
            });
        }
    } catch (e) {
        res.status(400).json({ msg: `No se pudo actualizar la reserva con _id: ${_id}` });
    }
});

router.delete("/", async (_, res) => {
    try {
        await reservasService.deleteAll();
        res.status(200).json({ msg: "Reservas eliminadas correctamente" });
    } catch (e) {
        res.status(400).json({ msg: "No se pudieron eliminar las reservas" });
    }
});

router.delete("/:_id", async (req, res) => {
    const { _id } = req.params;
    try {
        await reservasService.delete(_id);
        res.status(200).json({ msg: `Reserva ${_id} eliminada correctamente` });
    } catch (e) {
        res.status(400).json({ msg: `No se pudo eliminar la reserva con _id: ${_id}` });
    }
});

module.exports = router;
