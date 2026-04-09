package ZIoVPO.ZIoVPO_Server.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class BinaryWriter {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    public void writeUint8(int value) {
        outputStream.write(value & 0xFF);
    }

    public void writeUint16(int value) {
        outputStream.write((value >>> 8) & 0xFF);
        outputStream.write(value & 0xFF);
    }

    public void writeUint32(long value) {
        outputStream.write((int) ((value >>> 24) & 0xFF));
        outputStream.write((int) ((value >>> 16) & 0xFF));
        outputStream.write((int) ((value >>> 8) & 0xFF));
        outputStream.write((int) (value & 0xFF));
    }

    public void writeUint64(long value) {
        outputStream.write((int) ((value >>> 56) & 0xFF));
        outputStream.write((int) ((value >>> 48) & 0xFF));
        outputStream.write((int) ((value >>> 40) & 0xFF));
        outputStream.write((int) ((value >>> 32) & 0xFF));
        outputStream.write((int) ((value >>> 24) & 0xFF));
        outputStream.write((int) ((value >>> 16) & 0xFF));
        outputStream.write((int) ((value >>> 8) & 0xFF));
        outputStream.write((int) (value & 0xFF));
    }

    public void writeInt64(long value) {
        writeUint64(value);
    }

    public void writeUuid(UUID uuid) {
        writeUint64(uuid.getMostSignificantBits());
        writeUint64(uuid.getLeastSignificantBits());
    }

    public void writeBytes(byte[] bytes) {
        try {
            outputStream.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e); // не случится
        }
    }

    public void writeString(String s) {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        writeUint32(bytes.length);
        writeBytes(bytes);
    }

    public void writeBytesWithLength(byte[] bytes) {
        writeUint32(bytes.length);
        writeBytes(bytes);
    }

    public byte[] toByteArray() {
        return outputStream.toByteArray();
    }

    public void reset() { outputStream.reset();
    }

    public long position() {
        return outputStream.size();
    }

    public void writeStringHex(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i+1), 16));
        }
        writeBytesWithLength(data);
    }


}