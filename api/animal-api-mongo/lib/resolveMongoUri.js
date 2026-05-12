"use strict";

/**
 * Resuelve la cadena de conexión a Atlas.
 *
 * Opción A (recomendada si la contraseña tiene @ # $ % & etc.):
 *   MONGODB_USER, MONGODB_PASSWORD, MONGODB_CLUSTER_HOST
 *
 * Opción B:
 *   MONGODB_URL o MONGODB_URI (contraseña debe ir codificada en la URL)
 */
function trimEnv(name) {
    const v = process.env[name];
    if (v === undefined || v === null) return undefined;
    const s = String(v).trim();
    return s === "" ? undefined : s;
}

function resolveMongoUri() {
    const user = trimEnv("MONGODB_USER");
    const passwordRaw = process.env.MONGODB_PASSWORD;
    const password =
        passwordRaw !== undefined && passwordRaw !== null ? String(passwordRaw).trim() : undefined;
    const host = trimEnv("MONGODB_CLUSTER_HOST") || trimEnv("MONGODB_HOST");

    if (user && password !== undefined && password !== "" && host) {
        const u = encodeURIComponent(user);
        const p = encodeURIComponent(password);
        let uri = `mongodb+srv://${u}:${p}@${host}/?retryWrites=true&w=majority`;
        const appName = trimEnv("MONGODB_APPNAME") || "HeistCraftAndroidApi";
        uri += `&appName=${encodeURIComponent(appName)}`;
        if (!/[?&]authSource=/.test(uri)) {
            uri += "&authSource=admin";
        }
        return { uri, mode: "env-parts" };
    }

    const fromUrl = trimEnv("MONGODB_URL") || trimEnv("MONGODB_URI");
    if (fromUrl) {
        return { uri: fromUrl, mode: "url" };
    }

    return { uri: null, mode: null };
}

module.exports = { resolveMongoUri };
