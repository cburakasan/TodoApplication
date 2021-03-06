package com.asan.todosecurity.Security.Config;

import com.asan.todosecurity.Security.Filter.AuthTokenFilter;
import com.asan.todosecurity.SessionUtil;
import com.asan.todosecurity.User.Service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.Filter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    AuthTokenFilter authenticationJwtTokenFilter;

    @Autowired
    SessionUtil sessionUtil;

    public WebSecurityConfig() throws Exception {
    }


    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * /admin/signup ve /user/signin pathleri üzerinden gelen isteklerde herhangi bir
     * login girişi istenmemektedir.
     * Bu pathler dışında gelen her servis isteği için önce login olunmalıdır.
     * Login sonrası kullanıcı bilgileri session üzerinde tutulduğu için,
     * bir kere login olduktan sonra yetkisi dahilindeki diğer servislere istek yapılabilir.
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint((AuthenticationEntryPoint) unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers("/admin/signup").permitAll()
                .antMatchers("/user/signin").permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore((Filter) authenticationJwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
