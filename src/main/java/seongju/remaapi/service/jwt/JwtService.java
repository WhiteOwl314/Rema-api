package seongju.remaapi.service.jwt;

import seongju.remaapi.vo.JwtResponse;

public interface JwtService {
    JwtResponse authenticate(String username, String password) throws Exception;
}
