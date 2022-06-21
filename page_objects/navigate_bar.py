import os
import time

from utils.object_map import ObjectMap
from common.logger import MyLogging

log = MyLogging(__name__).logger
map_path = os.path.abspath(os.path.join(os.path.dirname(__file__), "../page_element"))
navigate_bar_map = map_path + "/login/navigate_bar.xml"
from config.public_data.delay_time import *

delay_time = DelayTime.short_time.value

class NavigateBar(object):
    def __init__(self, driver):
        log.info("init NavigateBar")
        self.driver = driver
        self.navigate = ObjectMap(navigate_bar_map)
        log.info("end of init")
    def go_to_home_page(self):
        shou_ye = self.navigate.getLocator(self.driver, 'ShouYe')
        shou_ye.click()

    def go_to_patient(self):
        patient = self.navigate.getLocator(self.driver, 'HuanZheGuanLi')
        patient.click()

    def go_to_registration(self):
        registration = self.navigate.getLocator(self.driver, 'Registration')
        registration.click()

    def go_to_work_order(self):
        work_order = self.navigate.getLocator(self.driver, 'Work_order')
        # 获取class的值，判断下拉框状态
        work_order_class = work_order.get_attribute('class').strip()
        if work_order_class.endswith('is-opened'):
            my_work = self.navigate.getLocator(self.driver, 'My_Work')
            my_work.click()
        else:
            work_order.click()
            my_work = self.navigate.getLocator(self.driver, 'My_Work')
            my_work.click()


    def go_to_Position(self):
        position_Preserv = self.navigate.getLocator(self.driver, 'Department_Preserve')
        # 获取class的值，判断下拉框状态
        position_class = position_Preserv.get_attribute('class').strip()
        if position_class.endswith('is-opened'):
            department = self.navigate.getLocator(self.driver, 'Position')
            department.click()
        else:
            position_Preserv.click()
            department = self.navigate.getLocator(self.driver, 'Position')
            department.click()

    def go_to_disease(self):
        treatment_preserve = self.navigate.getLocator(self.driver, "Treatment_Preserve")
        # 获取class的值，判断下拉框状态
        treatment_class = treatment_preserve.get_attribute('class').strip()
        if treatment_class.endswith('is-opened'):
            disease_type = self.navigate.getLocator(self.driver, "Disease_Type")
            disease_type.click()
        else:
            treatment_preserve.click()
            disease_type = self.navigate.getLocator(self.driver, "Disease_Type")
            disease_type.click()

    def go_to_department(self):
        department_Preserv = self.navigate.getLocator(self.driver, 'Department_Preserve')
        # 获取class的值，判断下拉框状态
        department_class = department_Preserv.get_attribute('class').strip()
        if department_class.endswith('is-opened'):
            department = self.navigate.getLocator(self.driver, 'Department')
            department.click()
        else:
            department_Preserv.click()
            department = self.navigate.getLocator(self.driver, 'Department')
            department.click()

    def go_to_system_management(self):
        system_management = self.navigate.getLocator(self.driver, 'System_Management')
        # 获取class的值，判断下拉框状态
        system_class = system_management.get_attribute('class').strip()
        if system_class.endswith('is-opened'):
            version_manegement = self.navigate.getLocator(self.driver, 'Version_Management')
            version_manegement.click()
        else:
            system_management.click()
            version_manegement = self.navigate.getLocator(self.driver, 'Version_Management')
            version_manegement.click()

    def go_to_system_maintain(self):
        system_maintain = self.navigate.getLocator(self.driver, 'System_Maintain')
        # 获取class的值，判断下拉框状态
        work_order_class = system_maintain.get_attribute('class').strip()
        if work_order_class.endswith('is-opened'):
            personnel_list = self.navigate.getLocator(self.driver, 'Personnel_List')
            personnel_list.click()
        else:
            system_maintain.click()
            personnel_list = self.navigate.getLocator(self.driver, 'Personnel_List')
            personnel_list.click()

    def navigate_bar(self):
        organization = self.navigate.getLocator(self.driver, 'Organization')
        organization.click()
        time.sleep(delay_time)
        shou_ye = self.navigate.getLocator(self.driver, 'ShouYe')
        shou_ye.click()
        time.sleep(delay_time)
        huan_zhe_guan_li = self.navigate.getLocator(self.driver, 'HuanZheGuanLi')
        huan_zhe_guan_li.click()
        time.sleep(delay_time)
        deng_ji_gua_hao = self.navigate.getLocator(self.driver, 'DengJiGuaHao')
        deng_ji_gua_hao.click()
        time.sleep(delay_time)
        jiu_zhen_xiang_qing = self.navigate.getLocator(self.driver, 'JiuZhenXiangQing')
        jiu_zhen_xiang_qing.click()
        time.sleep(delay_time)
        gong_dan_guan_li = self.navigate.getLocator(self.driver, 'GongDanGuanLi')
        gong_dan_guan_li.click()
        time.sleep(delay_time)
        mywork = self.navigate.getLocator(self.driver, 'MyWork')
        mywork.click()
        time.sleep(delay_time)
        patientwork = self.navigate.getLocator(self.driver, 'PatientWork')
        patientwork.click()
        time.sleep(delay_time)
        allwork = self.navigate.getLocator(self.driver, 'AllWork')
        allwork.click()
        time.sleep(delay_time)
        ping_ding_guan_li = self.navigate.getLocator(self.driver,'PingDingGuanLi')
        ping_ding_guan_li.click()
        time.sleep(delay_time)
        kang_fu_ping_ding = self.navigate.getLocator(self.driver, 'KangFuPingDing')
        kang_fu_ping_ding.click()
        time.sleep(delay_time)
        kang_fu_ping_ding_hui = self.navigate.getLocator(self.driver, 'KangFuPingDingHui')
        kang_fu_ping_ding_hui.click()
        time.sleep(delay_time)
        quan_bu_ping_ding = self.navigate.getLocator(self.driver, 'QuanBuPingDing')
        quan_bu_ping_ding.click()
        time.sleep(delay_time)
        quan_bu_ping_ding_hui = self.navigate.getLocator(self.driver, 'QuanBuPingDingHui')
        quan_bu_ping_ding_hui.click()
        time.sleep(delay_time)
        kang_fu_xin_shi = self.navigate.getLocator(self.driver, 'KangFuXinShi')
        kang_fu_xin_shi.click()
        time.sleep(delay_time)
        wo_de_xin_xi = self.navigate.getLocator(self.driver, 'WoDeXinXi')
        wo_de_xin_xi.click()
        time.sleep(delay_time)
        fa_song_xin_xi = self.navigate.getLocator(self.driver, 'FaSongXinXi')
        fa_song_xin_xi.click()
        time.sleep(delay_time)
        pai_ban_cha_xun = self.navigate.getLocator(self.driver, 'PaiBanChaXun')
        pai_ban_cha_xun.click()
        time.sleep(delay_time)
        zhi_liao_shi_pai_ban = self.navigate.getLocator(self.driver, 'ZhiLiaoShiPaiBan')
        zhi_liao_shi_pai_ban.click()
        time.sleep(delay_time)
        yi_sheng_pai_ban = self.navigate.getLocator(self.driver, 'YiShengPaiBan')
        yi_sheng_pai_ban.click()
        time.sleep(delay_time)
        quan_bu_pai_ban = self.navigate.getLocator(self.driver, 'QuanBuPaiBan')
        quan_bu_pai_ban.click()
        time.sleep(delay_time)
        lun_ban_guan_li = self.navigate.getLocator(self.driver, 'LunBanGuanLi')
        lun_ban_guan_li.click()
        time.sleep(delay_time)
        ji_xiao_tong_ji = self.navigate.getLocator(self.driver, 'JiXiaoTongJi')
        ji_xiao_tong_ji.click()
        time.sleep(delay_time)
        # yi_sheng_gong_zuo_liang = self.navigate.getLocator(self.driver, 'YiShengGongZuoLiang')
        # yi_sheng_gong_zuo_liang.click()
        # time.sleep(delay_time)
        # yi_sheng_ji_xiao = self.navigate.getLocator(self.driver, 'YiShengJiXiao')
        # yi_sheng_ji_xiao.click()
        # time.sleep(delay_time)
        # zhi_liao_shi_gong_zuo_liang = self.navigate.getLocator(self.driver, 'ZhiLiaoShiGongZuoLiang')
        # zhi_liao_shi_gong_zuo_liang.click()
        # time.sleep(delay_time)
        # zhi_liao_shi_ji_xiao = self.navigate.getLocator(self.driver, 'ZhiLiaoShiJiXiao')
        # zhi_liao_shi_ji_xiao.click()
        # time.sleep(delay_time)
        # ke_shi_hua_pai_ban = self.navigate.getLocator(self.driver, 'PaiBanKeShiHua')
        # ke_shi_hua_pai_ban.click()
        # time.sleep(delay_time)
        # ren_yuan_guan_li = self.navigate.getLocator(self.driver, 'RenYuanGuanLi')
        # ren_yuan_guan_li.click()
        # time.sleep(delay_time)

        self.driver.implicitly_wait(10)


    '''
    def confirm_login(self):
        try:
            login_reminder = Alert()
    '''