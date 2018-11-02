package ru.agri.web.authservice;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import ru.agri.web.authservice.clients.ClientRepository;
import ru.agri.web.authservice.clients.MyClientDetailsService;

@EnableAuthorizationServer
@Configuration
@Order(1)
public class AuthorizationServerAppConfigurer extends AuthorizationServerConfigurerAdapter {

    private final ClientRepository clientRepository;
    private final LoadBalancerClient loadBalancerClient;
    private final AuthenticationManager authenticationManager;


    public AuthorizationServerAppConfigurer(ClientRepository clientRepository,
                                            LoadBalancerClient loadBalancerClient,
                                            AuthenticationManager authenticationManager) {
        this.clientRepository = clientRepository;
        this.loadBalancerClient = loadBalancerClient;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(new MyClientDetailsService(clientRepository, loadBalancerClient));
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager);
    }
}
