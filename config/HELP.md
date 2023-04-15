# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.0.5/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.0.5/maven-plugin/reference/html/#build-image)
* [Distributed Tracing Reference Guide](https://micrometer.io/docs/tracing)
* [Getting Started with Distributed Tracing](https://docs.spring.io/spring-boot/docs/3.0.5/reference/html/actuator.html#actuator.micrometer-tracing.getting-started)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/3.0.5/reference/htmlsingle/#actuator)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/3.0.5/reference/htmlsingle/#using.devtools)
* [codecentric's Spring Boot Admin (Client)](https://codecentric.github.io/spring-boot-admin/current/#getting-started)
* [Spring Configuration Processor](https://docs.spring.io/spring-boot/docs/3.0.5/reference/htmlsingle/#appendix.configuration-metadata.annotation-processor)
* [Testcontainers](https://www.testcontainers.org/)
* [Cloud Bootstrap](https://docs.spring.io/spring-cloud-commons/docs/current/reference/html/)
* [Spring Security](https://docs.spring.io/spring-boot/docs/3.0.5/reference/htmlsingle/#web.security)
* [Config Server](https://docs.spring.io/spring-cloud-config/docs/current/reference/html/#_spring_cloud_config_server)

### Guides

The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)
* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)
* [Centralized Configuration](https://spring.io/guides/gs/centralized-configuration/)

## Generate keystore

```shell
keytool -genkeypair -alias gf-invest -keyalg RSA \
-dname "CN=GF Invest,OU=Oleksii Khalikov,O=GFalcon-UA,L=Kyiv,S=Kyiv,C=UA" \
-keypass changeme -keystore server.jks -storepass letmein
```

for Java 11

```shell
keytool -genkeypair -alias gf-invest -keyalg RSA \
-dname "CN=GF Invest,OU=Oleksii Khalikov,O=GFalcon-UA,L=Kyiv,S=Kyiv,C=UA" \
-keypass changeme -keystore server.jks -storepass changeme
```