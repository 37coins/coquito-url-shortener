package com._37coins.coquito;

import java.util.Random;

import javax.servlet.ServletContextEvent;

import org.restnucleus.filter.CorsFilter;
import org.restnucleus.filter.DigestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

public class ShortenerServletConfig extends GuiceServletContextListener {
	public static String digestToken;
	public static String basePath;
	public static Logger log = LoggerFactory.getLogger(ShortenerServletConfig.class);
	public static Injector injector;
	static {
		digestToken = System.getProperty("digestToken");
		basePath = System.getProperty("basePath");
	}
    
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		super.contextInitialized(servletContextEvent);
		log.info("ServletContextListener started");
	}
	
    @Override
    protected Injector getInjector(){
        injector = Guice.createInjector(new ServletModule(){
            @Override
            protected void configureServlets(){
                filter("/*").through(CorsFilter.class);
            	filter("/product*").through(DigestFilter.class);
        	}

            @Provides @Singleton
            public CorsFilter provideCorsFilter(){
                return new CorsFilter("*");
            }
            
            @Provides @Singleton
            public DigestFilter getDigestFilter(){
                return new DigestFilter(ShortenerServletConfig.digestToken);
            }

    	    @Provides @Singleton
    		public Random provideRandom(){
    			return new Random();
        	}

        	});
        return injector;
    }
	
    @Override
	public void contextDestroyed(ServletContextEvent sce) {
		super.contextDestroyed(sce);
		log.info("ServletContextListener destroyed");
	}

}
