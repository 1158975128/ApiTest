from datetime import datetime


class PatientObject:
    def __init__(self, id):
        self.id = id
        self._name = None
        self._email = None
        self._phone = None
        self._identity_number = None
        self._profession = None
        self._sex = None
        self._social_security_number = None
        self._birthday = None
        self._birthday_str = None
        self._age = None
        self._marriage = None
        self._address = None
        self._linkman = None
        self._linkman_phone = None
        self._relation = None
        self._nationality = None
        self._identity_type = None
        self._weight = None
        self._height = None

    @property
    def name(self):
        return self._name

    @name.setter
    def name(self, name):
        self._name = name

    @property
    def email(self):
        return self._email

    @email.setter
    def email(self, email):
        self._email = email

    @property
    def phone(self):
        return self._phone

    @phone.setter
    def phone(self, phone):
        self._phone = phone

    @property
    def identity_number(self):
        return self._identity_number

    @identity_number.setter
    def identity_number(self, identity_number):
        self._identity_number = identity_number

    @property
    def profession(self):
        return self._profession

    @profession.setter
    def profession(self, profession):
        self._profession = profession

    @property
    def sex(self):
        return self._sex

    @sex.setter
    def sex(self, sex):
        self._sex = sex

    @property
    def social_security_number(self):
        return self._social_security_number

    @social_security_number.setter
    def social_security_number(self, social_security_number):
        self._social_security_number = social_security_number

    @property
    def birthday(self):
        return self._birthday

    @birthday.setter
    def birthday(self, birthday):
        self._birthday = birthday

    @property
    def birthday_str(self):
        try:
            self._birthday_str = datetime.strftime(self._birthday, "%Y-%m-%d %H:%M:%S")
        except TypeError:
            self._birthday_str = None
        finally:
            return self._birthday_str

    @property
    def age(self):
        return self._age

    @age.setter
    def age(self, age):
        self._age = age

    @property
    def marriage(self):
        return self._marriage

    @marriage.setter
    def marriage(self, marriage):
        self._marriage = marriage

    @property
    def address(self):
        return self._address

    @address.setter
    def address(self, address):
        self._address = address

    @property
    def linkman(self):
        return self._linkman

    @linkman.setter
    def linkman(self, linkman):
        self._linkman = linkman

    @property
    def linkman_phone(self):
        return self._linkman_phone

    @linkman_phone.setter
    def linkman_phone(self, linkman_phone):
        self._linkman_phone = linkman_phone

    @property
    def relation(self):
        return self._relation

    @relation.setter
    def relation(self, relation):
        self._relation = relation

    @property
    def nationality(self):
        return self._nationality

    @nationality.setter
    def nationality(self, nationality):
        self._nationality = nationality

    @property
    def identity_type(self):
        return self._identity_type

    @identity_type.setter
    def identity_type(self, identity_type):
        self._identity_type = identity_type

    @property
    def weight(self):
        return self._weight

    @weight.setter
    def weight(self, weight):
        self._weight = weight

    @property
    def height(self):
        return self._height

    @height.setter
    def height(self, height):
        self._height = height

