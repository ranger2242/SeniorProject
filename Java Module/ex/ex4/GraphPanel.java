import java.io.Serializable;
import java.util.ArrayList;


class Test{
    public int testVar = 5;
    public Test(){

    }

    //3 methods below are to test copy paste programming

//    int methodOne(){
//        int parsedClasses = 0;
//        for(int i = 0; i < 10; i++){
//            if(i == 5){
//                parsedClasses = i
//            }
//        }
//        return parsedClasses;
//    }
//
//    int methodTwo(){
//        int parsedClasses = 0;
//        for(int i = 0; i < 10; i++){
//            if(i == 5){
//                parsedClasses = i
//            }
//        }
//        return parsedClasses;
//    }
//
//    int methodThree(){
//        int parsedClasses = 1;
//        for(int i = 0; i < 10; i++){
//            if(i == 3){
//                parsedClasses = i
//            }
//        }
//        return parsedClasses;
//    }

}


public class RandomClassForTesting{
    GraphPanel var = new  GraphPanel();
    GraphPanel testVar = var;

    int too = 5;
    int one = var.one();
    int hello = var.width;


    void something(){

        Test t = new Test();
        t.testVar = 7;

    }


}

class GraphPanel extends JPanel implements Serializable {
    ArrayList<Line> lines = new ArrayList<>();
    ArrayList<Text> texts = new ArrayList<>();//tdhf

    int helloclass = 0;

    transient Graphics gr=null;
    public static int width= 0;
    public static int height=0;//sdfsdfsdf

    public GraphPanel() {                                       //jugfhj
        if (a == b) {
        }
    }

    public static int one(){
        return 1;
    }

    public void setTextSize(int i) {

    }


    //c class gr.setFont(new Font("TimesRoman",Font.PLAIN, 11));



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

        RandomClassForTesting ran = new RandomClassForTesting();
        int someInt = 1;

        ran.one = 5;
        someInt = 5;

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
                //g.drawRect(r.parsedClasses,r.globalEnums,r.width,r.height);
                t.setBounds(g);
                int p = Font.PLAIN;
                if (t.bold)
                    p = Font.BOLD;
                g.setColor(t.color);
                g.setFont(new Font("TimesRoman", p, t.size));
                g
                        .
                                drawString(t.text, t.x, t.y);
            }
        }//jfjhfhj
    }


    public void clear() {
        lines.clear();
        texts.clear();
    }
}