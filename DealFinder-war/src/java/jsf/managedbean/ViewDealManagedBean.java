/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Deal;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author yeerouhew
 */
@Named(value = "viewDealManagedBean")
@SessionScoped
public class ViewDealManagedBean implements Serializable {

    private Deal dealToView;

    private StreamedContent QR;
    private ByteArrayOutputStream rawQR;
    
    private Deal selectedDeal;

    public ViewDealManagedBean() {
        dealToView = new Deal();
        QR = new DefaultStreamedContent();
        rawQR = new ByteArrayOutputStream();
    }
    
    public void generateQR(ActionEvent event) {
        selectedDeal = (Deal)event.getComponent().getAttributes().get("dealToDisplayQR");
        rawQR = QRCode.from(selectedDeal.getSerialNum()).to(ImageType.PNG).stream();
    }

    public Deal getDealToView() {
        return dealToView;
    }

    public void setDealToView(Deal dealToView) {
        this.dealToView = dealToView;
    }
    
    public StreamedContent getQR(){
        ByteArrayInputStream is = new ByteArrayInputStream(rawQR.toByteArray());
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
