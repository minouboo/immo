package studi.immo.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity (prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .anyRequest().permitAll()
                .and().formLogin().permitAll()
                .and().logout().permitAll().logoutSuccessUrl("/");

    }

    /*@Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http

                .redirectToHttps(redirectToHttps ->
                        redirectToHttps
                                .httpsRedirectWhen(e -> e.getRequest().getHeaders().containsKey("X-Forwarded-Proto"))
                );
        return http.build();
    }*/

    @Bean
    public PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean()throws Exception{
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    /*@Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .requiresChannel(channel ->
                        channel.anyRequest().requiresSecure())
                .authorizeRequests(authorize ->
                        authorize.anyRequest().permitAll())
                .build();
    }*/


}
