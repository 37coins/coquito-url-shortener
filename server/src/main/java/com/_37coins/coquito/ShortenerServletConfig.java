package com._37coins.coquito;

import java.util.Random;

import javax.servlet.ServletContextEvent;

import org.restnucleus.filter.CorsFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

public class ShortenerServletConfig extends GuiceServletContextListener {
	public static String basePath;

    public static String awsAccessKeyId;
    public static String awsSecretKey;
    public static String awsSecurityGroup;
    public static String awsRegion;

	public static Logger log = LoggerFactory.getLogger(ShortenerServletConfig.class);
	public static Injector injector;
	static {
		basePath = System.getProperty("basePath");
		awsAccessKeyId = System.getProperty("awsAccessKeyId");
		awsSecretKey = System.getProperty("awsSecretKey");
		awsSecurityGroup = System.getProperty("awsSecurityGroup");
		awsRegion = System.getProperty("awsRegion");
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
        	}

            @Provides @Singleton
            public CorsFilter provideCorsFilter(){
                return new CorsFilter("*");
            }
            
            @Provides @Singleton
            public HazelcastInstance getHazelcast() {
                Config config = new Config();
                if (awsAccessKeyId!=null) {
                    config.setProperty("hazelcast.icmp.enabled", "true");
                    JoinConfig joinConfig = config.getNetworkConfig().getJoin();
                    joinConfig.getTcpIpConfig().setEnabled(false).setConnectionTimeoutSeconds(20);
                    joinConfig.getMulticastConfig().setEnabled(false);
                    joinConfig.getAwsConfig().setEnabled(true)
                    .setAccessKey(awsAccessKeyId)
                    .setSecretKey(awsSecretKey)
                    .setRegion(awsRegion)
                    .setSecurityGroupName(awsSecurityGroup);                        
                }       
                config.getGroupConfig().setName("shortener");
                HazelcastInstance h = Hazelcast.newHazelcastInstance(config);
                // Configure max queue size to 1000 elements
                h.getConfig().getQueueConfig("default").setMaxSize(1000);
                return h;
            }
            
            @Provides @Singleton
            public IMap<String,String> provideSentNotificationsMap(HazelcastInstance h){
                h.getConfig().getMapConfig("tx-tainting").setTimeToLiveSeconds(3600);
                IMap<String,String> sentNotificationsMap = h.getMap("tx-tainting");
                return sentNotificationsMap;
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
