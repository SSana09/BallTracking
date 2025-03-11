package Filters;

import Interfaces.Interactive;
import Interfaces.PixelFilter;
import core.DImage;

import java.awt.*;

public class ColorMask implements PixelFilter, Interactive {

    private static float hueThresh = (float) 0.0278;
    private static float satThresh = (float) 0.02;
    private static float valThresh = (float) 0.02;

    private int targetRed1 = 255;
    private int targetBlue1 = 5;
    private int targetGreen1 = 0; // red

    private int targetRed2 = 255;
    private int targetBlue2 = 0;
    private int targetGreen2 = 165; // orange

    private int targetRed3 = 0;
    private int targetBlue3 = 255;
    private int targetGreen3 = 5; // blue

    public DImage processImage(DImage img) {
        float[] targetHSB1 = new float[3];
        float[] targetHSB2 = new float[3];
        float[] targetHSB3 = new float[3];
        targetHSB1 = Color.RGBtoHSB(targetRed1, targetGreen1, targetBlue1, targetHSB1);
        targetHSB2 = Color.RGBtoHSB(targetRed2, targetGreen2, targetBlue2, targetHSB2);
        targetHSB3 = Color.RGBtoHSB(targetRed3, targetGreen3, targetBlue3, targetHSB3);

        short[][] red = img.getRedChannel();
        short[][] blue = img.getBlueChannel();
        short[][] green = img.getGreenChannel();

        for (int r = 0; r <red.length; r++) {
            for (int c = 0; c<red[r].length; c++) {
                int redPix = red[r][c];
                int greenPix = green[r][c];
                int bluePix = blue[r][c];
                float[] hsb = new float[3];
                hsb = Color.RGBtoHSB(redPix, greenPix, bluePix, hsb);

                if (hsb[0]<=targetHSB1[0]+hueThresh && hsb[0]>=targetHSB1[0]-hueThresh) {
                    if (hsb[1]<=targetHSB1[1]+satThresh && hsb[1]>=targetHSB1[1]-satThresh) {
                        if (hsb[2]<=targetHSB1[2]+valThresh && hsb[2]>=targetHSB1[2]-valThresh) {
                            red[r][c] = 255;
                            green[r][c] = 0;
                            blue[r][c] = 0;
                        }
                    }
                }//red

                else if (hsb[0]<=targetHSB2[0]+hueThresh && hsb[0]>=targetHSB2[0]-hueThresh) {
                    if (hsb[1]<=targetHSB2[1]+satThresh && hsb[1]>=targetHSB2[1]-satThresh) {
                        if (hsb[2]<=targetHSB2[2]+valThresh && hsb[2]>=targetHSB2[2]-valThresh) {
                            red[r][c] = 255;
                            green[r][c] = 165;
                            blue[r][c] = 0;
                        }
                    }
                }//orange

                else if (hsb[0]<=targetHSB3[0]+hueThresh && hsb[0]>=targetHSB3[0]-hueThresh) {
                    if (hsb[1]<=targetHSB3[1]+satThresh && hsb[1]>=targetHSB3[1]-satThresh) {
                        if (hsb[2]<=targetHSB3[2]+valThresh && hsb[2]>=targetHSB3[2]-valThresh) {
                            red[r][c] = 0;
                            green[r][c] = 5;
                            blue[r][c] = 255;
                        }
                    }
                }//blue

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
    }

}
