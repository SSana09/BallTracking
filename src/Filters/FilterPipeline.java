package Filters;

import Interfaces.PixelFilter;
import core.DImage;

import java.util.ArrayList;

public class FilterPipeline implements PixelFilter {

    ArrayList<PixelFilter> filters = new ArrayList<>();

    public FilterPipeline() {
        PixelFilter blur = new Convolution();
        PixelFilter mask = new ColorMask();
        filters.add(blur);
        filters.add(mask);
    }

    @Override
    public DImage processImage(DImage img) {
        for(PixelFilter filter: filters) {
            img = filter.processImage(img);
        }
        ColorMask mask = (ColorMask) filters.get(1);
//        img = mask.refineMask(img, 25);
        img = mask.refineMask(img, 10);
        img = mask.fillIn(img, 50);
        return img;
    }
}
