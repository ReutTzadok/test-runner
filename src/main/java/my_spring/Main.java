package my_spring;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new ApplicationContext();
        IRobot iRobot = context.getObject(IRobot.class);
        IRobot iRobot2 = context.getObject(IRobot.class);
        iRobot.cleanRoom();
    }
}
