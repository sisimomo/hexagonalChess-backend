package com.github.sisimomo.hexagonalchess.backend;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import com.cosium.spring.data.jpa.entity.graph.repository.support.EntityGraphJpaRepositoryFactoryBean;
import com.github.sisimomo.hexagonalchess.backend.commons.exception.error.Error;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import jakarta.persistence.NamedEntityGraphs;

@OpenAPIDefinition(
    info = @Info(title = "Hexagonal Chess API", version = "1.1.0",
        description = "This is the Hexagonal Chess API description",
        license = @License(name = "Licence", url = "https://opensource.org/license/mit/"),
        contact = @Contact(name = "Simon Vallières", email = "simon@vallieres.ca")),
    servers = {@Server(url = "/")}, security = @SecurityRequirement(name = "security_auth"))
@SecurityScheme(name = "security_auth", type = SecuritySchemeType.OAUTH2,
    flows = @OAuthFlows(
        authorizationCode = @OAuthFlow(authorizationUrl = "${springdoc.swagger-ui.oauth.authorizationUrl}",
            tokenUrl = "${springdoc.swagger-ui.oauth.tokenUrl}",
            scopes = {@OAuthScope(name = "openid", description = "OpenId"),
                @OAuthScope(name = "email", description = "email")})))
@EnableJpaRepositories(repositoryFactoryBeanClass = EntityGraphJpaRepositoryFactoryBean.class)
@SpringBootApplication
@EnableScheduling
public class HexagonalChessApplication {

  public static void main(String[] args) {
    validateEntityGraphBaseNames();
    validateAllUniqueErrorId();
    SpringApplication.run(HexagonalChessApplication.class, args);
  }

  private static void validateEntityGraphBaseNames() {
    ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);

    scanner.addIncludeFilter(new AnnotationTypeFilter(NamedEntityGraphs.class));

    final String staticFieldName = "ENTITY_GRAPH_BASE_NAME";

    scanner.findCandidateComponents(HexagonalChessApplication.class.getPackage().getName()).stream().map(bd -> {
      try {
        return Class.forName(bd.getBeanClassName());
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }).forEach(clazz -> {
      try {
        if (!clazz.getField(staticFieldName).get(String.class).equals(clazz.getName())) {
          throw new RuntimeException(
              String.format("Class \"%s\" static field called \"%s\" doesn't match the full name of its class.",
                  clazz.getName(), staticFieldName));
        }
      } catch (NoSuchFieldException e) {
        throw new RuntimeException(
            String.format("Class \"%s\" doesn't have a static field called \"%s\".", clazz.getName(), staticFieldName));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }

  private static void validateAllUniqueErrorId() {
    ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
    scanner.addIncludeFilter(new AssignableTypeFilter(Error.class));

    Set<String> set = new HashSet<>();
    scanner.findCandidateComponents(HexagonalChessApplication.class.getPackage().getName()).stream().map(bd -> {
      try {
        return Class.forName(bd.getBeanClassName());
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }).forEach(clazz -> {
      if (clazz.isEnum()) {
        Arrays.stream(clazz.getEnumConstants()).forEach(e -> {
          if (!set.add(((Error) e).getErrorId())) {
            throw new RuntimeException(String.format("Enum value \"%s\" of Class \"%s\" doesn't have a unique ErrorId.",
                ((Error) e).name(), clazz.getName()));
          }
        });
      }
    });
  }

  @Bean
  public SpelAwareProxyProjectionFactory projectionFactory() {
    return new SpelAwareProxyProjectionFactory();
  }

  // For KeycloakLogoutHandler
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

}
