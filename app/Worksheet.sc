import java.util.UUID
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

val password = "123"

val KEY_LENGTH = 1024

val HASHING_ITERATIONS = 1000

val DEFAULT_KEY_FACTORY = "PBKDF2WithHmacSHA512"

val keyFactory = SecretKeyFactory.getInstance(DEFAULT_KEY_FACTORY)

val salt = UUID.randomUUID().toString

val keySpec = new PBEKeySpec(
  password.toCharArray, salt.getBytes, HASHING_ITERATIONS, KEY_LENGTH
)

keyFactory.generateSecret(keySpec).getEncoded.map(byte => Math.abs(byte.toInt).toChar).mkString
