package sandbox;

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
import com.xeeva.catalog.RequisitioningPage;
import com.xeeva.catalog.SearchItems.GlobalItemsTab;
import com.xeeva.login.LoginPage;
import com.xeeva.navigation.MainNav;

/**
 * @summary Test To add non price agreement from recent orders list
 * @author  Lalitha Banda
 * @version	08/09/2016
 * *
 */

public class ChangeCC_LineLevel extends TestEnvironment{


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
		testStart("AddingNonPriceAgreement");
	}

	@AfterTest
	public void close(ITestContext testResults){
		//endTest("TestAlert", testResults);
	}

	@Test(dataProvider = "dataScenario")
	public void CostCenterLineLevel(String role, String location,String GlobalItem,String ItemDescription,String Quantity,
			String UnitofMeasure,String UnitPrice,String UpdatedUnitPrice,String UpdatedUnitofMeasure){

		// Application Login 
		TestReporter.logStep("Login into application");
		LoginPage loginPage = new LoginPage(getDriver());
		loginPage.loginWithCredentials(role,location);

		MainNav mainNav = new MainNav(getDriver());
		boolean getStatus = mainNav.verifyCartValue(GlobalItem);
		System.out.println(getStatus);
		if(getStatus!=true){
				TestReporter.logStep("Clicking the GlobalItems Link");
				RequisitioningPage reqPage = new RequisitioningPage(getDriver());
				reqPage.perform_CatalogSearch(GlobalItem);

				// GlobalItemsTab  - Clicking the GlobalItems Link
				TestReporter.logStep("Clicking the GlobalItems Link");
				GlobalItemsTab globalitems = new GlobalItemsTab(getDriver());
				globalitems.click_GlobalItemsTab();

				//Modifying the ItemDetailsPage 
				TestReporter.logStep("ItemDetailsPage  - Modifing Item Details");
				ItemDetailsPage itemdetails = new ItemDetailsPage(getDriver());
				itemdetails.add_TwoDiffrent_ItemsToCart(UnitPrice,Quantity,UnitofMeasure);

				//Clicking Add To Cart Button
				TestReporter.logStep("Clicking on Add To Cart Button - from Global Search Records");
				globalitems.click_AddToCartButton();

				Sleeper.sleep(5000);
				reqPage.click_ReqTab();
			}

		// Application Logout
		//TestReporter.logStep("Application Logout");
		//mainNav.clickLogout();

	}

}
