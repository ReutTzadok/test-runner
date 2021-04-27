package my_spring;


public class SpeakerImpl implements Speaker {
    @Override
    public void speak(String message) {
        System.out.println(message);
    }
}
