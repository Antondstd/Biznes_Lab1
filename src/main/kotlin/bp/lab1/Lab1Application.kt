package bp.lab1

import bp.lab1.models.MyUserDetails
import bp.lab1.reposetory.UserRepository
import com.fasterxml.classmate.TypeResolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.PathSelectors.regex

import springfox.documentation.builders.RequestHandlerSelectors

import springfox.documentation.spi.DocumentationType

import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.service.ApiInfo
import springfox.documentation.swagger2.annotations.EnableSwagger2
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import springfox.documentation.service.Contact
import java.util.*


@SpringBootApplication
@EnableScheduling
open class Lab1Application

    fun main(args: Array<String>) {
        runApplication<Lab1Application>(*args)
    }





@Configuration
@EnableSwagger2
class SpringFoxConfig {
    private val API_VERSION = "0.0.1"

    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("bp.lab1.controllers"))
            .paths(PathSelectors.any())
            .build()
    }


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
            .defaultSuccessUrl("/profile")
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


