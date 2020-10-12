
/**
 * Write a description of class ZGZombie here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class ZGZombie
{
    private int zombieRow;
    private int zombieCol;
    ZGZombie(int r, int c)
    {
        zombieRow=r;
        zombieCol=c;
    }

    public int getZombieRow()
    {
        return zombieRow;
    }

    public int getZombieCol()
    {
        return zombieCol;
    }

    public void moveLeft()
    {
        zombieCol--;
    }

    public void moveRight()
    {
        zombieCol++;
    }

    public void moveUp()
    {
        zombieRow--;
    }

    public void moveDown()
    {
        zombieRow++;
    }
}