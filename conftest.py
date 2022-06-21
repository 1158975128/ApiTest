import os
import sys
from py.xml import html
import pytest
import logging
from utils.browser_tool import Browser

sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))


@pytest.fixture(scope='session', autouse=True)
def driver():
    logging.info("打开浏览器")
    driver = Browser.open_browser()
    yield driver
    driver.quit()
    logging.info("浏览器已关闭")