let db;

function setDb(database) {
    db = database;
}

function getCollection(collectionName) {
    if (!db) {
        throw new Error("Base de datos no inicializada");
    }
    return db.collection(collectionName);
}

module.exports = {
    setDb,
    getCollection,
};
