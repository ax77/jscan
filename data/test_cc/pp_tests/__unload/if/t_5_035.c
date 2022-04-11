#define NEST 0
#ifdef  A
#else
#   ifdef   B
#   else
#       ifdef   C
#       else
#           ifdef   D
#           else
#               ifdef   E
#               else
#                   ifdef   F
#                   else
#                       ifdef   G
#                       else
#                           ifdef   H
#                           else
#undef NEST
#define NEST 1
#                           endif
#                       endif
#                   endif
#               endif
#           endif
#       endif
#   endif
#endif
NEST

#if  0 + (1 - (2 + (3 - (4 + (5 - (6 + (7 - (8 + (9 - (10 + (11 - (12 +  \
     (13 - (14 + (15 - (16 + (17 - (18 + (19 - (20 + (21 - (22 + (23 -   \
     (24 + (25 - (26 + (27 - (28 + (29 - (30 + (31 - (32 + 0))))))))))   \
     )))))))))))))))))))))) == 0
#undef NEST
#define NEST 32
#endif
NEST






