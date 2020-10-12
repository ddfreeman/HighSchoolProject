import sun.audio.AudioStream;      //put these at the top
import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import java.io.*;
/**
 * Write a description of class ZGMusic here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class ZGMusic
{
    AudioStream as1;
    AudioStream as2;
    public void playSound1(){
        try
        {
            InputStream in = new FileInputStream("ohno.wav");        //the file name  goes in the quotes
            as1 = new AudioStream(in);
            AudioPlayer.player.start(as1);
        }
        catch(FileNotFoundException e)
        {
            System.out.print(e.toString());
            e.printStackTrace();
        }
        catch(IOException error)
        {
            System.out.print(error.toString());
        }
    }
    public void playSound2(){
        try
        {
            InputStream in = new FileInputStream("rejoicing.wav");        //the file name  goes in the quotes
            as1 = new AudioStream(in);
            AudioPlayer.player.start(as1);
        }
        catch(FileNotFoundException e)
        {
            System.out.print(e.toString());
            e.printStackTrace();
        }
        catch(IOException error)
        {
            System.out.print(error.toString());
        }
    }
    public void playSound3(){
        try
        {
            InputStream in = new FileInputStream("background.wav");        //the file name  goes in the quotes
            as2 = new AudioStream(in);
            AudioPlayer.player.start(as2);
        }
        catch(FileNotFoundException e)
        {
            System.out.print(e.toString());
            e.printStackTrace();
        }
        catch(IOException error)
        {
            System.out.print(error.toString());
        }
    } 
    public void stopSound3(){
            AudioPlayer.player.stop(as2);
    }
}
