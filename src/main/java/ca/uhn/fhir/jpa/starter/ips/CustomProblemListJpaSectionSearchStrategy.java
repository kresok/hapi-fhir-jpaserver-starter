package ca.uhn.fhir.jpa.starter.ips;

import ca.uhn.fhir.jpa.ips.api.IpsSectionContext;
import ca.uhn.fhir.jpa.ips.jpa.JpaSectionSearchStrategy;
import jakarta.annotation.Nonnull;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.hl7.fhir.r4.model.Condition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomProblemListJpaSectionSearchStrategy extends JpaSectionSearchStrategy<Condition> {

    private static final Logger ourLog = LoggerFactory.getLogger(CustomProblemListJpaSectionSearchStrategy.class);

	@SuppressWarnings("RedundantIfStatement")
	@Override
	public boolean shouldInclude(
			@Nonnull IpsSectionContext<Condition> theIpsSectionContext, @Nonnull Condition theCandidate) {
		if (theCandidate
						.getClinicalStatus()
						.hasCoding("http://terminology.hl7.org/CodeSystem/condition-clinical", "inactive")
				|| theCandidate
						.getClinicalStatus()
						.hasCoding("http://terminology.hl7.org/CodeSystem/condition-clinical", "resolved")
				|| theCandidate
						.getVerificationStatus()
						.hasCoding("http://terminology.hl7.org/CodeSystem/condition-ver-status", "entered-in-error")) {
			return false;
		}

        if (theCandidate.hasOnsetDateTimeType()) {
			Instant onsetDate = theCandidate.getOnsetDateTimeType().getValue().toInstant();
            Instant cutoff = Instant.now().minus(30, ChronoUnit.DAYS);

            ourLog.info("Has onset date: {}", onsetDate);
            if(cutoff.isAfter(onsetDate)) {
                ourLog.info("Excluding condition with old onset date: {}", theCandidate.getId());
                return false;
            } 
        }

		return true;
	}
}
