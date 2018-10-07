How to iterate over child elements using TestObject as XPath envelope
===========

## What is this?

This is a [Katalon Studio](https://www.katalon.com/) project for demonstration purpose. You can clone this out to your PC and run it with your Katalon Studio.

This project was developed using Katalon Studio 5.7.1.

I made this project in the hope to respond to a question raised by [a discussion in Katalon Forum](https://forum.katalon.com/discussion/10035/whats-the-purpose-of-testobject). The original question was:
>**What's the purpose of TestObject?** Hello, I'm trying to understand TestObject it seems great to be able to declare and reuse existing elements in different tests.  But I cannot get their child elements, I can't really do anything dynamic with them.. So in these cases I drop down to the WebElement.. And if so.. What's the point? I could use WebElement all the time, discarding TestObject. After considering that I'm wondering why using Katalon at all. I'm a developer so that is defiantly coloring my view of this. If TestObject could reference an array, and if it's child elements could be accessed with a filter returning another TestObject that would definitely change my mind on this matter.

## How to run the demo

1. download the zip file of this project from [Releases]() page, unzip it.
2. start Katalon Studio, open the project
3. open `Test Cases/TC1_iterate_over_Katalon_Discussions`, and run it with any browser

## Input to the demo

The TestCase opens https://forum.katalon.com/discussions. There you will find a list of 37 discussions.

## Output from the demo

The TestCase iterates over the list of discussions and print the titles like this:

```
10-07-2018 01:33:23 PM - [INFO]   - >>> 1: Visual Testing In Katalon Studio
...
10-07-2018 01:33:23 PM - [INFO]   - >>> 37: Query on: Running remotely and paralel thread, Merging teams code in Git,Ex/Import of KatalonProject
```

## How the demo works

### TestObjects made


1. [`Page_Recent Discussions/Content.ul_class-Disscussions`](Object Repository/Page_Recent Discussions/Content.ul_class-Discussions.rs) with xpath `//div[@id='Content']/div/ul`
2. [`Page_Recent Discussions/Content.(li_Item)`](Object Repository/Page_Recent Discussions/Content.(li_Item).rs) with xpath `//li`
3. [`Page_Recent Discussions/Content.(a_Title)`](Object Repository/Page_Recent Discussions/Content.(a_Title).rs) with xpath `//div[contains(@class, 'Title')]/a`


### TestCase made

I made 1 TestCase [`TC1_iterate_over_Katalon_Discussions`](Scripts/TC1_iterate_over_Katalon_Discussions/Script1538870798504.groovy). I would copy its portion and past it here:

```
WebUI.navigateToUrl('https://forum.katalon.com/discussions')
WebDriver driver = DriverFactory.getWebDriver()

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
```

The following line is the core part of this script:
```
String liSelector = ulSelector + findTestObject('Object Repository/Page_Recent Discussions/Content.(li_Item)').findPropertyValue('xpath')
...
for (int pos = 1; pos < rowCount; pos++) {
    String titleSelector = liSelector + "[position()=${pos}]" +
    findTestObject('Object Repository/Page_Recent Discussions/Content.(a_Title)').findPropertyValue('xpath')
    ...
}
```

Generated tilesSelectors will be as follows:
- `//div[@id='Content']/div/ul//li[position()=1]//div[contains(@class, 'Title')]/a`
- `//div[@id='Content']/div/ul//li[position()=2]//div[contains(@class, 'Title')]/a`
- `//div[@id='Content']/div/ul//li[position()=3]//div[contains(@class, 'Title')]/a`
- `//div[@id='Content']/div/ul//li[position()=4]//div[contains(@class, 'Title')]/a`
...
- `//div[@id='Content']/div/ul//li[position()=37]//div[contains(@class, 'Title')]/a`

## Conclusion

1. In the demo I used TestObject just an Envelope of XPath selector literal.
1. TestObject is designed primarilly for the Katalon Studio features: "Record Web" and
"Spy Web". But you can modify/refactor the generated TestObject manually, and it is highly recommended.
1. I had no XPath literals hard-coded in the sample TestCase script. All the XPath literals
are externalized into TestObjects. This would make code maintenance easier.
It is good to have magical values centralized in the Object Repository is good.
Scattering XPath literals all over the TestCase scripts is a nightmare.
1. This sample shows you that you can introduce "searching for child elements" and
"anything dynamic" by a bit tricky coding. Bigginer would not be able to understand this,
but novice programmers would understand it quickly.
