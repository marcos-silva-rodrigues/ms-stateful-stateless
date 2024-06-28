package com.rodrigues.silva.marcos.ms.any_api.infra.config;

import com.rodrigues.silva.marcos.ms.any_api.core.client.TokenClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class HttpInterfaceConfig {

  @Value("${app.client.base-url}")
  private String baseUrl;

  @Bean
  public TokenClient tokenClient() {
    WebClient  restClient = WebClient .builder().baseUrl(baseUrl).build();
    WebClientAdapter  adapter = WebClientAdapter.create(restClient);
    HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

    return factory.createClient(TokenClient.class);
  }
}
