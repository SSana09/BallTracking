import core.DisplayWindow;

public class RunMe {
    public static void main(String[] args) {
        // --== Load an image to filter ==--
//        DisplayWindow.showFor("images/singleColor.jpg", 1000, 600, "DoNothingFilter");
//
//         --== Determine your input interactively with menus ==--
        DisplayWindow.getInputInteractively(1000,600);
    }
}
