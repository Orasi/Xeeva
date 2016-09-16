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
import com.xeeva.login.LoginPage;
import com.xeeva.navigation.MainNav;

/**
 * @summary Test To Crate Smart Form Request
 * @author  Lalitha Banda
 * @date 	08/09/2016
 *
 */

public class CreateSmartFormRequest extends TestEnvironment{

	public String RequisitionType = "serviceRequestGeneral";

	// **************
	// Data Provider
	// **************
	@DataProvider(name = "dataScenario")
	public Object[][] scenarios() {
		try {
			Object[][] excelData = new ExcelDataProvider("/datasheets/Catalog.xlsx","SmartForm").getTestData();
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
		testStart("CreateSmartFormRequest");
	}

	@AfterTest
	public void close(ITestContext testResults){
		endTest("TestAlert", testResults);
	}

	@Test(dataProvider = "dataScenario")
	public void smartForm(String role, String location,String ItemDescription,String UNSPSCCode,String SS,
			String CategoryType,String Category,String SubCategory,String MN,String MPN,
			String Quantity,String UnitofMeasure,String Price){

		String[] QuantityArray = Quantity.split(";");
		String[] UOMArray = UnitofMeasure.split(";");
		String[] UPArray = Price.split(";");

		// Application Login 
		TestReporter.logStep("Login into application");
		LoginPage loginPage = new LoginPage(getDriver());
		loginPage.loginWithCredentials(role,location);

		// Requisition Page 
		TestReporter.logStep("Navigating to requisition page to create Smart Form Request");
		RequisitioningPage reqPage = new RequisitioningPage(getDriver());

		TestReporter.logStep("Navigating to requisition Tab");
		reqPage.click_ReqTab();

		TestReporter.logStep("Creating Smart Form Request");
		reqPage.createSmartFormRequest( RequisitionType,ItemDescription, UNSPSCCode,SS,CategoryType, Category, SubCategory,MN,MPN, 
				QuantityArray[0], UOMArray[0],UPArray[0]);

		TestReporter.logStep("Navigating to MainTab Page to Verify Updated Smart Form Item");
		MainNav mainNav = new MainNav(getDriver());
		mainNav.verifyCartItem(ItemDescription,UPArray[1],UOMArray[1], QuantityArray[1]);

		// Application Logout
		TestReporter.logStep("Application Logout");
		mainNav.clickLogout();
		
		}

}
