from datetime import datetime


class OfficeObject:
    def __init__(self, id):
        self.id = id
        self._name = None
        self._office_type_id = None
        self._principal = None
        self._address = None
        self._sort = None
        self._remark = None
        self._create_time = None
        self._update_time = None
        self._create = None
        self._update = None

    @property
    def name(self):
        return self._name

    @name.setter
    def name(self, name):
        self._name = name

    @property
    def office_type_id(self):
        return self._office_type_id

    @office_type_id.setter
    def office_type_id(self, office_type_id):
        self._office_type_id = office_type_id

    @property
    def principal(self):
        return self._principal

    @principal.setter
    def principal(self, principal):
        self._principal = principal

    @property
    def address(self):
        return self._address

    @address.setter
    def address(self, address):
        self._address = address

    @property
    def sort(self):
        return self._sort

    @sort.setter
    def sort(self, sort):
        self._sort = sort

    @property
    def remark(self):
        return self._remark

    @remark.setter
    def remark(self, remark):
        self._remark = remark

    @property
    def create_time(self):
        return self._create_time

    @create_time.setter
    def create_time(self, create_time):
        self._create_time = create_time

    @property
    def update_time(self):
        return self._update_time

    @update_time.setter
    def update_time(self, update_time):
        self._update_time = update_time

    @property
    def create(self):
        try:
            self._create = datetime.strftime(self._create_time, "%Y-%m-%d %H:%M:%S")
        except TypeError:
            self._create = None
        finally:
            return self._create

    @property
    def update(self):
        try:
            self._update = datetime.strftime(self._update_time, "%Y-%m-%d %H:%M:%S")
        except TypeError:
            self._update = None
        finally:
            return self._update
