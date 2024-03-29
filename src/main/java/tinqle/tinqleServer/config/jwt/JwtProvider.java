package tinqle.tinqleServer.config.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.config.redis.RedisService;
import tinqle.tinqleServer.config.security.PrincipalDetailService;
import tinqle.tinqleServer.domain.account.dto.AccountDto.SigningAccount;
import tinqle.tinqleServer.domain.account.exception.AuthException;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.account.model.Role;
import tinqle.tinqleServer.util.CustomEncryptUtil;

import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static tinqle.tinqleServer.common.constant.GlobalConstants.AT_SIGN;

@Component
@Slf4j
@Transactional
public class JwtProvider {

    private final PrincipalDetailService principalDetailService;
    private final Key privateKey;
    private final CustomEncryptUtil customEncryptUtil;
    private final RedisService redisService;


    public JwtProvider(PrincipalDetailService principalDetailService, @Value("${jwt.secret-key}") String secretKey, CustomEncryptUtil customEncryptUtil, RedisService redisService) {
        this.principalDetailService = principalDetailService;
        this.privateKey = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.customEncryptUtil = customEncryptUtil;
        this.redisService = redisService;
    }

    @Value("${jwt.access-duration}")
    public long accessDuration;

    @Value("${jwt.refresh-duration}")
    public long refreshDuration;

    public String resolveToken(HttpServletRequest request) {
        String rawToken = request.getHeader("Authorization");
        if (rawToken != null && rawToken.startsWith("Bearer "))
            return rawToken.replace("Bearer ","");
        else return null;
    }

    public String resolveToken(String rawToken) {
        if (rawToken != null && rawToken.startsWith("Bearer "))
            return rawToken.replace("Bearer ","");
        else return null;
    }

    //SignToken 생성
    public String createSignToken(String socialEmail, String nickname, String refreshToken) {
        Date now = new Date(System.currentTimeMillis());
        return Jwts.builder()
                .setSubject(customEncryptUtil.encrypt(socialEmail + "@" + refreshToken))
                .claim("sign", Role.ROLE_USER)
                .claim("nickname", customEncryptUtil.encrypt(nickname))
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessDuration))
                .signWith(privateKey)
                .compact();
    }

    //accessToken 생성
    private String createAccessToken(Account account) {
        Date now = new Date(System.currentTimeMillis());
        return Jwts.builder()
                .setSubject(customEncryptUtil.encrypt(account.getSocialEmail()))
                .claim("role", account.getRole())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+accessDuration))
                .signWith(privateKey)
                .compact();
    }

    private String createAccessToken(String socialEmail, Role role) {
        Date now = new Date(System.currentTimeMillis());
        return Jwts.builder()
                .setSubject(customEncryptUtil.encrypt(socialEmail))
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+accessDuration))
                .signWith(privateKey)
                .compact();
    }

    //RefreshToken 생성
    private String createRefreshToken(Account account) {
        Date now = new Date(System.currentTimeMillis());
        return Jwts.builder()
                .setSubject(customEncryptUtil.encrypt(account.getSocialEmail()))
                .setIssuedAt(now)
                .claim("role", account.getRole())
                .claim("tokenType", "refresh")
                .setExpiration(new Date(now.getTime()+refreshDuration))
                .signWith(privateKey)
                .compact();
    }

    private String createRefreshToken(String socialEmail, Role role) {
        Date now = new Date(System.currentTimeMillis());
        return Jwts.builder()
                .setSubject(customEncryptUtil.encrypt(socialEmail))
                .setIssuedAt(now)
                .claim("role", role)
                .claim("tokenType", "refresh")
                .setExpiration(new Date(now.getTime()+refreshDuration))
                .signWith(privateKey)
                .compact();
    }

    //Access, Refresh Token 검증 (만료 여부 검사)
    public boolean validate(String token){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(privateKey)  //검증키 지정
                    .build()
                    .parseClaimsJws(token); //토큰의 유효 기간을 확인하기 위해 exp claim을 가져와 현재와 비교
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.warn("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT 입니다.");
        } catch (UnsupportedJwtException e) {
            log.warn("지원되지 않는 JWT입니다.");
        } catch (IllegalArgumentException e) {
            log.warn("JWT 잘못 되었습니다.");
        }
        return false;
    }

    //Authentication 객체 가져오기
    public Authentication getAuthentication(String accessToken) {
        Claims body = Jwts.parserBuilder()
                .setSigningKey(privateKey)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();
        String socialEmail = customEncryptUtil.decrypt(body.getSubject());
        UserDetails userDetails = principalDetailService.loadUserByUsername(socialEmail);
        return new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
    }

    public String getSocialEmailAtSocket(String accessToken) {
        Claims body = Jwts.parserBuilder()
                .setSigningKey(privateKey)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();
        return customEncryptUtil.decrypt(body.getSubject());
    }

    public SigningAccount getSignKey(String signToken) {
        Claims body = Jwts.parserBuilder()
                .setSigningKey(privateKey)
                .build()
                .parseClaimsJws(signToken)
                .getBody();
        String socialEmail = customEncryptUtil.decrypt(body.getSubject());
        String nickname = customEncryptUtil.decrypt((String) body.get("nickname"));
        String[] split = socialEmail.split(AT_SIGN);
        //socialEmail, socialUuid,socialType, refreshToken, nickname
        return new SigningAccount(split[0]+AT_SIGN+split[1], split[0],split[1], split[2], nickname);
    }

    /**
     * Access, Refresh 최초 발행
     */
    public JwtDto issue(Account account) {
        String access = createAccessToken(account);
        String refresh = createRefreshToken(account);

        redisService.setValues(account.getSocialEmail(), refresh, refreshDuration, TimeUnit.MILLISECONDS);
        return new JwtDto(access,refresh);
    }

    public JwtDto reissue(String refreshToken) {

        try {
            Claims body = Jwts.parserBuilder()
                    .setSigningKey(privateKey)
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();
            if (body.get("tokenType") == null || !body.get("tokenType").equals("refresh"))
                throw new AuthException(StatusCode.IS_NOT_REFRESH);
            String socialEmail = customEncryptUtil.decrypt(body.getSubject());
            String valueToken = redisService.getValues(socialEmail);
            if (valueToken == null || !valueToken.equals(refreshToken)) throw new AuthException(StatusCode.IS_NOT_CORRECT_REFRESH);

            Role role = changeObjectToRole(body.get("role"));
            String newAccessToken = createAccessToken(socialEmail, role);
            String newRefreshToken = createRefreshToken(socialEmail, role);

            redisService.setValues(socialEmail, newRefreshToken, refreshDuration, TimeUnit.MILLISECONDS);
            return new JwtDto(newAccessToken,newRefreshToken);
        } catch (ExpiredJwtException e) {
            throw new AuthException(StatusCode.EXPIRED_REFRESH);
        }
    }

    private Role changeObjectToRole(Object role) {
        if (role.equals("ROLE_USER")) {
            return Role.ROLE_USER;
        } else throw new AuthException(StatusCode.FILTER_ROLE_FORBIDDEN);
    }
}
