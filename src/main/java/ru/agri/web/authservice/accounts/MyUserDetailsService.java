package ru.agri.web.authservice.accounts;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private final AccountRepository accountRepository;

    public MyUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return accountRepository
                .findByUsername(username)
                .map(account -> {
                    boolean active = account.isActive();
                    return new User(account.getUsername(),
                                    account.getPassword(),
                                    active, active, active, active,
                                    AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER"));
                })
                .orElseThrow(() -> new UsernameNotFoundException(String.format("username %s not found!", username)));
    }
}
