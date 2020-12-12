package ru.artak.currencyConverter.config;


import net.sf.ehcache.config.CacheConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CacheConfig extends CachingConfigurerSupport {

    @Bean
    public net.sf.ehcache.CacheManager ehCacheManager() {
        CacheConfiguration cacheConfigurationForExchange = new CacheConfiguration();
        cacheConfigurationForExchange.setName("exchangeRateCache");
        cacheConfigurationForExchange.setMemoryStoreEvictionPolicy("LRU");
        cacheConfigurationForExchange.setMaxEntriesLocalHeap(400);
        cacheConfigurationForExchange.setTimeToLiveSeconds(3600);
        cacheConfigurationForExchange.setEternal(false);

        net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration();
        config.addCache(cacheConfigurationForExchange);

        return net.sf.ehcache.CacheManager.newInstance(config);
    }

    @Bean
    @Override
    public CacheManager cacheManager() {
        return new EhCacheCacheManager(ehCacheManager());
    }
}
