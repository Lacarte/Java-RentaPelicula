 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import net.sf.jasperreports.web.servlets.ReportServlet;
import utilities.ConnectionManager;

/**
 *
 * @author LCRT
 */
public class Renta {

    private static Connection con = ConnectionManager.getInstance().getConnection();

    public Renta() {

    }

    public void generarReporte() throws JRException, net.sf.jasperreports.engine.JRException, SQLException {

        try {

            InputStream is = ReportServlet.class.getResourceAsStream("D:\\[LCRTDEV]\\[JAVA]\\NetBeansProjects\\RentaPelicula\\src\\report\\y.jrxml");

            JasperDesign jspd = JRXmlLoader.load(is);
            String sqlQuery = "SELECT pel.titpel AS 'TITULO',pel.durpel AS 'DURACION',gen.desgen AS 'GENERO',pel.anopel AS 'AÑOS',detren.preciopel AS 'PRECIO',detren.durren AS 'DIAS',detren.preciototal AS 'PRECIOTOTAL',DATE_FORMAT(ren.fecren + INTERVAL detren.durren DAY,'%d/%m/%Y') AS 'FECHA DEVOLUCION' FROM tbrenta ren INNER JOIN tb_detalle_renta detren ON detren.codren=ren.codren\n"
                    + "INNER JOIN tbpelicula_copia pelcop ON detren.codpel=pelcop.codpel AND detren.numcopia=pelcop.numcopia\n"
                    + "INNER JOIN tbpelicula pel ON pel.codpel=detren.codpel \n"
                    + "INNER JOIN tbgenero gen ON pel.codgen=gen.codgen";

            JRDesignQuery newQuery = new JRDesignQuery();
            newQuery.setText(sqlQuery);
            jspd.setQuery(newQuery);
            JasperReport reporteJasper = JasperCompileManager.compileReport(jspd);

            JasperPrint mostrarReporte = JasperFillManager.fillReport(reporteJasper, null, con);

            JasperViewer ver = new JasperViewer(mostrarReporte, false);
            ver.setTitle("Person car Report");
            ver.setVisible(true);

        } catch (JRException ex) {
            Logger.getLogger(Renta.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /////////////with param
    public void generarReporte(int idRenta) throws JRException, net.sf.jasperreports.engine.JRException, SQLException {

        try {

            // other wau to load file   
            try {
                InputStream Inputstream = new FileInputStream(new File("D:\\[LCRTDEV]\\[JAVA]\\NetBeansProjects\\RentaPelicula\\src\\report\\renta.jrxml"));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Renta.class.getName()).log(Level.SEVERE, null, ex);
            }

            // InputStream is = ReportServlet.class.getResourceAsStream("D:\\[LCRTDEV]\\[JAVA]\\NetBeansProjects\\RentaPelicula\\src\\report\\y.jrxml");
            JasperDesign jspd = JRXmlLoader.load("D:\\[LCRTDEV]\\[JAVA]\\NetBeansProjects\\RentaPelicula\\src\\report\\renta.jrxml");
            String sqlQuery = "SELECT pel.titpel AS 'TITULO',pel.durpel AS 'DURACION',gen.desgen AS 'GENERO',pel.anopel AS 'AÑOS',detren.preciopel AS 'PRECIO',detren.durren AS 'DIAS',detren.preciototal AS 'PRECIOTOTAL',DATE_FORMAT(ren.fecren + INTERVAL detren.durren DAY,'%d/%m/%Y') AS 'FECHA DEVOLUCION',ren.codren AS 'Nro Factura',ren.ncf as 'NCF',CONCAT(ter.nomter,' ',per.apeper) AS 'Nombre Cliente',usu.nomusu AS 'USUARIO' FROM tbrenta ren INNER JOIN tb_detalle_renta detren ON detren.codren=ren.codren\n"
                    + "INNER JOIN tbpelicula_copia pelcop ON detren.codpel=pelcop.codpel AND detren.numcopia=pelcop.numcopia\n"
                    + "INNER JOIN tbpelicula pel ON pel.codpel=detren.codpel \n"
                    + "INNER JOIN tbgenero gen ON pel.codgen=gen.codgen\n"
                    + "INNER JOIN tbtercero ter ON ter.codter=ren.codcli\n"
                    + "INNER JOIN tbpersona per ON  per.codper=ter.codter\n"
                    + "LEFT JOIN  tbusuario usu ON usu.codusu=ren.codusu\n"
                    + "WHERE ren.codren=" + idRenta;

            JRDesignQuery newQuery = new JRDesignQuery();
            newQuery.setText(sqlQuery);
            jspd.setQuery(newQuery);
            JasperReport reporteJasper = JasperCompileManager.compileReport(jspd);

            JasperPrint mostrarReporte = JasperFillManager.fillReport(reporteJasper, null, con);

            //JasperViewer ver = new JasperViewer(mostrarReporte, false); //false to prevent the main program to close
            //ver.setTitle("Mr movies Factura");
            //ver.setVisible(true);
            //if you dont want  to see the viewer
            //comment the 3 lines above
            ////////////print
            //to export it as pdf
            String pdfReportPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
            pdfReportPath = pdfReportPath.split("build")[0];
            System.out.println("<<>>" + pdfReportPath);

            String decodedPath;
            try {
                pdfReportPath = URLDecoder.decode(pdfReportPath, "UTF-8");
                System.out.println("> " + pdfReportPath);
            } catch (UnsupportedEncodingException ex) {
                JOptionPane.showMessageDialog(null, "decodedPath >>" + ex);
            }

            Date date = new Date();
            Format formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            String dateTimeName = formatter.format(date);

            
            try {

                //cuidado con src while build jar
                OutputStream outputstream = new FileOutputStream(new File(pdfReportPath + "/src/pdf/"+dateTimeName+".pdf"));

                JasperExportManager.exportReportToPdfStream(mostrarReporte, outputstream);

                //close the stream
                try {
                    outputstream.close();
                } catch (IOException ex) {
                    Logger.getLogger(Renta.class.getName()).log(Level.SEVERE, null, ex);
                }

            } catch (FileNotFoundException ex) {
                Logger.getLogger(Renta.class.getName()).log(Level.SEVERE, null, ex);
            }

            //print it
        /*    
            try {
                JasperPrintManager.printReport(mostrarReporte, false); //to print it 

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
                */
                

        } catch (JRException ex) {
            Logger.getLogger(Renta.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
