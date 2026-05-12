const { getCollection } = require("../db");
const { ObjectId } = require("mongodb");

class ReservasService {
    async getAll() {
        const col = getCollection("reservas");
        return await col.find({}).toArray();
    }

    async get(_id) {
        const col = getCollection("reservas");
        return await col.findOne({ _id: new ObjectId(_id) });
    }

    async create(data) {
        const col = getCollection("reservas");
        if (Array.isArray(data)) {
            return await col.insertMany(data);
        }
        return await col.insertOne(data);
    }

    async deleteAll() {
        const col = getCollection("reservas");
        return await col.deleteMany({});
    }

    async delete(_id) {
        const col = getCollection("reservas");
        return await col.deleteOne({ _id: new ObjectId(_id) });
    }

    async update(_id, data) {
        const col = getCollection("reservas");
        return await col.updateOne({ _id: new ObjectId(_id) }, { $set: data });
    }
}

module.exports = new ReservasService();
