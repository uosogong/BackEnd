package sogoing.backend_server.common.error.exception.user

import sogoing.backend_server.common.error.ErrorCode
import sogoing.backend_server.common.error.exception.EntityNotFoundException

class UserNotFoundException : EntityNotFoundException(ErrorCode.USER_NOT_FOUND)
