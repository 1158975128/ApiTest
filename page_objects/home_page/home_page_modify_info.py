import os
import time
from utils.object_map import ObjectMap



map_path = os.path.abspath(os.path.join(os.path.dirname(__file__), "../../page_element"))
home_page_path = map_path + "/home_page/home_page_modify_info.xml"

class HomePage:
    """
    新增患者对话框
    """
    def __init__(self, driver):
        self.driver = driver
        self.my_photo_map = ObjectMap(home_page_path)

    def click_my_photo(self):
        """
        点击用户头像
        """
        my_photo = self.my_photo_map.getLocator(self.driver, 'My_Photo')
        my_photo.click()

    def close_login_tips(self):
        """
        关闭登录成功提示
        """
        close_tips = self.my_photo_map.getLocator(self.driver, 'CloseTips')
        close_tips.click()
        time.sleep(1)

    def click_info_title(self):
        """
        单击 基本信息
        """
        title = self.my_photo_map.getLocator(self.driver, 'InfoTitle')
        title.click()

