package jscan.preproc.preprocess.args;

//MARK:{,##__VA_ARGS__}
//GNU {,##__VA_ARGS__}
//
//#define F2(X, ...) f(10, X, ## __VA_ARGS__)
//#define H2(...) f(10, ## __VA_ARGS__)
//
//F2(a, b)   =>   f(10, a, b)  ArgInfo [info=vararg_present_normal] 
//F2(a, )    =>   f(10, a, )   ArgInfo [info=vararg_present_empty] 
//F2(a)      =>   f(10, a)     ArgInfo [info=vararg_not_present]                   => delete COMMA here
//H2(a)      =>   f(10, a)     ArgInfo [info=vararg_single_arg_and_present_normal] 
//H2()       =>   f(10)        ArgInfo [info=vararg_single_arg_and_present_empty]  => delete COMMA here
//
public enum ArgInfoVararg {
    noinfo,
      vararg_present_normal,
      vararg_not_present, // actual parameters don't have variadic part at all...
      vararg_present_empty, // this mean, that macros have formal parameters, instead of variadic, and variadic part is empty

      vararg_single_arg_and_present_empty,
      vararg_single_arg_and_present_normal,
}
