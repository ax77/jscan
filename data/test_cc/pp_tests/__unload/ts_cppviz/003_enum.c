#include "cppviz.h"

#define PLANET_DATA(_,__,___, T, A, E) \
    T(_,__,___, int         , char *        , int    ) \
    A(_,__,___, id          , name          , radius ) \
    /*----------------------------------------------*/ \
    E(_,__,___, MERCURY     , "Mercury"     , 2439   ) \
    E(_,__,___, VENUS       , "Venus"       , 6051   ) \
    E(_,__,___, EARTH       , "Earth"       , 6378   )

ENUM(PLANET);
