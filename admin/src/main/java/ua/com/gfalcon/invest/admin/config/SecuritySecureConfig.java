/*
 * Copyright (c) 2016-2023 Oleksii Khalikov @GFalcon-UA (http://gfalcon.com.ua)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ua.com.gfalcon.invest.admin.config;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Profile("secure")
@Configuration(proxyBeanMethods = false)
public class SecuritySecureConfig {

    private final String adminContextPath;

    public SecuritySecureConfig(AdminServerProperties adminServerProperties) {
        this.adminContextPath = adminServerProperties.getContextPath();
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(this.adminContextPath + "/");

        http.authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                        .requestMatchers(new AntPathRequestMatcher(this.adminContextPath + "/assets/**"))
                        .permitAll()
                        .requestMatchers(new AntPathRequestMatcher(this.adminContextPath + "/login"))
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .formLogin((formLogin) -> formLogin.loginPage(this.adminContextPath + "/login")
                        .successHandler(successHandler))
                .logout((logout) -> logout.logoutUrl(this.adminContextPath + "/logout"))
                .httpBasic(Customizer.withDefaults())
                .csrf((csrf) -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers(
                                new AntPathRequestMatcher(this.adminContextPath + "/instances", HttpMethod.POST.toString()),
                                new AntPathRequestMatcher(this.adminContextPath + "/instances/*",
                                        HttpMethod.DELETE.toString()),
                                new AntPathRequestMatcher(this.adminContextPath + "/actuator/**")));

        return http.build();
    }
}
