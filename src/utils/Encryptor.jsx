import pki from "node-forge/lib/x509.js";
import util from "node-forge/lib/x509.js"

// Generate an RSA key pair
const generateKeyPair = () => {
    return new Promise((resolve, reject) => {
        pki.rsa.generateKeyPair({ bits: 2048, workers: -1 }, (err, keypair) => {
            if (err) {
                reject(err);
            } else {
                resolve(keypair);
            }
        });
    });
};

const encrypt = (message, publicKey) => {
    const publicKeyForge = pki.publicKeyFromPem(publicKey);
    const encrypted = publicKeyForge.encrypt(util.encodeUtf8(message), 'RSAES-PKCS1-V1_5');
    return util.encode64(encrypted);
};

const decrypt = (encryptedMessage, privateKey) => {
    const privateKeyForge = pki.privateKeyFromPem(privateKey);
    const decrypted = privateKeyForge.decrypt(util.decode64(encryptedMessage), 'RSAES-PKCS1-V1_5');
    return util.decodeUtf8(decrypted);
};

export { generateKeyPair, encrypt, decrypt };
