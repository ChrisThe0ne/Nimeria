package hu.uni.ekcu.Nimeria.security;

import hu.uni.ekcu.Nimeria.auth.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserService applicationUserService;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder encoder,
                                     ApplicationUserService applicationUserService){
        this.passwordEncoder = encoder;
        this.applicationUserService = applicationUserService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
                .antMatchers("/api/v*/registration").permitAll()
                .antMatchers("/api/v*/registration/*").permitAll()
//                .antMatchers("/api/v*/exercises").permitAll()
//                .antMatchers("/api/v*/exercises/*").permitAll()
//                .antMatchers("/api/v*/user").permitAll()
//                .antMatchers("/api/v*/user*").permitAll()
//                .antMatchers("/api/v*/user/deleteAnyUser/*").permitAll()
//                .antMatchers("/api/v*/user/modifyProfile/*").permitAll()
                .antMatchers("/api/v*/user/deleteProfile*").permitAll()
                .antMatchers("/api/v*/userManagement/updateAnyUser/*").permitAll()
                .antMatchers("/api/v*/userManagement/getAllUsers").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .passwordParameter("password")
                .usernameParameter("username");
                  /*  .loginPage("/login")
                    .permitAll()
                    .defaultSuccessUrl("/main", true)
                    .passwordParameter("password")
                    .usernameParameter("username")
                .and()
                .rememberMe()
                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(28))
                    .key("tokensecurityseed")
                    .rememberMeParameter("remember-me")
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID", "remember-me")
                    .logoutSuccessUrl("/login");*/


    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(applicationUserService);
        return provider;
    }
}
