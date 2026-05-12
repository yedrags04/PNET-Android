const { getCollection } = require("../db");
const { ObjectId } = require("mongodb");

class BancosService {
    async getAll() {
        const col = getCollection("bancos");
        return await col.find({}).toArray();
    }

    async get(_id) {
        const col = getCollection("bancos");
        return await col.findOne({ _id: new ObjectId(_id) });
    }

    async create(data) {
        const col = getCollection("bancos");
        if (Array.isArray(data)) {
            return await col.insertMany(data);
        }
        return await col.insertOne(data);
    }

    async deleteAll() {
        const col = getCollection("bancos");
        return await col.deleteMany({});
    }

    async delete(_id) {
        const col = getCollection("bancos");
        return await col.deleteOne({ _id: new ObjectId(_id) });
    }

    async update(_id, data) {
        const col = getCollection("bancos");
        return await col.updateOne({ _id: new ObjectId(_id) }, { $set: data });
    }
}

module.exports = new BancosService();
