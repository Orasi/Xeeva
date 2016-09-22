package catalog.cartCheckoutProcess;

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
import com.xeeva.catalog.CostCenterPage;
import com.xeeva.catalog.ItemDetailsPage;
import com.xeeva.catalog.RequisitioningPage;
import com.xeeva.catalog.SearchItems.GlobalItemsTab;
import com.xeeva.login.LoginPage;
import com.xeeva.navigation.MainNav;

/**
 * @Summary: To verify that requester is able to change the require by for a existing line level requisition. 
 * @author praveen varma, @version: Created 21-09-2016
 */
public class VerifyRequester_changeRequire_ExistingLineLevel extends TestEnvironment {

	 public String RequisitionType = "serviceRequestGeneral";
		
		// **************
		// Data Provider
		// **************
		@DataProvider(name = "dataScenario")
		public Object[][] scenarios() {
			try {
				Object[][] excelData = new ExcelDataProvider("/datasheets/VerifyRequestor_changeRequire_ExistingLineLevel.xlsx",
						"VerifyReq_changeReq_ExiLineLevl").getTestData();
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
		public void changeRequire_ExistingLineLevel(String role, String location,String GlobalItem,String UnitPrice,
				String Quantity,String UnitofMeasure,String daysOut){
			
			// Application Login 
			LoginPage loginPage = new LoginPage(getDriver());
			TestReporter.logStep("Launch the application and Login with valid Requestor credentials");
			loginPage.loginWithCredentials(role,location);
			
			// Requisition Page  - Navigating to requisition page to create Smart Form Request
			RequisitioningPage reqPage = new RequisitioningPage(getDriver());
			TestReporter.logStep("Navigate to Requisitioning Page.");
			reqPage.click_ReqTab();
			
			
			TestReporter.logStep("Navigating to MainNav Page");
			MainNav mainNav = new MainNav(getDriver());
			boolean getStatus = mainNav.verifyCartValue(GlobalItem);
			if(getStatus!=true){
				TestReporter.logStep("Clicking the GlobalItems Link");
				reqPage.perform_CatalogSearch(GlobalItem);

				TestReporter.logStep("Clicking the GlobalItems Link");
				GlobalItemsTab globalitems = new GlobalItemsTab(getDriver());
				globalitems.click_GlobalItemsTab();

				TestReporter.logStep("ItemDetailsPage  - Modifing Item Details");
				ItemDetailsPage itemdetails = new ItemDetailsPage(getDriver());
				itemdetails.add_TwoDiffrent_ItemsToCart(UnitPrice,Quantity,UnitofMeasure);
			}

			TestReporter.logStep("Cart CheckOut");
			mainNav.cart_CheckOut();

			CostCenterPage ccPage = new CostCenterPage(getDriver());
			ccPage.change_RequiredByAtLineLevel(daysOut);

			// Application Logout
			TestReporter.logStep("Application Logout");
			mainNav.clickLogout();
			
		}
		
   }
