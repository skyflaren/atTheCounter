
public class Button {
    
    private int x,y,h,w;
    
    public Button(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.h = h;
        this.w = w;
    }
    
    public boolean mouseOver(int mx, int my) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }
}
