const express = require("express");
const router = express.Router();
const utensiliosService = require("../services/utensilios-service");

router.get("/", async (_, res) => {
    try {
        const utensilios = await utensiliosService.getAll();
        res.status(200).json(utensilios);
    } catch (e) {
        res.status(404).json({ msg: "No se pudieron obtener los utensilios" });
    }
});

router.get("/:_id", async (req, res) => {
    const { _id } = req.params;
    try {
        const utensilio = await utensiliosService.get(_id);
        res.status(200).json(utensilio);
    } catch (e) {
        res.status(404).json({ msg: `No se pudo obtener el utensilio con _id: ${_id}` });
    }
});

module.exports = router;
