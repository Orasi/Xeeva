package catalog;

import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.orasi.utils.Sleeper;
import com.orasi.utils.TestEnvironment;
import com.orasi.utils.TestReporter;
import com.orasi.utils.dataProviders.ExcelDataProvider;
import com.xeeva.catalog.ItemDetailsPage;
import com.xeeva.catalog.RecentOrderInformationPage;
import com.xeeva.catalog.RequisitioningPage;
import com.xeeva.login.LoginPage;
import com.xeeva.navigation.MainNav;

/**
 * @Summary: To verify that requestor is able to add the price agreement item from recent order list. 
 * @author praveen varma, @version: Created 07-09-2016
 */
 public class Add_PriceAgreementItem_RecentOrder extends TestEnvironment{
		
	 public String RequisitionType = "serviceRequestGeneral";
		
		// **************
		// Data Provider
		// **************
		@DataProvider(name = "dataScenario")
		public Object[][] scenarios() {
			try {
				Object[][] excelData = new ExcelDataProvider("/datasheets/Catalog.xlsx","AddPriceAgrmnt_RecentOrder").getTestData();
				return excelData;
			}
			catch (RuntimeException e){
				TestReporter.assertTrue(false, "An error occured with accessing the data provider: " + e);
			}
			return new Object[][] {{}};
	    }
		
		/**
		 * @description: To initialize the driver and setup the environment.
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
		 * @description: Close the driver instance.
		 * @param testResults
		 */
		@AfterTest
		public void close(ITestContext testResults){
			endTest("TestAlert", testResults);
		}
		
		/**
		 * @Description: Main business-logic of the test-case resides here.
		 * @param role,location,selectUOM
		 */
		@Test(dataProvider = "dataScenario")
		public void add_PriceAgreementItem_RecentOrder(String role, String location,String selectUOM){
			
			// Application Login 
			LoginPage loginPage = new LoginPage(getDriver());
			TestReporter.logStep("Launch the application and Login with valid Requestor credentials");
			loginPage.loginWithCredentials(role,location);
			
			// Requisition Page  - Navigating to requisition page to create Smart Form Request
			RequisitioningPage reqPage = new RequisitioningPage(getDriver());
			TestReporter.logStep("Navigating to the Requisitioning Page.");
			reqPage.click_ReqTab();
			reqPage.clickRequisitionCartLink();
			
			// Navigating to Recent Order Info page - to click on Requistion Cart link
			RecentOrderInformationPage recentOrderInfoPage = new RecentOrderInformationPage(getDriver());
			TestReporter.logStep("Navigating to Recent Order Information page.");
			recentOrderInfoPage.clickItemNumberLink();
			//Sleeper.sleep(8000);
			
			// Navigating to Item Details page - to add the Item to cart.
			ItemDetailsPage itemDetailsPage = new ItemDetailsPage(getDriver());
			TestReporter.logStep("Navigating to Item Details page.");
			itemDetailsPage.selectUOMValueAndAddItemToCart(selectUOM);
			
			// Application Logout
			MainNav mainNav = new MainNav(getDriver());
			TestReporter.logStep("Log-Out of the application.");
			mainNav.clickLogout();
			
		}
		
	}
