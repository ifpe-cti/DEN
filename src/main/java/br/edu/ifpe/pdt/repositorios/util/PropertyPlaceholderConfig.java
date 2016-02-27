package br.edu.ifpe.pdt.repositorios.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
 
/**
 * Carrega propriedades do arquivo db.properties
 */
@Configuration
@PropertySource("classpath:db.properties")
public class PropertyPlaceholderConfig {
 
  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }
}
