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
	public void smartForm(String role, String location,String ItemDescription,String CategoryType,String Category,String SubCategory,
			String Quantity,String UnitofMeasure,String Price){
		
		// Application Login 
		LoginPage loginPage = new LoginPage(getDriver());
		loginPage.loginWithCredentials(role,location);
		
		// Requisition Page  - Navigating to requisition page to create Smart Form Request
		RequisitioningPage reqPage = new RequisitioningPage(getDriver());
		reqPage.createSmartFormRequest( RequisitionType,ItemDescription, CategoryType, Category, SubCategory, Quantity, UnitofMeasure, Price);
		reqPage.Verify_SmartFormItem(ItemDescription);
		
		// Application Logout
		MainNav mainNav = new MainNav(getDriver());
		mainNav.clickLogout();
		
	}
	
}
