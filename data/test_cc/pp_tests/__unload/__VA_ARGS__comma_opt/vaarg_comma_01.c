#define va_macro(...) va_sub(-9,##__VA_ARGS__,9)

void setup() {
   va_macro(1,2); // va_sub(-9,1,2,9);
   va_macro(1);   // va_sub(-9,1,9); 
   va_macro();    // va_sub(-9,9);
}
