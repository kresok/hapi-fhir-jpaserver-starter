package ca.uhn.fhir.jpa.starter.ips;

import ca.uhn.fhir.jpa.ips.api.Section;
import ca.uhn.fhir.jpa.ips.jpa.DefaultJpaIpsGenerationStrategy;
import ca.uhn.fhir.jpa.ips.jpa.JpaSectionSearchStrategyCollection;
import ca.uhn.fhir.jpa.ips.jpa.section.ProblemListJpaSectionSearchStrategy;
import ca.uhn.fhir.jpa.ips.strategy.ProblemNoInfoR4Generator;

import org.hl7.fhir.r4.model.Condition;


/**
 * Custom IPS generation strategy that allows overriding problem-list retrieval.
 *
 * Override {@link #retrieveProblemListForPatient(String)} to implement
 * custom retrieval logic (for example, a specialized search or denormalized store).
 */
public class CustomJpaIpsGenerationStrategy extends DefaultJpaIpsGenerationStrategy {

    public CustomJpaIpsGenerationStrategy() {
        super();
    }

    protected void addJpaSectionProblemList() {
		Section section = Section.newBuilder()
				.withTitle("Problem List")
				.withSectionSystem(SECTION_SYSTEM_LOINC)
				.withSectionCode(SECTION_CODE_PROBLEM_LIST)
				.withSectionDisplay("Problem list - Reported12345")
				.withResourceType(Condition.class)
				.withProfile(
						"https://hl7.org/fhir/uv/ips/StructureDefinition-Composition-uv-ips-definitions.html#Composition.section:sectionProblems")
				.withNoInfoGenerator(new ProblemNoInfoR4Generator())
				.build();

		JpaSectionSearchStrategyCollection searchStrategyCollection = JpaSectionSearchStrategyCollection.newBuilder()
				.addStrategy(Condition.class, new CustomProblemListJpaSectionSearchStrategy())
				.build();

		addJpaSection(section, searchStrategyCollection);
	}
}
