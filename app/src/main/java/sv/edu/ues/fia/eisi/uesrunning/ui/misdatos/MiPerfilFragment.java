package sv.edu.ues.fia.eisi.uesrunning.ui.misdatos;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import sv.edu.ues.fia.eisi.uesrunning.R;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.*;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.*;
import android.graphics.*;

public class MiPerfilFragment extends Fragment {

    private XYPlot plot;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_mi_perfil, container, false);

        // initialize our XYPlot reference:
        plot = (XYPlot) view.findViewById(R.id.plot);

        // create a couple arrays of y-values to plot:
        final Number[] domainLabels = {1, 2, 3, 6, 7, 8, 9};
        Number[] series1Numbers = {1, 4, 2, 8, 4, 16, 8};

        // turn the above arrays into XYSeries':
        // (Y_VALS_ONLY means use the element index as the x value)
        XYSeries series1 = new SimpleXYSeries(
                Arrays.asList(series1Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Pasos diarios");

        // create formatters to use for drawing a series using LineAndPointRenderer
        // and configure them from xml:
        LineAndPointFormatter series1Format =
                new LineAndPointFormatter(getContext(), R.xml.line_point_formatter_with_labels);
        // just for fun, add some smoothing to the lines:
        // see: http://androidplot.com/smooth-curves-and-androidplot/
        series1Format.setInterpolationParams(
                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));

        // add a new series to the xyplot:
        plot.addSeries(series1, series1Format);

        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                int i = Math.round(((Number) obj).floatValue());
                return toAppendTo.append(domainLabels[i]);
            }
            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }
        });

        // Set the bounds of the plot based on the domainLabels length and calculate the maximum value manually
        plot.setDomainBoundaries(0, domainLabels.length - 1, BoundaryMode.FIXED);
        //plot.getGraph().getGridBackgroundPaint().setColor(Color.TRANSPARENT);
        double maxValue = Double.MIN_VALUE;
        for (Number number : series1Numbers) {
            double value = number.doubleValue();
            if (value > maxValue) {
                maxValue = value;
            }
        }
        plot.setRangeBoundaries(0, maxValue, BoundaryMode.FIXED);


        return view;
    }
}