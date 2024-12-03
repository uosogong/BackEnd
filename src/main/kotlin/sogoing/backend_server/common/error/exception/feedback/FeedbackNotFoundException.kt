package sogoing.backend_server.common.error.exception.feedback

import sogoing.backend_server.common.error.ErrorCode
import sogoing.backend_server.common.error.exception.EntityNotFoundException

class FeedbackNotFoundException : EntityNotFoundException(ErrorCode.FEEDBACK_NOT_FOUND)
