package shelter.backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import shelter.backend.models.Role;
import shelter.backend.models.User;
import shelter.backend.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Użytkwonik nie znaleziony");
        }
        return buildUserDetailsFromUser(user);
    }

    private UserDetails buildUserDetailsFromUser(User user) {
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .disabled(user.isDisabled())
                .authorities(getAuthorities(user)).build();
    }

    private Collection<GrantedAuthority> getAuthorities(User user) {
        Set<Role> userRoles = user.getRoles();
        Collection<GrantedAuthority> authorities = new ArrayList<>(userRoles.size());
        for (Role role : userRoles) {
            authorities.add(new SimpleGrantedAuthority(role.getName().name()));
        }
        return authorities;
    }
}
