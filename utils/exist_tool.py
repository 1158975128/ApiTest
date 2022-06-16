from common.operate_excel import *
from common.logger import MyLogging
from selenium.common.exceptions import NoSuchElementException
from selenium.webdriver.common.by import By



log = MyLogging(__name__).logger

class Element(object):
    def __init__(self, driver):
        self.driver = driver

    def isElementPresent(self,Xpath):
        try:
            self.driver.find_element(By.CSS_SELECTOR,value=Xpath)
            return True
        except NoSuchElementException as e:
            # print("except:", e)
            return False




