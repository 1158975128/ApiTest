import os
from selenium import webdriver
from selenium.webdriver.chrome.options import Options


lib_path = os.path.abspath(os.path.join(os.path.dirname(__file__), "../drivers"))
# windows driver
# driver_path = lib_path + "/" + "chromedriver.exe"  # excel地址x

# mac driver
driver_path = lib_path + "/" + "chromedriver-102"

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
        driver.get('https://qafris.fftai.com/fris2/#/login')
        # driver.get('https://uatfris.fftai.com/fris2/#/login')


        return driver
