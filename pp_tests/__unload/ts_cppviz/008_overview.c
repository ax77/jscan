/* Include the magick. */
#include "cppviz.h"

/* and some other stuff */
//#include <stdio.h>

/*
Create the data table for planets. The table name has to end
with "_DATA", everything before "_DATA" will be the main name,
here "PLANET". Sorry for the crude underscore parameters ...
*/
#define PLANET_DATA(_,__,___, T, A, E) \
    T(_,__,___, int         , char *        , int    ) \
    A(_,__,___, id          , name          , radius ) \
    /*----------------------------------------------*/ \
    E(_,__,___, MERCURY     , "Mercury"     , 2439   ) \
    E(_,__,___, VENUS       , "Venus"       , 6051   ) \
    E(_,__,___, EARTH       , "Earth"       , 6378   )

/* Now, create the enum, simple as that! */
ENUM(PLANET);

/* We want to access the planets attributes, name and radius. Simple as that! */
ATTRIBUTE(PLANET, 2);
ATTRIBUTE(PLANET, 3);

/* We also want the enum ELEMENT NAMES of all planets, simple as that! */
NAMES(PLANET);

/* The landing procedures, one for each planet. Your job to fill them ... */
void PLANET_MERCURY_landing_procedure(int x, int y) { /* ... */ }
void PLANET_VENUS_landing_procedure(int x, int y) { /* ... */ }
void PLANET_EARTH_landing_procedure(int x, int y) { /* ... */ }

/* Let the spaceship roll! */
int main() {

    /* Some totally relevant x, y coordinates. */
    int x, y;

    /* Print stuff for each planet , simple as that! */
    FOREACH(PLANET, planet) {
        printf("%s %s %d\n", PLANET_element_name[planet], PLANET_name[planet], PLANET_radius[planet]);
    }

    /* Perform the planet specific landing procedure. */
    /* Yes, simple as that! */
    PLANET planet;
    /* ... */
    SWITCH(PLANET, planet, landing_procedure(x, y));

}
