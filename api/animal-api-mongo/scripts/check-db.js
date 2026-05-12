/**
 * Comprueba la conexión a MongoDB sin arrancar el servidor HTTP.
 * Uso: npm run check-db
 */
const path = require("path");
require("dotenv").config({ path: path.join(__dirname, "..", ".env") });

const mongoose = require("mongoose");
const { resolveMongoUri } = require("../lib/resolveMongoUri");

const { uri, mode } = resolveMongoUri();
const dbName = process.env.MONGODB_NAME ? String(process.env.MONGODB_NAME).trim() : undefined;

function logHint() {
    const user = process.env.MONGODB_USER ? String(process.env.MONGODB_USER).trim() : null;
    const host =
        process.env.MONGODB_CLUSTER_HOST?.trim() ||
        process.env.MONGODB_HOST?.trim() ||
        null;
    if (mode === "env-parts" && user && host) {
        console.log(`Modo variables: usuario "${user}" → host "${host}"`);
        return;
    }
    if (!uri) return;
    try {
        const parsed = new URL(uri.replace(/^mongodb(\+srv)?:\/\//, "http://"));
        console.log(`Modo MONGODB_URL: usuario "${parsed.username || "?"}" → host "${parsed.hostname || "?"}"`);
    } catch {
        console.log("URI presente (resumen no disponible).");
    }
}

function printAtlasHelp() {
    console.error(`
Fallo de autenticación ("bad auth"). Suele ser la contraseña tal como llega a Node, no Atlas en sí.

  1) Usa variables separadas (evita pegar la contraseña dentro de la URL):

     MONGODB_USER=tu_usuario
     MONGODB_PASSWORD='contraseña_exacta'
     MONGODB_CLUSTER_HOST=cluster0.ketrpdw.mongodb.net
     MONGODB_NAME=nombre_de_la_base

     (Comenta o borra MONGODB_URL para no mezclar.)

  2) En Atlas → Database Access → "Edit" → "Show" / nueva contraseña y copia
     de nuevo (sin espacios al final).

  3) El usuario debe ser "Database Access" (no solo cuenta Atlas de login web).

Vuelve a ejecutar: npm run check-db
`);
}

if (!uri) {
    console.error("❌ Falta configuración: MONGODB_URL o (MONGODB_USER + MONGODB_PASSWORD + MONGODB_CLUSTER_HOST).");
    process.exit(1);
}

logHint();

const connectOpts = {
    serverSelectionTimeoutMS: 20_000,
};
if (dbName) {
    connectOpts.dbName = dbName;
}

mongoose
    .connect(uri, connectOpts)
    .then(async () => {
        const name = mongoose.connection.db.databaseName;
        const cols = await mongoose.connection.db.listCollections().toArray();
        console.log(`✅ Conexión correcta. Base activa: "${name}"`);
        console.log(`   Colecciones (${cols.length}):`, cols.map((c) => c.name).sort().join(", ") || "(ninguna aún)");
        await mongoose.disconnect();
        process.exit(0);
    })
    .catch((err) => {
        const msg = err.message || String(err);
        console.error("❌", msg);
        if (msg.includes("bad auth") || err.code === 8000 || err.codeName === "AtlasError") {
            printAtlasHelp();
        } else if (msg.includes("ENOTFOUND") || msg.includes("querySrv")) {
            console.error("\nProblema de DNS o red al resolver el host SRV de Atlas.\n");
        } else if (msg.includes("timed out") || msg.includes("Server selection")) {
            console.error(
                "\nTimeout al seleccionar servidor: revisa Network Access en Atlas (IP permitida) y firewall.\n",
            );
        }
        process.exit(1);
    });
