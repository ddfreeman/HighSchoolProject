
public class ZGBullet
{
    private int bulletRow;
    private int bulletCol;
    private int bulletDir;
    /*
    private final int SHOOTLEFT=37;
    private final int SHOOTRIGHT=39;
    private final int SHOOTUP=38;
    private  final int SHOOTDOWN=40;
    private final int STOP=-1;
     */

    ZGBullet(int r, int c, int d)
    {
        bulletRow=r;
        bulletCol=c;
        bulletDir=d;
    }

    public int getBulletRow()
    {
        return bulletRow;
    }

    public int getBulletCol()
    {
        return bulletCol;
    }

    public int getBulletDir()
    {
        return bulletDir;
    }

    public void moveLeft()
    {
        bulletCol--;
    }

    public void moveRight()
    {
        bulletCol++;
    }

    public void moveUp()
    {
        bulletRow--;
    }

    public void moveDown()
    {
        bulletRow++;
    }

    public void wipeOutBullet()//This is to ensure that the bullet gets deleted when it removes bullets on the edges
    {
        bulletRow=0;
        bulletCol=0;
    }
}