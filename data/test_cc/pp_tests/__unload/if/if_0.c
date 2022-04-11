#define one 1
#define two 2
#if one+two
	#define FIRST_OPEN "1 block open"
	FIRST_OPEN
    
    #if (two-one+1)
        #define SECOND_OPEN "2 block open"
        SECOND_OPEN
        #undef SECOND_OPEN

        #if 3
        	"3 block open"
        	#undef FIRST_OPEN
        	#if 1
        		"in 3: block open"
        			"OK"	
        		"in 3: block close"
        	#else
        		"error 5"	
        	#endif
        	"3 block close"
        #else
        	"error 3"	
       	#endif
       	
       	"2 block close"
    #else
    	"error 2"   	
    #endif

    "1 block close"
#else
	"error 1"    
#endif

#define x 32
x
FIRST_OPEN  (no expanded)
SECOND_OPEN (no expanded)
