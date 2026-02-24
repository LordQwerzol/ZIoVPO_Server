package ZIoVPO.ZIoVPO_Server.service;

import ZIoVPO.ZIoVPO_Server.configuration.JwtTokenProvider;
import ZIoVPO.ZIoVPO_Server.entity.ApplicationUser;
import ZIoVPO.ZIoVPO_Server.entity.UserSession;
import ZIoVPO.ZIoVPO_Server.model.AuthenticationResponse;
import ZIoVPO.ZIoVPO_Server.model.enums.SessionStatus;
import ZIoVPO.ZIoVPO_Server.repository.ApplicationUserRepository;
import ZIoVPO.ZIoVPO_Server.repository.UserSessionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtTokenProvider tokenProvider;
    private final UserSessionRepository sessionRepository;
    private final ApplicationUserRepository applicationUserRepository;

    @Value("${jwt.refreshExpiration}")
    private long refreshExpiration;

    // Первая генерация пары токенов
    @Transactional
    public AuthenticationResponse generateTokenPair(Authentication authentication, String deviceId) {
        String email = authentication.getName();

        ApplicationUser user = applicationUserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("The user with this email was not found."));

        UserSession session = new UserSession();
        session.setUser(user);
        session.setDeviceId(deviceId);
        session.setStatus(SessionStatus.ACTIVE);
        session.setCreatedAt(Instant.now());
        session.setExpiresAt(Instant.now().plusMillis(refreshExpiration));
        session.setRefreshToken("temp"); // временное значение
        session = sessionRepository.save(session);

        String accessToken = tokenProvider.createAccessToken(
                email,
                user.getRole().getGrantedAuthorities(),
                session.getId().toString()
        );
        String refreshToken = tokenProvider.createRefreshToken(email, session.getId().toString());
        session.setRefreshToken(refreshToken);
        sessionRepository.save(session);
        return new AuthenticationResponse(email, accessToken, refreshToken);
    }

    // Обновление пары токенов по refreshToken
    @Transactional
    public AuthenticationResponse refreshTokens(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken) ||
                !tokenProvider.validateTokenType(refreshToken, "REFRESH")) {
            throw new SecurityException("Invalid refresh token");
        }
        String sessionId = tokenProvider.getSessionIdFromToken(refreshToken);
        UserSession session = sessionRepository.findById(UUID.fromString(sessionId))
                .orElseThrow(() -> new SecurityException("Session not found"));
        if (session.getStatus() != SessionStatus.ACTIVE) {
            throw new SecurityException("Session is not active");
        }
        if (!session.getRefreshToken().equals(refreshToken)) {
            throw new SecurityException("Refresh token mismatch");
        }
        if (session.getExpiresAt().isBefore(Instant.now())) {
            session.setStatus(SessionStatus.EXPIRED);
            sessionRepository.save(session);
            throw new SecurityException("Session expired");
        }

        session.setStatus(SessionStatus.REVOKED);
        session.setRevokedAt(Instant.now());
        sessionRepository.save(session);

        ApplicationUser user = session.getUser();
        if (user == null) {
            throw new SecurityException("User not found in session");
        }


        UserSession newSession = new UserSession();
        newSession.setUser(user);
        newSession.setDeviceId(session.getDeviceId());
        newSession.setStatus(SessionStatus.ACTIVE);
        newSession.setCreatedAt(Instant.now());
        newSession.setExpiresAt(Instant.now().plusMillis(refreshExpiration));
        newSession.setRefreshToken("temp");
        newSession = sessionRepository.save(newSession);


        String newAccessToken = tokenProvider.createAccessToken(
                user.getEmail(),
                user.getRole().getGrantedAuthorities(),
                newSession.getId().toString()
        );
        String newRefreshToken = tokenProvider.createRefreshToken(user.getEmail(), newSession.getId().toString());

        newSession.setRefreshToken(newRefreshToken);
        sessionRepository.save(newSession);

        return new AuthenticationResponse(user.getEmail(), newAccessToken, newRefreshToken);
    }
}
