from selenium.webdriver.common.by import By
import time


def select_droplist(driver,name):
    droplist_ul = driver.find_element(By.CSS_SELECTOR, value='[aria-hidden="false"] ul')
    droplist_lis = droplist_ul.find_elements(By.TAG_NAME, value='li')
    for droplist_li in droplist_lis:
        # print(droplist_li.get_attribute('textContent').strip())
        if droplist_li.get_attribute('textContent').strip() == name:
            droplist_li.click()
            time.sleep(1)
            break

