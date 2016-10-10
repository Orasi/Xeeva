package catalog.cartCheckoutConfirmRequest;

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
import com.xeeva.catalog.ConfirmRequestPage;
import com.xeeva.catalog.CostCenterPage;
import com.xeeva.catalog.RequisitioningPage;
import com.xeeva.catalog.SearchItems.LocalItemsTab;
import com.xeeva.login.LoginPage;
import com.xeeva.navigation.MainNav;

/**
 * @Summary: To verify the functionality of back button.
 * @author Praveen Namburi, @version: Created 30-09-2016
 */
public class Verify_BackButton_CartCheckConfirmRequest extends TestEnvironment{

	public String RequisitionType = "serviceRequestGeneral";
	
	// **************
	// Data Provider
	// **************
	@DataProvider(name = "dataScenario")
	public Object[][] scenarios() {
		try {
			Object[][] excelData = new ExcelDataProvider("/datasheets/Verify_BackButtonFunctionality.xlsx",
					"BackButtonFunctionality").getTestData();
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
	public void verifyBackButtonFunctionality(String role, String location,String GlobalItem,
			String UnitPrice,String Quantity,String UnitofMeasure){
		
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

		// Get the Cost center page title.
		CostCenterPage ccPage = new CostCenterPage(getDriver());
		String actualCCPageTitle = ccPage.getCostCenterPageTitle();
		TestReporter.log("Captured Cost Center page title before clicking "
				+ "on Back button in ConfirmRequest page: " + actualCCPageTitle);
				
		// Click on Continue CheckOut.
		TestReporter.logStep("Click on Continue CheckOut");
		ccPage.click_ContinueCheckOut();

		// Verifying the 'back' button functionality in Confirm Request page.
		TestReporter.logStep("Verifying the 'back' button functionality in Confirm Request page.");
		ConfirmRequestPage crPage = new ConfirmRequestPage(getDriver());
		crPage.clickbtnBack_ConfirmRequestPage();

		// Capturing the cost center page title after clicking on 
		// back button in Confirm Request page.
		String expectedPageTitle = ccPage.getCostCenterPageTitle();
		TestReporter.log("Captured Cost Center page title after clicking on back button : " + expectedPageTitle);
		
		// Verifying the back button functionality in ConfirmRequest page.
		TestReporter.logStep("Verifying the back button functionality in ConfirmRequest page.");
		TestReporter.assertEquals(actualCCPageTitle, expectedPageTitle, "The back button functionality is Verified!");
		
		//Application Logout
		TestReporter.logStep("Application Logout");
		mainNav.clickLogout();
			
	 }
			
 }




