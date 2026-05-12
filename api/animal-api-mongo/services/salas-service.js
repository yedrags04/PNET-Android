const { getCollection } = require("../db");
const { ObjectId } = require("mongodb");

class SalasService {
    async getAll() {
        const col = getCollection("salas");
        return await col.find({}).toArray();
    }

    async get(_id) {
        const col = getCollection("salas");
        return await col.findOne({ _id: new ObjectId(_id) });
    }

    async create(data) {
        const col = getCollection("salas");
        if (Array.isArray(data)) {
            return await col.insertMany(data);
        }
        return await col.insertOne(data);
    }

    async deleteAll() {
        const col = getCollection("salas");
        return await col.deleteMany({});
    }

    async delete(_id) {
        const col = getCollection("salas");
        return await col.deleteOne({ _id: new ObjectId(_id) });
    }

    async update(_id, data) {
        const col = getCollection("salas");
        return await col.updateOne({ _id: new ObjectId(_id) }, { $set: data });
    }
}

module.exports = new SalasService();
