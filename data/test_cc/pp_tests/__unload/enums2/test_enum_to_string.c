#include "EnumToString.h"
  
  DECLARE_ENUM( EUserState
        , STATE_ZERO_NO_USE // use 0 for invalid state
        , STATE_INIT // use 1 for initial state
        , STATE_LOGIN
        , STATE_EXIT
    );

    DECLARE_ENUM( EUserStateInput
        , INPUT_AUTHENTICATED
        , INPUT_EXIT
    );
    