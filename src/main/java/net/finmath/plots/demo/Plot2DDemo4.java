/*
 * (c) Copyright Christian P. Fries, Germany. All rights reserved. Contact: email@christianfries.com.
 *
 * Created on 21 May 2018
 */

package net.finmath.plots.demo;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.function.DoubleUnaryOperator;

import net.finmath.plots.Named;
import net.finmath.plots.Plot;
import net.finmath.plots.Plot2D;

/**
 * Plots the value of a finite difference approximation of the derivative of (exp(x)-x) at 0.
 *
 * @author Christian Fries
 */
public class Plot2DDemo4 {

	/**
	 * Run the demo.
	 * Plots the value of an option under the Black-Scholes model as a function of strike and time-to-maturity.
	 *
	 * @param args Not used.
	 * @throws Exception Exception from the graphics backend.
	 */
	public static void main(final String[] args) throws Exception {

		final double x			= 0.0;

		final DoubleUnaryOperator functionDerivativeCentral = (shiftScale) -> {
			final double shiftSize = Math.pow(10, -shiftScale);	// shift = 10^{-scale}

			final double valueUp		= Math.exp(x + shiftSize);
			final double valueDown	= Math.exp(x - shiftSize);

			final double derivativeFiniteDifference = (valueUp-valueDown) / 2 / shiftSize - 1;

			return derivativeFiniteDifference;
		};

		final DoubleUnaryOperator functionDerivativeForward = (shiftScale) -> {
			final double shiftSize = Math.pow(10, -shiftScale);	// shift = 10^{-scale}

			final double valueUp		= Math.exp(x + shiftSize);
			final double value		= Math.exp(x);

			final double derivativeFiniteDifference = (valueUp-value) / shiftSize - 1;

			return derivativeFiniteDifference;
		};

		final DoubleUnaryOperator functionDerivativeAnalytic = (shiftScale) -> {

			return Math.exp(x) - 1;
		};

		final Plot plotDerivativeForward = new Plot2D(5, 10.0, 100, Arrays.asList(
				new Named<DoubleUnaryOperator>("Finite Difference Approximation", functionDerivativeForward),
				new Named<DoubleUnaryOperator>("Analytic", functionDerivativeAnalytic)));
		plotDerivativeForward.setTitle("(One Sided Finite Difference) Derivative of exp(x) at x = " + x).setXAxisLabel("scale (h = 10^{-scale})").setYAxisLabel("value").setIsLegendVisible(true);
		((Plot2D)plotDerivativeForward).setYAxisNumberFormat(new DecimalFormat("0.0E00"));
		plotDerivativeForward.show();

		final Plot plotDerivativeCentral1 = new Plot2D(4.5, 6.5, 100, Arrays.asList(
				new Named<DoubleUnaryOperator>("Finite Difference Approximation", functionDerivativeCentral),
				new Named<DoubleUnaryOperator>("Analytic", functionDerivativeAnalytic)));
		plotDerivativeCentral1.setTitle("(Central Finite Difference) Derivative of exp(x) at x = " + x).setXAxisLabel("scale (h = 10^{-scale})").setYAxisLabel("value").setIsLegendVisible(true);
		((Plot2D)plotDerivativeCentral1).setYAxisNumberFormat(new DecimalFormat("0.0E00"));
		((Plot2D)plotDerivativeCentral1).setYRange(-5E-10, 5E-10);
		plotDerivativeCentral1.show();

		final Plot plotDerivativeCentral2 = new Plot2D(1.0, 20.0, 100, Arrays.asList(
				new Named<DoubleUnaryOperator>("Finite Difference Approximation", functionDerivativeCentral),
				new Named<DoubleUnaryOperator>("Analytic", functionDerivativeAnalytic)));
		plotDerivativeCentral2.setTitle("(Central Finite Difference) Derivative of exp(x) at x = " + x).setXAxisLabel("scale (h = 10^{-scale})").setYAxisLabel("value").setIsLegendVisible(true);
		((Plot2D)plotDerivativeCentral1).setYAxisNumberFormat(new DecimalFormat("0.0E00"));
		plotDerivativeCentral2.show();

	}
}
