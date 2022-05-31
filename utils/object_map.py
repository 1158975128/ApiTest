from selenium import webdriver
from xml.dom.minidom import parse
from selenium.webdriver.common.by import By
from selenium.common.exceptions import NoSuchElementException

class ObjectMap:
    def __init__(self, mapFile):
        self.domTree = parse(mapFile)
        self.rootNode = self.domTree.documentElement

    def getLocator(self, driver, logicalElementName):
        self.locators = self.rootNode.getElementsByTagName("element")
        for locator in self.locators:
            # print(locator.getElementsByTagName("name")[0].childNodes[0].data)
            if locator.getElementsByTagName("name")[0].childNodes[0].data == logicalElementName:
                try:
                    self.locatorType = locator.getElementsByTagName("type")[0].childNodes[0].data
                    self.locatorValue = locator.getElementsByTagName("value")[0].childNodes[0].data
                    # target_element = self.getLocatorByType(driver)
                    return self.getLocatorByType(driver)
                except NoSuchElementException:
                    raise NoSuchElementException("Failed to generate locator for '" + logicalElementName + "'")

    def getLocatorByType(self, driver):
        # if self.locatorType.lower() == "id":
        #     return driver.find_element(By.ID,value=self.locatorValue)
        # elif self.locatorType.lower() == "name":
        #     return driver.find_element(By.NAME,value=self.locatorValue)
        # elif self.locatorType.lower() == "classname":
        #     return driver.find_element(By.CLASS_NAME,value=self.locatorValue)
        # elif self.locatorType.lower() == "linktext":
        #     return driver.find_element(By.LINK_TEXT,value=self.locatorValue)
        # elif self.locatorType.lower() == "partiallinktext":
        #     return driver.find_element(By.PARTIAL_LINK_TEXT,value=self.locatorValue)
        # elif self.locatorType.lower() == "css":
        #     return driver.find_element(By.CSS_SELECTOR,value=self.locatorValue)
        # elif self.locatorType.lower() == "xpath":
        #     return driver.find_element(By.XPATH,value=self.locatorValue)
        # elif self.locatorType.lower() == "tagname":
        #     return driver.find_element(By.TAG_NAME,value=self.locatorValue)
        # else:
        #     raise Exception("Locator Type '" + self.locatorType + "' not supported!")

        if self.locatorType.lower() == "id":
            return driver.find_element_by_id(self.locatorValue)
        elif self.locatorType.lower() == "name":
            return driver.find_element_by_name(self.locatorValue)
        elif self.locatorType.lower() == "classname":
            return driver.find_element_by_class_name(self.locatorValue)
        elif self.locatorType.lower() == "linktext":
            return driver.find_element_by_link_text(self.locatorValue)
        elif self.locatorType.lower() == "partiallinktext":
            return driver.find_element_by_partial_link_text(self.locatorValue)
        elif self.locatorType.lower() == "css":
            return driver.find_element_by_css_selector(self.locatorValue)
        elif self.locatorType.lower() == "xpath":
            return driver.find_element_by_xpath(self.locatorValue)
        elif self.locatorType.lower() == "tagname":
            return driver.find_element_by_tag_name(self.locatorValue)
        else:
            raise Exception("Locator Type '" + self.locatorType + "' not supported!")