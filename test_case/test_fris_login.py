import os
import unittest
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from page_objects.login.login_page import LoginPage
from utils.browser_tool import Browser
from common.logger import MyLogging
from selenium.webdriver.common.by import By
from selenium.common.exceptions import NoSuchElementException
from page_objects.navigate_bar import NavigateBar
import time
import logging
from utils.object_map import ObjectMap
from selenium.common.exceptions import NoSuchElementException, TimeoutException, StaleElementReferenceException
from selenium.webdriver.support.wait import WebDriverWait
from selenium.webdriver.support import expected_conditions

log = MyLogging(__name__).logger

def test(driver):
    print('1次',time.strftime("%Y_%m_%d_%H_%M_%S"))
    login_title = driver.find_element(By.XPATH, value='//*[@id="content-panel"]/p')

    print('第2次',time.strftime("%Y_%m_%d_%H_%M_%S"))

    user_input = driver.find_element(By.CSS_SELECTOR, value='input[placeholder="手机号/邮箱号"]')
    user_input.send_keys('18501407218')
    login_title.click()
    print('第3次',time.strftime("%Y_%m_%d_%H_%M_%S"))

    wait = WebDriverWait(driver, 30)
    try:
        login_tips = driver.find_element(By.CSS_SELECTOR, value='.el-form-item__error')
        wait.until(expected_conditions.visibility_of(login_tips))
    except (NoSuchElementException, TimeoutException, StaleElementReferenceException):
        logging.info("没有提示信息！")

    else:
        logging.info("点击忘记密码按钮")
        forget_button = driver.find_element(By.CSS_SELECTOR, value='.ms-forget')
        forget_button.click()

    login_button = driver.find_element(By.CSS_SELECTOR, value='.colorful')
    login_button.click()
    print('第4次',time.strftime("%Y_%m_%d_%H_%M_%S"))
    time.sleep(2)
