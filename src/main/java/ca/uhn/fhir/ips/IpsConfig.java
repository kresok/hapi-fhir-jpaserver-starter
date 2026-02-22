package ca.uhn.fhir.ips;


import ca.uhn.fhir.jpa.ips.provider.IpsOperationProvider;
import ca.uhn.fhir.jpa.ips.generator.IIpsGeneratorSvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IpsConfig {

  @Bean
  public IpsOperationProvider ipsOperationProvider(IIpsGeneratorSvc ipsGeneratorSvc) {
    return new IpsOperationProvider(ipsGeneratorSvc);
  }
}