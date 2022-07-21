import os
from common.logger import MyLogging
import time
from selenium.webdriver.common.by import By
map_path = os.path.abspath(os.path.join(os.path.dirname(__file__), "../../page_element"))
unit_map = map_path + "/system_management/unit.xml"


log = MyLogging(__name__).logger


def close_login_tips(driver):
    """
    关闭登录成功提示
    """
    close_tips = driver.find_element(By.CSS_SELECTOR,value='.el-message__closeBtn')
    close_tips.click()



