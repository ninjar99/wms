package main;

public class thisModel extends sorttable.SortTableModel
{
  public int[] keyCol;int[] newRow;
  public thisModel()
  {
    super();
  }
  public void setkeyCol(int[] keycols){
    this.keyCol = keycols;
  }
  public void setNewrow(int[] newrows){
    this.newRow = newrows;
  }

  public boolean isCellEditable(int row,int col){
    if (keyCol == null) return super.isCellEditable(row,col);
    if (newRow == null) return super.isCellEditable(row,col);
//    if (main.PBSUIBaseSetupFrame.isIntInInteger(col,keyCol) && main.PBSUIBaseSetupFrame.isIntInInteger(row,newRow)){
//      return false;
//    }
    return super.isCellEditable(row,col);
  }
}
