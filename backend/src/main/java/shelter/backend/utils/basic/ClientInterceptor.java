package shelter.backend.utils.basic;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class ClientInterceptor {

    public static String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal.equals("anonymousUser")) {
            throw new UsernameNotFoundException("Nie znaleziono żadnego zalogowanego użytkownika");
        }
        UserDetails userDetails = (UserDetails) principal;
        return userDetails.getUsername();
    }

    public static boolean isAnyUserLoggedIn() {
        return !SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser");
    }
}