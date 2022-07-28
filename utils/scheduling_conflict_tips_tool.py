from selenium.webdriver.common.by import By
import time
from utils.close_tips_tool import left_corner_cancel


def scheduling_conflict(driver,tips):
    '''
    排班冲突验证
    :param tips: 传入排班冲突提示语（患者当前时间段已排满,设备当前时间段已排满,治疗师当前时间段已排满）多个提示语时用‘，’隔开
    '''
    fr_tips = driver.find_elements(By.CSS_SELECTOR, value='.fr-tips')
    time.sleep(1)
    tips_list = []
    for fr_tip in fr_tips:
        if fr_tip.is_displayed():
            tips_list.append(fr_tip.get_attribute('textContent').strip())
    time.sleep(1)
    left_corner_cancel(driver)
    for tip in tips_list:
        if tip in tips:
            assert True
        else:
            assert False, '当前页面显示%d个;传入的提示语：%s;页面显示提示语：%s' % (len(tips_list), tips, tip)
    time.sleep(1)
