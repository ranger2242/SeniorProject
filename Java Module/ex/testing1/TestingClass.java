class Test{
    public int testVar = 5;
    public Test(){
    }
}
class A{
    public static void main(String[] args){
        Test t = new Test();
        t.testVar = 7;
    }
}
