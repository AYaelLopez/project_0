import java.util.zip.CRC32;

public class CRC32Hashing {
    public static String hash(String password) {
        CRC32 crc32 = new CRC32();
        crc32.update(password.getBytes());
        return Long.toHexString(crc32.getValue());
    }
}
