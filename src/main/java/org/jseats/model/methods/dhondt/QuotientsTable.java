package org.jseats.model.methods.dhondt;


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jseats.model.Candidate;
import org.jseats.model.InmutableTally;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class QuotientsTable {

	private static final int MIN_NUMBER_OF_SEATS_TO_ALLOCATE = 1;

	private static final int MIN_NUMBER_OF_CANDIDATES = 1;

	private final int numberOfSeats;
	private final List<Candidate> candidates;

	TreeMap<Quotient, List<Candidate>> quotientsTable = new TreeMap<>();

	private QuotientsTable(int numberOfSeats, InmutableTally tally) {
		this.numberOfSeats = numberOfSeats;
		this.candidates = new ArrayList<>();

		for (int i = 0; i < tally.getNumberOfCandidates(); i++){
			candidates.add(tally.getCandidateAt(i));
		}
	}

	public static QuotientsTable from(int numberOfSeats, InmutableTally tally) {
		if (numberOfSeats < MIN_NUMBER_OF_SEATS_TO_ALLOCATE) {
			throw new IllegalArgumentException("numberOfSeats can not be less than " + MIN_NUMBER_OF_SEATS_TO_ALLOCATE);
		}

		if (tally.getNumberOfCandidates() < MIN_NUMBER_OF_CANDIDATES) {
			throw new IllegalArgumentException("numberOfCandidates can not be less than " + MIN_NUMBER_OF_CANDIDATES);
		}

		return new QuotientsTable(numberOfSeats, tally);
	}

	public void calculate() {
		for (double divisor = this.numberOfSeats; divisor > 0; divisor--) {
			for (Candidate candidate: this.candidates) {
				addNewQuotient(candidate, divisor);
			}
		}
	}

	public Map.Entry<Quotient, List<Candidate>> getMaxQuotientEntry() {
		return this.quotientsTable.lastEntry();
	}

	public Map.Entry<Quotient, List<Candidate>> removeMaxQuotientEntry() {
		return this.quotientsTable.pollLastEntry();
	}

	public boolean removeCandidateFromMaxQuotient(Candidate candidate) {
		Map.Entry<Quotient, List<Candidate>> maxQuotientEntry = this.quotientsTable.firstEntry();
		return maxQuotientEntry.getValue().remove(candidate);
	}

	private void addNewQuotient(Candidate candidate, double divisor) {
		Quotient quotient = Quotient.from(candidate.getVotes(), divisor);
		boolean added;

		if (quotientsTable.containsKey(quotient)) {
			added = quotientsTable.get(quotient).add(candidate);

		} else {
			List<Candidate> candidateSet = new ArrayList<>();
			candidateSet.add(candidate);
			quotientsTable.put(quotient, candidateSet);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if ((obj == null) || !(obj instanceof QuotientsTable)) {
			return false;
		}

		QuotientsTable other = (QuotientsTable) obj;

		EqualsBuilder eBuilder = new EqualsBuilder();

		eBuilder = eBuilder.append(this.numberOfSeats, other.numberOfSeats);
		eBuilder = eBuilder.append(this.candidates, other.candidates);
		eBuilder = eBuilder.append(this.quotientsTable, other.quotientsTable);

		return eBuilder.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hcBuilder = new HashCodeBuilder();

		hcBuilder.append(this.numberOfSeats);
		hcBuilder.append(this.candidates);
		hcBuilder.append(this.quotientsTable);

		return hcBuilder.toHashCode();
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		builder.append("numberOfSeats", this.numberOfSeats);
		builder.append("candidates", this.candidates);
		builder.append("quotientsTable", this.quotientsTable);
		return builder.build();
	}
}
