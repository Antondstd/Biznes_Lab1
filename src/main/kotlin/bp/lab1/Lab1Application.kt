package bp.lab1

import bp.lab1.models.MyUserDetails
import bp.lab1.reposetory.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder


@SpringBootApplication
//@EnableAutoConfiguration
//@ComponentScan
@EnableScheduling
open class Lab1Application

    fun main(args: Array<String>) {
        runApplication<Lab1Application>(*args)
    }



@EnableWebSecurity
open class Lab1SecurityConfiguration: WebSecurityConfigurerAdapter (){
    override fun configure(http: HttpSecurity) {

        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/action/*").hasAuthority("ROLE_REGISTERED")
            .antMatchers("/show/*").hasAuthority("ROLE_REGISTERED")
            .antMatchers("/**").permitAll()
            .and()
            .formLogin()
            .loginProcessingUrl("/try_login")
            .defaultSuccessUrl("/test")
            .failureUrl("/fail")
            .and()
            .logout()
            .logoutUrl("/logout")
            .deleteCookies("JSESSIONID")

    }
    @Bean
    open fun passwordEncoder(): PasswordEncoder {
        return NoOpPasswordEncoder.getInstance()
    }
}


@Service
class MyUserDetailsService:UserDetailsService{
    @Autowired
    lateinit var user3Repository:UserRepository

    @Override
    override fun loadUserByUsername(email: String): UserDetails {
        //val encoder = passwordEncoder()
        var user = user3Repository.findByEmail(email)
        //user.password = encoder.encode(user.password)
        return MyUserDetails(user)
    }
}

//@Configuration
//class MyAutoConfiguration {
//    @PostConstruct
//    fun printConfigurationMessage() {
//        LOGGER.info("Configuration for My Lib is complete...")
//    }
//
//    companion object {
//        private val LOGGER: Logger = LoggerFactory.getLogger(MyAutoConfiguration::class.java)
//    }
//}


