package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class ColorMask implements PixelFilter {

    private static int threshold = 50;

    private int targetRed = 105;
    private int targetGreen = 27;
    private int targetBlue = 50; //these three vals can be changed

    public DImage processImage(DImage img) {
        short[][] red = img.getRedChannel();
        short[][] blue = img.getBlueChannel();
        short[][] green = img.getGreenChannel();
        for (int r=0; r<red.length; r++) {
            for (int c=0; c<red[r].length; c++) {
                int redDiff = targetRed-red[r][c];
                int greenDiff = targetGreen-green[r][c];
                int blueDiff = targetBlue-blue[r][c];
                int diff = (int) Math.sqrt(redDiff*redDiff + greenDiff*greenDiff + blueDiff*blueDiff);
                if (diff<threshold) {
                    red[r][c] = 255;
                    green[r][c] = 255;
                    blue[r][c] = 255;
                }
                else {
                    red[r][c] = 0;
                    green[r][c] = 0;
                    blue[r][c] = 0;
                }
            }
        }
        img.setColorChannels(red, green, blue);
        return img;
    }
}
