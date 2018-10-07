import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import org.openqa.selenium.By as By
import org.openqa.selenium.WebDriver as WebDriver
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject

import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

WebUI.openBrowser('')

WebUI.navigateToUrl('https://forum.katalon.com/discussions')
WebDriver driver = DriverFactory.getWebDriver()

// wait for page to load
WebUI.verifyElementPresent(findTestObject('Page_Recent Discussions/Banner.h1_text-Welcome'), 10)

// read XPath fragments
String ulSelector = findTestObject('Object Repository/Page_Recent Discussions/Content.ul_class-Discussions').findPropertyValue('xpath')
String liSelector = ulSelector + findTestObject('Object Repository/Page_Recent Discussions/Content.(li_Item)').findPropertyValue('xpath')

// obtain the number of Discussion rows
int rowCount = driver.findElements(By.xpath(liSelector)).size()
WebUI.comment(">>> rowCount=${rowCount}")

// iterate over Discussions in the Forum
for (int pos = 1; pos <= rowCount; pos++) {
	// selector to the Title of each Discussion
	String titleSelector = liSelector + "[position()=${pos}]" +
		findTestObject('Object Repository/Page_Recent Discussions/Content.(a_Title)').findPropertyValue('xpath')
	TestObject to = new TestObject().addProperty('xpath', ConditionType.EQUALS, titleSelector)
	WebUI.verifyElementPresent(to, 1)
	String title = WebUI.getText(to)
	WebUI.comment(">>> ${pos}: ${title}")
}

WebUI.closeBrowser()

