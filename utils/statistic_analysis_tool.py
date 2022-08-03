import time

from selenium.webdriver.common.by import By


def statictic_analysis(driver):
    fr_body = driver.find_element(By.CSS_SELECTOR, value='.el-table__body-wrapper tbody')
    fr_trs = fr_body.find_elements(By.TAG_NAME, value='tr')
    count_list = []
    for fr_tr in fr_trs:
        count_list.append(fr_tr.get_attribute('textContent').strip())
    money = driver.find_element(By.CSS_SELECTOR, value='.el-table__footer-wrapper tr')
    count_list.append(money.get_attribute('textContent').strip())
    return count_list



