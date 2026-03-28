package ZIoVPO.ZIoVPO_Server.signature;

import ZIoVPO.ZIoVPO_Server.util.SignatureKeyStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;

@Service
@RequiredArgsConstructor
public class KeyProvider {

    private final SignatureKeyStoreService keyStoreService;

    private volatile PrivateKey cachedPrivateKey;
    private volatile PublicKey cachedPublicKey;

    public PrivateKey getSigningKey() throws Exception {
        PrivateKey key = cachedPrivateKey;
        if (key != null) {
            return key;
        }
        synchronized (this) {
            try {
                cachedPrivateKey = keyStoreService.getPrivateKey();
                return cachedPrivateKey;
            } catch (IllegalStateException e) {
                throw new Exception("Failed to authenticate with keystore: " + e.getMessage(), e);
            } catch (Exception e) {
                throw new Exception("Other errors: " + e.getMessage(), e);
            }
        }
    }

    public PublicKey getPublicKey() throws Exception {
        PublicKey key = cachedPublicKey;
        if (key != null) {
            return key;
        }
        synchronized (this) {
            try {
                cachedPublicKey = keyStoreService.getPublicKey();
                return cachedPublicKey;
            } catch (IllegalStateException e) {
                throw new Exception("Failed to authenticate with keystore", e);
            } catch (Exception e) {
                throw new Exception("Other errors:", e);
            }
        }
    }
}
