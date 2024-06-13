package utils

import java.security.{KeyPair, KeyPairGenerator, Security}
import java.security.spec.RSAKeyGenParameterSpec
import javax.crypto.Cipher
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.util.Base64

object Encryptor {
  Security.addProvider(new BouncyCastleProvider())

  val serverKey: KeyPair = generateKeyPair()

  private def generateKeyPair(): KeyPair = {
    val keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC")
    keyPairGenerator.initialize(new RSAKeyGenParameterSpec(2048, RSAKeyGenParameterSpec.F4))
    keyPairGenerator.generateKeyPair()
  }

  def encrypt(input: String, publicKey: java.security.PublicKey): String = {
    val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC")
    cipher.init(Cipher.ENCRYPT_MODE, publicKey)
    Base64.getEncoder.encodeToString(
      cipher.doFinal(input.getBytes("UTF-8"))
    )
  }

  def decrypt(encryptedDataString: String): String = {
    val encryptedData = Base64.getDecoder.decode(encryptedDataString)
    val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC")
    cipher.init(Cipher.DECRYPT_MODE, serverKey.getPrivate)
    new String(cipher.doFinal(encryptedData), "UTF-8")
  }

}
