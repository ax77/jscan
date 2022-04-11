#        define CHAOS_PP_VARIADICS 1

#include <chaos/preprocessor/tuple.h>
#include <chaos/preprocessor/tuple/core.h>
#include <chaos/preprocessor/tuple/size.h>
#include <chaos/preprocessor/tuple/elem.h>
#include <chaos/preprocessor/algorithm/transform.h>
#include <chaos/preprocessor/algorithm/enumerate.h>
#include <chaos/preprocessor/comparison/equal.h>
#include <chaos/preprocessor/control/if.h>

#define STYLE_DEFINITION_HELPER1(s, tup) \
  CHAOS_PP_TUPLE_ELEM_ALT(1, tup) CHAOS_PP_TUPLE_ELEM_ALT(0, tup);

#define STYLE_DEFINITION_HELPER2A(name, type) \
  name(spec_.get<type>( #name ))

#define STYLE_DEFINITION_HELPER2B(name, type, spec_type)  \
  name(dc_, spec_.get<spec_type>( #name ))

#define STYLE_DEFINITION_HELPER2(s, tup) \
  CHAOS_PP_IF(CHAOS_PP_EQUAL(CHAOS_PP_TUPLE_SIZE(tup),2)) \
  (STYLE_DEFINITION_HELPER2A tup,                         \
   STYLE_DEFINITION_HELPER2B tup)

#define STYLE_DEFINITION(name, properties) \
  class name \
  {          \
  public:    \
    CHAOS_PP_EXPR(CHAOS_PP_TUPLE_FOR_EACH(STYLE_DEFINITION_HELPER1, properties)) \
    name(WDrawContext &dc_, const style::Spec &spec_)                          \
      : CHAOS_PP_ENUMERATE(CHAOS_PP_TRANSFORM(STYLE_DEFINITION_HELPER2,        \
                                              (CHAOS_PP_TUPLE)properties))     \
    {}                                                                         \
  };                                                                           \
  /**/
  
  
  namespace style
{
  STYLE_DEFINITION(TextColor,
                   ((foreground, WColor, ascii_string),
                    (background, WColor, ascii_string)))

  STYLE_DEFINITION(Label,
                   ((horizontal_padding, int),
                    (vertical_padding, int),
                    (font, WFont, ascii_string)))
  
} // namespace style

