package Filters;

import Interfaces.Interactive;
import Interfaces.PixelFilter;
import core.DImage;

public class ColorMask implements PixelFilter, Interactive {

    //mini squares, dlt if surrounded by black, or on 3 sides
    //100x100 squares (testable)

    private double threshold = 50;

    private short[] red1 = {250, 108, 88};
    private short[] red2 = {177, 29, 15};
    private short[] red3 = {200, 83, 72};
    private short[][] setReds = {red1, red2, red3};

    private short[] green1 = {107, 163, 88};
    private short[] green2 = {34, 87, 53};
    private short[] green3 = {16, 66, 17};
    private short[][] setGreens = {green1, green2, green3};

    private short[] blue1 = {54, 68, 133};
    private short[] blue2 = {20, 90, 70};
    private short[] blue3 = {2, 3, 60};
    private short[][] setBlues = {blue1, blue2, blue3};

    private short[] yellow1 = {189, 149, 15};
    private short[] yellow2 = {195, 157, 22};
    private short[] yellow3 = {176, 127, 22};
    private short[][] setYellows = {yellow1, yellow2, yellow3};

    public DImage processImage(DImage img) {
        short[][] red = img.getRedChannel();
        short[][] blue = img.getBlueChannel();
        short[][] green = img.getGreenChannel();
        //rgb
        
        for (int r = 0; r<red.length; r++) {
            for (int c = 0; c<red[r].length; c++) {
                boolean matched = false;

                for (int i=0; i<setReds.length; i++) {
                    double distRed = Math.sqrt(Math.pow(red[r][c] - setReds[i][0], 2) +
                            Math.pow(green[r][c] - setReds[i][1], 2) +
                            Math.pow(blue[r][c] - setReds[i][2], 2));

                    double distGreen = Math.sqrt(Math.pow(red[r][c] - setGreens[i][0], 2) +
                            Math.pow(green[r][c] - setGreens[i][1], 2) +
                            Math.pow(blue[r][c] - setGreens[i][2], 2));

                    double distBlue = Math.sqrt(Math.pow(red[r][c] - setBlues[i][0], 2) +
                            Math.pow(green[r][c] - setBlues[i][1], 2) +
                            Math.pow(blue[r][c] - setBlues[i][2], 2));

                    double distYellow = Math.sqrt(Math.pow(red[r][c] - setYellows[i][0], 2) +
                            Math.pow(green[r][c] - setYellows[i][1], 2) +
                            Math.pow(blue[r][c] - setYellows[i][2], 2));

                    if (distRed<=threshold) {
                        red[r][c] = 255;
                        green[r][c] = 0;
                        blue[r][c] = 0;
                        matched = true;
                    }

                    else if (distGreen<=threshold) {
                        red[r][c] = 0;
                        green[r][c] = 255;
                        blue[r][c] = 0;
                        matched = true;
                    }

                    else if (distBlue<=threshold) {
                        red[r][c] = 0;
                        green[r][c] = 0;
                        blue[r][c] = 255;
                        matched = true;
                    }

                    else if (distYellow<=threshold) {
                        red[r][c] = 255;
                        green[r][c] = 255;
                        blue[r][c] = 0;
                        matched = true;
                    }

                    if (matched == false) {
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

    public void mouseClicked(int mouseX, int mouseY, DImage img) {
        //add colors here
    }

    @Override
    public void keyPressed(char key) {
        if (key == '+') {
            threshold+=1;
        }

        if (key == '-') {
            if (threshold>1) {
                threshold -= 1;
            }
        }
    }

}
