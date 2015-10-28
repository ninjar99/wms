package sorttable;
import javax.swing.*;
import java.util.Date;
import javax.swing.table.TableColumnModel;

public class SortExample extends JPanel{

  main.PBSUIBaseGrid table;
  SortTableModel dm = new SortTableModel();
  public SortExample() {
    String[] columnNames = new String[]{
      "STRING","DOUBLE","STRINGDATE","INT","BOOLEAN"};
    dm.setDataVector(new Object[][]{
      {"b",new Double("1.3"),"02/12/02",new Integer(3),new Boolean(true)},
      {"c",new Double("1.8"),"03/03/27",new Integer(6),new Boolean(false)},
      {"a",new Double("2.34"),"02/07/22",new Integer(9),new Boolean(false)}
    },
    columnNames);
    table = new main.PBSUIBaseGrid(dm);
// set HeaderRenderer
    SortButtonRenderer renderer = new SortButtonRenderer();
    TableColumnModel model = table.getColumnModel();
    int n = columnNames.length;
    for (int i=0;i<n;i++) {
      model.getColumn(i).setHeaderRenderer(renderer);
    }
// add MouseListener for header
    javax.swing.table.JTableHeader header = table.getTableHeader();
    header.addMouseListener(new HeaderListener(header,renderer));

    JScrollPane pane = new JScrollPane(table);
    JButton btn = new JButton("Set Data");
    btn.addActionListener(new java.awt.event.ActionListener(){
      public void actionPerformed(java.awt.event.ActionEvent evt){
        adddata();
      }
    });
    this.add(btn,java.awt.BorderLayout.SOUTH);
    add(pane, java.awt.BorderLayout.CENTER);
  }

  public void adddata(){
//    dm.addRow(new Object[]
//        {"e",new Double("1.223"),"02/08/02",new Integer(3),new Boolean(true)});
    if (dm.getRowCount() > 0)
      dm.removeRow(dm.getRowCount()-1);
    System.out.println();
  }

  public static void main(String[] args) {
    JFrame f= new JFrame("SortableTable Example");
    f.getContentPane().add(new SortExample(), java.awt.BorderLayout.CENTER);
    f.setSize(400, 160);
    f.setVisible(true);
    f.addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent e) {System.exit(0);}
    });
  }
}
