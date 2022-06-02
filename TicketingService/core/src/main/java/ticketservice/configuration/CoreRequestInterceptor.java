package ticketservice.configuration;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import ticketservice.entity.UserEntity;
import ticketservice.exception.CoreError;
import ticketservice.repository.UserRepository;
import ticketservice.util.HttpUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static ticketservice.exception.CoreErrorCode.USER_TOKEN_INVALID;
import static ticketservice.exception.CoreErrorCode.USER_TOKEN_MISSING;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class CoreRequestInterceptor extends HandlerInterceptorAdapter {

    UserRepository userRepository;
    HttpUtils httpUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userToken = request.getHeader("User-Token");
        if (userToken == null) {
            httpUtils.setResponse(BAD_REQUEST.value(), new CoreError(USER_TOKEN_MISSING), response);
            return false;
        }
        Optional<UserEntity> optionalUser = userRepository.findByTokensToken(userToken);
        if (!optionalUser.isPresent()) {
            httpUtils.setResponse(BAD_REQUEST.value(), new CoreError(USER_TOKEN_INVALID), response);
            return false;
        }
        return true;
    }

}
