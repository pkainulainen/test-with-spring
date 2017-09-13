package com.testwithspring.master.web

final class WebTestConstants {

    /**
     * Contains the field names of JSON documents
     */
    static class FieldNames {

        static class Task {

            static final TITLE = 'title'
        }
    }
    
    /**
     * Contains the JSON path property expressions of JSON documents.
     */
    static class JsonPathProperty {

        static class Task {

            static final ASSIGNEE = '$.assignee'
            static class Assignee {

                static final ID = '$.assignee.userId'
                static final NAME = '$.assignee.name'
            }

            static final CLOSER = '$.closer'
            static class Closer {

                static final ID = '$.closer.userId'
                static final NAME = '$.closer.name'
            }

            static final CREATION_TIME = '$.creationTime'

            static class Creator {

                static final ID = '$.creator.userId'
                static final  NAME = '$.creator.name'
            }

            static final DESCRIPTION = '$.description'
            static final ID = '$.id'
            static final MODIFICATION_TIME = '$.modificationTime'

            static class Modifier {

                static final ID = '$.modifier.userId'
                static final NAME = '$.modifier.name'
            }

            static final RESOLUTION = '$.resolution'
            static final STATUS = '$.status'
            static final TAGS = '$.tags'
            static final TITLE = '$.title'
        }
    }

    static class RequestParameter {

        static final SEARCH_TERM = 'searchTerm'
    }

    /**
     * Contains the validation error codes.
     */
    static class ValidationErrorCode {

        static final EMPTY_FIELD = 'NotBlank'
        static final SIZE = 'Size'
    }
}
