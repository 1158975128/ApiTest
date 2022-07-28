import os
import time
import random
from selenium import webdriver
from selenium.webdriver.common.by import By
from config.public_data.delay_time import *
from utils.object_map import ObjectMap
from common.logger import MyLogging
from page_objects.navigate_bar import NavigateBar
from selenium.common.exceptions import NoSuchElementException
from utils.close_tips_tool import right_corner_cancel
from utils.close_tips_tool import close_login_tips
from utils.droplist_select_tool import select_droplist
from utils.droplist_select_tool import Select

log = MyLogging(__name__).logger
map_path = os.path.abspath(os.path.join(os.path.dirname(__file__), "../../page_element"))
patient_map = map_path + "/patient/patient.xml"

delay_time = DelayTime.short_time.value


class Patient():
    def __init__(self, driver):
        self.driver = driver
        self.version = ObjectMap(patient_map)
        self.patient = NavigateBar(self.driver)
        self.select = Select()

    def check_patient_page(self):
        '''
        检查页面元素是否正确
        :return: new_patient,origin_droplist,patient_droplist
        '''
        self.patient.go_to_patient()
        time.sleep(1)
        new_patient = self.version.getLocator(self.driver, "New_patient").text
        origin_droplist = self.version.getLocator(self.driver, "Origin_droplist").get_attribute('value')
        patient_droplist = self.version.getLocator(self.driver, "Patient_droplist").get_attribute('value')
        return new_patient,origin_droplist,patient_droplist

    def find_patient(self,patient_name):
        '''
        查找住院患者
        :param patient_name: 传入查找的患者姓名
        '''
        self.patient.go_to_patient()
        time.sleep(3)
        origin_select = self.version.getLocator(self.driver, "Origin_select")
        origin_select.click()
        time.sleep(1)
        hospital_select = self.version.getLocator(self.driver, "Hospital_select")
        hospital_select.click()
        time.sleep(1)
        name_input = self.version.getLocator(self.driver, "Name_input")
        name_input.clear()
        name_input.send_keys(patient_name)
        time.sleep(1)

    def click_patient_card(self,patient_name):
        '''
            点击患者卡片
        '''
        time.sleep(1)
        check_name = self.version.getLocator(self.driver, "Check_name")
        if patient_name == check_name.text:
            check_name.click()
            time.sleep(1)

    def add_treatment_item(self,item,region,department,bed_side,limit,dose,frequen,total,therapeutist,intern,notes,attention):
        '''
        新增一个治疗项目
        '''
        time.sleep(1)
        add_Treatment = self.version.getLocator(self.driver, "Add_Treatment")
        add_Treatment.click()
        # 项目名称
        item_name = self.version.getLocator(self.driver, "Item_name")
        item_name.send_keys(item)
        time.sleep(1)
        select_droplist(self.driver,item)
        # 部位
        region_name = self.version.getLocator(self.driver, "Region_name")
        region_name.click()
        time.sleep(1)
        select_droplist(self.driver,region)
        # 治疗科室
        department_name = self.version.getLocator(self.driver, "Department_name")
        department_name.click()
        time.sleep(1)
        select_droplist(self.driver,department)
        # 实习生
        if intern:
            intern_name = self.version.getLocator(self.driver, "Intern")
            intern_name.send_keys(intern)
        # 治疗师
        if therapeutist:
            therapeutist_name = self.version.getLocator(self.driver, "Therapeutist")
            therapeutist_name.click()
            time.sleep(1)
            select_droplist(self.driver,therapeutist)

        # 是否床边
        self.select.bed_side(self.driver,bed_side)
        # 项目备注
        if notes:
            project_notes = self.version.getLocator(self.driver, "ProjectNotes")
            project_notes.send_keys(notes)
        # 注意事项
        if attention:
            matters_attention = self.version.getLocator(self.driver, "MattersNeedingAttention")
            matters_attention.send_keys(attention)
        if limit == '长期':
            # 长短期
            time_limit = self.version.getLocator(self.driver, "TimeLimit")
            time_limit.click()
            time.sleep(1)
            select_droplist(self.driver,limit)
            # 单次剂量
            single_dose = self.version.getLocator(self.driver, "Dose")
            single_dose.clear()
            single_dose.send_keys(dose)
            # 频次
            frequency = self.version.getLocator(self.driver, "Frequency")
            frequency.clear()
            frequency.send_keys(frequen)
        elif limit == '短期':
            # 长短期
            time_limit = self.version.getLocator(self.driver, "TimeLimit")
            time_limit.click()
            time.sleep(1)
            select_droplist(self.driver,limit)
            # 单次剂量
            single_dose = self.version.getLocator(self.driver, "Dose")
            single_dose.clear()
            single_dose.send_keys(dose)
            # 频次
            frequency = self.version.getLocator(self.driver, "Frequency")
            frequency.clear()
            frequency.send_keys(frequen)
            # 总次数
            total_times = self.version.getLocator(self.driver, "Total")
            total_times.clear()
            total_times.send_keys(total)
        # 点击确定
        item_ensure = self.version.getLocator(self.driver, "Item_Ensure")
        item_ensure.click()
        time.sleep(1)
        try:
            tips = self.version.getLocator(self.driver, 'Tips').get_attribute('textContent')
        except NoSuchElementException:
            right_corner_cancel(self.driver)
            assert False, "无提示语，失败！"
        else:
            # time.sleep(1)
            if tips != '新增项目成功':
                right_corner_cancel(self.driver)
                close_login_tips(self.driver)
                assert False, "新增失败,提示语：%s" % tips
            else:
                close_login_tips(self.driver)
                return tips

    def appoint_therapeutist(self,job_type,name):
        '''
        给某个小类指定治疗师
        '''
        appoint = self.version.getLocator(self.driver, "Appoint_Therapeutist")
        appoint.click()
        form = self.driver.find_element(By.CSS_SELECTOR, value='.dialog-form')
        form_labels = form.find_elements(By.TAG_NAME, value='label')
        for form_label in form_labels:
            # print(job_type,'----->',form_label.get_attribute('textContent').strip())
            if job_type in form_label.get_attribute('textContent').strip():
                form.find_element(By.CSS_SELECTOR, value='.el-select--medium').click()
                time.sleep(1)
                break
        select_droplist(self.driver,name)
        item_ensure = self.version.getLocator(self.driver, "Item_Ensure")
        item_ensure.click()
        again_ensure = self.version.getLocator(self.driver, "Again_Ensure")
        again_ensure.click()

    def checkbox_operation(self,limit,name,therapeutist,operation):
        '''
        指定治疗师
        '''
        self.select.choose_limit_time(self.driver,limit)
        self.select.checkbox(self.driver,name)
        self.select.choose_operation(self.driver,operation)
        therapeutist_name = self.version.getLocator(self.driver, "Therapeutist")
        therapeutist_name.click()
        time.sleep(1)
        select_droplist(self.driver, therapeutist)
        checkbox_ensure = self.version.getLocator(self.driver, "CheckboxEnsure")
        checkbox_ensure.click()
        time.sleep(1)
        try:
            tips = self.version.getLocator(self.driver, 'Tips').get_attribute('textContent')
        except NoSuchElementException:
            right_corner_cancel(self.driver)
            assert False, "无提示语，失败！"
        else:
            # time.sleep(1)
            if tips != '批量指定成功！':
                right_corner_cancel(self.driver)
                close_login_tips(self.driver)
                assert False, "新增失败,提示语：%s" % tips
            else:
                close_login_tips(self.driver)
                return tips

    def add_judge_item(self,patient_name,item):
        '''
        新增一个评定项目
        :param patient_name: 传入患者姓名
        :param item: 传入评定项目
        :return: 成功返回True ，失败返回False
        '''
        check_name = self.version.getLocator(self.driver, "Check_name")
        print(check_name.text,patient_name)
        if patient_name == check_name.text:
            check_name.click()
            time.sleep(1)
            Judge_item = self.version.getLocator(self.driver, "Judge_Item")
            Judge_item.click()
            add_Treatment = self.version.getLocator(self.driver, "Add_Treatment")
            add_Treatment.click()
            item_name = self.version.getLocator(self.driver, "Item_name")
            item_name.click()
            time.sleep(1)
            item_name.send_keys(item)
            time.sleep(1)
            item_name_select = self.version.getLocator(self.driver, "Item_name_select")
            item_name_select.click()
            time.sleep(1)
            region_name = self.version.getLocator(self.driver, "Region_name")
            region_name.click()
            time.sleep(1)
            region_name_select = self.version.getLocator(self.driver, "Region_name_select")
            region_name_select.click()
            time.sleep(1)
            hospital_department_name = self.version.getLocator(self.driver, "Hospital_Department_name")
            hospital_department_name.click()
            time.sleep(1)
            department_name_select = self.version.getLocator(self.driver, "Department_name_select")
            department_name_select.click()
            time.sleep(1)
            item_ensure = self.version.getLocator(self.driver, "Item_Ensure")
            item_ensure.click()
            time.sleep(1)
            tips = self.version.getLocator(self.driver, "Tips").text
            if tips == "新增项目成功":
                print("新增治疗项目成功：",tips)
                return True
            else:
                return False

    def leave_hospital(self,patient_name):
        '''
        测试住院患者结束疗程出院
        :param patient_name: 出院患者的姓名
        :return: 成功返回True ，失败返回False
        '''
        check_name = self.version.getLocator(self.driver, "Check_name")
        if patient_name == check_name.text:
            check_name.click()
            time.sleep(1)
            patientInfo = self.version.getLocator(self.driver, "PatientInfo")
            patientInfo.click()
            end_treatment = self.version.getLocator(self.driver, "End_Treatment")
            end_treatment.click()
            leave_time = self.version.getLocator(self.driver, "Time")
            leave_time.click()
            time.sleep(1)
            choose_time = self.version.getLocator(self.driver, "Choose_Time")
            choose_time.click()
            time.sleep(1)
            leave_ensure = self.version.getLocator(self.driver, "Leave_Ensure")
            leave_ensure.click()
            time.sleep(1)
            again_ensure = self.version.getLocator(self.driver, "Again_Ensure")
            again_ensure.click()
            time.sleep(1)
            tips = self.version.getLocator(self.driver, "Tips").text
            if tips == "结束疗程成功":
                print("结束疗程成功：",tips)
                return True
            else:
                return False

    def click_patientInfo(self):
        '''
        在患者管理详情页点击患者信息办理出院
        :return: 成功返回True ，失败返回False
        '''
        patientInfo = self.version.getLocator(self.driver, "PatientInfo")
        patientInfo.click()
        end_treatment = self.version.getLocator(self.driver, "End_Treatment")
        end_treatment.click()
        leave_time = self.version.getLocator(self.driver, "Time")
        leave_time.click()
        time.sleep(1)
        choose_time = self.version.getLocator(self.driver, "Choose_Time")
        choose_time.click()
        time.sleep(1)
        leave_ensure = self.version.getLocator(self.driver, "Leave_Ensure")
        leave_ensure.click()
        time.sleep(1)
        again_ensure = self.version.getLocator(self.driver, "Again_Ensure")
        again_ensure.click()
        time.sleep(1)
        tips = self.version.getLocator(self.driver, "Tips").text
        if tips == "结束疗程成功":
            print("结束疗程成功：", tips)
            return True
        else:
            return False