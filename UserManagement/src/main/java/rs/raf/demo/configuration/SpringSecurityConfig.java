package rs.raf.demo.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import rs.raf.demo.filters.JwtFilter;
import rs.raf.demo.handlers.MyAccessDeniedHandler;
import rs.raf.demo.model.UserAuthority;
import rs.raf.demo.model.VacuumAuthority;
import rs.raf.demo.services.UserService;

@EnableWebSecurity
@EnableScheduling
@EnableAsync
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final JwtFilter jwtFilter;

    @Autowired
    public SpringSecurityConfig(UserService userService, JwtFilter jwtFilter) {
        this.userService = userService;
        this.jwtFilter = jwtFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userService);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers(HttpMethod.GET, "/users/all").hasAuthority(UserAuthority.CAN_READ_USERS.name())
                .antMatchers(HttpMethod.POST, "/users/create" ).hasAuthority(UserAuthority.CAN_CREATE_USERS.name())
                .antMatchers(HttpMethod.DELETE, "/users/delete/**").hasAuthority(UserAuthority.CAN_DELETE_USERS.name())
                .antMatchers(HttpMethod.PUT, "/users/update/**").hasAuthority(UserAuthority.CAN_UPDATE_USERS.name())
                .antMatchers(HttpMethod.GET, "/vacuum/search").hasAuthority(VacuumAuthority.CAN_SEARCH_VACUUM.name())
                .antMatchers(HttpMethod.POST, "/vacuum/start/**").hasAuthority(VacuumAuthority.CAN_START_VACUUM.name())
                .antMatchers(HttpMethod.POST, "/vacuum/stop/**").hasAuthority(VacuumAuthority.CAN_STOP_VACUUM.name())
                .antMatchers(HttpMethod.POST, "/vacuum/discharge/**").hasAuthority(VacuumAuthority.CAN_DISCHARGE_VACUUM.name())
                .antMatchers(HttpMethod.POST, "/vacuum/add").hasAuthority(VacuumAuthority.CAN_ADD_VACUUM.name())
                .antMatchers(HttpMethod.PUT, "/vacuum/remove/**").hasAuthority(VacuumAuthority.CAN_REMOVE_VACUUM.name())
                .antMatchers(HttpMethod.POST, "/vacuum/schedule").hasAnyAuthority(VacuumAuthority.CAN_START_VACUUM.name(), VacuumAuthority.CAN_STOP_VACUUM.name(), VacuumAuthority.CAN_DISCHARGE_VACUUM.name())
                .anyRequest().authenticated()
                .and().exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.addFilterBefore(this.jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
//        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new MyAccessDeniedHandler();
    }

}
