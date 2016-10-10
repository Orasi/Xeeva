package mainAppFlow;

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
import com.xeeva.navigation.MainNav;
import com.xeeva.quote.QuotePage;
import com.xeeva.review.ReviewPage;

/**
 * @summary Test To verify Approver is able to approve the REQ
 * @author  Lalitha Banda
 * @version	05/10/2016
 * *
 */

public class VerifyApproverPerformAction extends TestEnvironment{
	public String RequisitionType = "serviceRequestGeneral";

	// **************
	// Data Provider
	// **************
	@DataProvider(name = "dataScenario")
	public Object[][] scenarios() {
		try {
			Object[][] excelData = new ExcelDataProvider("/datasheets/approvalProcess.xlsx",
					"FinanceApprover").getTestData();
			return excelData;
		}
		catch (RuntimeException e){
			TestReporter.assertTrue(false, "An error occured with accessing the data provider: " + e);
		}
		return new Object[][] {{}};
	}

	@BeforeTest
	@Parameters({ "runLocation", "browserUnderTest", "browserVersion","operatingSystem", "environment" })
	public void setup(@Optional String runLocation, String browserUnderTest,String browserVersion, String operatingSystem, String environment) {
		setApplicationUnderTest("XEEVA");
		setBrowserUnderTest(browserUnderTest);
		setBrowserVersion(browserVersion);
		setOperatingSystem(operatingSystem);
		setRunLocation(runLocation);
		setTestEnvironment(environment);
		testStart("VerifyFinanceApproverApprovesRequest");
	}

	@AfterTest
	public void close(ITestContext testResults){
		//endTest("TestAlert", testResults);
	}

	@Test(dataProvider = "dataScenario")
	public void FinanceApproverApprovesRequest(String role, String location,String ItemDescription,String UNSPSCCode,String SS,
			String CategoryType,String Category,String SubCategory,String MN,String MPN,
			String Quantity,String UnitofMeasure,String Price,String changeType,String selectCC,String BuyerRole,String Taxtype,
			String TaxCode,String ItemName,String ExpectedMsg,String ExpectedStatus){

		// Reading DataProvider Sets
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

		// ReviewPage - Reviewing the RFQNumber
		TestReporter.logStep("Reviewing the RFQNumber");
		ReviewPage rPage = new ReviewPage(getDriver());
		rPage.reviewRFQ(RFQ_Number);

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
		TestReporter.logStep("Approver Role : " + approverRole);

		//Application Logout
		TestReporter.logStep("Application Logout");
		mainNav.clickLogout();

		TestReporter.logStep("********************************************************************************");
		TestReporter.logStep("Login as Approver to verify Verify Finance Approver Approves Request");
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
		approvalPage.performApprovalProcess();

		// Click Approval Tab 
		TestReporter.logStep("Clicking on Approval Tab");
		approvalPage.click_ApprovalsTab();

		// Perform RFQ search 
		TestReporter.logStep("RFQ Search");
		approvalPage.perform_RFQSearch(rfqNumber);

		//Application Logout
		TestReporter.logStep("Application Logout");
		mainNav.clickLogout();

	}

}
