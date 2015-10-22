package org.jseats.model.methods.dhondt;


import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * Represents the quotient of an integer dividend and a double divisor.
 * The value of the quotient is stored as BigDecimal with a precision of two
 * decimals and a rounding method 'ROUND_HALF_UP'.
 */
public class Quotient implements Comparable<Quotient>{

	private static final int MIN_DIVIDEND = 0;

	private static final int MIN_DIVISOR = 1;

	private static final int NUMBER_OF_DECIMAL_DIGITS = 2;

	private static final int ROUNDING_METHOD = BigDecimal.ROUND_HALF_UP;

	private final BigDecimal value;

	/**
	 * This method is only used for Testing Purposes.
	 */
	public Quotient(BigDecimal value) {
		this.value = value;
	}

	public BigDecimal getValue() {
		return value;
	}

	public static Quotient from(int rawDividend, double rawDivisor) {
		if (rawDividend < MIN_DIVIDEND) {
			throw new IllegalArgumentException("rawDividend can not be less than " + MIN_DIVIDEND);
		}

		if (rawDivisor < MIN_DIVISOR) {
			throw new IllegalArgumentException("rawDivisor can not be less than " + MIN_DIVISOR);
		}

		BigDecimal dividend = new BigDecimal(Integer.toString(rawDividend));
		BigDecimal divisor = new BigDecimal(Double.toString(rawDivisor));
		BigDecimal quotient = dividend.divide(divisor, NUMBER_OF_DECIMAL_DIGITS, ROUNDING_METHOD);
		return new Quotient(quotient);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if ((obj == null) || !(obj instanceof Quotient)) {
			return false;
		}

		Quotient other = (Quotient) obj;

		return this.value.compareTo(other.getValue()) == 0;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hcBuilder = new HashCodeBuilder();
		hcBuilder.append(this.value.doubleValue());
		return hcBuilder.toHashCode();
	}

	@Override
	public int compareTo(Quotient other) {
		return this.value.compareTo(other.getValue());
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		builder.append("value", this.value);
		return builder.build();
	}
}
