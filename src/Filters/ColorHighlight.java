package Filters;

import Interfaces.Interactive;
import Interfaces.PixelFilter;
import core.DImage;

public class ColorHighlight implements PixelFilter, Interactive {

    private static int threshold = 50;

    private int targetRed = 105;
    private int targetBlue = 50;
    private int targetGreen = 27; // can edited

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
                if (diff>threshold) {
                    red[r][c] = (short) ((red[r][c] + green[r][c] + blue[r][c])/3);
                    green[r][c] = (short) ((red[r][c] + green[r][c] + blue[r][c])/3);
                    blue[r][c] = (short) ((red[r][c] + green[r][c] + blue[r][c])/3);
                }
            }
        }
        img.setColorChannels(red, green, blue);
        return img;
    }


    @Override
    public void mouseClicked(int mouseX, int mouseY, DImage img) {
        short[][] red = img.getRedChannel();
        short[][] blue = img.getBlueChannel();
        short[][] green = img.getGreenChannel();
        targetRed = red[mouseY][mouseX];
        targetGreen = green[mouseY][mouseX];
        targetBlue = blue[mouseY][mouseX];
    }

    @Override
    public void keyPressed(char key) {
        if (key=='+') threshold+=5;
        if (key == '-') {
            if (threshold>=5) threshold-=5;
        }
    }
}
