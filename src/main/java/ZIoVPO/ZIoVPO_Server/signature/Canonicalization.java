package ZIoVPO.ZIoVPO_Server.signature;

import ZIoVPO.ZIoVPO_Server.util.JsonCanonicalizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class Canonicalization {

    private final JsonCanonicalizer jsonCanonicalizer;

    public byte[] canonicalize(Object payload) throws Exception {
        try {
            String canonicalJson = jsonCanonicalizer.canonizeJson(payload);
            return canonicalJson.getBytes(StandardCharsets.UTF_8);
        } catch (IllegalStateException e) {
            throw new Exception("Failed to canonicalization:", e);
        } catch (Exception e) {
            throw new Exception("Unexpected error during canonicalization", e);
        }
    }

}
