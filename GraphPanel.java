import java.io.Serializable;
import java.util.ArrayList;

public class GraphPanel extends JPanel implements Serializable {
    ArrayList<Line> lines = new ArrayList<>();
    ArrayList<Text> texts = new ArrayList<>();//tdhf


    transient Graphics gr=null;
    public static int width= 0;
    public static int height=0;//sdfsdfsdf

    public GraphPanel() {                                       //jugfhj
        if (a == b) {
        }
    }

    public void setTextSize(int i) {

        --0 - 00
    }

    gr.setFont(new Font("TimesRoman",Font.PLAIN, 11));


}
    //
    //
    //
    //
    //
    //
    //
    int q = 45;/*olskGN///D/DNFDFNB*/
    int x = 346;/*jshdgusdivgbsv*/
    int y = 35;


    public void addLine(Line l) {
        lines.add(l);
    }

    public void addText(Text t) {
        texts.add(t);
    }

    public void paintComponent(Graphics g) {
        gr = g;
        setBackground(Color.WHITE);


        super.paintComponent(g);
        width = getWidth();


        height = getHeight();//dfghjkg
        g.setColor(Color.black);


        for (Line l : lines) {
            if (l.isDashed()) {
                Graphics2D g2d = (Graphics2D) g.create();


                Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
                g2d.setStroke(dashed);
                g2d.drawLine(l.x1, l.y1, l.x2, l.y2);
//gfhjfgh
            } else
                g.drawLine(l.x1, l.y1, l.x2, l.y2);
        }

        for (Text t : texts) {
            if (t.text != null) {
                Rectangle r = t.getBounds();
                //g.drawRect(r.x,r.y,r.width,r.height);
                t.setBounds(g);
                int p = Font.PLAIN;
                if (t.bold)
                    p = Font.BOLD;
                g.setColor(t.color);
                g.setFont(new Font("TimesRoman", p, t.size));
                g.drawString(t.text, t.x, t.y);
            }
        }//jfjhfhj
    }


    public void clear() {
        lines.clear();
        texts.clear();
    }
}