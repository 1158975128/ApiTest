from selenium.webdriver.common.by import By
import time


def verify_arrange(driver, item, arrange_time):
    fr_ul = driver.find_element(By.CSS_SELECTOR, value='.time-line-list')
    fr_lis = fr_ul.find_elements(By.TAG_NAME, value='li')
    count = 1
    dict_time = {}
    for fr_li in fr_lis:
        dict_time[count] = fr_li.get_attribute('textContent').strip()
        count += 1
    fr_div = driver.find_element(By.CSS_SELECTOR, value='.card-list-group')
    fr_uls = fr_div.find_elements(By.TAG_NAME, value='ul')
    number = 1
    for fr_ul in fr_uls:
        if item in fr_ul.get_attribute('textContent').strip():
            assert arrange_time == dict_time[number], '排班时间不一致'
            print('匹配成功：', arrange_time, '--->', dict_time[number])
            break
        else:
            print('没有找到---》%s' % item)
        number += 1