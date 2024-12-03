package sogoing.backend_server.common.error.exception.department

import sogoing.backend_server.common.error.ErrorCode
import sogoing.backend_server.common.error.exception.EntityNotFoundException

class DepartmentNotFoundException : EntityNotFoundException(ErrorCode.DEPARTMENT_NOT_FOUND)
