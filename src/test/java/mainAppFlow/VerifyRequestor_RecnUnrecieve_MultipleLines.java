package mainAppFlow;

import java.text.ParseException;

import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.orasi.utils.TestEnvironment;
import com.orasi.utils.TestReporter;
import com.orasi.utils.dataProviders.ExcelDataProvider;
import com.xeeva.approvals.ApprovalsPage;
import com.xeeva.catalog.CheckoutDetailPage;
import com.xeeva.catalog.ConfirmRequestPage;
import com.xeeva.catalog.CostCenterPage;
import com.xeeva.catalog.RequisitioningPage;
import com.xeeva.login.LoginPage;
import com.xeeva.marketPlace.Supplier;
import com.xeeva.navigation.MainNav;
import com.xeeva.quote.QuotePage;
import com.xeeva.suppliercollaborator.SupplierCollaborator;

/**
 * @Summary: To verify Requistor Receive and Unreceive MultipleLines
 * @author Lalitha Banda, 
 * @version: Created 18-10-2016
 */
public class VerifyRequestor_RecnUnrecieve_MultipleLines extends TestEnvironment {

	public String RequisitionType = "serviceRequestGeneral";

	// **************
	// Data Provider
	// **************
	@DataProvider(name = "dataScenario")
	public Object[][] scenarios() {
		try {
			Object[][] excelData = new ExcelDataProvider("/datasheets/VerifyRequestor_RecnUnrecieve_MultipleLines.xlsx",
					"RecnUnrecieve_MultipleLines").getTestData();
			return excelData;
		}
		catch (RuntimeException e){
			TestReporter.assertTrue(false, "An error occured with accessing the data provider: " + e);
		}
		return new Object[][] {{}};
	}

	/**
	 * @Description: To initialize the driver and setup the environment.
	 * @param runLocation,browserUnderTest,@param browserVersion
	 * @param operatingSystem,@param environment
	 */
	@BeforeTest
	@Parameters({ "runLocation", "browserUnderTest", "browserVersion","operatingSystem", "environment" })
	public void setup(@Optional String runLocation, String browserUnderTest,String browserVersion, String operatingSystem, String environment) {
		setApplicationUnderTest("XEEVA");
		setBrowserUnderTest(browserUnderTest);
		setBrowserVersion(browserVersion);
		setOperatingSystem(operatingSystem);
		setRunLocation(runLocation);
		setTestEnvironment(environment);
		testStart("Add_PriceAgreementItem_InCart");
	}

	/**
	 * @Description: Close the driver instance.
	 * @param testResults
	 */
	@AfterTest
	public void close(ITestContext testResults){
		endTest("TestAlert", testResults);
	}

	/**
	 * @Description: Main business-logic of the test-case resides here.
	 * @param role,location
	 */
	@Test(dataProvider = "dataScenario")
	public void RecnUnrecieve_MultipleLines(String role, String location,String ItemDescription,String UNSPSCCode,String SS,
			String CategoryType,String Category,String SubCategory,String MN,String MPN,
			String Quantity,String UnitofMeasure,String Price,String changeType,String selectCC,String BuyerRole,String Taxtype,
			String TaxCode,String ItemName,String ExpectedMsg,String ExpectedStatus,String supplier,
			String SupplierRole,String price,String quantity,String leadTime,String frieghtID,String inputComments,String CurrentView,String ActiveStatusIndex,String ReceivingAgentRole,String PkgSlipNo)throws ParseException{


		String[] QuantityArray = Quantity.split(";");
		String[] UOMArray = UnitofMeasure.split(";");
		String[] UPArray = Price.split(";");
		String[] ExpMsg = Quantity.split(";");
		String[] ExpStatus = UnitofMeasure.split(";");

		TestReporter.logStep("********************************************************************************");
		TestReporter.logStep("Login as Requester to Perform Check out Process and Genarate REQ Number");
		TestReporter.logStep("********************************************************************************");

		// Application Login 
		TestReporter.logStep("Login into application");
		LoginPage loginPage = new LoginPage(getDriver());
		loginPage.loginWithCredentials(role,location);

		// Requisition Page  - Navigating to requisition page.
		RequisitioningPage reqPage = new RequisitioningPage(getDriver());
		TestReporter.logStep("Navigate to Requisitioning Page to click on Requisition link");
		reqPage.click_ReqTab();

		// Create Smart Form Request.
		TestReporter.logStep("Create Smart Form Request.");
		reqPage.createSmartFormRequest( RequisitionType,ItemDescription, UNSPSCCode,SS,CategoryType, Category, SubCategory,MN,MPN, 
				QuantityArray[0], UOMArray[0],UPArray[0]);

		// Perform Cart-CheckOut.
		TestReporter.logStep("Cart CheckOut");
		MainNav mainNav = new MainNav(getDriver());
		mainNav.cart_CheckOut();

		// Navigate to Cost-Center page.
		// Select the CC value at Header level and Continue check-out.
		TestReporter.logStep("Navigate to Cost-Center page.");
		CostCenterPage ccPage = new CostCenterPage(getDriver());
		TestReporter.logStep("Select the CC value at Header level and Continue check-out.");
		ccPage.selectCCValueAtHeaderLevel(selectCC);

		// Confirm the request.
		TestReporter.logStep("Confirm the request.");
		ConfirmRequestPage crPage = new ConfirmRequestPage(getDriver());
		crPage.clickbtnConfirm();

		// Navigate to Check-Out details page and Verify the request confirmation details.
		TestReporter.logStep("Navigate to Check-Out details page and Verify the request confirmation details.");
		CheckoutDetailPage checkOutdetailsPage = new CheckoutDetailPage(driver);
		checkOutdetailsPage.requestConfirmation();

		// Read REQ Number from Check Out Details Page
		TestReporter.logStep("Reading REQ Number From Check Out details Page");
		String RFQ_Number = checkOutdetailsPage.readRFQNumber();
		TestReporter.logStep("REQ Number : " +RFQ_Number);

		//Application Logout
		TestReporter.logStep("Application Logout");
		mainNav.clickLogout();

		TestReporter.logStep("********************************************************************************");
		TestReporter.logStep("Login as Buyer to Perform 'Submit for Pre Approval' Process");
		TestReporter.logStep("********************************************************************************");

		// Application Login 
		TestReporter.logStep("Login as Buyer");
		loginPage.loginWithCredentials(BuyerRole,location);

		// QuotePage - Clicking on QuoteTab 
		TestReporter.logStep("Clicking on QuoteTab");
		QuotePage qPage = new QuotePage(getDriver());
		qPage.click_quoteTab();

		// QuotePage - Performing Requisition process 
		TestReporter.logStep("Performing Requisition process");
		qPage.perform_Requisition(RFQ_Number);

		// QuotePage - Performing File Attachment Process 
		TestReporter.logStep("Performing File Attachment Process");
		qPage.SubmitRFQ(RFQ_Number);

		//Reading Approver Role
		TestReporter.logStep("Reading Approver Role");
		qPage.enter_RFQNumber(RFQ_Number);

		String approverRole = qPage.getApproverEmail();
		TestReporter.logStep("Pre Approver Role : " + approverRole);

		//Application Logout
		TestReporter.logStep("Application Logout");
		mainNav.clickLogout();

		TestReporter.logStep("********************************************************************************");
		TestReporter.logStep("Login as Approver to  pre-Approve RFQ ");
		TestReporter.logStep("********************************************************************************");

		// Application Login 
		TestReporter.logStep("Login into application");
		loginPage.loginWithRuntimeUsername(approverRole.trim(),location);

		// Navigating to Approvals Page
		TestReporter.logStep("Clicking the GlobalItems Link");
		ApprovalsPage approvalPage = new ApprovalsPage(getDriver());

		// Reading Available Row for Approval Process
		int selectedRow = approvalPage.selectOrderToApprove();
		TestReporter.logStep("Row Number : "+selectedRow);

		// Reading RFQ number using selected Row 
		String rfqNumber = approvalPage.ReadRFQNumber();
		TestReporter.logStep("RFQ Number : "+rfqNumber);

		// Verify Hold Functionality 
		TestReporter.logStep("Verifing REQ - HOLD");
		approvalPage.clickonHoldAndValidate(selectedRow,ExpMsg[0],ExpStatus[0]);

		// Verify UnHold Functionality
		TestReporter.logStep("Verifing REQ - UNHOLD");
		approvalPage.clickonHoldAndValidate(selectedRow,ExpMsg[1],ExpStatus[1]);

		// Perform Approval Process
		TestReporter.logStep("Perform Approval Process");
		approvalPage.performApprovalProcess(rfqNumber);

		// Click Approval Tab 
		TestReporter.logStep("Clicking on Approval Tab");
		approvalPage.click_ApprovalsTab();

		// Perform RFQ search 
		TestReporter.logStep("RFQ Search");
		approvalPage.perform_RFQSearch(rfqNumber);

		//Reading RFQ Status
		String getStatus  = approvalPage.read_RFQStatus(rfqNumber);
		TestReporter.logStep("RFQ Status  : "+getStatus);

		//Application Logout
		TestReporter.logStep("Application Logout");
		mainNav.clickLogout();

		TestReporter.logStep("********************************************************************************");
		TestReporter.logStep("Login as Buyer and submit RFQ to Suppliers.");
		TestReporter.logStep("********************************************************************************");

		// Application Login 
		TestReporter.logStep("Launch the application and Login with valid 'BUYER' credentials.");
		loginPage.loginWithCredentials(BuyerRole,location);

		// Navigating to RFQ page and click on Quote link.
		QuotePage quotePage = new QuotePage(getDriver());
		TestReporter.logStep("Navigate to Requisitioning page and click on Quote link.");
		quotePage.click_quoteTab();

		// Quote Page - Filter and Edit RFQ with Draft Status.
		TestReporter.logStep(" Navigate to Quotepage - Filter and Edit RFQ with Draft Status.");
		quotePage.filterAndEditRFQ_withDraftStatus(RFQ_Number);

		// Update and Save - 'RFQ Information' and Continue.
		TestReporter.logStep("Update and Save - 'RFQ Information' and Continue.");
		quotePage.setRFQInfoAndContinue(UOMArray[1], QuantityArray[0]);

		// Select and add supplier to the list.
		TestReporter.logStep("Select and add supplier to the list.");
		quotePage.selectSupplier(supplier);

		// Submit and send RFQ to suppliers.
		TestReporter.logStep("Submit and send RFQ to suppliers.");
		quotePage.clickSubmit();
		quotePage.clickSendRFQ();

		// verify_RFQStatus_AfterSubmittingRFQ
		TestReporter.logStep("Verifying whether the RFQStatus is changed to Active.");
		quotePage.verify_RFQStatus_AfterSubmittingRFQ(RFQ_Number);

		//Application Logout
		TestReporter.logStep("Application Logout");
		mainNav.clickLogout();

		// Closing current Application Driver
		driver.close();

		TestReporter.logStep("********************************************************************************");
		TestReporter.logStep("Login as Supplier to submit RFQ Response ");
		TestReporter.logStep("********************************************************************************");

		setApplicationUnderTest("XEEVAMKT");
		setBrowserUnderTest(browserUnderTest);
		setBrowserVersion(browserVersion);
		setOperatingSystem(operatingSystem);
		setRunLocation(runLocation);
		setTestEnvironment(environment);
		testStart("VerifyBuyerAwards_OR_RecommendsRFQ");

		// Application Login  - Market Place
		TestReporter.logStep("Login into Market Place application");
		LoginPage loginPageMp = new LoginPage(getDriver());
		loginPageMp.loginWithSupplierCredentials(SupplierRole);

		// Clicking on OpportunitiesTab 
		TestReporter.logStep("Clicking on OpportunitiesTab");
		Supplier sPage = new Supplier(getDriver());
		sPage.click_OpportunitiesTab();

		// Performing the RFQ Search functionality 
		TestReporter.logStep("Performing the RFQ Search functionality in OpportunitiesTab ");
		sPage.perform_RFQSearch(RFQ_Number);

		// Performing the Submit Response functionality
		TestReporter.logStep("Performing the Submit Response functionality in OpportunitiesTab ");
		sPage.submit_Response(price, quantity, leadTime, frieghtID);

		// SupplierPage - Supplier Application Logout
		TestReporter.logStep("Application Logout");
		sPage.supplierLogout();

		// Closing current Application Driver
		driver.close();

		TestReporter.logStep("********************************************************************************");
		TestReporter.logStep("Login as Buyer to Award/Recommends  RFQ ");
		TestReporter.logStep("********************************************************************************");

		setApplicationUnderTest("XEEVA");
		setBrowserUnderTest(browserUnderTest);
		setBrowserVersion(browserVersion);
		setOperatingSystem(operatingSystem);
		setRunLocation(runLocation);
		setTestEnvironment(environment);
		testStart("VerifyBuyerAwards_OR_RecommendsRFQ");

		// Application Login 
		TestReporter.logStep("Login into application");
		LoginPage loginPage1 = new LoginPage(getDriver());
		loginPage1.loginWithCredentials(BuyerRole,location);

		// QuotePage - Clicking on QuoteTab 
		TestReporter.logStep("Clicking on QuoteTab");
		QuotePage qPage1 = new QuotePage(getDriver());
		qPage1.click_quoteTab();

		// Performing Awards/Recommends RFQ
		TestReporter.logStep("Perform Award RFQ");
		qPage1.processAddComments(CurrentView,RFQ_Number,inputComments,ActiveStatusIndex);

		// Read RFQ status 
		TestReporter.logStep("Read RFQ status");
		String getStatus1 = qPage1.readStatus_RFQ(RFQ_Number);
		TestReporter.logStep("RFQ status"+getStatus1);

		//Application Logout
		TestReporter.logStep("Application Logout");
		MainNav mainPage = new MainNav(getDriver());
		mainPage.clickLogout();

		TestReporter.logStep("********************************************************************************");
		TestReporter.logStep("Login as approver to Approve RFQ ");
		TestReporter.logStep("********************************************************************************");

		// Application Login 
		TestReporter.logStep("Login into application");
		TestReporter.logStep("Approvals Role : "+approverRole);
		LoginPage loginPage2 = new LoginPage(getDriver());
		loginPage2.loginWithRuntimeUsername(approverRole.trim(),location);

		// Navigating to Approvals Page
		TestReporter.logStep("Clicking the Approvals Tab");
		ApprovalsPage approvalPage1 = new ApprovalsPage(getDriver());
		approvalPage1.click_ApprovalsSubTab();

		// Reading Available Row for Approval Process
		int selectedRow1 = approvalPage1.selectOrderToApprove();
		TestReporter.logStep("Row Number : "+selectedRow1);

		// Perform Approval Process
		TestReporter.logStep("Perform Approval Process");
		approvalPage1.performApprovalProcess(RFQ_Number);

		// Click Approval Tab 
		TestReporter.logStep("Clicking on Approval Tab");
		approvalPage1.click_ApprovalsTab();
		approvalPage1.click_ApprovalsSubTab();

		// Perform RFQ search 
		TestReporter.logStep("RFQ Search");
		approvalPage1.perform_RFQSearch(rfqNumber);

		//Reading RFQ Status
		String getStatus2  = approvalPage1.read_RFQStatus_ApprovalProcess(rfqNumber);
		TestReporter.logStep("RFQ Status  : "+getStatus2);

		//Application Logout
		TestReporter.logStep("Application Logout");
		mainPage.clickLogout();

		TestReporter.logStep("********************************************************************************");
		TestReporter.logStep("Login as Requestor Receiving Agent to receive / Unreceive ");
		TestReporter.logStep("********************************************************************************");

		// Application Login 
		TestReporter.logStep("Login into application");
		loginPage1.loginWithCredentials(ReceivingAgentRole,location);

		// Supplier Collaborator Page - Clicking on SupplierCollaboratorTab 
		TestReporter.logStep("Clicking on SupplierCollaboratorTab");
		SupplierCollaborator sCPage = new SupplierCollaborator(getDriver());
		sCPage.click_SupplierCollaboratorTab();

		// Supplier Collaborator Page - Perform Search
		TestReporter.logStep("Perform Search");
		sCPage.performSearch();

		// Supplier Collaborator Page - Select ReceiveMultiplePOs
		TestReporter.logStep("Select ReceiveMultiplePOs");
		sCPage.select_ReceiveMultiplePOs();

		// Supplier Collaborator Page - Performing Receive Purchase Order
		TestReporter.logStep("Performing Receive Purchase Order");
		sCPage.perform_RecPurchaseOrder(PkgSlipNo);
		
		//Get PO_Number.
		String PO_Number = sCPage.getPONumber();
		TestReporter.log("Supplier Role : " + PO_Number);

		// Supplier Collaborator Page - Searching with PO Number
		TestReporter.logStep("Searching with PO Number");
		sCPage.search_PONumber(PO_Number);
		
		//Reading Supplier Role
		TestReporter.logStep("Reading Supplier Role");
		String Supplier_Role = sCPage.getSupplierEmail();
		TestReporter.logStep("Supplier Role : " + Supplier_Role);

		//Application Logout
		TestReporter.logStep("Application Logout");
		mainPage.clickLogout();
	}

}
