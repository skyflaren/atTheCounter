import java.awt.image.BufferedImage;

public class SpriteSheet {
    
    private BufferedImage sprite;
    private int rowsz, colsz;
    
    //Constructor
    public SpriteSheet(BufferedImage ss, int rowsz, int colsz){
        this.sprite = ss;
        this.rowsz = rowsz;
        this.colsz = colsz;
        System.out.println(ss + " " + rowsz + " " + colsz);
    }
    
    //Helper method to get images from the spritesheet
    public BufferedImage grabImage(int row, int col, int w, int h) {
        BufferedImage img = sprite.getSubimage(col * colsz, row * rowsz, w, h);
        return img;
    }
}
