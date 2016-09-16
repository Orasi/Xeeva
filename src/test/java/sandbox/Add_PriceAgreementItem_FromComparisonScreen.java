package sandbox;

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
import com.xeeva.catalog.ProductComparisonTab;
import com.xeeva.catalog.RequisitioningPage;
import com.xeeva.catalog.SearchItems.GlobalItemsTab;
import com.xeeva.catalog.SearchItems.LocalItemsTab;
import com.xeeva.login.LoginPage;
import com.xeeva.navigation.MainNav;

/**
 * @summary Test to add price agreement Item from Comparison Screen.
 * @author  Praveen Namburi
 * @version	14/09/2016
 */
public class Add_PriceAgreementItem_FromComparisonScreen extends TestEnvironment {

	// **************
	// Data Provider
	// **************
	@DataProvider(name = "dataScenario")
	public Object[][] scenarios() {
		try {
			Object[][] excelData = new ExcelDataProvider("/datasheets/Catalog.xlsx","AddPriceAgrmnt_CompareScreen").getTestData();
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
		testStart("AddNonPriceAgreementItem_ComparisonScreen");
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
	 * @param role,location,GlobalItem 
	 */
	@Test(dataProvider = "dataScenario")
	public void addPriceAgreementFromCompareScreen(String role, String location,String GlobalItem,String UnitofMeasure){
		
		// Application Login 
		TestReporter.logStep("Application Login");
		LoginPage loginPage = new LoginPage(getDriver());
		loginPage.loginWithCredentials(role,location);

		// Requisition Page 
		TestReporter.logStep("Navigating to requisition page to perform catalog search");
		RequisitioningPage reqPage = new RequisitioningPage(getDriver());
		reqPage.click_ReqTab();
		reqPage.perform_CatalogSearch(GlobalItem);

		// GlobalItemsTab  - Click on GlobalItems Link
		TestReporter.logStep("Clicking on GlobalItems Link");
		GlobalItemsTab globalitems = new GlobalItemsTab(getDriver());
		globalitems.click_GlobalItemsTab();
		globalitems.selectGlobalItems_FromComparison();

		// Adding Item to cart 
		TestReporter.logStep("Clicking on Add to cart From Product Comparison Tab");
		ProductComparisonTab pComparison = new ProductComparisonTab(getDriver());
		String itemNumber = pComparison.ReadItemNumber();
		pComparison.click_AddToCart(UnitofMeasure);
		
		//Navigate to Local-Items page and Add-Item to Cart.
		TestReporter.logStep("Click on Cart-Items link.");
		LocalItemsTab localItemsPage = new LocalItemsTab(getDriver());
		localItemsPage.clickCartItemsLink();
		
		//Navigate to Cart-Info page and grab the Quantity value before adding Item-To-Cart.
		TestReporter.logStep("Navigate to Cart-Info page and grab the Quantity value "
				+ "before adding Item-To-Cart.");
		CartInformationPage cartInfoPage = new CartInformationPage(getDriver());
		String getQuantityBefore = cartInfoPage.grabQuantityForAddedItem(itemNumber);
		TestReporter.log("Quantity value before adding item to cart : "+getQuantityBefore);
		
		//Close cart-Info page.
		TestReporter.logStep("Close Cart-Information page.");
		cartInfoPage.closeCartInfoPage();
		
		// Adding Item to cart 
		TestReporter.logStep("Clicking on Add to cart From Product Comparison Tab");
		pComparison.click_AddToCart(UnitofMeasure);
		
		//Navigate to Cart-Info page and grab the Quantity value after adding Item-To-Cart.
		TestReporter.logStep("Navigate to Cart-Info page and grab the Quantity value "
				+ "after adding Item-To-Cart.");
		localItemsPage.clickCartItemsLink();
		String getQuantityAfter = cartInfoPage.grabQuantityForAddedItem(itemNumber);
		TestReporter.log("Quantity value after adding item to cart : "+getQuantityAfter);
		TestReporter.logStep("Verifying Quantity value before and after adding item to cart.");
		TestReporter.assertNotEquals(getQuantityBefore, getQuantityAfter, "Quantity should be increased "
				+ "for already added item to cart.");
		
		//Close cart-Info page.
		TestReporter.logStep("Close Cart-Information page.");
		cartInfoPage.closeCartInfoPage();
		
		// Application Logout
		MainNav mainNav = new MainNav(getDriver());
		TestReporter.logStep("Log-Out of the application.");
		mainNav.clickLogout();
	}

}

