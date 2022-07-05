from selenium.webdriver.remote.webelement import WebElement
from selenium.webdriver.common.by import By
import random
import time


class DropDown:
    @staticmethod
    def get_any_item_from_drop_down(drop_down):
        drop_down_list = drop_down.find_elements(By.CSS_SELECTOR, 'li > span')
        try:
            drop_down_item = random.choice(drop_down_list)
        except IndexError:
            logging.info("列表为空")
            return None
        else:
            return drop_down_item