package Filters;

import Interfaces.Interactive;
import Interfaces.PixelFilter;
import core.DImage;

public class ColorMask implements PixelFilter, Interactive {

    private static int threshold = 55;

    private int targetRed1 = 0;
    private int targetBlue1 = 0;
    private int targetGreen1 = 255; // green

    private int targetRed2 = 255;
    private int targetBlue2 = 0;
    private int targetGreen2 = 165; // orange

    private int targetRed3 = 0;
    private int targetBlue3 = 255;
    private int targetGreen3 = 10; // blue

    public DImage processImage(DImage img) {
        short[][] red = img.getRedChannel();
        short[][] blue = img.getBlueChannel();
        short[][] green = img.getGreenChannel();

        for (int r = 0; r <red.length; r++) {
            for (int c = 0; c<red[r].length; c++) {
                int redDiff = targetRed1-red[r][c];
                int greenDiff = targetGreen1-green[r][c];
                int blueDiff = targetBlue1-blue[r][c];
                int diff = (int) Math.sqrt(redDiff*redDiff + greenDiff*greenDiff + blueDiff*blueDiff);

                redDiff = targetRed2-red[r][c];
                greenDiff = targetGreen2-green[r][c];
                blueDiff = targetBlue2-blue[r][c];
                int diff2 = (int) Math.sqrt(redDiff*redDiff + greenDiff*greenDiff + blueDiff*blueDiff);

                redDiff = targetRed3-red[r][c];
                greenDiff = targetGreen3-green[r][c];
                blueDiff = targetBlue3-blue[r][c];
                int diff3 = (int) Math.sqrt(redDiff*redDiff + greenDiff*greenDiff + blueDiff*blueDiff);

                if (diff>threshold && diff2>threshold && diff3>threshold) {
                    red[r][c] = 0;
                    green[r][c] = 0;
                    blue[r][c] = 0;
                }
            }
        }
        img.setColorChannels(red, green, blue);
        return img;
    }

    public double rgbToH(double r, double g, double b) {
        r/=255.0;
        g/=255.0;
        b/=255.0;

        double cMax = Math.max(r, (Math.max(g, b)));
        double cMin = Math.min(r, (Math.min(g, b)));
        double diff = cMax-cMin;

        if (diff==0) return 0;
        if (cMax==r) return ((60 * ((g-b) / diff) + 360) % 360);
        if (cMax==g) return  ((60 * ((b-r) / diff) + 120) % 360);
        else return ((60 * ((r-g) / diff) + 240) % 360);
    }

    public double rgbToS(double r, double g, double b) {
        r/=255.0;
        g/=255.0;
        b/=255.0;

        double cMax = Math.max(r, (Math.max(g, b)));
        double cMin = Math.min(r, (Math.min(g, b)));
        double diff = cMax-cMin;

        if (cMax==0) return 0;
        else return ((diff/cMax)*100);
    }

    public double rgbToV(double r, double g, double b) {
        r/=255.0;
        g/=255.0;
        b/=255.0;

        double cMax = Math.max(r, (Math.max(g, b)));
        return cMax*100;
    }

    public void mouseClicked(int mouseX, int mouseY, DImage img) {
        short[][] red = img.getRedChannel();
        short[][] blue = img.getBlueChannel();
        short[][] green = img.getGreenChannel();
        targetRed2 = red[mouseY][mouseX];
        targetGreen2 = green[mouseY][mouseX];
        targetBlue2 = blue[mouseY][mouseX];
    }

    @Override
    public void keyPressed(char key) {
        if (key=='+') threshold+=5;
        if (key == '-') {
            if (threshold>=5) threshold-=5;
        }
    }

}
