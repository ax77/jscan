// http://evincarofautumn.blogspot.cz/2012/04/c-functional-style-tips.html
// "
// When C++ fails to provide a mechanism of semantic abstraction for a particular problem, higher-order macros at least allow you to abstract syntactically repetitive code. Judicious use of macros to reduce repetition is a Good Thing, even if it might make you feel a bit dirty.
// "

#define LETTERS_SMALL(M)\
        M(a)\
        M(b)\
        M(c)

#define LETTERS_CAPITAL(M)\
        M(A)\
        M(B)\
        M(C)
        
#define LETTERS(M)\
        LETTERS_SMALL(M)\
        LETTERS_CAPITAL(M)

#define DECLARE(LETTER) \
        int letter_##LETTER = 0;
        
        LETTERS(DECLARE);
        letter_a = 2;
        letter_c = 3;
        
        letter_B = 5;
    }
    