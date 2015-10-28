package util;

public class MONOInfo {
  private boolean invalidate=false;
  private String MONO="";
  private String SONO="";
  private String item="";
  public MONOInfo() {
  }
  public boolean isInvalidate() {
    return invalidate;
  }
  public void setInvalidate(boolean invalidate) {
    this.invalidate = invalidate;
  }
  public String getMONO() {
    return MONO;
  }
  public void setMONO(String MONO) {
    this.MONO = MONO;
  }
  public String getSONO() {
    return SONO;
  }
  public void setSONO(String SONO) {
    this.SONO = SONO;
  }
  public String getItem() {
    return item;
  }
  public void setItem(String item) {
    this.item = item;
  }
}
