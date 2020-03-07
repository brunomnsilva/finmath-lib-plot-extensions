/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christianfries.com.
 *
 * Created on 21 May 2018
 */

package net.finmath.plots;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.function.DoubleFunction;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import net.finmath.plots.jfreechart.JFreeChartUtilities;
import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;

/**
 * Small convenient wrapper for JFreeChart line plot of a stochastic process.
 *
 * @author Christian Fries
 */
public class PlotProcess2D implements Plot {

	private final TimeDiscretization timeDiscretization;
	private final Named<DoubleFunction<RandomVariable>> process;
	private final int maxNumberOfPaths;

	private String title = "";
	private String xAxisLabel = "x";
	private String yAxisLabel = "y";
	private NumberFormat xAxisNumberFormat;
	private NumberFormat yAxisNumberFormat;
	private Boolean isLegendVisible = false;

	private transient JFreeChart chart;

	/**
	 * Plot the first (maxNumberOfPaths) paths of a time discrete stochastic process.
	 *
	 * @param timeDiscretization The time discretization to be used for the x-axis.
	 * @param process The stochastic process to be plotted against the y-axsis (the first n paths are plotted).
	 * @param maxNumberOfPaths Maximum number of path (n) to be plotted.
	 */
	public PlotProcess2D(final TimeDiscretization timeDiscretization, final Named<DoubleFunction<RandomVariable>> process, final int maxNumberOfPaths) {
		super();
		this.timeDiscretization = timeDiscretization;
		this.process = process;
		this.maxNumberOfPaths = maxNumberOfPaths;
	}

	/**
	 * Plot the first (maxNumberOfPaths) paths of a time discrete stochastic process.
	 *
	 * @param timeDiscretization The time discretization to be used for the x-axis.
	 * @param process The stochastic process to be plotted against the y-axsis (the first n paths are plotted).
	 * @param maxNumberOfPaths Maximum number of path (n) to be plotted.
	 */
	public PlotProcess2D(final TimeDiscretization timeDiscretization, final DoubleFunction<RandomVariable> process, final int maxNumberOfPaths) {
		super();
		this.timeDiscretization = timeDiscretization;
		this.process = new Named<DoubleFunction<RandomVariable>>("", process);
		this.maxNumberOfPaths = maxNumberOfPaths;
	}

	/**
	 * Plot the first (maxNumberOfPaths) paths of a time discrete stochastic process.
	 *
	 * @param timeDiscretization The time discretization to be used for the x-axis.
	 * @param process The stochastic process to be plotted against the y-axsis (the first n paths are plotted).
	 * @param maxNumberOfPaths Maximum number of path (n) to be plotted.
	 */
	public PlotProcess2D(final TimeDiscretization timeDiscretization, final DoubleToRandomVariableFunction process, final int maxNumberOfPaths) {
		super();
		this.timeDiscretization = timeDiscretization;
		this.process = new Named<DoubleFunction<RandomVariable>>("", t -> { try{ return process.apply(t);} catch(Exception e) { return null; }});
		this.maxNumberOfPaths = maxNumberOfPaths;
	}

	/**
	 * Plot the first 100 paths of a time discrete stochastic process.
	 *
	 * @param timeDiscretization The time discretization to be used for the x-axis.
	 * @param process The stochastic process to be plotted against the y-axsis.
	 */
	public PlotProcess2D(final TimeDiscretization timeDiscretization, final Named<DoubleFunction<RandomVariable>> process) {
		this(timeDiscretization, process, 100);
	}

	private void init() {
		final ArrayList<XYSeries> seriesList = new ArrayList<XYSeries>();
		for(final double time : timeDiscretization) {
			final RandomVariable randomVariable = process.get().apply(time);
			for(int pathIndex=0; pathIndex<Math.min(randomVariable.size(),maxNumberOfPaths); pathIndex++) {
				XYSeries series = pathIndex < seriesList.size() ? seriesList.get(pathIndex) : null;
				if(series == null) {
					series = new XYSeries(pathIndex);
					seriesList.add(pathIndex, series);
				}
				series.add(time, randomVariable.get(pathIndex));
			}
		}
		final XYSeriesCollection data = new XYSeriesCollection();
		for(final XYSeries series : seriesList) {
			data.addSeries(series);
		}

		final StandardXYItemRenderer renderer	= new StandardXYItemRenderer(StandardXYItemRenderer.LINES);
		renderer.setSeriesPaint(0, new java.awt.Color(255, 0,  0));
		renderer.setSeriesPaint(1, new java.awt.Color(0, 255,   0));
		renderer.setSeriesPaint(2, new java.awt.Color(0,   0, 255));

		chart = JFreeChartUtilities.getXYPlotChart(title, xAxisLabel, "#.#" /* xAxisNumberFormat */, yAxisLabel, "#.#" /* yAxisNumberFormat */, data, renderer, isLegendVisible);
	}

	@Override
	public void show() {
		init();
		final JPanel chartPanel = new ChartPanel(chart,
				800, 400,   // size
				128, 128,   // minimum size
				2024, 2024, // maximum size
				false, true, true, false, true, false);    // useBuffer, properties, save, print, zoom, tooltips

		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				final JFrame frame = new JFrame();
				frame.add(chartPanel);
				frame.setVisible(true);
				frame.pack();
			}
		});
	}

	@Override
	public void saveAsJPG(final File file, final int width, final int height) throws IOException {
		JFreeChartUtilities.saveChartAsJPG(file, chart, width, height);
	}

	@Override
	public void saveAsPDF(final File file, final int width, final int height) throws IOException {
		JFreeChartUtilities.saveChartAsPDF(file, chart, width, height);
	}

	@Override
	public void saveAsSVG(final File file, final int width, final int height) throws IOException {
		JFreeChartUtilities.saveChartAsSVG(file, chart, width, height);
	}

	@Override
	public PlotProcess2D setTitle(final String title) {
		this.title = title;
		return this;
	}

	@Override
	public PlotProcess2D setXAxisLabel(final String xAxisLabel) {
		this.xAxisLabel = xAxisLabel;
		return this;
	}

	@Override
	public PlotProcess2D setYAxisLabel(final String yAxisLabel) {
		this.yAxisLabel = yAxisLabel;
		return this;
	}

	@Override
	public Plot setZAxisLabel(final String zAxisLabel) {
		throw new UnsupportedOperationException("The 2D plot does not suport a z-axis. Try 3D plot instead.");
	}

	/**
	 * @param isLegendVisible the isLegendVisible to set
	 */
	@Override
	public Plot setIsLegendVisible(final Boolean isLegendVisible) {
		this.isLegendVisible = isLegendVisible;
		return this;
	}

	@Override
	public String toString() {
		return "PlotProcess2D [timeDiscretization=" + timeDiscretization + ", process=" + process
				+ ", maxNumberOfPaths=" + maxNumberOfPaths + ", title=" + title + ", xAxisLabel=" + xAxisLabel
				+ ", yAxisLabel=" + yAxisLabel + ", xAxisNumberFormat=" + xAxisNumberFormat + ", yAxisNumberFormat="
				+ yAxisNumberFormat + ", isLegendVisible=" + isLegendVisible + "]";
	}
}
