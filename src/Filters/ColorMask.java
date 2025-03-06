package Filters;

import Interfaces.Interactive;
import Interfaces.PixelFilter;
import core.DImage;

public class ColorMask implements PixelFilter, Interactive {

    private static int threshold = 50;

    private int targetRed1 = 0;
    private int targetBlue1 = 5;
    private int targetGreen1 = 255; // green

    private int targetRed2 = 255;
    private int targetBlue2 = 0;
    private int targetGreen2 = 165; // orange

    private int targetRed3 = 0;
    private int targetBlue3 = 255;
    private int targetGreen3 = 0; // blue

    public DImage processImage(DImage img) {
        short[][] red = img.getRedChannel();
        short[][] blue = img.getBlueChannel();
        short[][] green = img.getGreenChannel();

        for (int r=0; r<red.length; r++) {
            for (int c=0; c<red[r].length; c++) {
                int redDiff = targetRed1-red[r][c];
                int greenDiff = targetGreen1-green[r][c];
                int blueDiff = targetBlue1-blue[r][c];
                int diff = (int) Math.sqrt(redDiff*redDiff + greenDiff*greenDiff + blueDiff*blueDiff);
                if (diff>threshold) {
                    red[r][c] = 0;
                    green[r][c] = 0;
                    blue[r][c] = 0;
                }

                redDiff = targetRed2-red[r][c];
                greenDiff = targetGreen2-green[r][c];
                blueDiff = targetBlue2-blue[r][c];
                int diff2 = (int) Math.sqrt(redDiff*redDiff + greenDiff*greenDiff + blueDiff*blueDiff);
                if (diff2 >threshold) {
                    red[r][c] = 0;
                    green[r][c] = 0;
                    blue[r][c] = 0;
                }

                redDiff = targetRed3-red[r][c];
                greenDiff = targetGreen3-green[r][c];
                blueDiff = targetBlue3-blue[r][c];
                int diff3 = (int) Math.sqrt(redDiff*redDiff + greenDiff*greenDiff + blueDiff*blueDiff);
                if (diff3 >threshold) {
                    red[r][c] = 0;
                    green[r][c] = 0;
                    blue[r][c] = 0;
                }
            }
        }
        img.setColorChannels(red, green, blue);
        return img;
    }

    public void mouseClicked(int mouseX, int mouseY, DImage img) {
        short[][] red = img.getRedChannel();
        short[][] blue = img.getBlueChannel();
        short[][] green = img.getGreenChannel();
        targetRed1 = red[mouseY][mouseX];
        targetGreen1 = green[mouseY][mouseX];
        targetBlue1 = blue[mouseY][mouseX];
    }

    @Override
    public void keyPressed(char key) {
        if (key=='+') threshold+=5;
        if (key == '-') {
            if (threshold>=5) threshold-=5;
        }
    }

}
