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

def right_corner_cancel(driver):
    """
    点击右上角×取消
    """
    right_corner = driver.find_element(By.CSS_SELECTOR,value='[aria-label="close"]:nth-child(1) > .el-dialog__close')
    right_corner.click()

def left_corner_cancel(driver):
    """
    点击左上角×取消
    """
    left_corner = driver.find_element(By.CSS_SELECTOR,value='.fr-drawer-content-header > .el-icon-close')
    left_corner.click()

def cancel_button(driver):
    """
    点击取消按钮
    """
    cancel_button = driver.find_element(By.CSS_SELECTOR,value='.cancel-btn > span')
    cancel_button.click()

