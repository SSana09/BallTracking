package Filters;
import Interfaces.PixelFilter;
import core.DImage;
public class Convolution implements PixelFilter {
    private double[][] boxBlurKernel = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
    private double[][] PrewittEdgeKernel = {{-1, -1, -1}, {-1, 8, -1}, {-1, -1, -1}};
    private double[][] gaussianBlur = {{1, 2, 1}, {2, 4, 2}, {1, 2, 1}};
    private double[][] sharpenKernel = {{0, -1, 0}, {-1, 5, -1}, {0, -1, 0}};
    private double[][] lineKernel = {{-1, -1, -1}, {2, 2, 2}, {-1, -1, -1}};
    private double[][] funsiesKernel = {{0, 5, 1}, {11, 11, 11}, {4, 8, 1}};
    private double[][] largerGaussian = {{0, 0, 0, 5, 0, 0, 0}, {0, 5, 18, 32, 18, 5, 0},
            {0, 18, 64, 100, 64, 18, 0}, {5, 32, 100, 100, 100, 32, 5}, {0, 18, 64, 100, 64, 18, 0},
            {0, 5, 18, 32, 18, 5, 0}, {0, 0, 0, 5, 0, 0, 0}};

    private double[][] kernel;

    public Convolution(){
        //kernel in use
        this.kernel = boxBlurKernel;
    }

    public DImage processImage(DImage img) {
        short[][] red = img.getRedChannel();
        short[][] green = img.getGreenChannel();
        short[][] blue = img.getBlueChannel();

        short[][] out = new short[red.length][red[0].length];
        int kernelW = kernelW(kernel);
        int kernelL = kernel.length;
        for (int r = 0; r<red.length-kernelL-1; r++) {
            for (int c=0; c<red[r].length-kernelL-1; c++) {
                short RreplaceW = (short) calcAvg(red, kernel, r, c, kernelW);
                short GreplaceW = (short) calcAvg(green, kernel, r, c, kernelW);
                short BreplaceW = (short) calcAvg(blue, kernel, r, c, kernelW);
                red[r+(kernelL/2)-1][c+(kernelL/2)-1] = RreplaceW;
                green[r+(kernelL/2)-1][c+(kernelL/2)-1] = GreplaceW;
                blue[r+(kernelL/2)-1][c+(kernelL/2)-1] = BreplaceW;
            }
        }
        img.setColorChannels(red, green, blue);
        return img;
    }

    public double calcAvg(short[][] grid, double[][] kernel, int startR, int startC, int kernelW) {
        int out=0;
        int len = kernel.length;
        for (int r=startR; r<(startR+len); r++) {
            for (int c=startC; c<(startC+len); c++) {
                int val1 = grid[r][c];
                double val2 = kernel[r-startR][c-startC];
                out+= (val1*val2);
            }
        }
        out/=kernelW;
        if (out>255) out=255;
        if (out<0) out=0;
        return out;
    }

    public int kernelW(double[][] kernel) {
        int kernelW=0;
        for (int i=0; i< kernel.length; i++) {
            for (int j=0; j<kernel[i].length; j++) {
                kernelW+=kernel[i][j];
            }
        }
        if (kernelW==0) kernelW=1;
        return kernelW;
    }
}