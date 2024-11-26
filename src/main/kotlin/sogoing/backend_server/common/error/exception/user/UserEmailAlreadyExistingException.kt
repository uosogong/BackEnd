package sogoing.backend_server.common.error.exception.user

import sogoing.backend_server.common.error.ErrorCode
import sogoing.backend_server.common.error.exception.AccessDeniedException

class UserEmailAlreadyExistingException :
    AccessDeniedException(ErrorCode.USER_EMAIL_ALREADY_EXISTING) {}
