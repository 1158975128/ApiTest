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
    # 首页
    def go_to_home_page(self):
        shou_ye = self.navigate.getLocator(self.driver, 'ShouYe')
        shou_ye.click()

    # 患者管理
    def go_to_patient(self):
        patient = self.navigate.getLocator(self.driver, 'HuanZheGuanLi')
        patient.click()

    # 就诊详情
    def go_to_registration(self):
        registration = self.navigate.getLocator(self.driver, 'Registration')
        registration.click()

    # 我的工单
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

    # 治疗师排班
    def go_to_therapeutist_arrange(self):
        arrange_query = self.navigate.getLocator(self.driver, 'ArrangeQuery')
        # 获取class的值，判断下拉框状态
        arrange_query_class = arrange_query.get_attribute('class').strip()
        if arrange_query_class.endswith('is-opened'):
            therapeutist_arrange = self.navigate.getLocator(self.driver, 'TherapeutistArrange')
            self.driver.execute_script("arguments[0].click();", therapeutist_arrange)
        else:
            arrange_query.click()
            therapeutist_arrange = self.navigate.getLocator(self.driver, 'TherapeutistArrange')
            self.driver.execute_script("arguments[0].click();", therapeutist_arrange)

    # 全部排班
    def go_to_all_arrange(self):
        arrange_query = self.navigate.getLocator(self.driver, 'ArrangeQuery')
        # 获取class的值，判断下拉框状态
        arrange_query_class = arrange_query.get_attribute('class').strip()
        if arrange_query_class.endswith('is-opened'):
            all_arrange = self.navigate.getLocator(self.driver, 'AllArrange')
            self.driver.execute_script("arguments[0].click();", all_arrange)
        else:
            arrange_query.click()
            all_arrange = self.navigate.getLocator(self.driver, 'AllArrange')
            self.driver.execute_script("arguments[0].click();", all_arrange)

    def go_to_arrange_display(self):
        arrange_display = self.navigate.getLocator(self.driver, 'ArrangeDisplay')
        arrange_display.click()

    # 职务
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
    # 岗位小类
    def go_to_job_type(self):
        department_Preserv = self.navigate.getLocator(self.driver, 'Department_Preserve')
        # 获取class的值，判断下拉框状态
        department_class = department_Preserv.get_attribute('class').strip()
        if department_class.endswith('is-opened'):
            job_type = self.navigate.getLocator(self.driver, 'JobType')
            job_type.click()
        else:
            department_Preserv.click()
            job_type = self.navigate.getLocator(self.driver, 'JobType')
            job_type.click()

    # 科室
    def go_to_office(self):
        department_Preserv = self.navigate.getLocator(self.driver, 'Department_Preserve')
        # 获取class的值，判断下拉框状态
        department_class = department_Preserv.get_attribute('class').strip()
        if department_class.endswith('is-opened'):
            office = self.navigate.getLocator(self.driver, 'Office')
            office.click()
        else:
            department_Preserv.click()
            office = self.navigate.getLocator(self.driver, 'Office')
            office.click()

    # 职称
    def go_to_title(self):
        department_Preserv = self.navigate.getLocator(self.driver, 'Department_Preserve')
        # 获取class的值，判断下拉框状态
        department_class = department_Preserv.get_attribute('class').strip()
        if department_class.endswith('is-opened'):
            title = self.navigate.getLocator(self.driver, 'Title')
            title.click()
        else:
            department_Preserv.click()
            title = self.navigate.getLocator(self.driver, 'Title')
            title.click()

    # 岗位
    def go_to_job(self):
        department_Preserv = self.navigate.getLocator(self.driver, 'Department_Preserve')
        # 获取class的值，判断下拉框状态
        department_class = department_Preserv.get_attribute('class').strip()
        if department_class.endswith('is-opened'):
            job = self.navigate.getLocator(self.driver, 'Job')
            job.click()
        else:
            department_Preserv.click()
            job = self.navigate.getLocator(self.driver, 'Job')
            job.click()

    # 疾病类型
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

    # 治疗设备
    def go_to_treatment_equipment(self):
        treatment_preserve = self.navigate.getLocator(self.driver, "Treatment_Preserve")
        # 获取class的值，判断下拉框状态
        treatment_class = treatment_preserve.get_attribute('class').strip()
        if treatment_class.endswith('is-opened'):
            equipment = self.navigate.getLocator(self.driver, "Treatment_Equipment")
            equipment.click()
        else:
            treatment_preserve.click()
            time.sleep(1)
            equipment = self.navigate.getLocator(self.driver, "Treatment_Equipment")
            equipment.click()
    # 部位
    def go_to_region(self):
        treatment_preserve = self.navigate.getLocator(self.driver, "Treatment_Preserve")
        # 获取class的值，判断下拉框状态
        treatment_class = treatment_preserve.get_attribute('class').strip()
        if treatment_class.endswith('is-opened'):
            region = self.navigate.getLocator(self.driver, "Region")
            region.click()
        else:
            treatment_preserve.click()
            region = self.navigate.getLocator(self.driver, "Region")
            region.click()
    # 项目模板
    def go_to_project_template(self):
        treatment_preserve = self.navigate.getLocator(self.driver, "Treatment_Preserve")
        # 获取class的值，判断下拉框状态
        treatment_class = treatment_preserve.get_attribute('class').strip()
        if treatment_class.endswith('is-opened'):
            project_template = self.navigate.getLocator(self.driver, "Project_Template")
            project_template.click()
        else:
            treatment_preserve.click()
            project_template = self.navigate.getLocator(self.driver, "Project_Template")
            project_template.click()

    # 部门
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

    # 版本管理
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
    # 机构管理
    def go_to_organization_management(self):
        system_management = self.navigate.getLocator(self.driver, 'System_Management')
        # 获取class的值，判断下拉框状态
        system_class = system_management.get_attribute('class').strip()
        if system_class.endswith('is-opened'):
            organization_manegement = self.navigate.getLocator(self.driver, 'Organization_Management')
            organization_manegement.click()
        else:
            system_management.click()
            organization_manegement = self.navigate.getLocator(self.driver, 'Organization_Management')
            organization_manegement.click()

    # 人员列表
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

    # 单位
    def go_to_unit(self):
        system_maintain = self.navigate.getLocator(self.driver, 'System_Maintain')
        # 获取class的值，判断下拉框状态
        work_order_class = system_maintain.get_attribute('class').strip()
        if work_order_class.endswith('is-opened'):
            unit = self.navigate.getLocator(self.driver, 'Unit')
            unit.click()
        else:
            system_maintain.click()
            unit = self.navigate.getLocator(self.driver, 'Unit')
            unit.click()
    # 租赁设备
    def go_to_lease(self):
        system_maintain = self.navigate.getLocator(self.driver, 'System_Maintain')
        # 获取class的值，判断下拉框状态
        work_order_class = system_maintain.get_attribute('class').strip()
        if work_order_class.endswith('is-opened'):
            lease = self.navigate.getLocator(self.driver, 'Lease')
            lease.click()
        else:
            system_maintain.click()
            lease = self.navigate.getLocator(self.driver, 'Lease')
            lease.click()
    # 职业
    def go_to_profession(self):
        system_maintain = self.navigate.getLocator(self.driver, 'System_Maintain')
        # 获取class的值，判断下拉框状态
        work_order_class = system_maintain.get_attribute('class').strip()
        if work_order_class.endswith('is-opened'):
            profession = self.navigate.getLocator(self.driver, 'Profession')
            profession.click()
        else:
            system_maintain.click()
            profession = self.navigate.getLocator(self.driver, 'Profession')
            profession.click()
    # 关系
    def go_to_relation(self):
        system_maintain = self.navigate.getLocator(self.driver, 'System_Maintain')
        # 获取class的值，判断下拉框状态
        work_order_class = system_maintain.get_attribute('class').strip()
        if work_order_class.endswith('is-opened'):
            relation = self.navigate.getLocator(self.driver, 'Relation')
            relation.click()
        else:
            system_maintain.click()
            relation = self.navigate.getLocator(self.driver, 'Relation')
            relation.click()
    # 时间模板
    def go_to_timetemplate(self):
        system_maintain = self.navigate.getLocator(self.driver, 'System_Maintain')
        # 获取class的值，判断下拉框状态
        work_order_class = system_maintain.get_attribute('class').strip()
        if work_order_class.endswith('is-opened'):
            timetemplate = self.navigate.getLocator(self.driver, 'TimeTemplate')
            timetemplate.click()
        else:
            system_maintain.click()
            timetemplate = self.navigate.getLocator(self.driver, 'TimeTemplate')
            timetemplate.click()
    # 支付管理
    def go_to_pay(self):
        system_maintain = self.navigate.getLocator(self.driver, 'System_Maintain')
        # 获取class的值，判断下拉框状态
        work_order_class = system_maintain.get_attribute('class').strip()
        if work_order_class.endswith('is-opened'):
            pay = self.navigate.getLocator(self.driver, 'Pay')
            pay.click()
        else:
            system_maintain.click()
            pay = self.navigate.getLocator(self.driver, 'Pay')
            pay.click()
    # 挂号费用
    def go_to_register(self):
        system_maintain = self.navigate.getLocator(self.driver, 'System_Maintain')
        # 获取class的值，判断下拉框状态
        work_order_class = system_maintain.get_attribute('class').strip()
        if work_order_class.endswith('is-opened'):
            register = self.navigate.getLocator(self.driver, 'Register')
            register.click()
        else:
            system_maintain.click()
            register = self.navigate.getLocator(self.driver, 'Register')
            register.click()

    # 统计分析--》治疗师工作量
    def go_to_therapist_workload(self):
        statistic_analysis = self.navigate.getLocator(self.driver, 'StatisticAnalysis')
        # 获取class的值，判断下拉框状态
        statistic_analysis_class = statistic_analysis.get_attribute('class').strip()
        if statistic_analysis_class.endswith('is-opened'):
            therapist_workload = self.navigate.getLocator(self.driver, 'TherapistWorkLoad')
            therapist_workload.click()
        else:
            statistic_analysis.click()
            therapist_workload = self.navigate.getLocator(self.driver, 'TherapistWorkLoad')
            therapist_workload.click()

    # 统计分析--》科室工作量
    def go_to_office_workload(self):
        statistic_analysis = self.navigate.getLocator(self.driver, 'StatisticAnalysis')
        # 获取class的值，判断下拉框状态
        statistic_analysis_class = statistic_analysis.get_attribute('class').strip()
        if statistic_analysis_class.endswith('is-opened'):
            office_workload = self.navigate.getLocator(self.driver, 'OfficeWorkLoad')
            office_workload.click()
        else:
            statistic_analysis.click()
            office_workload = self.navigate.getLocator(self.driver, 'OfficeWorkLoad')
            office_workload.click()

    # 统计分析--》设备使用统计
    def go_to_device_use_statistics(self):
        statistic_analysis = self.navigate.getLocator(self.driver, 'StatisticAnalysis')
        # 获取class的值，判断下拉框状态
        statistic_analysis_class = statistic_analysis.get_attribute('class').strip()
        if statistic_analysis_class.endswith('is-opened'):
            device_use_statistics = self.navigate.getLocator(self.driver, 'DeviceUseStatistics')
            device_use_statistics.click()
        else:
            statistic_analysis.click()
            device_use_statistics = self.navigate.getLocator(self.driver, 'DeviceUseStatistics')
            device_use_statistics.click()

    # 统计分析--》费用统计
    def go_to_cost_statistics(self):
        statistic_analysis = self.navigate.getLocator(self.driver, 'StatisticAnalysis')
        # 获取class的值，判断下拉框状态
        statistic_analysis_class = statistic_analysis.get_attribute('class').strip()
        if statistic_analysis_class.endswith('is-opened'):
            cost_statistics = self.navigate.getLocator(self.driver, 'CostStatistics')
            cost_statistics.click()
        else:
            statistic_analysis.click()
            cost_statistics = self.navigate.getLocator(self.driver, 'CostStatistics')
            cost_statistics.click()

    # 统计分析--》项目统计
    def go_to_item_statistics(self):
        statistic_analysis = self.navigate.getLocator(self.driver, 'StatisticAnalysis')
        # 获取class的值，判断下拉框状态
        statistic_analysis_class = statistic_analysis.get_attribute('class').strip()
        if statistic_analysis_class.endswith('is-opened'):
            item_statistics = self.navigate.getLocator(self.driver, 'ItemStatistics')
            item_statistics.click()
        else:
            statistic_analysis.click()
            item_statistics = self.navigate.getLocator(self.driver, 'ItemStatistics')
            item_statistics.click()