import os
from selenium import webdriver
from selenium.webdriver.chrome.options import Options


lib_path = os.path.abspath(os.path.join(os.path.dirname(__file__), "../drivers"))
# driver_path = lib_path + "/" + "chromedriver.exe"  # excel地址
driver_path = '/Users/pca001/Downloads/chromedriver'


class Browser:
    @staticmethod
    def open_browser():
        chrome_options = Options()
        chrome_options.add_argument('--no-sandbox')
        chrome_options.add_argument('--disable-dev-shm-usage')
        chrome_options.add_experimental_option('useAutomationExtension', False)
        driver = webdriver.Chrome(executable_path=driver_path)
        driver.implicitly_wait(30)
        driver.maximize_window()
        driver.get('http://47.103.97.21:8088/fris2/#/login')
        return driver
