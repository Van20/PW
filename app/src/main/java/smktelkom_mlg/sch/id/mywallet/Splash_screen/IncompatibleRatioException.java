package smktelkom_mlg.sch.id.mywallet.Splash_screen;

public class IncompatibleRatioException extends RuntimeException {

    public static final long serialVersionUID = 234608108593115395L;

    public IncompatibleRatioException() {
        super("Can't perform Ken Burns effect on rects with distinct aspect ratios!");
    }
}