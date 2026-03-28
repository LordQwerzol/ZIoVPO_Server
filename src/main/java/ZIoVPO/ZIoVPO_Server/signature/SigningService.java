package ZIoVPO.ZIoVPO_Server.signature;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.*;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class SigningService {

    private final Canonicalization canonicalization;
    private final KeyProvider keyProvider;
    @Value("${signature.algorithm}")
    private String algorithm;

    public String sign(Object payload) throws Exception {
        byte[] canonicalBytes = canonicalization.canonicalize(payload);
        PrivateKey signingKey = keyProvider.getSigningKey();
        return sign(signingKey, canonicalBytes);
    }

    private String sign(PrivateKey key, byte[] bytes) throws Exception  {
        Signature signature = Signature.getInstance(algorithm);
        signature.initSign(key);
        signature.update(bytes);
        byte[] signatureBytes = signature.sign();
        return Base64.getEncoder().encodeToString(signatureBytes);
    }

    public boolean verify(String signature, Object payload) throws Exception {
        byte[] canonicalBytes = canonicalization.canonicalize(payload);
        PublicKey publicKey = keyProvider.getPublicKey();
        Signature verifier = Signature.getInstance(algorithm);
        verifier.initVerify(publicKey);
        verifier.update(canonicalBytes);
        return verifier.verify(Base64.getDecoder().decode(signature));
    }
}
