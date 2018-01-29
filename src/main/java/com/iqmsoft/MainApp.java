package com.iqmsoft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.support.SpringBootServletInitializer;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.faces.application.ProjectStage;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;

import org.apache.catalina.Context;
import org.primefaces.util.Constants;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;

import com.sun.faces.config.FacesInitializer;

@SpringBootApplication
public class MainApp extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MainApp.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(MainApp.class, args);
	}

	@Bean
	public static CustomScopeConfigurer customScopeConfigurer() {
		CustomScopeConfigurer configurer = new CustomScopeConfigurer();
		configurer.setScopes(Collections.<String, Object>singletonMap(FacesViewScope.NAME, new FacesViewScope()));
		return configurer;
	}

	@Bean
	public ServletContextInitializer servletContextCustomizer() {
		return new ServletContextInitializer() {
			@Override
			public void onStartup(ServletContext sc) throws ServletException {
//				 sc.setInitParameter(Constants.ContextParams.THEME, "bootstrap");
				sc.setInitParameter(Constants.ContextParams.AUTO_UPDATE, "true");
				sc.setInitParameter(ProjectStage.PROJECT_STAGE_PARAM_NAME, ProjectStage.Development.name());
			}
		};
	}

	
	@Bean
	@ConditionalOnMissingBean()
	public EmbeddedServletContainerFactory embeddedServletContainerFactory() {
		TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();

		tomcat.addContextCustomizers(new TomcatContextCustomizer() {
			@Override
			public void customize(Context context) {
				// register FacesInitializer
				context.addServletContainerInitializer(new FacesInitializer(),
						getServletContainerInitializerHandlesTypes(FacesInitializer.class));

				// add configuration from web.xml
				context.addWelcomeFile("index.jsf");

				// register additional mime-types that Spring Boot doesn't register
				context.addMimeMapping("eot", "application/vnd.ms-fontobject");
				context.addMimeMapping("ttf", "application/x-font-ttf");
				context.addMimeMapping("woff", "application/x-font-woff");
			}
		});

		return tomcat;
	}

	@SuppressWarnings("rawtypes")
	private Set<Class<?>> getServletContainerInitializerHandlesTypes(
			Class<? extends ServletContainerInitializer> sciClass) {
		HandlesTypes annotation = sciClass.getAnnotation(HandlesTypes.class);
		if (annotation == null) {
			return Collections.emptySet();
		}

		Class[] classesArray = annotation.value();
		Set<Class<?>> classesSet = new HashSet<Class<?>>(classesArray.length);
		for (Class clazz : classesArray) {
			classesSet.add(clazz);
		}

		return classesSet;
	}

}
