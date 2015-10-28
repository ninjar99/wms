package util;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class NeedWaitUtil implements NeedWait{
  private boolean isneedme;
  private boolean isFinished = false;
  private Thread sleepThread;
  public NeedWaitUtil(boolean needme) {
    this.isneedme = needme;
    sleepThread = new Thread();
  }

  public boolean gettoBeWaited(){
    return this.isneedme;
  }

  public boolean setfinishedtag(boolean isFinished) {
    boolean oldfinished = this.isFinished;
    this.isFinished = isFinished;
    return oldfinished;
  }

  public boolean getfinishedtag() {
    if (!isneedme) return true;
    return this.isFinished;
  }
  public void sleep(){
    try {
      sleepThread.sleep(100);
    }
    catch (InterruptedException ex) {
    }
  }
}
