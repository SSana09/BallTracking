package Filters;

import Interfaces.PixelFilter;
import core.DImage;

import java.awt.*;
import java.util.ArrayList;

public class FilterPipeline implements PixelFilter {

    ArrayList<PixelFilter> filters = new ArrayList<>();

    public FilterPipeline() {
        PixelFilter blur = new Convolution();
        PixelFilter highlight = new ColorHighlight();

        filters.add(blur);
        filters.add(highlight);
    }

    @Override
    public DImage processImage(DImage img) {
        for(PixelFilter filter: filters) {
            img = filter.processImage(img);
        }
        return img;
    }
}
