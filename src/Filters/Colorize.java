package Filters;

import Interfaces.PixelFilter;
import core.DImage;

import java.util.Scanner;

public class Colorize implements PixelFilter {
    private int n;
    private int interval;
    private short[] red;
    private short[] green;
    private short[] blue;

    public Colorize() {
        Scanner s = new Scanner(System.in);
        System.out.println("How many different shades would you like? ");
        this.n = s.nextInt();
        interval = 256/n;
        blue = makeRandomColors(n);
        green = makeRandomColors(n);
        red = makeRandomColors(n);
    }

    public DImage processImage(DImage img) {
        short[][] grid = img.getBWPixelGrid();
        short[][] redChannel = img.getRedChannel();
        short[][] greenChannel = img.getGreenChannel();
        short[][] blueChannel = img.getBlueChannel();

        for (int r=0; r< grid.length; r++) {
            for (int c=0; c<grid[0].length; c++) {
                redChannel[r][c] = red[grid[r][c]/interval];
                greenChannel[r][c] = green[grid[r][c]/interval];
                blueChannel[r][c] = blue[grid[r][c]/interval];
            }
        }

        img.setColorChannels(redChannel, greenChannel, blueChannel);
        return img;
    }

    public short[] makeRandomColors(int n) {
        short[] colors = new short[n];
        for (int i=0; i<n; i++) {
            colors[i] = (short) (Math.random()*256);
        }
        return colors;
    }
}
