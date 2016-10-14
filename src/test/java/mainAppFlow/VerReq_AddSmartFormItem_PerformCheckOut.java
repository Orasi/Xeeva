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
import com.xeeva.catalog.CheckoutDetailPage;
import com.xeeva.catalog.ConfirmRequestPage;
import com.xeeva.catalog.CostCenterPage;
import com.xeeva.catalog.RequisitioningPage;
import com.xeeva.login.LoginPage;
import com.xeeva.navigation.MainNav;

/**
 * @Summary: To verify that requestor is able to add the smart form item and perform check out process.
 * @author Praveen Namburi, @version: Created 04-10-2016
 */
public class VerReq_AddSmartFormItem_PerformCheckOut extends TestEnvironment{

		public String RequisitionType = "serviceRequestGeneral";
		
		// **************
		// Data Provider
		// **************
		@DataProvider(name = "dataScenario")
		public Object[][] scenarios() {
			try {
				Object[][] excelData = new ExcelDataProvider("/datasheets/VerReq_AddSmartFormItem_PerformCheckOut.xlsx","AddSmartFormItem").getTestData();
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
		public void addSmartFormItem_PerformCheckOut(String role, String location,String ItemDescription,String UNSPSCCode,String SS,
				String CategoryType,String Category,String SubCategory,String MN,String MPN,
				String Quantity,String UnitofMeasure,String Price,String changeType,String selectCC){

			String[] QuantityArray = Quantity.split(";");
			String[] UOMArray = UnitofMeasure.split(";");
			String[] UPArray = Price.split(";");
			
			// Application Login 
			LoginPage loginPage = new LoginPage(getDriver());
			TestReporter.logStep("Launch the application and Login with valid Requestor credentials");
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
			TestReporter.logStep("Navigate to Check-Out details page and Verify "
					+ "the request confirmation details.");
			CheckoutDetailPage checkOutdetailsPage = new CheckoutDetailPage(driver);
			checkOutdetailsPage.requestConfirmation();
			
			//Application Logout
			TestReporter.logStep("Application Logout");
			mainNav.clickLogout();
			
	 }
		
}


