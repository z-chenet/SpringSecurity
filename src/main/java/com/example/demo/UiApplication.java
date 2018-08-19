package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SpringBootApplication
@Controller
public class UiApplication {

	public static void main(String[] args) {
		SpringApplication.run(UiApplication.class, args);
	}

	@GetMapping("/resource")
  @ResponseBody
  public Map<String, Object> home(){
	  Map<String, Object> model = new HashMap<>();
	  model.put("id", UUID.randomUUID().toString());
	  model.put("content", "Hello World");
	  return model;
  }

  @RequestMapping("/user")
  public Principal user(Principal user){
	  return user;
  }

  @GetMapping(value = "/{path:[^\\.]*}")
  public String redirect() {
    return "forward:/";
  }

  @Configuration
  protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(WebSecurity web) throws Exception {
      web.ignoring().antMatchers("/*.bundle.*", "/favicon.ico");
    }

	  @Override
    protected void configure(HttpSecurity http) throws Exception {
      http
        .httpBasic()
        .and()
          .authorizeRequests()
          .antMatchers("/index.html", "/", "/home", "/login").permitAll()
          .anyRequest().authenticated()
        .and().csrf()
          .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }
  }

}
