from common.operate_excel import *
from common.logger import MyLogging
import base64
from selenium.common.exceptions import NoSuchElementException


log = MyLogging(__name__).logger


def get_path_and_method(api_name, file_path, sheet_name="Sheet1"):
    api_list = ExcelData(file_path, sheet_name).readExcel()
    api_dict = {'path': '', 'method': ''}
    for item in api_list:
        if item[0] == api_name:
            api_dict['path'] = item[1]
            api_dict['method'] = item[2]
            break
    return api_dict

def get_base64(file):
    with open(file, "rb") as f:
        base64_data = base64.b64encode(f.read())
        base64_data = str(base64_data, 'utf-8')
    return base64_data


def isElementPresent(Xpath):
    try:
        driver.find_element_by_xpath(Xpath)
        return True
    except NoSuchElementException as e:
        print("except:",e)
        return False

driver = webdriver.Firefox(executable_path='/usr/local/bin/geckodriver')

tc = unittest.TestCase()
if isElementPresent("//select"):
    area = driver.find_element_by_xpath("//select")
    print(area.get_attribute("class"))
else:
    tc.fail("element doesn't exist!!")