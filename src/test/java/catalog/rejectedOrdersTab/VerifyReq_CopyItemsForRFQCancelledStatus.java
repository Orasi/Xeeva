package catalog.rejectedOrdersTab;

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
import com.xeeva.catalog.CartInformationPage;
import com.xeeva.catalog.CostCenterPage;
import com.xeeva.catalog.ItemDetailsPage;
import com.xeeva.catalog.RequisitioningPage;
import com.xeeva.catalog.SearchItems.LocalItemsTab;
import com.xeeva.login.LoginPage;
import com.xeeva.navigation.MainNav;

/**
 * @Summary: To verify that requester is able to copy items, in case of RFQ - cancelled.
 * @author praveen varma, @version: Created 27-09-2016
 */
public class VerifyReq_CopyItemsForRFQCancelledStatus extends TestEnvironment{

		public String RequisitionType = "serviceRequestGeneral";
				
		// **************
		// Data Provider
		// **************
		@DataProvider(name = "dataScenario")
		public Object[][] scenarios() {
			try {
				Object[][] excelData = new ExcelDataProvider("/datasheets/VerifyReq_CopyRFQCancelledItems.xlsx",
						"CopyRFQCancelledItems").getTestData();
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
		public void copyItemsForRFQCancelledStatus(String role, String location,String unitPrice, String Quantity,
				String UOMValue){
			
			// Application Login 
			LoginPage loginPage = new LoginPage(getDriver());
			TestReporter.logStep("Launch the application and Login with valid Requestor credentials");
			loginPage.loginWithCredentials(role,location);
			
			// Requisition Page  - Navigating to requisition page and click on RejectedOrders tab.
			RequisitioningPage reqPage = new RequisitioningPage(getDriver());
			TestReporter.logStep("Navigate to Requisitioning Page and click on RejectedOrders tab.");
			reqPage.click_ReqTab();
			reqPage.clickRejectedOrdersTab();
			
			// Rejected Orders Page  - Navigating to Rejected Orders details page.
			// Copy RFQ Cancelled status item from Rejected orders table.
			TestReporter.logStep("Navigate to Rejected Orders details page.");
			TestReporter.logStep("Copy RFQ Cancelled status item from Rejected orders table.");
			reqPage.copyRFQCancelledItem();
			
			// Item Details Page  - Navigating to Item Details page.
			ItemDetailsPage itemDetailsPge = new ItemDetailsPage(getDriver());
			TestReporter.logStep("Navigating to Item Details page.");
			TestReporter.logStep("Add the Copied Rejected Order Item to Cart.");
			String CaptureditemNumber = itemDetailsPge.addCopiedItemToCart_RejectedOrders(unitPrice, Quantity, UOMValue);
			TestReporter.log("Captured Item Number: " + CaptureditemNumber);
			
			//Click on Cart-Items link and Navigate to Cart-Info page.
			LocalItemsTab localItemsPage = new LocalItemsTab(getDriver());
			TestReporter.logStep("Click on Cart-Items link and Navigate to Cart-Info page.");
			localItemsPage.clickCartItemsLink();
			
			//CartInformation page - Navigate to Cart-Info page.
			CartInformationPage cartInfoPge = new CartInformationPage(getDriver());
			TestReporter.logStep("Navigate to Cart-Information page and Verify added Cart-Item.");
			cartInfoPge.verifyAddedCartItem_ForRejectedOrder(CaptureditemNumber);
			
			//Close cart-Info page.
			TestReporter.logStep("Close Cart-Information page.");
			cartInfoPge.closeCartInfoPage();
			
			// Application Logout
			TestReporter.logStep("Application Logout");
			MainNav mainNav = new MainNav(getDriver());
			mainNav.clickLogout();
			
	 }
			
  }
