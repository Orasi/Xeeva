package catalog.cartCheckoutProcess;

import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.orasi.utils.Constants;
import com.orasi.utils.TestEnvironment;
import com.orasi.utils.TestReporter;
import com.orasi.utils.dataProviders.ExcelDataProvider;
import com.xeeva.catalog.CartInformationPage;
import com.xeeva.catalog.CheckoutDetailPage;
import com.xeeva.catalog.ConfirmRequestPage;
import com.xeeva.catalog.CostCenterPage;
import com.xeeva.catalog.RequisitioningPage;
import com.xeeva.catalog.SearchItems.LocalItemsTab;
import com.xeeva.login.LoginPage;
import com.xeeva.navigation.MainNav;

/**
 * @Summary: To verify the cart checkout process for same / different cost center request.
 * @author Praveen Namburi, @version: Created 03-10-2016
 */
public class VerReq_CartCheckOutWithSameOrDifferntCC extends TestEnvironment{

		public String RequisitionType = "serviceRequestGeneral";
		
		// **************
		// Data Provider
		// **************
		@DataProvider(name = "dataScenario")
		public Object[][] scenarios() {
			try {
				Object[][] excelData = new ExcelDataProvider("/datasheets/VerReq_CartCheckOutWithSameOrDifferntCC.xlsx",
						"CheckOutWithSameOrDifferntCC").getTestData();
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
		public void checkOutWithSameOrDifferntCC(String role, String location,String GlobalItem,
				String changeType,String selectCC){

			// Application Login 
			LoginPage loginPage = new LoginPage(getDriver());
			TestReporter.logStep("Launch the application and Login with valid Requestor credentials");
			loginPage.loginWithCredentials(role,location);
			
			// Requisition Page  - Navigating to requisition page.
			RequisitioningPage reqPage = new RequisitioningPage(getDriver());
			TestReporter.logStep("Navigate to Requisitioning Page.");
			reqPage.click_ReqTab();
			
			TestReporter.logStep("Navigating to MainNav Page");
			MainNav mainNav = new MainNav(getDriver());
			boolean getStatus = mainNav.verifyCartValue(GlobalItem);
			if(getStatus!=true){
				// Perform Catalog Search
				TestReporter.logStep("Perform Catalog Search");
				reqPage.perform_CatalogSearch(GlobalItem);

				// Clicking on GlobalItems Tab.
				TestReporter.logStep("Clicking on GlobalItems Tab");
				LocalItemsTab localItems = new LocalItemsTab(getDriver());
				localItems.click_localItemsTab();
				
				//Navigate to Local-Items page and Add-Item-To-Cart.
				LocalItemsTab localItemsPage = new LocalItemsTab(getDriver());
				TestReporter.logStep("Navigate to Local-Items page and Add-Item-To-Cart.");
				localItemsPage.addLocalItemToCartAndVerify();
				
			}

			// Perform Cart-CheckOut.
			TestReporter.logStep("Cart CheckOut");
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




