package sorttable;

public class CellID{
  private int col;
  private int row;
  private boolean editable;
  public CellID(int row,int col,boolean editable){
    this.col = col;
    this.row = row;
    this.editable = editable;
  }
  public int getRow(){
    return this.row;
  }
  public int getCol(){
    return this.col;
  }
  public boolean isEditable(){
    return this.editable;
  }
}
