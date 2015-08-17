package connection_utilies;

/**
 *
 * @author LCRT
 */
// Remember to use access you will jdk7  they remove it from jdk 8
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;

public class DBSql {

    private static Connection conn = ConnectionManager.getInstance().getConnection();

    /*

     public LinkedList Authentification(String username,String password){
     LinkedList llst=new LinkedList();
     Boolean exist=false;
     String nombre=null;
     int codter=0;
 
     int count = 0;
     try {
     //  System.out.println("Connection : "+ conn);
     String sql="SELECT * FROM users WHERE username=? AND password=?";
            
            
     PreparedStatement ps=conn.prepareStatement(sql);
     ps.setString(1,username);
     ps.setString(2, password);
     ResultSet rs=ps.executeQuery();
            
     // ResultSet rs=st.executeQuery(sql);
     while (rs.next()) {               
     exist=true;
     count++;
     nombre=rs.getString("lastname")+" "+rs.getString("firstname");
     codter=rs.getInt("codter");
     }
           
     if (count>1) {
              
     System.out.println("duplicate user;");
     }
     System.out.println("nombre : "+nombre);
     System.out.println("Found User : "+ count);
     System.out.println("Queried");
     //conn.close();
     
       
     } catch (SQLException e) {
     System.out.println(e);
     }
     llst.add(exist);
     llst.add(codter);
     llst.add(nombre);
  
     return llst;      
     }
 

 
  
     public boolean Authentification(String username){
     Boolean exist=false;
     int count = 0;
     try {
     //  System.out.println("Connection : "+ conn);
     String sql="SELECT * FROM users WHERE username='"+username+"'";
     Statement sta=conn.createStatement();
     ResultSet rs=sta.executeQuery(sql);
     while (rs.next()) {               
     exist=true;
     count++;
     }
           
     if (count>1) {
              
     System.out.println("duplicate username;");
     }   
     System.out.println("Found User : "+ count);
     System.out.println("Queried");
     //conn.close();
     
       
     } catch (SQLException e) {
     System.out.println(e);
     }
 
     return exist;      
       
     }
 
 
 
  
     public boolean WriteToDatabase(String username,String password,String firstname,String lastname,String gender,String email,int whoregisteredit,int userlevel){
     boolean success=false;
 
     try {
     //  System.out.println("Connection : "+ conn);
     String sql="INSERT INTO users(username,password,firstname,lastname,gender,email,whoregisteredit,userlevel)VALUES('"+username+"','"+password+"','"+firstname+"','"+lastname+"','"+gender+"','"+email+"','"+whoregisteredit+"','"+userlevel+"')";
     Statement sta=conn.createStatement();
     int s=sta.executeUpdate(sql);
     if (s==1) {
     success=true; 
     }else{
     success=false;
     }
     //   System.out.println("Inserted >> "+s);
     //conn.close();
     } catch (SQLException e) {
     System.out.println(e);
     }
 
     return success;
     }
 

 
 
     public boolean WriteLogToDatabase(int userid){
     boolean success=false;
 
     try {
     //  System.out.println("Connection : "+ conn);
     String sql="INSERT INTO userlog(logtime,userid) VALUES(NOW(),'"+userid+"')";
     Statement sta=conn.createStatement();
     int s=sta.executeUpdate(sql);
     if (s==1) {
     success=true; 
     }else{
     success=false;
     }
     //   System.out.println("Inserted >> "+s);
     //conn.close();
     } catch (SQLException e) {
     System.out.println(e);
     }
 
     return success;
     }

 
     public boolean WriteAttemptsToDatabase(String username,String password){
     boolean success=false;
 
     try {
     //  System.out.println("Connection : "+ conn);
     String sql="INSERT INTO logattempts(attemptime,username,password) VALUES(NOW(),'"+username+"','"+password+"')";
     Statement sta=conn.createStatement();
     int s=sta.executeUpdate(sql);
     if (s==1) {
     success=true; 
     }else{
     success=false;
     }
     //   System.out.println("Inserted >> "+s);
     conn.close();
     } catch (SQLException e) {
     System.out.println(e);
     }
 
     return success;
     }


     */
    public LinkedList Authentification(String usuario, String contrasena, String sqlQuery) {
        LinkedList llst = new LinkedList();
        boolean exist = false;
        String nombre = null;
        String codter = null;
        String tipousuario = null;

        int count = 0;
        try {

            sqlQuery += " WHERE usuario=? AND contrasena=?";
           
           // System.out.println("query login >>"+sqlQuery);
           
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ps.setString(1, usuario);
            ps.setString(2, contrasena);
            ResultSet rs = ps.executeQuery();

            // ResultSet rs=st.executeQuery(sql);
            while (rs.next()) {
                exist = true;
                count++;
                nombre = rs.getString("nombre");
                codter = rs.getString("codter");
                usuario = rs.getString("usuario");
                tipousuario = rs.getString("tipousuario");

            }

            if (count > 1) {

                System.out.println("duplicate user;");
                
            }
            //System.out.println("uname : " + nombre);
           // System.out.println("Found User : " + count);
           // System.out.println("Queried");
            System.out.println("exist "+exist+" count"+count);
            
             //conn.close();

        } catch (SQLException e) {
            System.out.println(e);
        }
        llst.add(exist);
        llst.add(codter);
        llst.add(nombre);
        llst.add(usuario);
        llst.add(tipousuario);
        
 return llst;
    }

    
    
    
    
    
    
    
    
    
    
    public boolean matriculaExist(String mat) {
        Boolean exist = false;
        int count = 0;
        try {
            //  System.out.println("Connection : "+ conn);
            String sql = "SELECT * FROM lector WHERE matricula='" + mat + "'";
            Statement sta = conn.createStatement();
            ResultSet rs = sta.executeQuery(sql);
            while (rs.next()) {
                exist = true;
                count++;
            }

            if (count > 1) {

                System.out.println("duplicate matricula;");
            }
            System.out.println("cts mat found : " + count);
            System.out.println("Queried");
             //conn.close();

        } catch (SQLException e) {
            System.out.println(e);
        }

        return exist;

    }
    
    
    
    
    
    
       public boolean alreadyExist(String sqlQuery ) {
        Boolean exist = false;
        int count = 0;
        try {
            //  System.out.println("Connection : "+ conn);
            String sql=sqlQuery;
            Statement sta = conn.createStatement();
            ResultSet rs = sta.executeQuery(sql);
            while (rs.next()) {
                exist = true;
                count++;
            }

            if (count > 1) {

                System.out.println("duplicate;");
            }
            System.out.println("cts mat found : " + count);
            System.out.println("Queried");
             //conn.close();

        } catch (SQLException e) {
            System.out.println(e);
        }

        return exist;

    }
       
       
       
       
       
    
    
    

    public boolean dataDescriptionExist(String table, String columnDescription, String Description) {
        Boolean exist = false;
        int count = 0;
        try {
            //  System.out.println("Connection : "+ conn);
            String sql = "SELECT * FROM " + table + " WHERE " + columnDescription + "='" + Description + "'";
            Statement sta = conn.createStatement();
            ResultSet rs = sta.executeQuery(sql);
            while (rs.next()) {
                exist = true;
                count++;
            }

            if (count > 1) {

                System.out.println("More than one found;");
            }
            System.out.println("found : " + count);

        } catch (SQLException e) {
            System.out.println(e);
        }

        return exist;

    }

    public String libroPendienteTituloCopia(String mat) {
        String tituloCopia = null;
        int count = 0;
        try {
            //  System.out.println("Connection : "+ conn);
            String sql = "SELECT * FROM (SELECT  * FROM (SELECT * FROM viewlector  GROUP BY  idlector,estadoprestamos  ORDER BY estadoprestamos DESC) AS DV GROUP BY idlector)AS DVX WHERE matricula=" + mat + " AND estadoprestamos=1";
            Statement sta = conn.createStatement();
            ResultSet rs = sta.executeQuery(sql);
            while (rs.next()) {
                count++;
                tituloCopia = rs.getString("titulo") + ",Copia:" + rs.getString("numcopia");
            }

            if (count >= 1) {

                System.out.println("duplicate matricula;");
            }
            System.out.println("cts mat found : " + count);
            System.out.println("Queried");
             //conn.close();

        } catch (SQLException e) {
            System.out.println(e);
        }

        return tituloCopia;
    }

    public String libroPendienteTituloCopiaReservado(String mat) {
        String tituloCopia = null;
        int count = 0;
        try {
            //  System.out.println("Connection : "+ conn);
            String sql = "SELECT * FROM (SELECT  * FROM (SELECT * FROM viewlectorreservacion  GROUP BY  idlector,estadoreservacion  ORDER BY estadoreservacion DESC) AS DV GROUP BY idlector)AS DVX WHERE matricula=" + mat + " AND estadoreservacion=1";
            System.out.println("test query" + sql);
            Statement sta = conn.createStatement();
            ResultSet rs = sta.executeQuery(sql);
            while (rs.next()) {
                count++;
                tituloCopia = rs.getString("titulo") + ",Copia:" + rs.getString("numcopia");
            }

            if (count >= 1) {

                System.out.println("duplicate matricula;");
            }
            System.out.println("cts mat found : " + count);
            System.out.println("Queried");
             //conn.close();

        } catch (SQLException e) {
            System.out.println(e);
        }

        return tituloCopia;
    }

    public String getIdFromMat(String mat) {
        String id = null;
        int count = 0;
        try {
            //  System.out.println("Connection : "+ conn);
            String sql = "SELECT id from lector WHERE matricula=" + mat;
            Statement sta = conn.createStatement();
            ResultSet rs = sta.executeQuery(sql);
            while (rs.next()) {
                count++;
                id = rs.getString("id");
            }

            if (count >= 1) {

                System.out.println("duplicate id;");
            }
            System.out.println("cts id found : " + count);
            System.out.println("Queried");
             //conn.close();

        } catch (SQLException e) {
            System.out.println(e);
        }

        return id;
    }

    public boolean executeQuery(String sql) {
        boolean success = false;

        try {
            System.out.println(sql);
            Statement sta = conn.createStatement();
            int s = sta.executeUpdate(sql);
            if (s == 1) {
                success = true;
            } else {
                success = false;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return success;
    }

    public String Curdate() {
        String now = "0000-00-00";
        String SqlNow = "SELECT CURDATE();";
        ResultSet rs = null;
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(SqlNow);
            rs = ps.executeQuery();

            while (rs.next()) {
                now = rs.getString(1);
                System.out.println(now);
            }

        } catch (SQLException e) {
            System.out.println(e);

        }
        return now;
    }

    public boolean dosGuardar(String desc, String tabla) {
        boolean success = false;

        String tablename = tabla;

        try {
            //  System.out.println("Connection : "+ conn);
            String sql = "INSERT INTO " + tabla + "(descripcion)VALUES('" + desc + "')";
            Statement sta = conn.createStatement();
            int s = sta.executeUpdate(sql);
            if (s == 1) {
                success = true;
            } else {
                success = false;
            }
          //   System.out.println("Inserted >> "+s);
            //conn.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return success;
    }

    public boolean dosGuardarWithDescColname(String tabla, String descriptionColname, String desc) {
        boolean success = false;

        String tablename = tabla;

        try {
            //  System.out.println("Connection : "+ conn);
            String sql = "INSERT INTO " + tabla + "(" + descriptionColname + ")VALUES('" + desc + "')";
            Statement sta = conn.createStatement();
            int s = sta.executeUpdate(sql);
            if (s == 1) {
                success = true;
            } else {
                success = false;
            }
          //   System.out.println("Inserted >> "+s);
            //conn.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return success;
    }

    public boolean guardarLibro(String desc, String tabla) {
        boolean success = false;

        String tablename = "des" + tabla;

        try {
            //  System.out.println("Connection : "+ conn);
            String sql = "INSERT INTO " + tabla + "(" + tablename + ")VALUES('" + desc + "')";
            Statement sta = conn.createStatement();
            int s = sta.executeUpdate(sql);
            if (s == 1) {
                success = true;
            } else {
                success = false;
            }
          //   System.out.println("Inserted >> "+s);
            //conn.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return success;
    }

    public int nextai(String dbname, String tblname) {
        int nextautoincrement = 0;
        String sql = "SELECT AUTO_INCREMENT FROM information_schema.TABLES WHERE TABLE_SCHEMA = '" + dbname + "' AND TABLE_NAME = '" + tblname + "'";
        ResultSet rs = null;
        PreparedStatement ps;

        try {

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                nextautoincrement = rs.getInt("Auto_increment");
                //System.out.println("num "+nextautoincrement); 
            }

            //conn.close();
        } catch (SQLException e) {
            System.out.println(e);

        }

        return nextautoincrement;
    }

    public int Max(String sql) {
        int max = 0;
//String sql="SELECT MAX("+colunmname+") FROM  '"+tblname+"' WHERE  id= "+where;
        ResultSet rs = null;
        PreparedStatement ps;

        try {

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                max = rs.getInt(1);
            }

            //conn.close();
        } catch (SQLException e) {
            System.out.println(e);

        }

        return max;
    }

    public ResultSet displaytb(String tblname, String specificQuery) {

        String sql = "SELECT * FROM " + tblname + " ORDER BY " + tblname + ".id DESC";
        if (specificQuery != null) {
            sql = specificQuery;
        }
        ResultSet rs = null;
        PreparedStatement ps;

        try {

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

    //while (rs.next()) {    
            //int id=rs.getInt(1);      
            //String desc=rs.getString(2);
            //System.out.println(id+" "+desc); 
            //}
            //conn.close();
        } catch (SQLException e) {
            System.out.println(e);

        }

        return rs;
    }

    public ResultSet displaytbWithIdColname(String tblname, String idcolname) {

        String sql = "SELECT * FROM " + tblname + " ORDER BY " + idcolname + " DESC";

        ResultSet rs = null;
        PreparedStatement ps;

        try {

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

    //while (rs.next()) {    
            //int id=rs.getInt(1);      
            //String desc=rs.getString(2);
            //System.out.println(id+" "+desc); 
            //}
            //conn.close();
        } catch (SQLException e) {
            System.out.println(e);

        }

        return rs;
    }

    public ResultSet displaytbAddcolumn(LinkedList columns, String tblname, String specificQuery) {

        //get all columns
        String clmns = "";
        for (int i = 0; i < columns.size(); i++) {
            //create a string with the columns
            clmns += columns.get(i) + " ,";
        }

        System.out.println("SELECT " + clmns + " " + tblname + ".* FROM " + tblname + " ORDER BY id DESC");
        //System.out.println(""+columns.size());

        String sql = "SELECT " + clmns + " " + tblname + ".* FROM " + tblname;
        if (specificQuery != null) {
            sql = specificQuery;
        }
        ResultSet rs = null;
        PreparedStatement ps;

        try {

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

    //while (rs.next()) {    
            //int id=rs.getInt(1);      
            //String desc=rs.getString(2);
            //System.out.println(id+" "+desc); 
            //}
            //conn.close();
        } catch (SQLException e) {
            System.out.println(e);

        }

        return rs;
    }

    public boolean insertUpdateData(String sql) {
        boolean success = false;
        String query = sql;
        try {
           //  System.out.println("Connection : "+ conn);

            Statement sta = conn.createStatement();
            int s = sta.executeUpdate(query);
            if (s == 1) {
                success = true;
            } else {
                success = false;
            }
          //   System.out.println("Inserted >> "+s);
            //conn.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        return success;

    }

    public boolean checkid(String tblname, int id) {
        boolean exist = false;

        try {
            //  System.out.println("Connection : "+ conn);
            String sql = "SELECT EXISTS(SELECT 1 FROM " + tblname + " WHERE id=" + id + ") AS exist";
            System.out.println(sql);
            ResultSet rs = null;
            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            rs.next();
            int ext = rs.getInt("exist");
            if (ext == 1) {
                exist = true;
            } else {
                exist = false;
                System.out.println("Li pa egziste");
            }

        } catch (SQLException e) {
            System.out.println(e);
        }
        return exist;
    }

    public boolean checkidWithIdColname(String tblname, String idconame, int id) {
        boolean exist = false;

        try {
            //  System.out.println("Connection : "+ conn);
            String sql = "SELECT EXISTS(SELECT 1 FROM " + tblname + " WHERE " + idconame + "=" + id + ") AS exist";
            System.out.println(sql);
            ResultSet rs = null;
            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            rs.next();
            int ext = rs.getInt("exist");
            if (ext == 1) {
                exist = true;
            } else {
                exist = false;
                System.out.println("Li pa egziste");
            }

        } catch (SQLException e) {
            System.out.println(e);
        }
        return exist;
    }

    public boolean dosUpdate(String tblname, String descolname, String desc, String idcolname, int id) {
        boolean success = false;

        try {
            //  System.out.println("Connection : "+ conn);
            String sql = "UPDATE " + tblname + " SET " + descolname + "='" + desc + "' WHERE " + idcolname + "=" + id;
            System.out.println(sql);
            Statement sta = conn.createStatement();
            int s = sta.executeUpdate(sql);
            if (s == 1) {
                success = true;
            } else {
                success = false;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return success;
    }

    public boolean delete(String tblname, int id) {
        boolean success = false;

        try {
            //  System.out.println("Connection : "+ conn);
            String sql = "DELETE FROM " + tblname + " WHERE id=" + id;
            System.out.println(sql);
            Statement sta = conn.createStatement();
            int s = sta.executeUpdate(sql);
            if (s == 1) {
                success = true;
            } else {
                success = false;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return success;
    }

    public boolean deleteWithIdColname(String tblname, String idColname, int id) {
        boolean success = false;

        try {
            //  System.out.println("Connection : "+ conn);
            String sql = "DELETE FROM " + tblname + " WHERE " + idColname + "=" + id;
            System.out.println(sql);
            Statement sta = conn.createStatement();
            int s = sta.executeUpdate(sql);
            if (s == 1) {
                success = true;
            } else {
                success = false;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return success;
    }

    public boolean delete(String tblname, String colA, int idA, String colB, int idB) {
        boolean success = false;

        try {
            //  System.out.println("Connection : "+ conn);
            String sql = "DELETE FROM " + tblname + " WHERE " + colA + " = " + idA + " AND " + colB + " = " + idB;
            System.out.println(sql);
            Statement sta = conn.createStatement();
            int s = sta.executeUpdate(sql);
            if (s == 1) {
                success = true;
            } else {
                success = false;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return success;
    }

    public int manLibro(String titulo, String tblname) {
        int nextautoincrement = 0;
        String sql = "";
        //"SELECT AUTO_INCREMENT FROM information_schema.TABLES WHERE TABLE_SCHEMA = '"+dbname+"' AND TABLE_NAME = '"+tblname+"'";
        ResultSet rs = null;
        PreparedStatement ps;

        try {

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                nextautoincrement = rs.getInt("Auto_increment");
                System.out.println("num " + nextautoincrement);
            }

            //conn.close();
        } catch (SQLException e) {
            System.out.println(e);

        }

        return nextautoincrement;
    }

    public void tstLinkedList() {
        LinkedList llst = new LinkedList();
        String myname = "lacarte";
        int mycode = 10;

        llst.add(myname);
        llst.add(mycode);

        System.out.println("Size : ," + llst.size() + ", object" + llst);
        System.out.println(llst.get(1));

    }

}
