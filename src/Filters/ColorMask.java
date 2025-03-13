package Filters;

import Interfaces.Interactive;
import Interfaces.PixelFilter;
import core.DImage;

public class ColorMask implements PixelFilter, Interactive {

    //mini squares, dlt if surrounded by black, or on 3 sides
    //100x100 squares (testable)

    private double threshold = 0.35;

    private short[] green1 = {107, 163, 88};
    private short[] green2 = {73, 94, 53};
    private short[] green3 = {54, 91, 39};
    private short[][] setGreens = {green1, green2, green3};

    private short[] red1 = {250, 108, 88};
    private short[] red2 = {188, 77, 57};
    private short[] red3 = {195, 80, 61};
    private short[][] setReds = {red1, red2, red3};

    private short[] blue1 = {54, 68, 133};
    private short[] blue2 = {25, 26, 83};
    private short[] blue3 = {56, 83, 157};
    private short[][] setBlues = {blue1, blue2, blue3};

    public DImage processImage(DImage img) {
        short[][] red = img.getRedChannel();
        short[][] blue = img.getBlueChannel();
        short[][] green = img.getGreenChannel();
        //rgb
        for (int r = 0; r<red.length; r++) {
            for (int c = 0; c<red[r].length; c++) {
                double bg, br;
                if (green[r][c]<=0) {
                    green[r][c] = 1;
                }
                if (red[r][c]<=0) {
                    red[r][c] = 1;
                }

                bg = blue[r][c]/green[r][c];
                br = blue[r][c]/red[r][c];

                for (int i=0; i<3; i++) {
                    double r1 = (double) setReds[i][2]/setReds[i][1];
                    double r2 = (double) setReds[i][2]/setReds[i][0];

                    double g1 = (double) setGreens[i][2]/ setGreens[i][1];
                    double g2 = (double) setGreens[i][2]/ setGreens[i][0];

                    double b1 = (double) setBlues[i][2]/ setBlues[i][1];
                    double b2 = (double) setBlues[i][2]/ setBlues[i][0];

                    if (r1<=bg+threshold && r1>=bg-threshold
                        && r2<=br+threshold && r2>=br-threshold) {
                        red[r][c] = 255;
                        green[r][c] = 0;
                        blue[r][c] = 0;
                    }

                    else if (g1<=bg+threshold && g1>=bg-threshold
                            && g2<=br+threshold && g2>=br-threshold) {
                        red[r][c] = 0;
                        green[r][c] = 255;
                        blue[r][c] = 0;
                    }

                    else if (b1<=bg+threshold && b1>=bg-threshold
                            && b2<=br+threshold && b2>=br-threshold) {
                        red[r][c] = 0;
                        green[r][c] = 0;
                        blue[r][c] = 255;
                    }

                    else {
                        red[r][c] = 0;
                        green[r][c] = 0;
                        blue[r][c] = 0;
                    }
                }
            }
        }
        img.setColorChannels(red, green, blue);
        return img;
    }

//    public DImage refreshImage(DImage img) {
//        short[][] grid = img.getBWPixelGrid();
//
//        for (int r=0; r<grid.length; r++) {
//            for (int c=0; c<grid[r].length; c++) {
//
//            }
//        }
//    }

    public void mouseClicked(int mouseX, int mouseY, DImage img) {
    }

    @Override
    public void keyPressed(char key) {
        if (key == '+') {
            threshold+=0.1;
        }

        if (key == '-') {
            if (threshold>0.1) {
                threshold -= 0.1;
            }
        }
    }

}
