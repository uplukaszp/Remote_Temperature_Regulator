package pl.uplukaszp.config;

import java.sql.SQLException;

import org.h2.server.web.WebServlet;
import org.h2.tools.Server;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;

@Configuration
@Profile("development")
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class DevelopmentConfig extends WebSecurityConfigurerAdapter {
	private static final String username = "user";
	private static final String password = "password";

	@Bean
	public ProjectionFactory projectionFactory() {
		return new SpelAwareProxyProjectionFactory();
	}

	@Bean
	ServletRegistrationBean<WebServlet> h2servletRegistration() {
		ServletRegistrationBean<WebServlet> registrationBean = new ServletRegistrationBean<>(new WebServlet());
		registrationBean.addUrlMappings("/console/*");
		return registrationBean;
	}

	@Bean(initMethod = "start", destroyMethod = "stop")
	public Server h2Server() throws SQLException {
		return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.authorizeRequests().antMatchers("/console/**").permitAll();
		httpSecurity.cors().and().csrf().disable();
		httpSecurity.headers().frameOptions().disable();
		httpSecurity.authorizeRequests().anyRequest().authenticated().and().httpBasic();
		httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser(
				User.withDefaultPasswordEncoder().username(username).password(password).roles("USER").build());
	}
}
