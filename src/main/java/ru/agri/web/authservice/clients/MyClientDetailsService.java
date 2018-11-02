package ru.agri.web.authservice.clients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

import java.util.Collections;
import java.util.Optional;

public class MyClientDetailsService implements ClientDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(MyClientDetailsService.class);
    private final ClientRepository clientRepository;
    private LoadBalancerClient loadBalancerClient;

    public MyClientDetailsService(ClientRepository clientRepository, LoadBalancerClient loadBalancerClient) {
        this.clientRepository = clientRepository;
        this.loadBalancerClient = loadBalancerClient;
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return clientRepository
                .findByClientId(clientId)
                .map(
                        client -> {

                            BaseClientDetails details = new BaseClientDetails(client.getClientId(),
                                                                              null,
                                                                              client.getScopes(),
                                                                              client.getAuthorizedGrantTypes(),
                                                                              client.getAuthorities());
                            details.setClientSecret(client.getSecret());

                            // <1>
                            // details.setAutoApproveScopes
                            //    (Arrays.asList(client.getAutoApproveScopes().split(",")));

                            // <2>
//                            String greetingsClientRedirectUri = Optional
//                                    .ofNullable(loadBalancerClient.choose("getewayservice"))
//                                    .map(si -> "http://" + si.getHost() + ':' + si.getPort() + '/')
//                                    .orElseThrow(
//                                            () -> {
//                                                logger.info("Not found service");
//                                                return new ClientRegistrationException("couldn't find and bind a greetings-client IP");
//                                            });
//
//                            logger.info("redirect to {}", greetingsClientRedirectUri);
//                            details.setRegisteredRedirectUri(Collections.singleton(greetingsClientRedirectUri));
                            return details;
                        })
                .orElseThrow(
                        () -> new ClientRegistrationException(String.format("no client %s registered", clientId)));
    }
}
