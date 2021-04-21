package jsf.managedbean;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import ejb.session.stateless.DealSessionBeanLocal;
import ejb.session.stateless.RedemptionSessionBeanLocal;
import ejb.session.stateless.SaleTransactionSessionBeanLocal;
import entity.Business;
import entity.Deal;
import entity.SaleTransaction;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.DashboardReorderEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.hbar.HorizontalBarChartDataSet;
import org.primefaces.model.charts.hbar.HorizontalBarChartModel;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;
import util.exception.BusinessNotFoundException;
import util.exception.DealNotFoundException;
import java.time.LocalDate;

/**
 *
 * @author yeerouhew
 */
@Named(value = "dashboardManagedBean")
@RequestScoped
public class dashboardManagedBean implements Serializable {

    @EJB
    private SaleTransactionSessionBeanLocal saleTransactionSessionBeanLocal;
    @EJB
    private RedemptionSessionBeanLocal redemptionSessionBeanLocal;
    @EJB
    private DealSessionBeanLocal dealSessionBeanLocal;

    @Resource(name = "dealFinder")
    private DataSource dealFinder;

    private Business business;

    private StreamedContent QR;
    private ByteArrayOutputStream rawQR;
    private ByteArrayInputStream is;

    private DashboardModel model;
    private HorizontalBarChartModel hbarModel;

    private PieChartModel pieModel;

    private Long dealId;
    private Long finDealId;
    private List<Deal> dealsThatBusinessHave;
    private List<SaleTransaction> saleTransactionThatBusinessHave;
    private Integer selectedYear;
//    private List<Integer> theYearsSinceTheFirstTransaction;

    @PostConstruct
    public void postConstruct() {
        model = new DefaultDashboardModel();
        DashboardColumn column1 = new DefaultDashboardColumn();
        DashboardColumn column2 = new DefaultDashboardColumn();

        column1.addWidget("qrcode");
        column1.addWidget("redemption");

        column2.addWidget("finance");

        model.addColumn(column1);
        model.addColumn(column2);

        business = (Business) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
        is = new ByteArrayInputStream(business.getQrCode());

        dealsThatBusinessHave = dealSessionBeanLocal.retrieveDealByBusinessId(business.getId());
//            saleTransactionThatBusinessHave = saleTransactionSessionBeanLocal.retrieveSaleTransactionDealByBusinessId(business.getId());
//
//            if (!saleTransactionThatBusinessHave.isEmpty()) {
//                for(SaleTransaction st:saleTransactionThatBusinessHave){
//                    Integer year = st.getTransactionDateTime().getYear();
//                    theYearsSinceTheFirstTransaction.add(year);
//                }
//            }

        createHorizontalBarModel();
        createPieModel();

    }

    public dashboardManagedBean() {
        QR = new DefaultStreamedContent();
        rawQR = new ByteArrayOutputStream();
        pieModel = new PieChartModel();
    }

    public void generateReport(ActionEvent event) throws IOException {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            HashMap parameters = new HashMap();

            parameters.put("businessId", business.getId());
            parameters.put("Description", business.getName());
            parameters.put("qrImage", is);
            String relativePath = "./resources/images/";
            parameters.put("CONTEXT", facesContext.getExternalContext().getRealPath(relativePath) + "/logo.png");

            
            InputStream reportStream = facesContext.getExternalContext().getResourceAsStream("/jasperreportbusiness/biz_qr_code_report.jasper");
            OutputStream outputStream = facesContext.getExternalContext().getResponseOutputStream();
            facesContext.responseComplete();
            JasperRunManager.runReportToPdfStream(reportStream, outputStream, parameters, dealFinder.getConnection());

            outputStream.flush();
            outputStream.close();
        } catch (JRException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
        }
    }

    public void createHorizontalBarModel() {
        try {
            hbarModel = new HorizontalBarChartModel();
            ChartData data = new ChartData();

            HorizontalBarChartDataSet hbarDataSet = new HorizontalBarChartDataSet();
            hbarDataSet.setLabel("Sale Transaction by Month");

            List<Number> values = new ArrayList<>();
            List<String> labels = new ArrayList<>();

            HashMap<Integer, Double> map = new HashMap<>();
            List<SaleTransaction> sts = saleTransactionSessionBeanLocal.retrieveSaleTransactionDealByBusinessId(business.getId());
            List<String> months = new ArrayList<>();
            months.add("January");
            months.add("February");
            months.add("March");
            months.add("April");
            months.add("May");
            months.add("June");
            months.add("July");
            months.add("August");
            months.add("September");
            months.add("October");
            months.add("November");
            months.add("December");

            Double totalAmt = 0.00;

            for (int i = 0; i < 12; i++) {
                List<SaleTransaction> stsByMonth = saleTransactionSessionBeanLocal.retrieveSaleTransactionDealByBusinessAndMonth(business.getId(), months.get(i));

                for (SaleTransaction stByM : stsByMonth) {
                    totalAmt = totalAmt + stByM.getTotalAmount().doubleValue();
                }

                values.add(totalAmt);
                labels.add(months.get(i));
                totalAmt = 0.00;
            }

            hbarDataSet.setData(values);
            data.setLabels(labels);

            List<String> bgColor = new ArrayList<>();
            bgColor.add("rgba(255, 99, 132, 0.2)");
            bgColor.add("rgba(255, 159, 64, 0.2)");
            bgColor.add("rgba(255, 205, 86, 0.2)");
            bgColor.add("rgba(75, 192, 192, 0.2)");
            bgColor.add("rgba(54, 162, 235, 0.2)");
            bgColor.add("rgba(153, 102, 255, 0.2)");
            bgColor.add("rgba(201, 203, 207, 0.2)");
            bgColor.add("rgba(139, 69, 19, 0.2)");
            bgColor.add("rgba(165, 42, 42, 0.2)");
            bgColor.add("rgba(0, 139, 139, 0.2)");
            bgColor.add("rgba(100, 149, 237, 0.2)");
            bgColor.add("rgba(128, 0, 128, 0.2)");
            hbarDataSet.setBackgroundColor(bgColor);

            List<String> borderColor = new ArrayList<>();
            borderColor.add("rgb(255, 99, 132)");
            borderColor.add("rgb(255, 159, 64)");
            borderColor.add("rgb(255, 205, 86)");
            borderColor.add("rgb(75, 192, 192)");
            borderColor.add("rgb(54, 162, 235)");
            borderColor.add("rgb(153, 102, 255)");
            borderColor.add("rgb(201, 203, 207)");
            borderColor.add("rgb(139, 69, 19)");
            borderColor.add("rgb(165, 42, 42)");
            borderColor.add("rgb(0, 139, 139)");
            borderColor.add("rgb(100, 149, 237)");
            borderColor.add("rgb(128, 0, 128)");
            hbarDataSet.setBorderColor(borderColor);
            hbarDataSet.setBorderWidth(1);

            data.addChartDataSet(hbarDataSet);

            hbarModel.setData(data);

            //Options
            BarChartOptions options = new BarChartOptions();
            CartesianScales cScales = new CartesianScales();
            CartesianLinearAxes linearAxes = new CartesianLinearAxes();
            linearAxes.setOffset(true);
            CartesianLinearTicks ticks = new CartesianLinearTicks();
            ticks.setBeginAtZero(true);
            linearAxes.setTicks(ticks);
            cScales.addXAxesData(linearAxes);
            options.setScales(cScales);

            Title title = new Title();
            title.setDisplay(true);
            title.setText("Sales Transaction of the Month");
            options.setTitle(title);

            hbarModel.setOptions(options);

        } catch (BusinessNotFoundException ex) {
            FacesMessage message = new FacesMessage();
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            message.setSummary("Business not provided");
            addMessage(message);
        }
    }

    public void createOnChangeHorizontalBarModel(ActionEvent event) {
        try {
            hbarModel = new HorizontalBarChartModel();
            ChartData data = new ChartData();

            HorizontalBarChartDataSet hbarDataSet = new HorizontalBarChartDataSet();
            hbarDataSet.setLabel("Sale Transaction by Month");

            List<Number> values = new ArrayList<>();
            List<String> labels = new ArrayList<>();
            List<String> months = new ArrayList<>();
            months.add("January");
            months.add("February");
            months.add("March");
            months.add("April");
            months.add("May");
            months.add("June");
            months.add("July");
            months.add("August");
            months.add("September");
            months.add("October");
            months.add("November");
            months.add("December");

            Long selectedDealId = finDealId;

            Double totalAmt = 0.00;

            for (int i = 0; i < 12; i++) {
                List<SaleTransaction> stsByMonth = saleTransactionSessionBeanLocal.retrieveSaleTransactionDealByBusinessAndMonthndDeal(business.getId(), months.get(i), selectedDealId);

                for (SaleTransaction stByM : stsByMonth) {
                    totalAmt = totalAmt + stByM.getTotalAmount().doubleValue();
                }

                values.add(totalAmt);
                labels.add(months.get(i));
                totalAmt = 0.00;
            }

            hbarDataSet.setData(values);
            data.setLabels(labels);

            List<String> bgColor = new ArrayList<>();
            bgColor.add("rgba(255, 99, 132, 0.2)");
            bgColor.add("rgba(255, 159, 64, 0.2)");
            bgColor.add("rgba(255, 205, 86, 0.2)");
            bgColor.add("rgba(75, 192, 192, 0.2)");
            bgColor.add("rgba(54, 162, 235, 0.2)");
            bgColor.add("rgba(153, 102, 255, 0.2)");
            bgColor.add("rgba(201, 203, 207, 0.2)");
            bgColor.add("rgba(139, 69, 19, 0.2)");
            bgColor.add("rgba(165, 42, 42, 0.2)");
            bgColor.add("rgba(0, 139, 139, 0.2)");
            bgColor.add("rgba(100, 149, 237, 0.2)");
            bgColor.add("rgba(128, 0, 128, 0.2)");
            hbarDataSet.setBackgroundColor(bgColor);

            List<String> borderColor = new ArrayList<>();
            borderColor.add("rgb(255, 99, 132)");
            borderColor.add("rgb(255, 159, 64)");
            borderColor.add("rgb(255, 205, 86)");
            borderColor.add("rgb(75, 192, 192)");
            borderColor.add("rgb(54, 162, 235)");
            borderColor.add("rgb(153, 102, 255)");
            borderColor.add("rgb(201, 203, 207)");
            borderColor.add("rgb(139, 69, 19)");
            borderColor.add("rgb(165, 42, 42)");
            borderColor.add("rgb(0, 139, 139)");
            borderColor.add("rgb(100, 149, 237)");
            borderColor.add("rgb(128, 0, 128)");
            hbarDataSet.setBorderColor(borderColor);
            hbarDataSet.setBorderWidth(1);

            data.addChartDataSet(hbarDataSet);

            hbarModel.setData(data);

            //Options
            BarChartOptions options = new BarChartOptions();
            CartesianScales cScales = new CartesianScales();
            CartesianLinearAxes linearAxes = new CartesianLinearAxes();
            linearAxes.setOffset(true);
            CartesianLinearTicks ticks = new CartesianLinearTicks();
            ticks.setBeginAtZero(true);
            linearAxes.setTicks(ticks);
            cScales.addXAxesData(linearAxes);
            options.setScales(cScales);

            Title title = new Title();
            title.setDisplay(true);
            title.setText("Sales Transaction of the Month");
            options.setTitle(title);

            hbarModel.setOptions(options);

        } catch (BusinessNotFoundException ex) {
            FacesMessage message = new FacesMessage();
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            message.setSummary("Business not provided");
            addMessage(message);
        }
    }

    public void createPieModel() {
        try {
            //retrieve redeemed and not redeemed customer
            if (!business.getDeals().isEmpty()) {
                Integer numberOfRedemption = redemptionSessionBeanLocal.countTheNumberOfRedemptionByDealId(1l);
                Integer numberOfDealsNotRedeemed = dealSessionBeanLocal.retrieveDealByDealId(1l).getOriginalQuantity() - numberOfRedemption;

                pieModel = new PieChartModel();
                ChartData data = new ChartData();

                PieChartDataSet dataSet = new PieChartDataSet();
                List<Number> values = new ArrayList<>();
                values.add(numberOfRedemption);
                values.add(numberOfDealsNotRedeemed);
                dataSet.setData(values);

                List<String> bgColors = new ArrayList<>();
                bgColors.add("rgba(255, 205, 86, 0.2)");
                bgColors.add("rgba(75, 192, 192, 0.2)");
                dataSet.setBackgroundColor(bgColors);

                List<String> borderColor = new ArrayList<>();
                borderColor.add("rgb(255, 205, 86)");
                borderColor.add("rgb(75, 192, 192)");
                dataSet.setBorderColor(borderColor);

                data.addChartDataSet(dataSet);
                List<String> labels = new ArrayList<>();
                labels.add("Redeemed");
                labels.add("Not Redeemed");
                data.setLabels(labels);

                pieModel.setData(data);
            } else {
                FacesMessage message = new FacesMessage();
                message.setSeverity(FacesMessage.SEVERITY_INFO);
                message.setSummary("You do not have any deal yet.");
                addMessage(message);
            }
        } catch (DealNotFoundException ex) {
            FacesMessage message = new FacesMessage();
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            message.setSummary("Deal not provided");
            addMessage(message);
        }
    }

    public void createOnChangePieModel(ActionEvent event) {
        Long selectedDealId = dealId;

        try {
            pieModel = new PieChartModel();
            //retrieve redeemed and not redeemed customer
            Integer numberOfRedemption = redemptionSessionBeanLocal.countTheNumberOfRedemptionByDealId(dealId);
            Integer numberOfDealsNotRedeemed = dealSessionBeanLocal.retrieveDealByDealId(dealId).getOriginalQuantity() - numberOfRedemption;

            ChartData data = new ChartData();

            PieChartDataSet dataSet = new PieChartDataSet();
            List<Number> values = new ArrayList<>();
            values.add(numberOfRedemption);
            values.add(numberOfDealsNotRedeemed);
            dataSet.setData(values);

            List<String> bgColors = new ArrayList<>();
            bgColors.add("rgba(255, 205, 86, 0.2)");
            bgColors.add("rgba(75, 192, 192, 0.2)");
            dataSet.setBackgroundColor(bgColors);

            List<String> borderColor = new ArrayList<>();
            borderColor.add("rgb(255, 205, 86)");
            borderColor.add("rgb(75, 192, 192)");
            dataSet.setBorderColor(borderColor);

            data.addChartDataSet(dataSet);
            List<String> labels = new ArrayList<>();
            labels.add("Redeemed");
            labels.add("Not Redeemed");
            data.setLabels(labels);

            pieModel.setData(data);
        } catch (DealNotFoundException ex) {
            createPieModel();
        }
    }

    public void handleReorder(DashboardReorderEvent event) {
        FacesMessage message = new FacesMessage();
        message.setSeverity(FacesMessage.SEVERITY_INFO);
        message.setSummary("You have reordered the items ");

        addMessage(message);
    }

    public void handleClose(CloseEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Panel Closed", "Closed panel id:'" + event.getComponent().getId() + "'");

        addMessage(message);
    }

    public void handleToggle(ToggleEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, event.getComponent().getId() + " toggled", "Status:" + event.getVisibility().name());

        addMessage(message);
    }

    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public DashboardModel getModel() {
        return model;
    }

    public StreamedContent getQR() {
        QR = new DefaultStreamedContent(is, "image/png");
        return QR;
    }

    public void setQR(StreamedContent QR) {
        this.QR = QR;
    }

    public HorizontalBarChartModel getHbarModel() {
        return hbarModel;
    }

    public void setHbarModel(HorizontalBarChartModel hbarModel) {
        this.hbarModel = hbarModel;
    }

    public PieChartModel getPieModel() {
        return pieModel;
    }

    public void setPieModel(PieChartModel pieModel) {
        this.pieModel = pieModel;
    }

    public Long getDealId() {
        return dealId;
    }

    public void setDealId(Long dealId) {
        this.dealId = dealId;
    }

    public List<Deal> getDealsThatBusinessHave() {
        return dealsThatBusinessHave;
    }

    public void setDealsThatBusinessHave(List<Deal> dealsThatBusinessHave) {
        this.dealsThatBusinessHave = dealsThatBusinessHave;
    }

//    public List<Integer> getTheYearsSinceTheFirstTransaction() {
//        return theYearsSinceTheFirstTransaction;
//    }
//
//    public void setTheYearsSinceTheFirstTransaction(List<Integer> theYearsSinceTheFirstTransaction) {
//        this.theYearsSinceTheFirstTransaction = theYearsSinceTheFirstTransaction;
//    }
//
//    public Integer getSelectedYear() {
//        return selectedYear;
//    }
//
//    public void setSelectedYear(Integer selectedYear) {
//        this.selectedYear = selectedYear;
//    }
    public List<SaleTransaction> getSaleTransactionThatBusinessHave() {
        return saleTransactionThatBusinessHave;
    }

    public void setSaleTransactionThatBusinessHave(List<SaleTransaction> saleTransactionThatBusinessHave) {
        this.saleTransactionThatBusinessHave = saleTransactionThatBusinessHave;
    }

    public Long getFinDealId() {
        return finDealId;
    }

    public void setFinDealId(Long finDealId) {
        this.finDealId = finDealId;
    }

}
