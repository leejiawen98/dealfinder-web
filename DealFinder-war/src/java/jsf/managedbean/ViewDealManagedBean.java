/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Deal;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.imageio.ImageIO;
import javax.inject.Named;
import javax.sql.DataSource;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author yeerouhew
 */
@Named(value = "viewDealManagedBean")
@SessionScoped
public class ViewDealManagedBean implements Serializable {

    @Resource(name = "dealFinder")
    private DataSource dealFinder;
    
    private Deal dealToView;

    private StreamedContent QR;
    private ByteArrayOutputStream rawQR;
    private ByteArrayInputStream is;
    
    private Deal selectedDeal;

    public ViewDealManagedBean() {
        dealToView = new Deal();
        QR = new DefaultStreamedContent();
        rawQR = new ByteArrayOutputStream();
    }
    
    public void generateQR(ActionEvent event) {
        selectedDeal = (Deal)event.getComponent().getAttributes().get("dealToDisplayQR");
//        rawQR = QRCode.from(selectedDeal.getSerialNum()).to(ImageType.PNG).stream();
        is = new ByteArrayInputStream(selectedDeal.getQrCode());
    }

    public void generateReport(ActionEvent event) throws IOException {
        try {
            HashMap parameters = new HashMap();
            parameters.put("Description", selectedDeal.getBusiness().getName());
            parameters.put("qrImage", is);

            InputStream reportStream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasperreportbusiness/qr_code_report.jasper");
            OutputStream outputStream = FacesContext.getCurrentInstance().getExternalContext().getResponseOutputStream();

            JasperRunManager.runReportToPdfStream(reportStream, outputStream, parameters, dealFinder.getConnection());
        } catch (JRException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
        }
    }
    
    public Deal getDealToView() {
        return dealToView;
    }

    public void setDealToView(Deal dealToView) {
        this.dealToView = dealToView;
    }
    
    public StreamedContent getQR(){
        QR = new DefaultStreamedContent(is, "image/png");
        return QR;
    }

    public void setQR(StreamedContent QR){
        this.QR = QR;
    }
    
    public Deal getSelectedDeal(){
        return selectedDeal;
    }
    
    public void setSelectedDeal(Deal selectedDeal){
        this.selectedDeal = selectedDeal;
    }
}
