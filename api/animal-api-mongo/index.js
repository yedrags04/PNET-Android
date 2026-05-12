const path = require("path");
require("dotenv").config({ path: path.join(__dirname, ".env") });

const mongoose = require("mongoose");
const app = require("./app");
const { setDb } = require("./db");
const { resolveMongoUri } = require("./lib/resolveMongoUri");

const { uri, mode } = resolveMongoUri();
const dbName = process.env.MONGODB_NAME ? String(process.env.MONGODB_NAME).trim() : undefined;
const PORT = Number(process.env.PORT) || 3000;

function logConnectionHint() {
    const user = process.env.MONGODB_USER ? String(process.env.MONGODB_USER).trim() : null;
    const host =
        process.env.MONGODB_CLUSTER_HOST?.trim() ||
        process.env.MONGODB_HOST?.trim() ||
        null;
    if (mode === "env-parts" && user && host) {
        console.log(
            `[MongoDB] Modo: usuario/contraseña por variables | usuario "${user}" | host "${host}" | dbName: ${dbName || "(por defecto)"}`,
        );
        return;
    }
    if (!uri) return;
    try {
        const normalized = uri.replace(/^mongodb(\+srv)?:\/\//, "http://");
        const parsed = new URL(normalized);
        console.log(
            `[MongoDB] Modo: MONGODB_URL | usuario "${parsed.username || "?"}" | host "${parsed.hostname || "?"}" | dbName: ${dbName || "(por defecto de la URI)"}`,
        );
    } catch {
        console.log("[MongoDB] URI cargada (no se pudo resumir).");
    }
}

function printAtlasAuthHelp() {
    console.error(`
→ Atlas rechaza usuario/contraseña ("bad auth").

  Aunque "estén bien" en Atlas, en el .env suele fallar por:
  · Contraseña con caracteres especiales (@ # $ % & + = / : ?) sin codificar en la URL.
  · Espacio o salto de línea al pegar la contraseña en MONGODB_URL.
  · Usuario de Atlas distinto al que copiaste (proyecto u otro cluster).

  Prueba el modo recomendado en .env (deja MONGODB_URL comentado):

    MONGODB_USER=ivysudadera_db_user
    MONGODB_PASSWORD='pega_aqui_la_contraseña_exacta'
    MONGODB_CLUSTER_HOST=cluster0.ketrpdw.mongodb.net
    MONGODB_NAME=tu_base_de_datos

  Luego: npm run check-db

  Si sigue igual: Atlas → Database Access → Edit Password (nueva) y pega solo en MONGODB_PASSWORD.
`);
}

if (!uri) {
    console.error(
        "❌ Configura MongoDB en .env: o bien MONGODB_URL, o bien MONGODB_USER + MONGODB_PASSWORD + MONGODB_CLUSTER_HOST (ver .env.example).",
    );
    process.exit(1);
}

logConnectionHint();

const connectOpts = {
    serverSelectionTimeoutMS: 25_000,
};
if (dbName) {
    connectOpts.dbName = dbName;
}

mongoose
    .connect(uri, connectOpts)
    .then(() => {
        setDb(mongoose.connection.db);
        console.log("✅ MongoDB conectado (API móvil HeistCraft)");
        app.listen(PORT, "0.0.0.0", () => {
            console.log(`🚀 API en http://0.0.0.0:${PORT} (emulador Android: http://10.0.2.2:${PORT}/)`);
        });
    })
    .catch((err) => {
        const msg = err.message || String(err);
        console.error("❌ Error al conectar a MongoDB:", msg);
        if (msg.includes("bad auth") || err.code === 8000) {
            printAtlasAuthHelp();
        }
        process.exit(1);
    });
