/*
 * Created on 26.12.2004
 *
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christian-fries.de.
 */

package net.finmath.plots.jfreechart;

import java.awt.Color;
import java.awt.Paint;

import org.jfree.chart.renderer.PaintScale;

public class HuePaintScale implements PaintScale {
	double lowerBound;
	double upperBound;

	public HuePaintScale(final double lowerBound, final double upperBound) {
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	@Override
	public double getLowerBound() {
		return lowerBound;
	}

	@Override
	public double getUpperBound() {
		return upperBound;
	}

	@Override
	public Paint getPaint(final double value) {
		if(value > upperBound || value < lowerBound || Double.isNaN(value)) {
			return Color.GRAY;
		}
		return Color.getHSBColor((float) ((value-lowerBound) / (upperBound-lowerBound) * 240.0/360.0), 1.0f, 1.0f);
	}
}
