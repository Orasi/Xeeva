package catalog.nonPriceAgreementItems;

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
import com.xeeva.catalog.ProductComparisonTab;
import com.xeeva.catalog.RequisitioningPage;
import com.xeeva.catalog.SearchItems.GlobalItemsTab;
import com.xeeva.login.LoginPage;
import com.xeeva.navigation.MainNav;

/**
 * @summary Test To add non price agreement Item from Comparison Screen
 * @author  Lalitha Banda
 * @version	14/09/2016
 * *
 */

public class AddNonPriceAgreementItem_FromComparisonScreen extends TestEnvironment {

	// **************
	// Data Provider
	// **************
	@DataProvider(name = "dataScenario")
	public Object[][] scenarios() {
		try {
			Object[][] excelData = new ExcelDataProvider("/datasheets/Catalog.xlsx","AddNonPriceAgr_GlobalCatalog").getTestData();
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
		testStart("AddNonPriceAgreementItem_ComparisonScreen");
	}

	@AfterTest
	public void close(ITestContext testResults){
		endTest("TestAlert", testResults);
	}

	@Test(dataProvider = "dataScenario")
	public void comparisonScreen(String role, String location,String GlobalItem,String ItemDescription,String Quantity,
			String UnitofMeasure,String UnitPrice,String UpdatedUnitPrice,String UpdatedUnitofMeasure){

		// Application Login 
		TestReporter.logStep("Application Login");
		LoginPage loginPage = new LoginPage(getDriver());
		loginPage.loginWithCredentials(role,location);

		// Requisition Page 
		TestReporter.logStep("Navigating to requisition page to perform catalog search");
		RequisitioningPage reqPage = new RequisitioningPage(getDriver());
		reqPage.click_ReqTab();
		reqPage.perform_CatalogSearch(GlobalItem);

		// GlobalItemsTab  - Clicking the GlobalItems Link
		TestReporter.logStep("Clicking the GlobalItems Link");
		GlobalItemsTab globalitems = new GlobalItemsTab(getDriver());
		globalitems.click_GlobalItemsTab();
		globalitems.perform_ItemsComparison();

		// Adding Item to cart 
		TestReporter.logStep("Clicking on Add to cart From Product Comparison Tab");
		ProductComparisonTab pComparison = new ProductComparisonTab(getDriver());
		String itemNumber = pComparison.ReadItemNumber();
		pComparison.click_AddToCart(UnitofMeasure);
		
		// Verifications for Cart Item 
		TestReporter.logStep("Verifications for Cart Item ");
		MainNav mainNav = new MainNav(getDriver());
		mainNav.pageLoaded();
		mainNav.perform_CartItemVerifications(UpdatedUnitPrice, UpdatedUnitofMeasure, Quantity,itemNumber);

		// Application Logout
		TestReporter.logStep("Application Logout");
		mainNav.clickLogout();
	}

}

