package utilities;




import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LCRT
 */
public class ResultsetTable {
private DefaultTableModel dataModel;


public TableModel rstomodel(ResultSet rs) throws SQLException{
    
    
    
dataModel = new DefaultTableModel(){

 @Override
 public boolean isCellEditable(int row, int column)
 {
     return false;
 }
 };


/*
////////to make the table editable///////////////////
dataModel = new DefaultTableModel();
*/





//setModel(dataModel);

/////////////////////////////////
 try {
      //create an array of column names
      ResultSetMetaData mdata = rs.getMetaData();
      int colCount = mdata.getColumnCount();
      String[] colNames = new String[colCount];
      for (int i = 1; i <= colCount; i++) {
        colNames[i - 1] = mdata.getColumnName(i);
      }
      dataModel.setColumnIdentifiers(colNames);
 
      //now populate the data
      while (rs.next()) {
        String[] rowData = new String[colCount];
        for (int i = 1; i <= colCount; i++) {
          rowData[i - 1] = rs.getString(i);
        }
        dataModel.addRow(rowData);
      }
    }
    finally{
      try {
      //  rs.close();
      }
      catch (Exception e) {
          System.err.println(e);
      }
    }
  
//////////////////////////////////

return dataModel;
}





}
