'''
Created on 1 May 2012

@author: Will
'''
import datetime
from random import randint
import logging.handlers

_LOG_FILENAME = "erights-performance.log"
_MAX_LOG_SIZE = 1024
_START_DATE = datetime.datetime(2011, 9, 1, 0, 0, 0)
_METHOD_NAMES = ("activateLicense", "activateLicenseResponse", "addLicense", "addLicenseResponse", "authenticate", "authenticateResponse", "changePasswordByUserId", "changePasswordByUserIdResponse", "changePasswordByUsername", "changePasswordByUsernameResponse", "createGroup", "createGroupResponse", "createProduct", "createProductResponse", "createUserAccount", "createUserAccountAddLicense", "createUserAccountAddLicenseResponse", "createUserAccountResponse", "credentialDataWS", "deactivateLicense", "deactivateLicenseResponse", "deleteGroup", "deleteGroupResponse", "deleteProduct", "deleteProductResponse", "deleteUserAccount", "deleteUserAccountResponse", "getGroup", "getGroupResponse", "getGroupUsers", "getGroupUsersResponse", "getLicensesForUser", "getLicensesForUserProduct", "getLicensesForUserProductResponse", "getLicensesForUserResponse", "getProduct", "getProductResponse", "getProductsFromURL", "getProductsFromURLResponse", "getUserAccount", "getUserAccountResponse", "getUserIdsFromSession", "getUserIdsFromSessionResponse", "logout", "logoutResponse", "oupCredentialLoginPasswordWS", "oupRollingLicenseData", "oupStandardConcurrencyLicenseData", "oupUsageLicenseData", "placeholderCredential", "removeLicense", "removeLicenseResponse", "updateGroup", "updateGroupResponse", "updateLicense", "updateLicenseResponse", "updateProduct", "updateProductResponse", "updateUserAccount", "updateUserAccountResponse")
_MIN_DURATION = 10
_MAX_DUATION = 5000
 
def main():
    file_handler = logging.handlers.RotatingFileHandler(filename=_LOG_FILENAME, maxBytes=1024*1024, backupCount=10)
    log = logging.getLogger("test_data")
    log.addHandler(file_handler)
    log.setLevel(logging.DEBUG)
    current_date = _START_DATE
    while current_date < datetime.datetime.today():
        formatted_date = current_date.strftime("%d/%m/%y %H:%M")
        log.debug(formatted_date + "," + _METHOD_NAMES[randint(0, len(_METHOD_NAMES))-1] + "," + str(randint(_MIN_DURATION, _MAX_DUATION)))
        current_date = current_date + datetime.timedelta(minutes=1)

if __name__ == '__main__':
    main()