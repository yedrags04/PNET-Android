const { getCollection } = require("../db");
const { ObjectId } = require("mongodb");

const getAll = async () => {
    const collection = getCollection("utensilios");
    return await collection.find({}).toArray();
};

const get = async (id) => {
    const collection = getCollection("utensilios");
    return await collection.findOne({ _id: new ObjectId(id) });
};

module.exports = { getAll, get };
