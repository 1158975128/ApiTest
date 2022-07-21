import time
from selenium import webdriver
from selenium.webdriver.common.action_chains import ActionChains
from selenium.webdriver.common.by import By


def get_verify(user_name,pwd):
    driver = webdriver.Chrome()
    driver.maximize_window()
    driver.get('https://mail.163.com/')
    time.sleep(2)
    # 获取ifram
    login_iframe = driver.find_element(By.XPATH, '//div[@class="loginWrap"]/div[@id="loginDiv"]/iframe')
    # 切换到ifram
    driver.switch_to.frame(login_iframe)
    # 点击账号输入
    u = driver.find_element(By.XPATH, '//div[@id="account-box"]/div[2]/input')
    u.send_keys(user_name)
    # 点击输入密码
    p = driver.find_element(By.XPATH, '//div[@class="inputbox"]/div[2]/input[2]')
    p.send_keys(pwd)
    i = driver.find_element(By.XPATH, '//div[@class="f-cb loginbox"]/a')
    # 创建鼠标行为链
    anctions = ActionChains(driver)
    # 输入账号
    anctions.send_keys_to_element(u)
    # 输入密码
    anctions.send_keys_to_element(p)
    # 点击登录
    anctions.move_to_element(i).click()
    # 提交鼠标行为链
    anctions.perform()
    time.sleep(2)
    driver.switch_to.default_content()  # 跳出iframe 。一定记住 只要之前跳入iframe，之后就必须跳出。进入主html
    driver.find_element(By.CSS_SELECTOR ,value='[class="nui-tree-item-text"][title="收件箱"]').click() # 点击收件箱
    time.sleep(2)
    driver.find_element(By.CSS_SELECTOR, value='[class="Dg0"]>div:nth-child(4)>div:nth-child(2) .dP0').click()

    # 获取ifram
    connent_iframe = driver.find_element(By.CSS_SELECTOR, '.nu0 iframe')
    # 切换到ifram
    driver.switch_to.frame(connent_iframe)
    connent = driver.find_element(By.CSS_SELECTOR ,value='[class="netease_mail_readhtml netease_mail_readhtml_webmail"]')

    print('准备获取验证码')
    verify = connent.text.split('is: ')[-1]
    print(connent.text.split('is: ')[-1])
    print('找到验证码啦')
    return verify
    driver.quit()