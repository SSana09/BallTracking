package Filters;

import Interfaces.Interactive;
import Interfaces.PixelFilter;
import core.DImage;

import java.awt.*;

public class ColorMask implements PixelFilter, Interactive {

    //mini squares, dlt if surrounded by black, or on 3 sides
    //100x100 squares (testable)

    private float[] red1 = new float[3];
    private float[] red2 = new float[3];
    private float[] red3 = new float[3];
    private float[][] setReds = {red1, red2, red3};

    private float[] green1 = new float[3];
    private float[] green2 = new float[3];
    private float[] green3 = new float[3];
    private float[][] setGreens = {green1, green2, green3};

    private float[] blue1 = new float[3];
    private float[] blue2 = new float[3];
    private float[] blue3 = new float[3];
    private float[][] setBlues = {blue1, blue2, blue3};

    private float[] yellow1 = new float[3];
    private float[] yellow2 = new float[3];
    private float[] yellow3 = new float[3];
    private float[][] setYellows = {yellow1, yellow2, yellow3};

    private float hueThresh = (float) 0.07;
    private float satThresh = (float) 0.15;
    private float valThresh = (float) 0.15;
    private float[] thresholds = {hueThresh, satThresh, valThresh};

    public DImage processImage(DImage img) {
        short[][] red = img.getRedChannel();
        short[][] blue = img.getBlueChannel();
        short[][] green = img.getGreenChannel();
        //rgb
        red1 = Color.RGBtoHSB(250, 108, 88, red1);
        red2 = Color.RGBtoHSB(177, 29, 15, red2);
        red3 = Color.RGBtoHSB(200, 83, 72, red3);

        green1 = Color.RGBtoHSB(107, 163, 88, green1);
        green2 = Color.RGBtoHSB(34, 87, 53, green2);
        green3 = Color.RGBtoHSB(16, 66, 17, green3);

        blue1 = Color.RGBtoHSB(54, 68, 133, blue1);
        blue2 = Color.RGBtoHSB(27, 90, 170, blue2);
        blue3 = Color.RGBtoHSB(2, 3, 90, blue3);

        yellow1 = Color.RGBtoHSB(189, 149, 15, yellow1);
        yellow2 = Color.RGBtoHSB(195, 157, 22, yellow2);
        yellow3 = Color.RGBtoHSB(176, 127, 22, yellow3);

        for (int r = 0; r<red.length; r++) {
            for (int c = 0; c<red[r].length; c++) {
                boolean matched = false;

                float[] currentPix = new float[3];
                Color.RGBtoHSB(red[r][c], green[r][c], blue[r][c], currentPix);

                for (int i=0; i<setReds.length; i++) {
                    if (colorMatch(setReds[i], currentPix)) {
                        red[r][c] = 255;
                        green[r][c] = 0;
                        blue[r][c] = 0;
                        matched = true;
                    }

                    else if (colorMatch(setGreens[i], currentPix)) {
                        red[r][c] = 0;
                        green[r][c] = 255;
                        blue[r][c] = 0;
                        matched = true;
                    }

                    else if (colorMatch(setBlues[i], currentPix)) {
                        red[r][c] = 0;
                        green[r][c] = 0;
                        blue[r][c] = 255;
                        matched = true;
                    }

                    else if (colorMatch(setYellows[i], currentPix)) {
                        red[r][c] = 255;
                        green[r][c] = 255;
                        blue[r][c] = 0;
                        matched = true;
                    }

                    if (!matched) {
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

    public boolean colorMatch(float[] target, float[] test) {
        if (test[0] > target[0]+thresholds[0] || test[0] < target[0]-thresholds[0]) return false;
        else if (test[1] > target[1]+thresholds[1] || test[1] < target[1]-thresholds[1]) return false;
        else if (test[2] > target[2]+thresholds[2] || test[2] < target[2]-thresholds[2]) return false;
        else return true;

    }

    public DImage fillIn(DImage img, int size) {
        short[][] red = img.getRedChannel();
        short[][] green = img.getGreenChannel();
        short[][] blue = img.getBlueChannel();

        for (int r = 0; r < red.length - size; r += size) {
            for (int c=0; c<red[r].length-size; c+=size) {
            if (blackIsland(r, c, red, green, blue, size)) {
                short tRed = red[r][c];
                short tGreen = green[r][c];
                short tBlue = blue[r][c];
                for (int row = r; row < r + size; row++) {
                    for (int col = c; col < c + size; col++) {
                        red[row][col] = tRed;
                        green[row][col] = tGreen;
                        blue[row][col] = tBlue;
                        }
                    }
                }
            }
        }
        img.setColorChannels(red, green, blue);
        return img;
    }

    public DImage refineMask(DImage img, int size) {
        short[][] red = img.getRedChannel();
        short[][] green = img.getGreenChannel();
        short[][] blue = img.getBlueChannel();

        for (int r=0; r<red.length-size; r+=size) {
            for (int c=0; c<red[r].length-size; c+=size) {
                if (toBeErased(r, c, red, green, blue, size)) {
                    for (int row=r; row<r+size; row++) {
                        for (int col=c; col<c+size; col++) {
                            red[row][col] = 0;
                            green[row][col] = 0;
                            blue[row][col] = 0;
                        }
                    }
                }
            }
        }

        img.setColorChannels(red, green, blue);
        return img;
    }

    public boolean toBeErased(int row, int col, short[][] red, short[][]green, short[][] blue, int size) {
        boolean toBeErased = false;
        int iterations=0;

        for (int r=row; r<row+size; r++) {
            if (red[r][col]!=0 && red[r][col+size]!=0) break;
            if (green[r][col]!=0 && green[r][col+size]!=0) break;
            if (blue[r][col]!=0 && blue[r][col+size]!=0) break;
            iterations++;
        }

        for (int c=col; c<col+size; c++) {
            if (red[row][c]!=0 && red[row+size][c]!=0) break;
            if (green[row][c]!=0 && green[row+size][c]!=0) break;
            if (blue[row][c]!=0 && blue[row+size][c]!=0) break;
            iterations++;
        }

        if (iterations==(size*2)) toBeErased = true;
        return toBeErased;
    }

    public boolean blackIsland(int row, int col, short[][] red, short[][] green, short[][] blue, int size) {
        boolean island = false;
        int iterations=0;

        short redPix = red[row][col];
        short greenPix = green[row][col];
        short bluePix = blue[row][col];

        if (redPix==0 && greenPix==0 && bluePix==0) return false;

        for (int r=row; r<row+(size-2); r++) {
            if (red[r][col]!=redPix && red[r][col+size]!=redPix) break;
            if (green[r][col]!=greenPix && green[r][col+size]!=greenPix) break;
            if (blue[r][col]!=bluePix && blue[r][col+size]!=bluePix) break;
            iterations++;
        }

        for (int c=col; c<col+size; c++) {
            if (red[row][c]!=redPix && red[row+size][c]!=redPix) break;
            if (green[row][c]!=greenPix && green[row+size][c]!=greenPix) break;
            if (blue[row][c]!=bluePix && blue[row+size][c]!=bluePix) break;
            iterations++;
        }
        if (iterations==(size*2)) island = true;
        return island;
    }

    public void mouseClicked(int mouseX, int mouseY, DImage img) {
        //add colors here
    }

    @Override
    public void keyPressed(char key) {
    }

}

