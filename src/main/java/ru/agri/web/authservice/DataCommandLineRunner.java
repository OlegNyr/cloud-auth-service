package ru.agri.web.authservice;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.agri.web.authservice.accounts.Account;
import ru.agri.web.authservice.accounts.AccountRepository;
import ru.agri.web.authservice.clients.Client;
import ru.agri.web.authservice.clients.ClientRepository;

import java.util.stream.Stream;

@Profile("dev")
@Component
class DataCommandLineRunner implements CommandLineRunner {

    private final AccountRepository accountRepository;

    private final ClientRepository clientRepository;
    private PasswordEncoder passwordEncoder;

    public DataCommandLineRunner(AccountRepository accountRepository,
                                 ClientRepository clientRepository,
                                 PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        Stream
                .of("dsyer,cloud", "pwebb,boot", "mminella,batch", "rwinch,security", "jlong,spring")
                .map(s -> s.split(","))
                .forEach(
                        tuple -> accountRepository.save(new Account(tuple[0], passwordEncoder.encode(tuple[1]), true)));

        Stream.of("html5,password", "android,secret").map(x -> x.split(","))
                .forEach(x -> clientRepository.save(new Client(x[0], passwordEncoder.encode(x[1]))));
    }
}
