package catalog;

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
import com.xeeva.catalog.RequisitioningPage;
import com.xeeva.catalog.SearchItems.BPOItemsTab;
import com.xeeva.catalog.SearchItems.GlobalItemsTab;
import com.xeeva.catalog.SearchItems.LocalItemsTab;
import com.xeeva.login.LoginPage;
import com.xeeva.navigation.MainNav;

/**
 * @summary Test to verify catalog search functionality 
 * @author  Lalitha Banda
 * @date 	06/09/2016
 *
 *
 */

public class VerifyCatalogSearch extends TestEnvironment {
	
	// **************
	// Data Provider
	// **************
	@DataProvider(name = "dataScenario")
	public Object[][] scenarios() {
		try {
			Object[][] excelData = new ExcelDataProvider("/datasheets/VerifyCatalogSearch.xlsx","VerifyCatalogSearch").getTestData();
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
		testStart("VerifyCatalogSearch");
	}

	@AfterTest
	public void close(ITestContext testResults){
		endTest("TestAlert", testResults);
	}

	@Test(dataProvider = "dataScenario")
	public void smartForm(String role, String location,String searchItem,String local,String global,String bpo){

		// Application Login 
		TestReporter.logStep("Login into application");
		LoginPage loginPage = new LoginPage(getDriver());
		loginPage.loginWithCredentials(role,location);

		// Perform catalog Search 
		TestReporter.logStep("Navigating to Requisitioning Page to Perform Catalog Search");
		RequisitioningPage reqPage = new RequisitioningPage(getDriver());
		reqPage.click_ReqTab();
		reqPage.perform_CatalogSearch(searchItem);

		// Navigating to Local Items Tab 
		TestReporter.logStep("Navigating to LocalItems Tab to read Local Item");
		LocalItemsTab localItems = new LocalItemsTab(getDriver());
		String LocalItem = localItems.getLocalItemNumber();

		// Performing Local Item Search
		TestReporter.logStep("Verifing Local Item Search Functionality");
		reqPage.verify_SearchItems(local,LocalItem);

		// Basic Catalog Search
		TestReporter.logStep("Performing Basic Catalog Search");
		reqPage.perform_CatalogSearch(searchItem);

		// Navigating to Global Items Tab 
		TestReporter.logStep("Navigating to GlobalItems Tab to read Global Item");
		GlobalItemsTab globalItems = new GlobalItemsTab(getDriver());
		String GlobalItem = globalItems.getGlobalItemNumber();

		// Verifying Global Item Search Functionality
		TestReporter.logStep("Verifing Global Item Search Functionality");
		reqPage.verify_SearchItems(global,GlobalItem);

		// Basic Catalog Search
		TestReporter.logStep("Performing Basic Catalog Search");
		reqPage.perform_CatalogSearch(searchItem);

		// Navigating to BPO Items Tab 
		TestReporter.logStep("Navigating to BPOItems Tab Tab to read Bpo Item");
		BPOItemsTab bpoItems = new BPOItemsTab(getDriver());
		String BpoItem = bpoItems.getBPOItemNumber();

		// Verifying BPO Item Search Functionality
		TestReporter.logStep("Verifing Global Item Search Functionality");
		reqPage.verify_SearchItems(bpo,BpoItem);

		// Application Logout
		TestReporter.logStep("Application Logout");
		MainNav mainNav = new MainNav(getDriver());
		mainNav.clickLogout();

	}
}
